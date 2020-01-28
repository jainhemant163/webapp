/**
 * 
 */
package com.cloud.csye6225.assignment.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author jainh
 *
 */
public class CryptPassword {
	  public BCrypt bct;

	    public CryptPassword() {
	        this.bct = new BCrypt();
	    }

	    public String hashPassword(String password, String salt) {

	        String passwordHash = bct.hashpw(password, salt);

	        return passwordHash;
	    }

	    public String generateSalt() {
	        return bct.gensalt();
	    }
}
