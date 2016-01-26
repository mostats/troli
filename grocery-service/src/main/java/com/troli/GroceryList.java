package com.troli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  custId, barcode, grocery, quantity
 */
public class GroceryList implements Serializable {

	private int customerId;
	private Map<String, Grocery> list = new HashMap<String, Grocery>();
	
	GroceryList() {
	}
	
	GroceryList(int customerId, Map<String, Grocery> list) {
		this.customerId = customerId;
		this.list = list;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public Map<String, Grocery> getGroceryList() {
		return list;
	}
	
	public void add (Grocery grocery) {
		
	}
}
