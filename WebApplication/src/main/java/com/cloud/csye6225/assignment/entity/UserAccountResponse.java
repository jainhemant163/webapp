/**
 * 
 */
package com.cloud.csye6225.assignment.entity;

/**
 * @author jainh
 *
 */
public class UserAccountResponse {

	public String id;
    private String email_address;
    private String password;
    private String first_name;
    private String last_name;
    private String account_created;
    private String account_updated;

    public UserAccountResponse() {

    }

  
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * @return the email_address
	 */
	public String getEmail_address() {
		return email_address;
	}

	/**
	 * @param email_address the email_address to set
	 */
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}


	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the account_created
	 */
	public String getAccount_created() {
		return account_created;
	}

	/**
	 * @param account_created the account_created to set
	 */
	public void setAccount_created(String account_created) {
		this.account_created = account_created;
	}

	/**
	 * @return the account_updated
	 */
	public String getAccount_updated() {
		return account_updated;
	}

	/**
	 * @param account_updated the account_updated to set
	 */
	public void setAccount_updated(String account_updated) {
		this.account_updated = account_updated;
	}

   

   

   

    
}
