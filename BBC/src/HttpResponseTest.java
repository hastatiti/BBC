import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class HttpResponseTest {
	@Test
	void urlMustBeValid() {
		assertTrue(new HttpResponse().isValidURL("http://www.bbc.co.uk/iplayer"));
		assertTrue(new HttpResponse().isValidURL("https://www.google.co.uk/"));
		assertFalse(new HttpResponse().isValidURL("bad://address"));
		assertFalse(new HttpResponse().isValidURL("hp:// www.bbc.co.uuk/iplayerrrrr"));
	}
}
