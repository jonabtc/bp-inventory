package com.example.apistock.repository;

import com.example.apistock.models.Category;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryDao extends MongoRepository<Category, String> {

    boolean existsByName(String name);

}
