/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import java.util.Collection;

import com.cloud.csye6225.assignment.entity.Bill;

/**
 * @author jainh
 *
 */
public interface BillDao {
	  Collection<Bill> getAllBill();

	    Bill getBillById(String id);

	    void updateBill(Bill bill);

	    void insertBill(Bill bill);

	    void deleteBill(Bill bill);

	    boolean deleteBillById(String id);
}
