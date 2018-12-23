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
@Entity
@Table(name="USER_DETAILS")
@NamedQuery(name="UserDetail.findAll", query="SELECT u FROM UserDetail u")
public class UserDetail implements Serializable {
	private static final long serialVersionUID = 8L;

	@Id
	@SequenceGenerator(name="USER_DETAILS_ID_GENERATOR", sequenceName="USER_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_DETAILS_ID_GENERATOR")
	private long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;
	
	
	private String name;

	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="userDetail")
	private Set<UserPost> userPosts;

	public UserDetail() {
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


	//bi-directional many-to-one association to UserPost
	public Set<UserPost> getUserPosts() {
		return this.userPosts;
	}

	public void setUserPosts(Set<UserPost> userPosts) {
		this.userPosts = userPosts;
	}

	public UserPost addUserPost(UserPost userPost) {
		getUserPosts().add(userPost);
		userPost.setUserDetail(this);

		return userPost;
	}

	public UserPost removeUserPost(UserPost userPost) {
		getUserPosts().remove(userPost);
		userPost.setUserDetail(null);

		return userPost;
	}

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", birthDate=" + birthDate + ", name=" + name + ", userPosts=" + userPosts
				+ "]";
	}

	
	
	

}