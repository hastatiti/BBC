import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpResponse {
	
	private Map<String, String> map = new LinkedHashMap<>();
	private static int SERVER_TIMEOUT = 10000;
	private static Map<String,Integer> statusCodeMap = new LinkedHashMap<>();
	
	// Check validity of URLs
	public boolean isValidURL(String theURL) {
		try {
			// Create a URL object from the String representation
			URL requestURL = new URL(theURL);
			// Use java.net.url class to validate the url
			// check if not starts with http:// or https://, has characters not allowed in a url
			requestURL.toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Return a URLConnection instance, create connection with valid URLs, populate map, create and print JSON format
	// For invalid URLs populate map without connection, create and print JSON format
	public void getHttpResponse(String theURL) {
		String url, statusCode, contentLength, date = null;
		try {
				if (isValidURL(theURL)) {
					URL requestURL = new URL(theURL);
					HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection(); //connection
					
					// Handle unresponsive server
					conn.setConnectTimeout(SERVER_TIMEOUT);
					
					url = conn.getURL().toString(); // URL
					statusCode = Integer.toString(conn.getResponseCode()); // Status code
					contentLength = Integer.toString(conn.getContentLength()); // Content length
					long myDate = conn.getDate(); // Date
					Date theDate = new Date(myDate);
					DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
					date = dateFormat.format(theDate); // Formatted date
					
					map.put("Url", url);
					map.put("Status_Code", statusCode);
					map.put("Content_length", contentLength);
					map.put("Date", date);
					
					int counter = 1;
					
					if(statusCodeMap.containsKey(statusCode)) {
						System.out.println("yessss");
						counter = statusCodeMap.get(statusCode) +1;
					statusCodeMap.put(statusCode, counter);
					}else {
						System.out.println("hep burda");
						statusCodeMap.put(statusCode, counter);
					}
					
					printJSON();
					
				//	printJSON("notError"); // map in JSON format
				}else {
					map.put("Url", theURL.trim());
					map.put("Error", "invalid url");
					
				//	printJSON("error"); // map in JSON format
				}
		}
		// Server timeout
		catch (SocketTimeoutException e) {
		    System.err.println("Server not responsive for :" + SERVER_TIMEOUT + " seconds.");
		}
		catch(Exception e) {
			map.put("Url", theURL.trim());
			map.put("Error", "invalid url");
			
			printJSON("error"); // map in JSON format
			}
	}
	
	public String printJSON() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(statusCodeMap);
		System.out.println(json); 
		return json;
	}

	
	// Gson framework for representation-required pattern
	// Create gson instance with GsonBuilder to have some additional configuration
	public String printJSON(String print) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		// setPrettyPrinting()- to print line by line
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(map);
		if (print == "error") {
			System.err.println(json); // errors written to stderr
		} else
			System.out.println(json); // output written to stdout
		return json;
	}
	
	public static void main(String[] args) {
		System.out.println("Please press ENTER after the input : ");
		Scanner scanner = new Scanner(System.in);
		String theURL = null;
		while (scanner.hasNextLine()) {
			theURL = scanner.nextLine();
			HttpResponse response = new HttpResponse();
			response.getHttpResponse(theURL);
		}
	}
}
