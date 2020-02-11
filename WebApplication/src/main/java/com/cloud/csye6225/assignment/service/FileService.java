/**
 * 
 */
package com.cloud.csye6225.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.csye6225.assignment.Dao.FileDaoImpl;
import com.cloud.csye6225.assignment.entity.FileUpload;

/**
 * @author jainh
 *
 */

@Service
public class FileService {

	 @Autowired
	    private FileDaoImpl fileDaoImpl;

	    public void addFile(FileUpload file) {
	        this.fileDaoImpl.insertFile(file);
	    }

	    public boolean deleteFile(String id) {
	       return this.fileDaoImpl.deleteFileById(id);
	    }

	    public FileUpload getFileById(String id) {

	        FileUpload file = this.fileDaoImpl.getFileById(id);

	        if(file == null) return null;

	        return file;
	    }
	 
	    public void updateFiles(FileUpload files) {
	        this.fileDaoImpl.updateFiles(files);

	    }



}
