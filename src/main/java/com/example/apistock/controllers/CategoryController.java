package com.example.apistock.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.apistock.models.Category;
import com.example.apistock.repository.CategoryDao;
import com.example.apistock.utils.DataUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController {

    private CategoryDao categoryDao;
    private final int CATEGORY_LIMIT = 3;

    @Autowired
    public CategoryController(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public ResponseEntity<Object> create(Map<String, Object> category) {
        Map<String, Object> categoryMap = getCategoryWithOutId(category);

        if (DataUtils.hasNullValue(categoryMap) || !categoryMap.containsKey("name")) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }

        if (existsByName(category.get("name").toString())) {
            return new ResponseEntity<>("Category already exists", HttpStatus.BAD_REQUEST);
        }

        if (isOnLimint()) {
            return new ResponseEntity<>("Category limit reached", HttpStatus.BAD_REQUEST);
        }

        Category categoryResponse = save(categoryMap);
        Map<String, String> response = Map.of("id", String.valueOf(categoryResponse.getId()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Map<String, Object> getCategoryWithOutId(Map<String, Object> category) {
        if (category.containsKey("id")) {
            category.remove("id");
        }
        return category;
    }

    public boolean existsByName(String name) {
        return categoryDao.existsByName(name);
    }

    private boolean isOnLimint() {
        return categoryDao.count() >= CATEGORY_LIMIT;
    }

    private Category save(Map<String, Object> categoryMap) {
        Category category = new Category();
        category.setName(categoryMap.get("name").toString());
        if (categoryMap.containsKey("description")) {
            category.setDescription(categoryMap.get("description").toString());
        }
        return categoryDao.save(category);
    }

    public ResponseEntity<Object> update(String categoryId, Map<String, Object> category) {
        Map<String, Object> categoryMap = getCategoryWithOutId(category);

        if (DataUtils.hasNullValue(categoryMap)) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }

        if (!categoryDao.existsById(categoryId)) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        categoryDao.findById(categoryId).ifPresent(categoryFound -> {
            categoryFound.setName(categoryMap.get("name").toString());
            if (categoryMap.containsKey("description")) {
                categoryFound.setDescription(categoryMap.get("description").toString());
            }
            categoryDao.save(categoryFound);
        });

        Map<String, String> response = Map.of("id", categoryId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public Optional<Category> findById(String id) {
        return categoryDao.findById(id);
    }

    public boolean existsById(String id) {
        return categoryDao.existsById(id);
    }

    public ResponseEntity<Object> getAll() {
        List<Category> categories = categoryDao.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
