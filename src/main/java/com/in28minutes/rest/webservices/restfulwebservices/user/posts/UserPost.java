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

	//bi-directional many-to-one association to UserDetail		
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private UserDetail userDetail;

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

	public UserDetail getUserDetail() {
		return this.userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

}