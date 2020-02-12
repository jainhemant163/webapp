/**
 * 
 */
package com.cloud.csye6225.assignment.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.entity.FileUpload;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.entity.UserAccountResponse;
import com.cloud.csye6225.assignment.service.FileService;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtilImpl;
import com.cloud.csye6225.assignment.util.FileValidatorUtil;
import com.cloud.csye6225.assignment.util.PasswordUtil;

/**
 * @author jainh
 *
 */
@RestController
@RequestMapping("/v1/bill")
public class FileController {

	@Autowired
	private UserAccountService accountService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EmailValidationUtilImpl emailValidationUtil;

	@Autowired
	private FileService fileService;

	@Autowired
	PasswordUtil passwordUtil;

    @Autowired
    FileValidatorUtil filevalidatorUtil;

//    }


}
