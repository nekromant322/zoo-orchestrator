package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return Optional.ofNullable(userDAO.findByEmail(email))
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email`ом " + email + " не найден!"));
    }

    private static class CustomUserDetails implements UserDetails {

        private final String userName;
        private final String password;
        private final List<GrantedAuthority> authorities;

        public CustomUserDetails(User user) {
            this.userName = user.getEmail();
            this.password = user.getPassword();
            this.authorities = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return userName;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}