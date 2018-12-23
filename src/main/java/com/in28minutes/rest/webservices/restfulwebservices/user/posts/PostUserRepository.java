package com.in28minutes.rest.webservices.restfulwebservices.user.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostUserRepository extends JpaRepository<UserDetail, Long>{

}
