package com.team4.backend.config.auth;

import com.team4.backend.mapper.MemberMapper;
import com.team4.backend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    public PrincipalDetailsService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername::" + username);
        User user = memberMapper.findUsername(username).orElse(null);
        return new PrincipalDetails(user);
    }
}
