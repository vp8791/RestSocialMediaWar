package com.in28minutes.rest.webservices.restfulwebservices.user.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostUserRepository extends CrudRepository<UserDetail, Long>{

}
