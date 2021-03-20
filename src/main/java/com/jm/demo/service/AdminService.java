package com.jm.demo.service;

import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.User;
import com.jm.demo.vo.UserRetVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public List<UserRetVO> getUsers() {
        return memberRepository.findBy();
    }
    public void saveUser(User user)  {
        memberRepository.save(user);
    }
}
