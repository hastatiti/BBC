import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpResponse implements HttpResponseService {
	
	private Map<String, String> map = new LinkedHashMap<>(); // main map , 4 key/value pairs(URL,StatusCode,ContentLength,Date)
	private static int SERVER_TIMEOUT = 10000;
	private static Map<String,Integer> statusCodeMap = new LinkedHashMap<>(); // counting responses, {301,2}, {200,3} ...
	private Map<String,Integer> sortedStatusCodeMap = new LinkedHashMap<>(); // sort above map by value 
	private Map<String,Integer> finalStatusCodeMap = new LinkedHashMap<>(); // get key value from above map, {Status_code : 200, Number_of_responses : 3} ...
	
	/* Starting point of the program*/
	@Override
	public void start() { 
		System.out.println("Please press ENTER after the input : ");
		System.out.println("Please press ctrl+D to see number of responses : ");
		Scanner scanner = new Scanner(System.in);
		String theURL = null;
		while (scanner.hasNextLine()) { // get URLs line by line
			theURL = scanner.nextLine();
			//send URLs to process one by one
			HttpResponseService response = new HttpResponse();
			response.getHttpResponse(theURL);
		}
	}
	
	/* Check validity of URLs*/
	@Override
	public boolean isValidURL(String theURL) {
		try {
			/* Create a URL object from the String representation*/
			URL requestURL = new URL(theURL);
			/*Use java.net.url class to validate the url
			check if not starts with http:// or https://, has characters not allowed in a url*/
			requestURL.toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/* Return a URLConnection instance, create connection with valid URLs, populate map, create and print JSON format
	For invalid URLs populate map without connection, create and print JSON error format*/
	@Override
	public Map<String, String> getHttpResponse(String theURL) {
		String url, statusCode, contentLength, date = null;
		try {
				if (isValidURL(theURL)) {
					URL requestURL = new URL(theURL);
					HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection(); //connection
					
					/* Handle unresponsive server*/
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
					
				/* Populate statusCodeMap(1 key-value pair), seperate from other map(4 key-value pairs)*/
				int counter = 1;
				if (statusCodeMap.containsKey(statusCode)) {
					counter = statusCodeMap.get(statusCode) + 1;
					statusCodeMap.put(statusCode, counter);
				} else
					statusCodeMap.put(statusCode, counter); // End of statusCodeMap

					
					printJSON("notError",map); // main map in JSON format
				}else {
					map.put("Url", theURL.trim());
					map.put("Error", "invalid url");
					
					printJSON("error",map); // map in JSON format
				}
		}
		/* Server timeout*/
		catch (SocketTimeoutException e) {
		    System.err.println("Server not responsive for :" + SERVER_TIMEOUT + " seconds.");
		}
		catch(Exception e) {
			map.put("Url", theURL.trim());
			map.put("Error", "invalid url");
			
			printJSON("error",map); // map in JSON format
			}
		return map;
	}
	
	
	/* Gson framework for representation-required pattern
	Create gson instance with GsonBuilder to have some additional configuration*/
	@Override
	public String printJSON(String print, Map map) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		/* setPrettyPrinting()- to print line by line*/
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(map);
		if (print == "error") {
			System.err.println(json); // errors written to stderr
		} else
			System.out.println(json); // output written to stdout
		return json;
	}

	/* statusCodeMap in form of {301,1},{200,2}
	sortedStatusCodeMap sorts above map by value {200,2},{301,1}
	finalStatusCodeMap is in form of {Status_code : 301, Number_of_responses : 1}*/
	@Override
	public Map<String, Integer> getFinalStatusCodeMap() {
		sortedStatusCodeMap = sortByValue(statusCodeMap);
		for (Map.Entry me : sortedStatusCodeMap.entrySet()) {
			finalStatusCodeMap.put("Status_code", Integer.parseInt((String) me.getKey()));
			finalStatusCodeMap.put("Number_of_responses", (Integer) me.getValue());
			printJSON();
		}
		return finalStatusCodeMap;
	}
	/* Another JSON format for counting responses*/
	@Override
	public String printJSON() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		/* setPrettyPrinting()- to print line by line*/
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(finalStatusCodeMap);
		System.out.println(json); // output written to stdout
		return json;
	}
	
	/* Sort map by value for response count*/
	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap){
		/* Convert Map to List of Map*/
		List<Map.Entry<String, Integer>> list =
	                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
		/*  Sort list with Collections.sort(), provide a custom Comparator
		Try switch the o1 o2 position for a different order*/
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	            public int compare(Map.Entry<String, Integer> o1,
	                               Map.Entry<String, Integer> o2) {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });
		 Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
		return sortedMap;
	}
	
	public static void main(String[] args) {
		HttpResponseService response = new HttpResponse();
		response.start();
		response.getFinalStatusCodeMap();
	}
}
