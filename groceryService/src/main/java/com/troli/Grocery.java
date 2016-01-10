package com.troli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/*
 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"A27JK98","date":"01/05/2016","name":"value"}' http://localhost:8080/grocery
 * custId, barcode, desc, date, quantity 
 */
public class Grocery implements Serializable {

	private int customerId;
	private String barcode;
	private String description;
	private int quantity = 1;
//	private Date submitDate;
	Grocery() {
	}
	
	Grocery (int customerId, String barcode, String description) {
		this.customerId = customerId;
		this.barcode = barcode;
		this.description = description;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void increaseQuantity() {
		quantity+=1;
	}
	
//	public Date getsubmitDate() {
//		return submitDate;
//	}
//	
//	public void setSubmitDate(Date submitDate) {
//		this.submitDate = submitDate;
//	}
}
