package com.quiztest.q5.controller;

import com.quiztest.q5.model.Product;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private RedisTemplate<String, Product> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String REDIS_KEY = "PRODUCTS";

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        redisTemplate.opsForHash().put(REDIS_KEY, product.getId(), product);
        kafkaTemplate.send("product_topic", "Sản phẩm mới: " + product.getName());
        return product;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return new ArrayList<>(redisTemplate.opsForHash().values(REDIS_KEY));
    }
}

