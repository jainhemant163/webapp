/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.Sql.SqlBill;

/**
 * @author jainh
 *
 */
@Repository
public class BillDaoImpl implements BillDao {

	@Autowired
	private SqlBill repo;

	BillDaoImpl() {
		this.repo = new SqlBill();
	}

	@Override
	public Collection<Bill> getAllBill() {
		return this.repo.getBill();
	}

	@Override
	public Bill getBillById(String id) {
		return this.repo.getBillById(id);
	}

	@Override
	public void updateBill(Bill bill) {
		this.repo.updateBill(bill);
	}

	@Override
	public void insertBill(Bill bill) {
		this.repo.addBill(bill);
	}

	@Override
	public void deleteBill(Bill bill) {
		this.repo.deleteBill(bill);
	}

	@Override
	public boolean deleteBillById(String id) {
		return this.repo.deleteBillById(id);
	}

}
