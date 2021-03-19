package com.jm.demo.service;

import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.User;
import com.jm.demo.vo.UserRetVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public List<UserRetVO> getUsers() {
        return memberRepository.findBy();
    }
    public boolean saveUser(User user) {

    }
}
