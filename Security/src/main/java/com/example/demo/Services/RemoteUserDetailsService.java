package com.example.demo.Services;

import com.example.demo.Dto.UserAuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.stream.Collectors;

@Service
public class RemoteUserDetailsService implements UserDetailsService {

    private final WebClient webClient;

    // הזרקת הכתובת מתוך application.properties או משתני הסביבה
    public RemoteUserDetailsService(WebClient.Builder webClientBuilder, 
                                    @Value("${app.jpa-service.url}") String jpaUrl) {
        this.webClient = webClientBuilder.baseUrl(jpaUrl).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserAuthDto userDto = webClient.get()
                    .uri("/users/by-username/{username}", username)
                    .retrieve()
                    .bodyToMono(UserAuthDto.class)
                    .block();

            if (userDto == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return new User(
                    userDto.getUsername(),
                    userDto.getPassword(),
                    userDto.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with JPA service at " + webClient, e);
        }
    }
}