package com.troli;

import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;

public class PostTest {
	private static void createEmployee()
	{
	    final String uri = "http://localhost:8080/groceries";
	 
	    ArrayList<String> list = new ArrayList<String>();
//	    GroceriesVO newEmployee = new GroceriesVO(list);
//	 
//	    RestTemplate restTemplate = new RestTemplate();
//	    GroceriesVO result = restTemplate.postForObject( uri, newEmployee, GroceriesVO.class);
	 
	    System.out.println("hello");
	}
}
