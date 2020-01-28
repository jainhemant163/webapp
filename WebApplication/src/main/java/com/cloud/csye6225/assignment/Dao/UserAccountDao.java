/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import java.util.Collection;

import com.cloud.csye6225.assignment.entity.UserAccount;

/**
 * @author jainh
 *
 */
public interface UserAccountDao {
	 Collection<UserAccount> getAllAccounts();

	 UserAccount getAccountByEmail(String email);

	    void updateAccount(UserAccount account);

	    void insertAccount(UserAccount account);
}
