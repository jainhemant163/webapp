package com.cloud.csye6225.assignment.controller;



import com.alibaba.fastjson.JSON;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.entity.UserAccountResponse;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtil;
import com.cloud.csye6225.assignment.util.PasswordUtilImpl;
import com.cloud.csye6225.assignment.service.UserAccountService;
import com.cloud.csye6225.assignment.util.EmailValidationUtilImpl;

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

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> welcome() {

        HashMap<String, String> response = new HashMap<>();

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            response.put("message", "you are not logged in!!!");
        } else {
            //Call database, repository(userMapper),service
            response.put("message", "you are logged in. current time is " + new Date().toString());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.GET)
    public Collection<UserAccountResponse> getAllAccounts(){

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

        return views;

    }


    @RequestMapping(value = "/v1/user/self", method = RequestMethod.GET)
    public ResponseEntity<UserAccountResponse> getAccountByEmail(){

        UserAccount user_db = this.accountService.currentUser;
        UserAccount account = accountService.getAccountByEmail(user_db.getEmail());
        UserAccountResponse response = new UserAccountResponse();
        response.setId(account.getUserId());
        response.setFirst_name(account.getFirstName());
        response.setLast_name(account.getLastName());
        response.setEmail_address(account.getEmail());
        response.setAccount_created(account.getUser_created());
        response.setAccount_updated(account.getUser_update());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/user/self", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody UserAccount account){
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


    @RequestMapping(value = "/v1/user",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerPost(@RequestBody String jsonUser) {
        UserAccount account = JSON.parseObject(jsonUser, UserAccount.class);
        HashMap<String, String> response = new HashMap<>();

        String username = account.getEmail();
        String password = account.getPassword();
        if (null == username || username.equals("") || null == password || password.equals("")) {
            response.put("Warning", "Please enter username or password!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
            response.put("message", "You have registered successfully!");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("Warning", "The username already exists!");
            //return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    
    }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
////	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
////	public ModelAndView defaultPage() {
////
////	  ModelAndView model = new ModelAndView();
////	  model.addObject("title", "Spring Security Login Form - Database Authentication");
////	  model.addObject("message", "This is default page!");
////	  model.setViewName("hello");
////	  return model;
////
////	}
//	
//	
//	
//	
//	
//	
//	
//	
//
//  @Autowired
//  private UserRepository userRepository;
//  
//  private UserService userService;
//
//  /**
//   * Get all users list.
//   *
//   * @return the list
//   */
//  @GetMapping("/users")
//  public List<User> getAllUsers() {
//    return userRepository.findAll();
//  }
//
//  /**
//   * Gets users by id.
//   *
//   * @param userId the user id
//   * @return the users by id
//   * @throws ResourceNotFoundException the resource not found exception
//   */
//  @GetMapping("/users/{id}")
//  public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId)
//      throws ResourceNotFoundException {
//	  
//	  
//
//			
//			
//    User user =
//        userRepository
//            .findById(userId)
//            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
//	// Lookup user in database by e-mail
//	User userExists = userService.findByEmail(user.getEmail());
//	System.out.println(userExists);
//    return ResponseEntity.ok().body(user);
//  }
//
//  /**
//   * Create user user.
//   *
//   * @param user the user
//   * @return the user
//   */
//  @PostMapping("/users")
//  public User createUser(@Valid @RequestBody User user) {
//    return userRepository.save(user);
//  }
//
//  /**
//   * Update user response entity.
//   *
//   * @param userId the user id
//   * @param userDetails the user details
//   * @return the response entity
//   * @throws ResourceNotFoundException the resource not found exception
//   */
//  @PutMapping("/users/{id}")
//  public ResponseEntity<User> updateUser(
//      @PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails)
//      throws ResourceNotFoundException {
//
//    User user =
//        userRepository
//            .findById(userId)
//            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
//
//    user.setEmail(userDetails.getEmail());
//    user.setLastName(userDetails.getLastName());
//    user.setFirstName(userDetails.getFirstName());
//    user.setAccountUpdated(new Date());
//    final User updatedUser = userRepository.save(user);
//    return ResponseEntity.ok(updatedUser);
//  }
//
//  /**
//   * Delete user map.
//   *
//   * @param userId the user id
//   * @return the map
//   * @throws Exception the exception
//   */
//  @DeleteMapping("/user/{id}")
//  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
//	  
//	  
//    User user =
//        userRepository
//            .findById(userId)
//            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
//
//    userRepository.delete(user);
//    Map<String, Boolean> response = new HashMap<>();
//    response.put("deleted", Boolean.TRUE);
//    return response;
//  }


