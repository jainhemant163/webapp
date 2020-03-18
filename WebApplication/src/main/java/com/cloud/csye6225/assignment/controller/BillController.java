/**
 * 
 */
package com.cloud.csye6225.assignment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.entity.FileUpload;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.entity.UserAccountResponse;
import com.cloud.csye6225.assignment.service.AmazonClient;
import com.cloud.csye6225.assignment.service.BillService;
import com.cloud.csye6225.assignment.service.FileService;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtilImpl;
import com.cloud.csye6225.assignment.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.timgroup.statsd.StatsDClient;

/**
 * @author jainh
 *
 */

@RestController
public class BillController {
	@Autowired
	private UserAccountService accountService;

	@Autowired
	EmailValidationUtilImpl emailValidationUtil;

	@Autowired
	private BillService billService;

	@Autowired
	private FileService fileService;

	@Autowired
	PasswordUtil passwordUtil;

	@Autowired
	private AmazonClient amazonClient;

	@Autowired
	BillController() {
		this.amazonClient = new AmazonClient();
	}

	@Autowired
	private StatsDClient statsDClient;

	private final static Logger logger = LoggerFactory.getLogger(BillController.class);

	@PostMapping("/v1/bill/fileupload")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) {

		return this.amazonClient.uploadFile(file);
	}

	@DeleteMapping("/v1/bill/filedelete")
	public String deleteFile(@RequestPart(value = "url") String fileUrl) {
		return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	}

	// Post Request for Bill
	@RequestMapping(value = "/v1/bill", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> registerPost(@RequestBody String bill) {
		statsDClient.incrementCounter("endpoint.v1.bill.api.post");
		Stopwatch stopwatch = Stopwatch.createStarted();

		logger.info("Add new bill to the user");

		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} else {

			Bill billObj = new Gson().fromJson(bill, Bill.class);
			UserAccount account = accountService.currentUser;

			billObj.setId(UUID.randomUUID().toString());
			billObj.setCreated_ts(new Date().toString());
			billObj.setUpdated_ts(new Date().toString());
			billObj.setOwner_id(account.getId());

			billService.addBill(billObj);
			stopwatch.stop();
			// send the recorded time to statsd
			statsDClient.recordExecutionTime("timer.v1.bill.api.post", stopwatch.elapsed(TimeUnit.MILLISECONDS));
			return new ResponseEntity<>(billObj, HttpStatus.OK);
		}
	}

	// GEt all bills
	@RequestMapping(value = "/v1/bills", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Collection<Bill>> registerGet() {

		statsDClient.incrementCounter("endpoint.v1.bills.api.get");
		logger.info("Get all bills for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();

		Collection<Bill> bills1 = null;
		// check whether user logged in using the basic auth credentials or not
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<Collection<Bill>>(HttpStatus.BAD_REQUEST);

		} else {

			UserAccount account = accountService.currentUser;

			Collection<Bill> bills = billService.getAllBill();

			bills1 = bills.stream().filter(p -> p.getOwner_id().equals(account.getId())).collect(Collectors.toList());
			stopwatch.stop();
			// send the recorded time to statsd
			statsDClient.recordExecutionTime("timer.v1.bills.api.get", stopwatch.elapsed(TimeUnit.MILLISECONDS));
			return new ResponseEntity<Collection<Bill>>(bills1, HttpStatus.OK);

		}

	}

	// Get bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> registerGet(@PathVariable("id") String billId) {
//        if() return null;// log in or not
		// get user id after log in

		statsDClient.incrementCounter("endpoint.v1.bill.id.api.get");
		logger.info("Get a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<Bill>(HttpStatus.BAD_REQUEST);

		} else {

			UserAccount account = accountService.currentUser;

			if (billId == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			else {
				Bill bill = billService.getBillById(billId);
				if (bill == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				stopwatch.stop();
				// send the recorded time to statsd
				statsDClient.recordExecutionTime("timer.v1.bill.id.api.get", stopwatch.elapsed(TimeUnit.MILLISECONDS));

				return new ResponseEntity<>(bill, HttpStatus.OK);
			}
		}
	}

	// DELETE API
	// Delete bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteBillById(@PathVariable("id") String billId) {

		statsDClient.incrementCounter("endpoint.v1.bill.id.api.delete");
		logger.info("Delete a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		boolean isSuccess = false;
		if (billId == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {

			Bill bill = billService.getBillById(billId);
			String billattach = bill.getAttachment();

			Gson g = new Gson();
			FileUpload p = g.fromJson(billattach, FileUpload.class);

			System.out.println(billattach);
			String imageUrl = p.getUrl();
			String fileId = p.getId();

			if (SecurityContextHolder.getContext().getAuthentication() != null
					&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
				// response.put("message", "you are not logged in!!!");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			} else {

				if (bill == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				this.amazonClient.deleteFileFromS3Bucket(imageUrl);
				fileService.deleteFile(fileId);
				isSuccess = billService.deleteBillById(billId);

				if (isSuccess) {
					stopwatch.stop();
					// send the recorded time to statsd
					statsDClient.recordExecutionTime("timer.v1.bill.id.api.delete",
							stopwatch.elapsed(TimeUnit.MILLISECONDS));

					return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
	}

	// Put bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> updateBill(@PathVariable("id") String billId, @RequestBody Bill infos) {

		statsDClient.incrementCounter("endpoint.v1.bill.id.api.put");
		logger.info("Put a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		Bill bill = billService.getBillById(billId);
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<Bill>(HttpStatus.BAD_REQUEST);

		} else {

			// bill.setOwner_id(infos.getOwner_id());
			bill.setAmount_due(infos.getAmount_due());
			bill.setVendor(infos.getVendor());
			bill.setBill_date(infos.getBill_date());
			bill.setDue_date(infos.getDue_date());

			billService.updateBill(bill);
			stopwatch.stop();
			// send the recorded time to statsd
			statsDClient.recordExecutionTime("timer.v1.bill.id.api.put", stopwatch.elapsed(TimeUnit.MILLISECONDS));
			return new ResponseEntity<Bill>(bill, HttpStatus.OK);
		}
	}

	public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty())
			return false;
		return true;
	}

	// Post File for the bill into the S3 bucket
	// Post File for the bill
	@RequestMapping(value = "/v1/bill/{id}/file", method = RequestMethod.POST)
	public ResponseEntity<FileUpload> uploadFile(@PathVariable String id,
			@RequestParam("file") MultipartFile attachment, final HttpServletRequest request) {

		statsDClient.incrementCounter("endpoint.v1.bill.id.file.api.post");
		logger.info("Post a file for a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		/** Below data is what we saving into database */
		Bill billById = billService.getBillById(id);

		FileUpload d = null;
		if (attachment.isEmpty()) {
			return new ResponseEntity<FileUpload>(HttpStatus.OK);
		}
//         if(!billById.getAttachment().equals(null)) {
//             return new ResponseEntity<String>("Already a file is attached", HttpStatus.BAD_REQUEST);
//         }

		if (!attachment.getContentType().equals("image/jpg") && !attachment.getContentType().equals("image/jpeg")
				&& !attachment.getContentType().equals("application/pdf")
				&& !attachment.getContentType().equals("image/png")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (isNullOrEmpty(billById.getAttachment())) {
			try {
				/** File will get saved to file system and database */
				d = saveUploadedFiles(billById, Arrays.asList(attachment));
			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		stopwatch.stop();
		// send the recorded time to statsd
		statsDClient.recordExecutionTime("timerS3Post.v1.bill.id.file.api.post", stopwatch.elapsed(TimeUnit.MILLISECONDS));
		return new ResponseEntity<FileUpload>(d, HttpStatus.OK);

	}

	// private static final String UPLOADED_FOLDER =
	// System.getProperty("user.dir")+"/src/main/resources/static/images/";
	private static String UPLOADED_FOLDER = "/home/hemant/CSYE6225-workspace/";

	private FileUpload saveUploadedFiles(Bill billById, List<MultipartFile> files) throws IOException {
		FileUpload metaData = new FileUpload();
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue;
			}
//
//			byte[] bytes = file.getBytes();
//			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//			Files.write(path, bytes);

			metaData.setFile_name(file.getOriginalFilename() + new Date().toString());
			metaData.setUpload_date(new Date().toString());
			String url = this.amazonClient.uploadFile(file);
			metaData.setUrl(url);
			metaData.setId(UUID.randomUUID().toString());

			ObjectMapper mapperObj = new ObjectMapper();
			Map<String, Object> displayFile = new HashMap<>();

			displayFile.put("file_name", metaData.getFile_name());
			displayFile.put("id", metaData.getId());
			displayFile.put("url", metaData.getUrl());
			displayFile.put("upload_date", metaData.getUpload_date());

			mapperObj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			String jsonResp = mapperObj.writeValueAsString(displayFile);
			System.out.println(jsonResp);

			Gson gson = new Gson();

			// convert java object to JSON format
			String json = gson.toJson(jsonResp);

			billById.setAttachment(jsonResp);
			billService.updateBill(billById);

			fileService.addFile(metaData);
			// this.amazonClient.uploadFile(file);
			// fileService.updateFiles(files);

		}
		return metaData;
	}

	// Get file for the bill from the S3 bucket
	@RequestMapping(value = "/v1/bill/{billId}/file/{fileId}", method = RequestMethod.GET)
	public ResponseEntity<FileUpload> getFileById(@PathVariable("billId") String billId,
			@PathVariable("fileId") String fileId) {

		statsDClient.incrementCounter("endpoint.v1.bill.billId.file.fileId.api.get");
		logger.info("Get a file for a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<FileUpload>(HttpStatus.BAD_REQUEST);

		} else {

			if (billId == null) {
				return new ResponseEntity<FileUpload>(HttpStatus.NO_CONTENT);
			}

			else {
				FileUpload file = fileService.getFileById(fileId);
				if (file == null) {
					return new ResponseEntity<FileUpload>(HttpStatus.NOT_FOUND);
				}

				stopwatch.stop();
				// send the recorded time to statsd
				statsDClient.recordExecutionTime("timerS3Get.v1.bill.billId.file.fileId.api.get",
						stopwatch.elapsed(TimeUnit.MILLISECONDS));

				return new ResponseEntity<FileUpload>(file, HttpStatus.OK);
			}
		}
	}

//Delete the file for the bill from S3bucket    
	@RequestMapping(value = "/v1/bill/{billId}/file/{fileId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFile(@PathVariable("billId") String billId, @PathVariable("fileId") String fileId)
			throws IOException {

		statsDClient.incrementCounter("endpoint.v1.bill.billId.file.fileId.api.delete");
		logger.info("Delete a file for a single bill for the user");
		Stopwatch stopwatch = Stopwatch.createStarted();
		boolean isSuccess = false;
		if (billId == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {

			if (SecurityContextHolder.getContext().getAuthentication() != null
					&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
				// response.put("message", "you are not logged in!!!");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			} else {

				Bill billById = billService.getBillById(billId);
				FileUpload file = fileService.getFileById(fileId);

				if (file == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				String imageUrl = file.getUrl();
				this.amazonClient.deleteFileFromS3Bucket(imageUrl);

				isSuccess = fileService.deleteFile(fileId);

				ObjectMapper mapperObj = new ObjectMapper();
				Map<String, Object> displayFile = new HashMap<>();

				displayFile.put("file_name", "");
				displayFile.put("id", "");
				displayFile.put("url", "");
				displayFile.put("upload_date", "");

				mapperObj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				String jsonResp = mapperObj.writeValueAsString(displayFile);
				System.out.println(jsonResp);

				billById.setAttachment(jsonResp);
				billService.updateBill(billById);

				if (isSuccess) {
					stopwatch.stop();
					// send the recorded time to statsd
					statsDClient.recordExecutionTime("timerS3Delete.v1.bill.billId.file.fileId.api.delete",
							stopwatch.elapsed(TimeUnit.MILLISECONDS));
					return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}

	}

}
