package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
public class AntiMoneyLaunderingResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostUserRepository postuserRepository;

	@Autowired
	private PostPostsRepository postPostsRepository;

	@GetMapping("/jpa/amlusers")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/jpa/amlusers/{id}")
	public Resource<User> retrieveUser(@PathVariable long id) {
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent())
			throw new UserNotFoundException("id-" + id);

		// "all-users", SERVER_PATH + "/users"
		// retrieveAllUsers
		Resource<User> resource = new Resource<User>(user.get());

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

		resource.add(linkTo.withRel("all-aml-users"));

		// HATEOAS

		return resource;
	}

	@DeleteMapping("/jpa/amlusers/{id}")
	public void deleteUser(@PathVariable long id) {	
		boolean userExists = postuserRepository.existsById(id);
		
		if (!userExists)
			throw new UserNotFoundException("id-" + id);
		
		Iterable<UserPost> allPosts = postPostsRepository.findAll();
		for(UserPost userPost : allPosts) {
			Long long1 = new Long(userPost.getUserId());
			Long long2 = new Long(id);
		
			if(long1.compareTo(long2) == 0 ) {
				postPostsRepository.deleteById(userPost.getId());
			}
		}
		
		postuserRepository.deleteById(id);
		
	}

	//
	// input - details of user
	// output - CREATED & Return the created URI

	// HATEOAS

	@PostMapping("/jpa/amlusers")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserDetailDto userDto) {
		UserDetail persistUserDetailsDto = persistUserDetailsDto(userDto);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistUserDetailsDto.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/jpa/amlusers/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDetailDto userDto) {
		
		System.out.println("=================Start PUT=======================");
		System.out.println(userDto.toString());
		System.out.println("==================End PUT======================");
		
		Optional<UserDetail> dbUser = postuserRepository.findById(id);

		if (!dbUser.isPresent())
		{
			UserDetail persistUserDetailsDto = persistUserDetailsDto(userDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistUserDetailsDto.getId())
				.toUri();
		return ResponseEntity.created(location).build();
		} else {
			UserDetail updateUser = dbUser.get();
			updateUser.setBirthDate(userDto.getBirthDate());
			updateUser.setName(userDto.getName());
			postuserRepository.save(updateUser);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updateUser.getId())
					.toUri();
			return ResponseEntity.created(location).build();
			
		}

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
	
	
	@GetMapping("/jpa/amlusers/{id}/posts")
	public List<Post> retrieveAllUsers(@PathVariable long id) {
		Optional<User> userOptional = userRepository.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		return userOptional.get().getPosts();
	}


	@PostMapping("/jpa/amlusers/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable long id, @RequestBody User post) {
		
		Optional<UserDetail> userOptional = postuserRepository.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}

		UserDetail user = userOptional.get();
		UserPost userPost = new UserPost();		
		userPost.setUserId(user.getId());
				
		postuserRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
