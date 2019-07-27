import java.net.URL;
import java.util.Scanner;

public class HttpResponse {
	private static boolean isValidURL(String theURL) {
		try {
			// Create a URL object from the String representation
			URL requestURL = new URL(theURL);
			// Use java.net.url class to validate the url
			requestURL.toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
public static void main(String[] args) {
	Scanner scanner = new Scanner(System.in);
	String theURL = null;
	while (scanner.hasNextLine()) {
		theURL = scanner.nextLine();
		isValidURL(theURL);
	}
}
}
