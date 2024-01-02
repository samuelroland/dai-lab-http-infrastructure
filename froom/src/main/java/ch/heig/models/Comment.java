package ch.heig.models;

public class Comment {
	// Attributes are public because Jackson (the JSON parser) needs to access it
	public Integer id;
	public String content;
	public long date;
	public Integer parent_id;

	// This default constructor is important to not get annoying parsing errors from Jackson
	public Comment() {
	}

	public Comment(Integer id, String content, long date, Integer parent_id) {
		this.id = id;
		this.content = content;
		this.date = date;
		this.parent_id = parent_id;
	}
}
