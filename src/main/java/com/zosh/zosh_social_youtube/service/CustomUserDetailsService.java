package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.configuration.UserPrincipal;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}