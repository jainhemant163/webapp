package com.cloud.csye6225.assignment.entity;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;


/**
 * @author jainh
 *
 */


public class Bill {

	private String id;
    private String created_ts;
    private String updated_ts;
    private String owner_id;
    private String vendor;
    private String bill_date;
    private String due_date;
    private double amount_due;

    private String[] categories;
    
    
    
    @Enumerated(EnumType.STRING)
    private Status paymentStatus;
    
    private String attachment;

public enum Status {
	 paid, due, past_due, no_payment_required;
}
public Bill() {
	
}


	public Bill(String id, String created_ts, String updated_ts, String owner_id, String vendor, String bill_date,
			String due_date, double amount_due) {
		this.id = id;
		this.created_ts = created_ts;
		this.updated_ts = updated_ts;
		this.owner_id = owner_id;
		this.vendor = vendor;
		this.bill_date = bill_date;
		this.due_date = due_date;
		this.amount_due = amount_due;
	}
	
	
	public Bill(String id, String created_ts, String updated_ts, String owner_id, String vendor, String bill_date,
			String due_date, double amount_due, Status paymentStatus) {
	
		this.id = id;
		this.created_ts = created_ts;
		this.updated_ts = updated_ts;
		this.owner_id = owner_id;
		this.vendor = vendor;
		this.bill_date = bill_date;
		this.due_date = due_date;
		this.amount_due = amount_due;
		this.paymentStatus = paymentStatus;
	}
	

	public Bill(String id, String created_ts, String updated_ts, String owner_id, String vendor, String bill_date,
			String due_date, double amount_due, String[] categories, Status paymentStatus) {
		super();
		this.id = id;
		this.created_ts = created_ts;
		this.updated_ts = updated_ts;
		this.owner_id = owner_id;
		this.vendor = vendor;
		this.bill_date = bill_date;
		this.due_date = due_date;
		this.amount_due = amount_due;
		this.categories = categories;
		this.paymentStatus = paymentStatus;
	}

	public Bill(String id, String created_ts, String updated_ts, String owner_id, String vendor, String bill_date,
			String due_date, @Min(0) double amount_due, String[] categories, Status paymentStatus, String attachment) {
		super();
		this.id = id;
		this.created_ts = created_ts;
		this.updated_ts = updated_ts;
		this.owner_id = owner_id;
		this.vendor = vendor;
		this.bill_date = bill_date;
		this.due_date = due_date;
		this.amount_due = amount_due;
		this.categories = categories;
		this.paymentStatus = paymentStatus;
		this.attachment = attachment;
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
	 * @return the created_ts
	 */
	public String getCreated_ts() {
		return created_ts;
	}


	/**
	 * @param created_ts the created_ts to set
	 */
	public void setCreated_ts(String created_ts) {
		this.created_ts = created_ts;
	}


	/**
	 * @return the updated_ts
	 */
	public String getUpdated_ts() {
		return updated_ts;
	}


	/**
	 * @param updated_ts the updated_ts to set
	 */
	public void setUpdated_ts(String updated_ts) {
		this.updated_ts = updated_ts;
	}


	/**
	 * @return the owner_id
	 */
	public String getOwner_id() {
		return owner_id;
	}


	/**
	 * @param owner_id the owner_id to set
	 */
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}


	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}


	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	/**
	 * @return the bill_date
	 */
	public String getBill_date() {
		return bill_date;
	}


	/**
	 * @param bill_date the bill_date to set
	 */
	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}


	/**
	 * @return the due_date
	 */
	public String getDue_date() {
		return due_date;
	}


	/**
	 * @param due_date the due_date to set
	 */
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public double getAmount_due() {
		return amount_due;
	}


	public void setAmount_due(double amount_due) {
		this.amount_due = amount_due;
	}


	/**
	 * @return the categories
	 */
	public String[] getCategories() {
		return categories;
	}


	/**
	 * @param categories the categories to set
	 */
	public void setCategories(String[] categories) {
		this.categories = categories;
	}


	/**
	 * @return the paymentStatus
	 */
	public Status getPaymentStatus() {
		return paymentStatus;
	}


	/**
	 * @param paymentStatus the paymentStatus to set
	 */
	public void setPaymentStatus(Status paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	

	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


	@Override
	public String toString() {
		return "Bill [id=" + id + ", created_ts=" + created_ts + ", updated_ts=" + updated_ts + ", owner_id=" + owner_id
				+ ", vendor=" + vendor + ", bill_date=" + bill_date + ", due_date=" + due_date + ", amount_due="
				+ amount_due + ", categories=" + Arrays.toString(categories) + ", paymentStatus=" + paymentStatus + "]";
	}
	
	
	
	

	}

