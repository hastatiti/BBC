import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {
	HttpResponse response;
	
	@Before
	public void init() {
		 response = new HttpResponse();
	}
	@Test
	void urlMustBeValid() {
		assertTrue(new HttpResponse().isValidURL("http://www.bbc.co.uk/iplayer"));
		assertTrue(new HttpResponse().isValidURL("https://www.google.co.uk/"));
		assertFalse(new HttpResponse().isValidURL("bad://address"));
		assertFalse(new HttpResponse().isValidURL("hp:// www.bbc.co.uuk/iplayerrrrr"));
	}
	@Test
	void getHttpResponseMustPopulateMap() {
		Map<String,String> actual = new HttpResponse().getHttpResponse("http://www.bbc.co.uk/iplayer");
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("Url", "http://www.bbc.co.uk/iplayer");
		expected.put("Status_Code", "301");
		expected.put("Content_length", "185");
		String date = actual.get("Date");
		expected.put("Date", date);
	
		assertThat(expected.size(), is(4));
		assertThat(actual, IsMapContaining.hasEntry("Status_Code","301"));
		assertThat(actual, IsMapContaining.hasEntry("Content_length","185"));
		assertThat(actual, IsMapContaining.hasKey("Url"));
		assertThat(expected, is(actual));
	}
	
	@Test
	void printJSONMustReturnRequiredFormat() {
		Map<String,String> actual = new HttpResponse().getHttpResponse("http://www.bbc.co.uk/iplayer");
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("Url", "http://www.bbc.co.uk/iplayer");
		expected.put("Status_Code", "301");
		expected.put("Content_length", "185");
		String date = actual.get("Date");
		expected.put("Date", date);
		
		String s1 = new HttpResponse().printJSON("notError",actual);
		String s2 = new HttpResponse().printJSON("notError",expected);
		
		assertEquals(s1, s2);
	}
}
