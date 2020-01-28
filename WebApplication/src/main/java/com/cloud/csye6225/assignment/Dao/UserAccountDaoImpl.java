/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.cloud.csye6225.assignment.Dao.UserAccountDao;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.Sql.Sql;

/**
 * @author jainh
 *
 */
@Repository
public class UserAccountDaoImpl implements UserAccountDao{
    private Sql repo;

    UserAccountDaoImpl(){
        this.repo = new Sql();
    }
    @Override
    public Collection<UserAccount> getAllAccounts() {
        return this.repo.getAccounts();
    }

    @Override
    public UserAccount getAccountByEmail(String email) {
        return this.repo.getAccountByEmail(email);
    }

    @Override
    public void updateAccount(UserAccount account) {
        this.repo.updateAccount(account);
    }

    @Override
    public void insertAccount(UserAccount account) {
        this.repo.addAccount(account);
    }
}
