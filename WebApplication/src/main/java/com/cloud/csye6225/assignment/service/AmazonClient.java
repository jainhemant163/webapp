package com.cloud.csye6225.assignment.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

@Service
public class AmazonClient {

//    private static final String endpointUrl = "https://s3.us-east-1.amazonaws.com";
//
//    @Value("${amazonProperties.accessKey}")
//    private String accessKey;
//    //private static final String accessKey = "AKIAI3X6B7NYDWD6CHKQ";
//    
//    @Value("${amazonProperties.secretKey}")
//    private String secretKey;
//    //private static final String secretKey = "z3tVN9hG5tKdist8fgrqIGKlpQhI259Y2iuux/Og";
//    
//    @Value("${amazonProperties.bucketName}")
//    private String bucketName;
//    //private static final String bucketName = "proddemo-s3bucket-ppx1s15j14s1";
//
//
//    private static String fileName="";
//
//    public AmazonS3 s3client;
//
//
//    public AmazonClient(){
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//        //this.s3client = new AmazonS3Client(credentials);
//        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
//    }

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

//    public ObjectMetadata getMetaData()
//    {
//        try{
//            GetObjectMetadataRequest request=new GetObjectMetadataRequest(bucketName,fileName);
//            return s3client.getObjectMetadata(request);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		// System.out.println(endpointUrl +"llllllllllllllllllllllllllll"+ bucketName);
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
		// .withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return "Successfully deleted";
	}

}