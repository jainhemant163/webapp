package com.cloud.csye6225.assignment;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = WebApplicationTests.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	
	public void testGetAllUsers() throws URISyntaxException {
		
		  RestTemplate restTemplate = new RestTemplate();
	        
	        final String baseUrl = "http://localhost:"+port+"/";
	        URI uri = new URI(baseUrl);       		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(uri + "/",
				HttpMethod.GET, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

////	@Test
////	public void testGetUserById() throws URISyntaxException {
////		   final String baseUrl = "http://localhost:"+port+"/";
////	        URI uri = new URI(baseUrl);       		
//////		HttpHeaders headers = new HttpHeaders();
//////		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
////		User user = restTemplate.getForObject(uri + "/v1/user/self", User.class);
////		System.out.println(user.getUsername());
////		Assert.assertNotNull(user);
////	}
//
////	@Test
////	public void testCreateUser() {
////		User user = new User();
////		user.setEmail("admin@gmail.com");
////		user.setFirstName("admin");
////		user.setLastName("admin");
////		
////
////		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);
////		Assert.assertNotNull(postResponse);
////		Assert.assertNotNull(postResponse.getBody());
////	}
////
////	@Test
////	public void testUpdatePost() {
////		int id = 1;
////		User user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
////		user.setFirstName("admin1");
////		user.setLastName("admin2");
////
////		restTemplate.put(getRootUrl() + "/users/" + id, user);
////
////		User updatedUser = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
////		Assert.assertNotNull(updatedUser);
////	}
//
////	@Test
////	public void testDeletePost() {
////		int id = 2;
////		User user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
////		Assert.assertNotNull(user);
////
////		restTemplate.delete(getRootUrl() + "/users/" + id);
////
////		try {
////			user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
////		} catch (final HttpClientErrorException e) {
////			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
////		}
////	}
}