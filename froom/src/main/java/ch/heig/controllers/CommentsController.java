package ch.heig.controllers;

import io.javalin.http.Context;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import ch.heig.models.Comment;

public class CommentsController {

    private ConcurrentHashMap<Integer, Comment> comments = new ConcurrentHashMap<>();
    private int lastId = 0;

    public CommentsController() {
        comments.put(++lastId, new Comment(lastId, "bonjour c'est un test", (new Date()).getTime() - 200, 3));
        comments.put(++lastId, new Comment(lastId, "Il pleut aujourd'hui", (new Date()).getTime() - 500, 5));
        comments.put(++lastId, new Comment(lastId, "J'aime les mandarines", (new Date()).getTime() - 1000, 2));
        comments.put(++lastId,
                new Comment(lastId, "L'eau, dans 20-30 ans y'en aura plus", (new Date()).getTime() - 10, 1));

    }

    private Integer checkIfCommentExist(String possibleID) {
        try {
            int id = Integer.parseInt(possibleID);

            return comments.containsKey(id) ? id : null;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void getOne(Context ctx) {
        Integer id = checkIfCommentExist(ctx.pathParam("id"));

        if (id != null) {
            ctx.json(comments.get(id));
        } else {
            ctx.status(404);
        }
    }

    public void getAll(Context ctx) {
        ctx.json(comments.elements());
    }

    public void create(Context ctx) {
        Comment comment = ctx.bodyAsClass(Comment.class);
        comment.date = new Date().getTime();
        comment.id = ++lastId;
        comments.put(lastId, comment);
        ctx.status(201);
        ctx.json(comment);
    }

    public void update(Context ctx) {
        Integer id = checkIfCommentExist(ctx.pathParam("id"));

        if (id != null) {
            Comment modifiedComment = ctx.bodyAsClass(Comment.class);
            Comment existingComment = comments.get(id);
            existingComment.content = modifiedComment.content;
            existingComment.parent_id = modifiedComment.parent_id;
            comments.put(id, existingComment);
            ctx.json(comments.get(id));
        } else {
            ctx.status(404);
        }

    }

    public void delete(Context ctx) {
        Integer id = checkIfCommentExist(ctx.pathParam("id"));

        if (id != null) {
            comments.remove(id);
            ctx.status(204);
        } else {
            ctx.status(404);
        }
    }

}
