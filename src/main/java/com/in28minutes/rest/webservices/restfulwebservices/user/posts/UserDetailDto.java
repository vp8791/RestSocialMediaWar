package com.in28minutes.rest.webservices.restfulwebservices.user.posts;



import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the USER_DETAILS database table.
 * 
 */
public class UserDetailDto implements Serializable {
	private static final long serialVersionUID = 8L;
	private long id;
	
	private Date birthDate;
		
	private String name;
	private List<UserPostDto> userPosts;

	public UserDetailDto() {
	}



	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}




	public List<UserPostDto> getUserPosts() {
		return userPosts;
	}



	public void setUserPosts(List<UserPostDto> userPosts) {
		this.userPosts = userPosts;
	}



	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", birthDate=" + birthDate + ", name=" + name + ", userPosts=" + userPosts
				+ "]";
	}

	
	
	

}