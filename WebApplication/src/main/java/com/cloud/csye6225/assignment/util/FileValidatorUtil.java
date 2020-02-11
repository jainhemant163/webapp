/**
 * 
 */
package com.cloud.csye6225.assignment.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author jainh
 *
 */
public interface FileValidatorUtil {
	public boolean isFileFormat(MultipartFile str);
}
