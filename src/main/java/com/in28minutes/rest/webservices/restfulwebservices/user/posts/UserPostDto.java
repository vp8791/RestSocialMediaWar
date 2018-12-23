package com.in28minutes.rest.webservices.restfulwebservices.user.posts;

import java.io.Serializable;


/**
 * The persistent class for the USER_POSTS database table.
 * 
 */
public class UserPostDto implements Serializable {
	private static final long serialVersionUID = 7L;
	private long id;

	private String description;

	public UserPostDto() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}