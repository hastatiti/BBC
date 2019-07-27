import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class HttpResponse {
	private static boolean isValidURL(String theURL) {
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

	private static void getHttpResponse(String theURL) {
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
					System.out.println("vvvvv");
				}else System.out.println("not valid url");
		}catch(Exception e) {
			System.out.println("Errorrrrrr");
			}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String theURL = null;
		while (scanner.hasNextLine()) {
			theURL = scanner.nextLine();
			getHttpResponse(theURL);
		}
	}
}
