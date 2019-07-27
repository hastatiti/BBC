import java.net.HttpURLConnection;
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
	
	public boolean isValidURL(String theURL) {
		try {
			// Create a URL object from the String representation
			URL requestURL = new URL(theURL);
			// Use java.net.url class to validate the url
			requestURL.toURI();
			HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
			return true;
		} catch (Exception e) {
	//		System.out.println(theURL);
			return false;
		}
	}

	public void getHttpResponse(String theURL) {
		String url, statusCode, contentLength, date = null;
		try {
				if (isValidURL(theURL)) {
					URL requestURL = new URL(theURL);
					HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
					url = conn.getURL().toString();
					statusCode = Integer.toString(conn.getResponseCode());
					contentLength = Integer.toString(conn.getContentLength());
					long myDate = conn.getDate();
					Date theDate = new Date(myDate);
					DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
					date = dateFormat.format(theDate);
					
					map.put("URL", url);
					map.put("Status_Code", statusCode);
					map.put("Content_length", contentLength);
					map.put("Date", date);
					
					printJSON();
				}else {
					map.put("URL", theURL.trim());
					map.put("Error", "invalid url");
					
					printJSON();
				}
		}catch(Exception e) {
			map.put("URL", theURL.trim());
			map.put("Error", "invalid url");
			
			printJSON();
			}
	}
	
	public String printJSON() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(map);
		System.out.println(json);
		return json;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String theURL = null;
		while (scanner.hasNextLine()) {
			theURL = scanner.nextLine();
			HttpResponse response = new HttpResponse();
			response.getHttpResponse(theURL);
		}
	}
}
