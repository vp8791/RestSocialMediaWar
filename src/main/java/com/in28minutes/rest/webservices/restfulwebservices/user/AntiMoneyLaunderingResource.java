package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
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

import com.in28minutes.rest.webservices.restfulwebservices.user.posts.PostUserRepository;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserDetail;
import com.in28minutes.rest.webservices.restfulwebservices.user.posts.UserPost;

@RestController
public class AntiMoneyLaunderingResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostUserRepository postuserRepository;


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
		userRepository.deleteById(id);
	}

	//
	// input - details of user
	// output - CREATED & Return the created URI

	// HATEOAS

	@PostMapping("/jpa/amlusers")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserDetail user) {
		System.out.println("=================Start POST=======================");
		System.out.println(user.toString());
		System.out.println("==================End POST======================");
		
		UserDetail savedUser = postuserRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/jpa/amlusers/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDetail user) {
		
		System.out.println("=================Start PUT=======================");
		System.out.println(user.toString());
		System.out.println("==================End PUT======================");
		
		Optional<UserDetail> dbUser = postuserRepository.findById(id);

		if (!dbUser.isPresent())
		{		
			UserDetail savedUser = postuserRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
		} else {
			UserDetail updateUser = dbUser.get();
			updateUser.setBirthDate(user.getBirthDate());
			updateUser.setName(user.getName());
			postuserRepository.save(updateUser);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updateUser.getId())
					.toUri();
			return ResponseEntity.created(location).build();
			
		}

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
	public ResponseEntity<Object> createPost(@PathVariable long id, @RequestBody UserPost post) {
		
		Optional<UserDetail> userOptional = postuserRepository.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}

		UserDetail user = userOptional.get();
		
		post.setUserDetail(user);
				
		postuserRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
