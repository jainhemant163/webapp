/**
 * 
 */
package com.cloud.csye6225.assignment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.entity.UserAccountResponse;
import com.cloud.csye6225.assignment.service.BillService;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtilImpl;
import com.cloud.csye6225.assignment.util.PasswordUtil;
import com.google.gson.Gson;

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
	PasswordUtil passwordUtil;

	// Post Request for Bill
	@RequestMapping(value = "/v1/bill", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> registerPost(@RequestBody String bill) {

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
			// billObj.setCategories(categories);
			// billObj.setPaymentStatus(bill);
			// billObj.setVendor(account.get);

			billService.addBill(billObj);
			return new ResponseEntity<>(billObj, HttpStatus.OK);
		}
	}

	// GEt all bills
	@RequestMapping(value = "/v1/bills", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Collection<Bill>> registerGet() {
//      if() return null;// log in or not
		// get user id after log in

		Collection<Bill> bills1=null;
		// check whether user logged in using the basic auth credentials or not
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} else {

			UserAccount account = accountService.currentUser;

			Collection<Bill> bills = billService.getAllBill();

			bills1 = bills.stream().filter(p -> p.getOwner_id().equals(account.getId()))
					.collect(Collectors.toList());
			return new ResponseEntity<>(bills1, HttpStatus.OK);

		}
		

	}

	// Get bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> registerGet(@PathVariable("id") String billId) {
//        if() return null;// log in or not
		// get user id after log in

		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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

				return new ResponseEntity<>(bill, HttpStatus.OK);
			}
		}
	}

	// DELETE API
	// Delete bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteBillById(@PathVariable("id") String billId) {

			boolean isSuccess = false;
			if (billId == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				
				
				Bill bill = billService.getBillById(billId);
				if (SecurityContextHolder.getContext().getAuthentication() != null
						&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
					// response.put("message", "you are not logged in!!!");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

				}
				else{
				
				if (bill == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				isSuccess = billService.deleteBillById(billId);

				if (isSuccess)
					return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
				else
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	// Put bill by id
	@RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Bill> updateBill(@PathVariable("id") String billId, @RequestBody Bill infos) {
//        if() return null;// log in or not
		// get user id after log in
		
		Bill bill = billService.getBillById(billId);
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			// response.put("message", "you are not logged in!!!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} else {

			// bill.setOwner_id(infos.getOwner_id());
			bill.setAmount_due(infos.getAmount_due());
			bill.setVendor(infos.getVendor());
			bill.setBill_date(infos.getBill_date());
			bill.setDue_date(infos.getDue_date());

			billService.updateBill(bill);
			return new ResponseEntity<>(bill, HttpStatus.OK);
		}
	}

}
