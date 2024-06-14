package com.nashtech.rookie.asset_management_0701.repositories;

import com.nashtech.rookie.asset_management_0701.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}
