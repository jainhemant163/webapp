/**
 * 
 */
package com.cloud.csye6225.assignment.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.csye6225.assignment.Dao.BillDaoImpl;
import com.cloud.csye6225.assignment.entity.Bill;


/**
 * @author jainh
 *
 */
@Service
public class BillService {

	  @Autowired
	    private BillDaoImpl billDaoImpl;

	    public Collection<Bill> getAllBill(){
	        return this.billDaoImpl.getAllBill();
	    }

	    public Bill getBillById(String id){
	        return this.billDaoImpl.getBillById(id);
	    }

	    public void updateBill(Bill bill){
	        this.billDaoImpl.updateBill(bill);
	    }

	    public void addBill(Bill bill){

	        this.billDaoImpl.insertBill(bill);
	    }

	    public void delete(Bill bill){
	        this.billDaoImpl.deleteBill(bill);
	    }

	    public boolean deleteBillById(String id){
	        return this.billDaoImpl.deleteBillById(id);
	    }

}
