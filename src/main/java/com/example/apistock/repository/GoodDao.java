package com.example.apistock.repository;

import java.util.List;

import com.example.apistock.models.Category;
import com.example.apistock.models.Good;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoodDao extends MongoRepository<Good, String> {

    List<Good> findByCategory(Category category);

    Long countByCategoryAndIsActive(Category category, boolean active);

}
