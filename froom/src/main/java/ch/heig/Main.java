package ch.heig;

import ch.heig.controllers.CommentsController;
import io.javalin.Javalin;

public class Main {
	static final String HELLO_MESSAGE = "Welcome on the Froom API. I hope you are Hapi ?";
	static final int PORT = 7000;
	static Javalin app;

	public static void main(String[] args) {
		app = setupApp().start(PORT);
		app.before(ctx -> System.out.println("New request " + ctx.method() + " on " + ctx.path()));
	}

	// Separated method to easily test the server
	public static Javalin setupApp() {
		// Create the Javalin server and start of given port
		Javalin app = Javalin.create();

		// Define a GET route on / to send the HELLO_MESSAGE
		app.get("/", ctx -> ctx.result(HELLO_MESSAGE));

		// Defines routes for comments

		CommentsController commentsController = new CommentsController();

		app.get("/api/comments/{id}", commentsController::getOne);
		app.get("/api/comments", commentsController::getAll);
		app.post("/api/comments", commentsController::create);
		app.delete("/api/comments/{id}", commentsController::delete);
		app.put("/api/comments/{id}", commentsController::update);

		return app;
	}
}
