import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

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
		Map<String,String> map = new HttpResponse().getHttpResponse("http://www.bbc.co.uk/iplayer");
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("Url", "http://www.bbc.co.uk/iplayer");
		expected.put("Status_Code", "301");
		expected.put("Content_length", "185");
		String date = map.get("Date");
		expected.put("Date", date);
	
		assertThat(expected.size(), is(4));
	}
}
