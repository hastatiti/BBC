import java.util.Map;

public interface HttpResponseService {

	// Starting point of the program
	void start();

	// Check validity of URLs
	boolean isValidURL(String theURL);

	/* Return a URLConnection instance, create connection with valid URLs, populate map, create and print JSON format
	For invalid URLs populate map without connection, create and print JSON error format*/
	Map<String, String> getHttpResponse(String theURL);

	/* Gson framework for representation-required pattern
	Create gson instance with GsonBuilder to have some additional configuration*/
	String printJSON(String print, Map map);

	/* Another JSON format for counting responses*/
	String printJSON();
	
	/* statusCodeMap in form of {301,1},{200,2}
	sortedStatusCodeMap sorts above map by value {200,2},{301,1}
	finalStatusCodeMap is in form of {Status_code : 301, Number_of_responses : 1}*/
	Map<String, Integer> getFinalStatusCodeMap();
	
}