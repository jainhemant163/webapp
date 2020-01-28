package com.cloud.csye6225.assignment.service;

import com.cloud.csye6225.assignment.Dao.UserAccountDaoImpl;
import com.cloud.csye6225.assignment.entity.UserAccount;
import com.cloud.csye6225.assignment.util.IdCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UserAccountService implements UserDetailsService {

//    @Autowired
//    private FakeAccountDaoImpl fakeAccountDaoImpl;

    private IdCrypto  idCrypto;

    public UserAccount currentUser;

    @Autowired
    private UserAccountDaoImpl accountDaoImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    UserAccountService(){
        try{
            this.idCrypto = new IdCrypto();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Collection<UserAccount> getAllAccounts(){
        List<UserAccount> accounts = (List<UserAccount>) this.accountDaoImpl.getAllAccounts();
        for(int i = 0; i < accounts.size(); i++){
        	UserAccount item = accounts.get(i);
            item.setUserId(UUID.randomUUID().toString());
        }
        return accounts;
    }

    public UserAccount getAccountByEmail(String email){

    	UserAccount result = this.accountDaoImpl.getAccountByEmail(email);
        if(result != null){
            result.userId = this.idCrypto.encrypt(String.valueOf(result.getId()));
            //result.userId = UUID.randomUUID().toString();
        }


        return result;
    }

    public void updateAccount(UserAccount account){
        this.accountDaoImpl.updateAccount(account);
    }

    public void addAccount(UserAccount account){

        this.accountDaoImpl.insertAccount(account);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    	UserAccount user = getAccountByEmail(s);
        this.currentUser = user;
        User.UserBuilder builder = null;

        if(user != null){
            builder = User.withUsername(s);
            System.out.println(user.getPassword());
            builder.password(user.getPassword());

            builder.roles();
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }

        return builder.build();

//        return null;
    }

}
