/**
 * 
 */
package com.cloud.csye6225.assignment.entity;

/**
 * @author jainh
 *
 */
public class FileUpload {
	String file_name;
	String id;
	String url;
	String upload_date;

	public FileUpload(String id, String url) {

		this.id = id;
		this.url = url;
	}
	
	public FileUpload(String file_name, String id, String url, String upload_date) {
		
		this.file_name = file_name;
		this.id = id;
		this.url = url;
		this.upload_date = upload_date;
	}

	public FileUpload() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(String upload_date) {
		this.upload_date = upload_date;
	}

	
}
