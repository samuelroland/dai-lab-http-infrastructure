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

public class CommentsTests {

	Javalin app = Main.setupApp();

	// Easy parsing of any JSON string into a object of the given class
	// It uses Jackson
	private Object parseAs(String text, Class<Object> classRef) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(text, classRef);
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
}