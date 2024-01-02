package ch.heig;

import ch.heig.controllers.CommentsController;
import io.javalin.Javalin;

public class Main {
	static final String HELLO_MESSAGE = "Welcome on the Froom API. I hope you are Hapi ?";
	static final int PORT = 7000;
	static Javalin server;

	public static void main(String[] args) {
		server = setupApp().start(PORT);

		server.after(ctx -> System.out.println("Quitting server..."));
	}

	// Separated method to easily test the server
	public static Javalin setupApp() {
		// Create the Javalin server and start of given port
		Javalin app = Javalin.create();

		// Define a GET route on / to send the HELLO_MESSAGE
		app.get("/", ctx -> ctx.result(HELLO_MESSAGE));

		// Defines routes for comments

		CommentsController commentsController = new CommentsController();

		app.get("/comments/{id}", commentsController::getOne);
		app.get("/comments", commentsController::getAll);
		app.post("/comments", commentsController::create);
		app.delete("/comments/{id}", commentsController::delete);
		app.put("/comments/{id}", commentsController::update);


		return app;
	}
}
