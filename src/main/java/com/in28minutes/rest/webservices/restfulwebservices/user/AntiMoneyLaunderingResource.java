package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.swagger.annotations.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.user.posts.PostPostsRepository;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.PostUserRepository;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserDetail;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserDetailDto;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserPost;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserPostDto;

@RestController
@Api(value="Aml Resources", description="Aml Users Operations")
public class AntiMoneyLaunderingResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostUserRepository postuserRepository;

	@Autowired
	private PostPostsRepository postPostsRepository;

	
	//Gets All AML USers
	@ApiOperation(value = "View a list Aml Users and their Posts", response = Iterable.class)
	@GetMapping("/jpa/amlusers")
	public List<User> retrieveAllUsers() {
		List<User> allUsers = userRepository.findAll();
		if(allUsers.isEmpty()) {
			throw new NoUsersNotFoundException("No Users Found");
		}	
		return allUsers;
	}

	
	//Gets Specific AML Users
	@ApiOperation(value = "View Aml User and His Post", response = User.class)
	@GetMapping("/jpa/amlusers/{userId}")
	public Resource<User> retrieveUser(@PathVariable long userId) {
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent())
			throw new NoUsersNotFoundException("id-" + userId);

		// "all-users", SERVER_PATH + "/users"
		// retrieveAllUsers
		Resource<User> resource = new Resource<User>(user.get());

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

		resource.add(linkTo.withRel("all-aml-users"));

		// HATEOAS

		return resource;
	}
	
	
	//Deletes AML User and his Posts
	@ApiOperation(value = "Delete AML user and Associated Posts")
	@DeleteMapping("/jpa/amlusers/{userId}")
	public void deleteUser(@PathVariable long userId) {	
		boolean userExists = postuserRepository.existsById(userId);
		
		if (!userExists)
			throw new UserNotFoundException("userId-" + userId);
		
		Iterable<UserPost> allPosts = postPostsRepository.findAll();
		for(UserPost userPost : allPosts) {
			Long long1 = new Long(userPost.getUserId());
			Long long2 = new Long(userId);
		
			if(long1.compareTo(long2) == 0 ) {
				postPostsRepository.deleteById(userPost.getId());
			}
		}
		
		postuserRepository.deleteById(userId);
		
	}

	//
	// input - details of user
	// output - CREATED & Return the created URI

	// HATEOAS
	//Posts new AML User with Posts
	@ApiOperation(value = "Creates a new User along with Posts")
	@PostMapping("/jpa/amlusers")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserDetailDto userDto) {
		UserDetail persistUserDetailsDto = persistUserDetailsDto(userDto);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistUserDetailsDto.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	
	
	//Craetes or Updates AML User Id (not Posts)
	@ApiOperation(value = "Creates a new User with Posts if user does not exist (OR) Updates only user details if present before and it will not touch any of his Posts")
	@PutMapping("/jpa/amlusers/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable long userId, @RequestBody UserDetailDto userDto) {
		
		System.out.println("=================Start PUT=======================");
		System.out.println(userDto.toString());
		System.out.println("==================End PUT======================");
		
		Optional<UserDetail> dbUser = postuserRepository.findById(userId);

		if (!dbUser.isPresent())
		{
			UserDetail persistUserDetailsDto = persistUserDetailsDto(userDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(persistUserDetailsDto.getId())
				.toUri();
		return ResponseEntity.created(location).build();
		} else {
			UserDetail updateUser = dbUser.get();
			updateUser.setBirthDate(userDto.getBirthDate());
			updateUser.setName(userDto.getName());
			postuserRepository.save(updateUser);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(updateUser.getId())
					.toUri();
			return ResponseEntity.created(location).build();
			
		}
	}
	
	//Gets AML User id Posts
	@ApiOperation(value = "Gets Posts associated with a User")
	@GetMapping("/jpa/amlusers/{userId}/posts")
	public List<Post> retrieveAllUsers(@PathVariable long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + userId);
		}
		
		return userOptional.get().getPosts();
	}

	//Creates new Posts for a User
	@ApiOperation(value = "Creates new posts for existing user")
	@PostMapping("/jpa/amlusers/{userId}/posts")
	public ResponseEntity<Object> createPost(@PathVariable long userId, @RequestBody UserPostDto postDto) {		
		Optional<UserDetail> userOptional = postuserRepository.findById(userId);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("userId-" + userId);
		}

		UserDetail user = userOptional.get();		
		UserPost userPost = new UserPost();		
		userPost.setUserId(user.getId());
		userPost.setDescription(postDto.getDescription());
				
		postPostsRepository.save(userPost);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(userPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	//Updates existing post of the user
	@ApiOperation(value = "Updates existing post of the given user. If post is present, it updates it else it creates a new Post")
	@PutMapping("/jpa/amlusers/{userId}/post/{postId}")
	public ResponseEntity<Object> updatePost(@PathVariable long userId,@PathVariable long postId,  @RequestBody UserPostDto userDto) {
		
		System.out.println("=================Start PUT=======================");
		System.out.println(userDto.toString());
		System.out.println("==================End PUT======================");
		
		Optional<UserDetail> dbUser = postuserRepository.findById(userId);

		if (!dbUser.isPresent())
		{
			throw new UserNotFoundException("userId-" + userId);
		} else {
			//Find whether Post exists
			Optional<UserPost> postedId = postPostsRepository.findById(postId);
			//if Posted Id is not present create a Post
			if(!postedId.isPresent()) {
				UserPost userPost = new UserPost();
				userPost.setDescription(userDto.getDescription());
				userPost.setId(userId);
				postPostsRepository.save(userPost);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(userId)
						.toUri();
				return ResponseEntity.created(location).build();
				
			} else { //Posted id is present, update the post
				UserPost existingPost = postedId.get();
				existingPost.setDescription(userDto.getDescription());
				postPostsRepository.save(existingPost);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(userId)
						.toUri();
				return ResponseEntity.created(location).build();			
			}	
		}
	}
	
	
	//Deletes AML User and his Posts
	@ApiOperation(value = "Deletes post of the given user.")
	@DeleteMapping("/jpa/amlusers/{userId}/delete/{postId}")
		public void deletePost(@PathVariable long userId, @PathVariable long postId) {	
			boolean userExists = postuserRepository.existsById(userId);
			
			if (!userExists)
				throw new UserNotFoundException("userId-" + userId);
			
			Optional<UserPost> postedId = postPostsRepository.findById(postId);
			if(!postedId.isPresent()) {
				throw new PostNotFoundException("usrerId/postId-" + userId + "/" + postId);
			} else {
				postPostsRepository.deleteById(postId);
			}
			
						
			postuserRepository.deleteById(userId);
			
		}
	
	
	private UserDetail persistUserDetailsDto(UserDetailDto userDto) {
		UserDetail userdetail = new UserDetail();
		userdetail.setBirthDate(userDto.getBirthDate());
		userdetail.setName(userDto.getName());
		
		System.out.println(userdetail.toString());
		System.out.println("==================End POST======================");
		
		UserDetail savedUser = postuserRepository.save(userdetail);
		
		List<UserPostDto> userPosts = userDto.getUserPosts();
		for(UserPostDto userPostDto : userPosts) {
			UserPost userPost = new UserPost();
			userPost.setDescription(userPostDto.getDescription());
			userPost.setUserId(savedUser.getId());
			postPostsRepository.save(userPost);
		}
		
		return savedUser;
	}

}
