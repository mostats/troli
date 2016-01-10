package com.troli;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * curl -i -H "Accept: application/json" http://localhost:8080/grocery/list/{customerId}
 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"customerId":"274598","barcode":"54B345YYC1","description":"cup of fruit","quantity":"1"}' http://localhost:8080/grocery/item
 * accounts should be put into a persistent data store
 */
@RestController
public class GroceryController {

	private static final Logger LOG = LoggerFactory.getLogger(GroceryController.class);
    private ConcurrentHashMap<Integer, GroceryList> accounts = new ConcurrentHashMap<Integer, GroceryList>();
    
    @RequestMapping(value = "/grocery/list/{customerId}", method = RequestMethod.GET)
    public GroceryList getGroceryList(@PathVariable("customerId") int customerId) {
    	LOG.info("GET Request for customerId: " + customerId);
    	return accounts.get(customerId);
    }
    /*
     * Upsert item
     */
    @RequestMapping(value = "/grocery/item", method = RequestMethod.POST)
    public ResponseEntity<String> upsertGroceryList(@RequestBody Grocery grocery) {
	  	LOG.info("value = " + grocery.getCustomerId());
	  	LOG.info("value = " + grocery.getDescription());
	  	
	  	// if account does not exist add it with new grocery, if exists add new grocery or increase quantity of existing one
	  	int customerId = grocery.getCustomerId();
	  	GroceryList customerGroceryList = accounts.get(customerId);
	  	Map<String, Grocery> list = new HashMap<String, Grocery>();
	  	if (customerGroceryList == null) {
	  		list.put(grocery.getBarcode(), grocery);
	  		accounts.put(customerId, new GroceryList(customerId, list));
	  	} else {
	  		list = customerGroceryList.getGroceryList();
	  		Grocery customerGrocery = list.get(grocery.getBarcode());
	  		if (customerGrocery == null) {
	  			list.put(grocery.getBarcode(), grocery);
	  		} else {
	  			customerGrocery.increaseQuantity();
	  		}
	  	}	  	
	  	LOG.info("valute = " + accounts.get(274598).getGroceryList().get("54B345YYC1").getDescription());
	  	LOG.info("valute = " + accounts.get(274598).getGroceryList().get("54B345YYC1").getCustomerId());
	  	return new ResponseEntity<String>(HttpStatus.OK);
    }
    
//    public ResponseEntity<String> upsertGroceryList(@RequestBody Grocery grocery) {
//	  	LOG.info("value = " + grocery.toString());
//	  	
//	  	// if account does not exist add it with new grocery, if exists add new grocery or increase quantity of existing one
//	  	int customerId = grocery.getCustomerId();
//	  	GroceryList customerGroceryList = accounts.get(customerId);
//	  	Map<String, Grocery> list = new HashMap<String, Grocery>();
//	  	if (customerGroceryList == null) {
//	  		list.put(grocery.getBarcode(), grocery);
//	  		accounts.put(customerId, new GroceryList(customerId, list));
//	  	} else {
//	  		list = customerGroceryList.getGroceryList();
//	  		Grocery customerGrocery = list.get(grocery.getBarcode());
//	  		if (customerGrocery == null) {
//	  			list.put(grocery.getBarcode(), grocery);
//	  		} else {
//	  			customerGrocery.increaseQuantity();
//	  		}
//	  	}	  	
//	  	return new ResponseEntity<String>(HttpStatus.OK);
//    }
}