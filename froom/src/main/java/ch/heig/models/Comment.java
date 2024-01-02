package ch.heig.models;

import java.util.Date;

public class Comment {
	// Attributes are public because Jackson (the JSON parser) needs to access it
	public Integer id;
	public String content;
	public Date date;
	public Integer parent_id;

	// This default constructor is important to not get annoying parsing errors from Jackson
	public Comment() {
	}

	public Comment(Integer id, String content, Date date, Integer parent_id) {
		this.id = id;
		this.content = content;
		this.date = date;
		this.parent_id = parent_id;
	}
}
