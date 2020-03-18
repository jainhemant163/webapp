package com.cloud.csye6225.assignment.controller;



import com.alibaba.fastjson.JSON;
import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.entity.UserAccountResponse;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtil;
import com.cloud.csye6225.assignment.util.PasswordUtilImpl;
import com.google.common.base.Stopwatch;
import com.timgroup.statsd.StatsDClient;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtilImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
/**
 * @author jainh
 *
 */

@RestController
//@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailValidationUtilImpl emailValidationUtil;

    @Autowired
    private UserAccountService accountService;

    @Autowired
    PasswordUtilImpl passwordUtil;
    
    @Autowired
    private StatsDClient statsDClient;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> welcome() {
    	 statsDClient.incrementCounter("endpoint.api.get");
    	Stopwatch stopwatch = Stopwatch.createStarted();
 		    	 
        HashMap<String, String> response = new HashMap<>();

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            response.put("message", "you are not logged in!!!");
        } else {
            //Call database, repository(userMapper), service, retrieve info
            response.put("message", "you are logged in. current time is " + new Date().toString());
        }

        stopwatch.stop();
 		// send the recorded time to statsd
 		statsDClient.recordExecutionTime("timer.api.get", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.GET)
    public ResponseEntity<Collection<UserAccountResponse>> getAllAccounts(){

    	  statsDClient.incrementCounter("endpoint.v1.users.api.get");
    	  logger.info("Get all Users");
    	
    	
    	  if (SecurityContextHolder.getContext().getAuthentication() != null
                  && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
 		  
    		  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    		  
          } else {
              //Call database, repository(userMapper), service, retrieve info
          
        List<UserAccount> accounts = (List<UserAccount>) accountService.getAllAccounts();
        List<UserAccountResponse> views = new ArrayList<UserAccountResponse>();

        for(int i = 0; i < accounts.size(); i++){
        	UserAccountResponse item = new UserAccountResponse();
            item.setId(accounts.get(i).getUserId());
            
            item.setFirst_name(accounts.get(i).getFirstName());
            item.setLast_name(accounts.get(i).getLastName());
            item.setEmail_address(accounts.get(i).getEmail());
            item.setAccount_created(accounts.get(i).getUser_created());
            item.setAccount_updated(accounts.get(i).getUser_update());
            views.add(item);
        }

        return new ResponseEntity<Collection<UserAccountResponse>>(views, HttpStatus.OK);
       // return views;
    }
    }


    @RequestMapping(value = "/v1/user/self", method = RequestMethod.GET)
    public ResponseEntity<UserAccountResponse> getAccountByEmail(){

    	  statsDClient.incrementCounter("endpoint.v1.user.self.api.get");
    	  Stopwatch stopwatch = Stopwatch.createStarted();
    	  logger.info("user details");
    	 if (SecurityContextHolder.getContext().getAuthentication() != null
                 && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
		  
   		  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   		  
         } else {
        UserAccount user_db = this.accountService.currentUser;
        UserAccount account = accountService.getAccountByEmail(user_db.getEmail());
        UserAccountResponse response = new UserAccountResponse();
        response.setId(account.getUserId());
        response.setFirst_name(account.getFirstName());
        response.setLast_name(account.getLastName());
        response.setEmail_address(account.getEmail());
        response.setAccount_created(account.getUser_created());
        response.setAccount_updated(account.getUser_update());

        stopwatch.stop();
 		// send the recorded time to statsd
 		statsDClient.recordExecutionTime("timer.api.get", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    }

    @RequestMapping(value = "/v1/user/self", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody UserAccount account){
    	
  	  statsDClient.incrementCounter("endpoint.v1.user.self.api.put");
  	 logger.info("user details modify");
    	 if (SecurityContextHolder.getContext().getAuthentication() != null
                 && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
		  
   		  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   		  
         } else {
        UserAccount user_db = this.accountService.currentUser;
        if(account.getPassword() != null && !account.getPassword().equals(user_db.getPassword())){
            String passwordHash = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
            user_db.setPassword(passwordHash);
        }
        if(account.getFirstName() != null && !account.getFirstName().equals(user_db.getFirstName())){
            user_db.setFirstName(account.getFirstName());
        }
        if(account.getLastName() != null && !account.getLastName().equals(user_db.getLastName())){
            user_db.setLastName(account.getLastName());
        }

        HashMap<String, String> response = new HashMap<>();
        if(user_db == null){
            response.put("Warning", "The username is not exists!");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        else {
            	if(user_db.getEmail().equals(account.getEmail())){
                    this.accountService.updateAccount(user_db);
                    //response.put("message", "The account has been updated");
                    //return new ResponseEntity<>(response, HttpStatus.OK);
                    return new ResponseEntity<>("Updated Successfully",HttpStatus.OK);
                }
            	else {
            		return new ResponseEntity<>("Updation failed",HttpStatus.BAD_REQUEST);
            	}
            }
    }
    }


    @RequestMapping(value = "/v1/user",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerPost(@RequestBody String jsonUser) {
       
    	  statsDClient.incrementCounter("endpoint.v1.user.api.post");
    	  logger.info("new user register");
    	 if (SecurityContextHolder.getContext().getAuthentication() != null
                 && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
		  
   		  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   		  
         } else {
    	
    	UserAccount account = JSON.parseObject(jsonUser, UserAccount.class);
        HashMap<String, String> response = new HashMap<>();

        String username = account.getEmail();
        String password = account.getPassword();
//        if (null == username || username.equals("") || null == password || password.equals("")) {
//            response.put("Warning", "Please enter username or password!");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
        if (!emailValidationUtil.isEmail(username)) {
            response.put("Warning", "Please use a valid email address as your username");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!passwordUtil.isStrongPassword(password)) {
            response.put("Warning", "Your password is too weak!");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        account.setPassword(passwordHash);
        account.setUserId(UUID.randomUUID().toString());
        UserAccount user_db = accountService.getAccountByEmail(username);

        if (user_db == null) {
            accountService.addAccount(account);
            response.put("Message", "You have registered successfully!");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("Warning", "The username already exists!");
            //return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
         }
    
    }
}



