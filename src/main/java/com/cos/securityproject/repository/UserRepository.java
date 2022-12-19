package com.cos.securityproject.repository;

import com.cos.securityproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD함수를 JpaRepository가 들고 있음
//@repository 어노테이션이 없어도 됨 -> jparepository 상속
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy 규칙 -> Username 문법
    // select * from user where username = ?1
    public User findByUsername(String username);
}
