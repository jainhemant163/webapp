/**
 * 
 */
package com.cloud.csye6225.assignment.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jainh
 *
 */
@Configuration
public class BCryptPasswordEncoderBean {

	 @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
	}
