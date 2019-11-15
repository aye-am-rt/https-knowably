package com.stackroute.datapopulator.userservices.repository;

import com.stackroute.datapopulator.userservices.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
            User findByName(String name);
}
