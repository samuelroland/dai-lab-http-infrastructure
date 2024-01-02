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
			assertEquals(null, givenComment.parent_id);


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
			var res = client.post("/comments/10");
			assertEquals(200, res.code());
			// TODO créer un commentaire id = 10 avec un contenu et check ici
			// si le contenu est bien correct avec quoi l'ajout est donc fonctionnel

		});
	}

	@Test
	public void deleteComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.delete("/comments/5");
			assertEquals(200, res.code());
			// TODO check si le contenu du commentaire supp n'est plus dispo ?
		});
	}

	@Test
	public void updateComment() {
		JavalinTest.test(app, (server, client) -> {
			var res = client.put("/comments/6");
			assertEquals(200, res.code());
			// TODO check si le contenu du commentaire a bien été modifié ?
		});
	}
}