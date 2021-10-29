package com.example.apistock.resource;

import java.util.Map;

import com.example.apistock.controllers.CategoryController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(CategoryResource.CATEGORY_PATH)
public class CategoryResource {

    public static final String CATEGORY_PATH = "/api/categories";
    public static final String CATEGORY_ID_PATH = "/{id}";
    public static final String CATEGORY_ID_GOODS_PATH = "/{id}/goods";
    public static final String CATEGORY_ID_GOODS_DETAILS_PATH = "/{id}/goods-details";

    private CategoryController categoryController;

    @Autowired
    public CategoryResource(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    @PostMapping()
    public ResponseEntity<Object> createCategory(@RequestBody Map<String, Object> jsonBody) {
        return this.categoryController.create(jsonBody);
    }

    @PutMapping(CATEGORY_ID_PATH)
    public ResponseEntity<Object> updateCategory(@PathVariable("id") String id, @RequestBody Map<String, Object> jsonBody) {
        return this.categoryController.update(id, jsonBody);
    }

    @GetMapping()
    public ResponseEntity<Object> getCategories() {
        return this.categoryController.getAll();
    }
    
}
