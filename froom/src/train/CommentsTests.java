package ch.heig;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.After;

import io.javalin.Javalin;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.heig.models.*;

public class UsersTest {

	Javalin app = Main.setupApp();

	private User parseAsUser(String text) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(text, User.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Before
	public void setup() {
		app.start();
	}

	@After
	public void tearDown() {
		app.stop();
	}

	// @Test
	// public void get_all_users_returns_an_empty_list_at_start() {
	// 	JavalinTest.test(app, (server, client) -> {
	// 		var res = client.get("/users");
	// 		assertEquals(200, res.code());
	// 		assertEquals("[]", res.body().string());
	// 	});
	// }
}