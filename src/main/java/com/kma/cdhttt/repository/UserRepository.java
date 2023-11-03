package com.kma.cdhttt.repository;

import com.kma.cdhttt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findAllByUserName(String userName);
    Boolean existsByUserName(String userName);
}
