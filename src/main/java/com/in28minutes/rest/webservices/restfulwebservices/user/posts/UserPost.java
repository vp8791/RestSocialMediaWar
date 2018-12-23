package com.in28minutes.rest.webservices.restfulwebservices.user.posts;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the USER_POSTS database table.
 * 
 */
@Entity
@Table(name="USER_POSTS")
@NamedQuery(name="UserPost.findAll", query="SELECT u FROM UserPost u")
public class UserPost implements Serializable {
	private static final long serialVersionUID = 7L;

	@Id
	@SequenceGenerator(name="USER_POSTS_ID_GENERATOR", sequenceName="POST_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_POSTS_ID_GENERATOR")
	private long id;

	private String description;

	@Column(name="USER_ID")
	private long userId;

	public UserPost() {
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}



}