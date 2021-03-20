package com.jm.demo.repository;

import com.jm.demo.vo.User;
import com.jm.demo.vo.UserRetVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    List<UserRetVO> findBy();

}
