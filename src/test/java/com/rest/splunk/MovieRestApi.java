package com.rest.splunk;

import org.testng.annotations.BeforeClass;

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

}
