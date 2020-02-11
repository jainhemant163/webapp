/**
 * 
 */
package com.cloud.csye6225.assignment.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jainh
 *
 */
@Component
public class FileValidatorUtilImpl implements FileValidatorUtil {
	@Override
	public boolean isFileFormat(MultipartFile uploadedFile) {

		boolean issuccess = false;

		MultipartFile file = uploadedFile;

//	       if(file.isEmpty() || file.getSize()==0)
//	           error.rejectValue("file", "Please select a file");
		if ((file.getContentType().toLowerCase().equals("image/jpg")
				|| file.getContentType().toLowerCase().equals("image/jpeg")
				|| file.getContentType().toLowerCase().equals("image/png")
				|| file.getContentType().toLowerCase().equals("application/pdf"))) {
			issuccess = true;
			return issuccess;
			// error.rejectValue("file", "jpg/png file types are only supported");
		}
		return issuccess;

	}
}
