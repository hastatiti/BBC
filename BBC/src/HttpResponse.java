import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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
			URL requestURL = new URL(theURL);
			HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
				if (isValidURL(theURL)) {
					System.out.println("vvvv");
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
