package ch.heig;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.After;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

	Javalin app = Main.setupApp();

	@Before
	public void setup() {
		app.start();
	}

	@After
	public void tearDown() {
		app.stop();
	}

	@Test
	public void home_page_returns_welcome_message() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.get("/");
			assertEquals(200, res.code());
			assertEquals(Main.HELLO_MESSAGE, res.body().string());
		});
	}
}