package ch.heig.controllers;

import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import ch.heig.models.Comment;

public class CommentsController {

    private ConcurrentHashMap<Integer, Comment> comments = new ConcurrentHashMap<>();
    private int lastId = 0;

    public CommentsController() {
        comments.put(++lastId, new Comment(lastId, "bonjour c'est un test", LocalDate.now().minusDays(1), null));
        comments.put(++lastId, new Comment(lastId, "Il pleut aujourd'hui", LocalDate.now().minusDays(1), null));
        comments.put(++lastId, new Comment(lastId, "J'aime les mandarines", LocalDate.now().minusDays(1), null));
        comments.put(++lastId, new Comment(lastId, "L'eau, dans 20-30 ans y'en aura plus", LocalDate.now().minusDays(1), null));

    }

    public void getOne(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        ctx.json(comments.get(id));
    }

    public void getAll(Context ctx) {
        ctx.json(comments);
    }

    public void post(Context ctx) {
        Comment comment = ctx.bodyAsClass(Comment.class);
        comments.put(++lastId, comment);
        ctx.status(201);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Comment comment = ctx.bodyAsClass(Comment.class);
        comments.put(id, comment);
        ctx.status(200);
    
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        comments.remove(id);
        ctx.status(204);
    }
    
}
