package com.example.demo.Controllers;

import com.example.demo.Security.JwtUtil;
import com.example.demo.Services.RemoteUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RemoteUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.jpa-service.url}")
    private String jpaServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {
        Map<String, String> response = new HashMap<>();
        try {
            var userDetails = userDetailsService.loadUserByUsername(req.get("username"));
            if (!passwordEncoder.matches(req.get("password"), userDetails.getPassword())) {
                response.put("status", "error");
                response.put("message", "סיסמה שגויה");
                return response;
            }
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);
            response.put("status", "success");
            response.put("token", token);
            response.put("role", role);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "משתמש לא נמצא");
        }
        return response;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> req) {
        Map<String, String> response = new HashMap<>();
        Map<String, String> payload = new HashMap<>();
        payload.put("username", req.get("username"));
        payload.put("password", passwordEncoder.encode(req.get("password")));
        payload.put("role", req.get("role"));
        try {
            restTemplate.postForEntity(jpaServiceUrl + "/users", payload, String.class);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "הרשמה נכשלה: " + e.getMessage());
        }
        return response;
    }
}
