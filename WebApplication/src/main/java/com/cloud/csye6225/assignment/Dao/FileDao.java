/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import org.springframework.web.multipart.MultipartFile;

import com.cloud.csye6225.assignment.entity.FileUpload;

/**
 * @author jainh
 *
 */
public interface FileDao {
	  FileUpload getFileById(String id);

	  boolean deleteFileById(String id);

	    void insertFile(FileUpload file);

}
