package com.thxgraduate.user.repository;

import com.thxgraduate.user.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLink(UUID link);
}
