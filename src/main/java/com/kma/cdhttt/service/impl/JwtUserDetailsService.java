package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.entity.UserRoleEntity;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findAllByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<String> roleList = userRoleRepository.findByUserId(user.getId()).stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        List<GrantedAuthority> grantList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (String role : roleList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(),
                grantList);
    }
}
