package ua.model.view;

import java.util.List;

import ua.entity.Comment;

public class IngredientView {
	
	private String id;

	private String name;
	
	private List<Comment> comments;
	
	public IngredientView(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
