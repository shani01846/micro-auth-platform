package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProxyController {

    @Value("${app.jpa-service.url}")
    private String jpaServiceUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(HttpServletRequest request) {
        HttpHeaders headers = createHeaders(request);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(jpaServiceUrl + "/products", HttpMethod.GET, entity, List.class);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> product, HttpServletRequest request) {
        HttpHeaders headers = createHeaders(request);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(product, headers);
        return restTemplate.exchange(jpaServiceUrl + "/products/add", HttpMethod.POST, entity, Object.class);
    }

    private HttpHeaders createHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            headers.set("Authorization", authorization);
        }
        return headers;
    }
}
