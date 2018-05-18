package com.rest.splunk;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
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
		List<String> poster_Name; 
		poster_Name= given()
		.spec(requestSpec)
		.when()
		.get("/movies")
		.then()
		.extract()
		.path("results.poster_path");
		
		
		//convert the list to string 
		StringBuilder poster = new StringBuilder();
		  for(String str : poster_Name){
		    poster.append(str);
		    poster.append(SEPARATOR);
		  }
		  String st = poster.toString();
		  st = st.substring(0, st.length() - SEPARATOR.length());
		  
		  String[]  posterNameSplit=st.split("[/,]");
		  
		  //System.out.println(Arrays.toString(poster_split));
		 // System.out.println(poster_split[2]);
		  List <String> imageList=new ArrayList<String>();
		  Set <String> imageSet =new LinkedHashSet<String>();
		  
		  for (int i = 0; i < posterNameSplit.length; i++) {			 
			  if (posterNameSplit[i].contains("jpg"))
				  imageList.add(posterNameSplit[i]);
		  }
		  for (int j = 0; j < posterNameSplit.length; j++) {
			  if (posterNameSplit[j].contains("jpg"))
				  imageSet.add(posterNameSplit[j]);
			
		}
		
		assertEquals(imageList, imageSet);    			
		  
	}

	//TestCase:SPL-002	// all poster path must be valid/null is acceptable
		@Test
		public void SP002() throws Exception  {
			List<String> posterImage;
	 
			posterImage= given()
			.spec(requestSpec)
			.when()
			.get("/movies")
			.then()
			.extract()
			.path("results.poster_path");
			
			String s= posterImage.toString();
		// link is valid when it starts with https:// also a null is acceptable.
		//	Method fails becasue one line starts with just "/". 
					  try {
					assertThat(s, anyOf(startsWith("https://www.dropbox.com"),isEmptyOrNullString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
			
		}

}
