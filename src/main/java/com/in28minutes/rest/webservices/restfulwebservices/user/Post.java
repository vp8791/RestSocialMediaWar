package com.in28minutes.rest.webservices.restfulwebservices.user;

import javax.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="USER_POSTS")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQUENCE")
	@SequenceGenerator(sequenceName = "post_sequence", allocationSize = 1, name = "POST_SEQUENCE")
	private Long id;
	
	private String description;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="USER_ID")
	@JsonIgnore
	private User user;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return String.format("Post [id=%s, description=%s]", id, description);
	}
	
}
