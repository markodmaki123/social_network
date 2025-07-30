package com.ftn.ues.socialnetwork.infrastructure.repository;

import com.ftn.ues.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
