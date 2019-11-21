/*
 * API testing using RestAssured
 * API : "https://istheapplestoredown.com/api/v1/status/worldwide"
 */

/*
 * Author: Priyanka Malleshaiah
 */

package com.applestore.apple;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.restassured.path.json.JsonPath;

public class StoreStatus {

	@Test
	public void checkResponse() throws IOException {
	
		//Checking the response code
		URL url = new URL("https://istheapplestoredown.com/api/v1/status/worldwide"); 
		HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 	
		conn.connect();
		int responseCode = conn.getResponseCode();
		
		if(responseCode != 200)
			throw new RuntimeException("Http ResponseCode is: " +responseCode);
		//Checking for status
		else {
			//Receiving response
			String response = given()
					.when()
					.get("https://istheapplestoredown.com/api/v1/status/worldwide")
					.then()
					.extract().response().asString();
		
			JsonPath jPath = new JsonPath(response);
		
			Map<String,String> jMap = jPath.getMap("$");
			Set<String> jMapSet = jMap.keySet();
		
			ObjectMapper objMapper = new ObjectMapper();
			
			// Creating jsonTree to access nodes
			JsonNode jTree = objMapper.readTree(response);
		
			ObjectNode objNode = objMapper.createObjectNode();
		
			String expStatus = "y";
			String actStatus;
		
			for(String str:jMapSet) {
				objNode.set(str, jTree.get(str).get("status"));
				actStatus = jTree.get(str).get("status").asText();
				
				// Checking the status of every Country
				if (jTree.get(str).get("status").asText().contains("y")){
					System.out.println("Country name with status:'y' is:" + jTree.get(str).get("name"));
				}  
				else
					assertEquals(actStatus, expStatus);
			} 
		}
	} 

}


