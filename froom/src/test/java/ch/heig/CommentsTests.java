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

import java.time.LocalDate;

import ch.heig.models.*;

public class CommentsTests {

	Javalin app = Main.setupApp();

	// Easy parsing of any JSON string into a object of the given class
	// It uses Jackson
	private Comment parseAs(String text) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(text, Comment.class);
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

	@Test
	public void getOneReturnsOneComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.get("/comments/4");
			Comment givenComment = parseAs(res.body().string());
			assertEquals(200, res.code());
			assertEquals("L'eau, dans 20-30 ans y'en aura plus", givenComment.content);
			assertEquals(4, givenComment.id);
			assertEquals(1, givenComment.parent_id);
		});
	}

	@Test
	public void getOneFailsOnNonExistantComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.get("/comments/420000");
			assertEquals(404, res.code());
		});
	}

	@Test
	public void getOneFailsWithNonValidId() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.get("/comments/bonjour");
			assertEquals(404, res.code());
		});
	}

	@Test
	public void getAllReturnsAllComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.get("/comments");
			assertEquals(200, res.code());
		});
	}

	@Test
	public void postCreateComment() {
		JavalinTest.test(app, (server, client) -> {
			String body = "{\"content\": \"Hello this is a test\", \"parent_id\": null}";
			var res = client.post("/comments", body);
			Comment createdComment = parseAs(res.body().string());
			assertEquals(201, res.code());
			assertEquals("Hello this is a test", createdComment.content);
			assertEquals(null, createdComment.parent_id);
			assertNotEquals(null, createdComment.id);
			assertNotEquals(null, createdComment.date);

		});
	}

	@Test
	public void deleteComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.delete("/comments/5");
			assertEquals(204, res.code());
			var res2 = client.get("/comments/5");
			assertEquals(404, res2.code());
		});
	}

	@Test
	public void updateComment() {
		JavalinTest.test(app, (server, client) -> {
			String body = "{\"content\": \"Hello this is an other test\", \"parent_id\": 3}";
			var res = client.put("/comments/4", body);
			assertEquals(200, res.code());
			Comment updatedComment = parseAs(res.body().string());
			assertEquals("Hello this is an other test", updatedComment.content);
			assertEquals(3, updatedComment.parent_id);
			assertEquals(4, updatedComment.id);
			assertNotEquals(null, updatedComment.date);
		});
	}

	@Test
	public void updateNonExistantCommentFails() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.put("/comments/420000");
			assertEquals(404, res.code());
		});
	}
}