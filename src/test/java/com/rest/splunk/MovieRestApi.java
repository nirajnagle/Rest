package com.rest.splunk;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class MovieRestApi {
	static RequestSpecBuilder builder;
	static RequestSpecification requestSpec;

@BeforeClass
public static void init() {
	// building the api path
	RestAssured.baseURI = "https://splunk.mocklab.io";
	
	builder = new RequestSpecBuilder();
	builder.addQueryParam("q","batman");
	builder.addHeader("Accept", "application/json");
	
	requestSpec = builder.build();
	
	}


//TestCase:SPL-001
//TestCase: No two movies should have the same image 	
//from the reposnse split the the string that contains .jpg 
//Loop through them and Add them to list and set. Then Compare two objects using assertequals()	
	//Our test returns passed if two Collections are not same 
	@Test
	public void SP001() throws Exception{
		final String SEPARATOR = ",";
		List<String> poster_path_image; 
		poster_path_image= given()
		.spec(requestSpec)
		.when()
		.get("/movies")
		.then()
		.extract()
		.path("results.poster_path");
		
		
		//convert the list to string 
		StringBuilder poster = new StringBuilder();
		  for(String str : poster_path_image){
		    poster.append(str);
		    poster.append(SEPARATOR);
		  }
		  String st = poster.toString();
		  st = st.substring(0, st.length() - SEPARATOR.length());
		  
		  String[]  poster_split=st.split("[/,]");
		  
		  //System.out.println(Arrays.toString(poster_split));
		 // System.out.println(poster_split[2]);
		  List <String> list=new ArrayList<String>();
		  Set <String> set =new LinkedHashSet<String>();
		  
		  for (int i = 0; i < poster_split.length; i++) {			 
			  if (poster_split[i].contains("jpg"))
				  list.add(poster_split[i]);
		  }
		  for (int j = 0; j < poster_split.length; j++) {
			  if (poster_split[j].contains("jpg"))
				  set.add(poster_split[j]);
			
		}
		
		assertEquals(list, set);    			
		  
	}
	







}
