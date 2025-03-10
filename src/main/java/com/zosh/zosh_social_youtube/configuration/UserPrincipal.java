package com.zosh.zosh_social_youtube.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private String id;
    private String username;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id, String username, String role, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    public String getId() { // Lấy userId
        return id;
    }

    public String getRole() { // Lấy vai trò (admin, user...)
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
