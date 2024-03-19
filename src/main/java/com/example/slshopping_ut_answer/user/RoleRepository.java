package com.example.slshopping_ut_answer.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.slshopping_ut_answer.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
