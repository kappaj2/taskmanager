package com.ajk.taskman.repository;

import com.ajk.taskman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    User findUsersByUsername(String userName);

}
