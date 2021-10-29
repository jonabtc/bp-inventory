package com.example.apistock.resource;

import java.util.List;
import java.util.Map;

import com.example.apistock.controllers.GoodController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GoodResource.GOODS_PATH)
public class GoodResource {
    public static final String GOODS_PATH = "/api/goods";
    public static final String EDIT_GOOD_STATE = "/change-state/{id}";
    public static final String TOTAL_GOODS_BY_CATEGORY_PATH = "/category/{id}";
    public static final String DETAILED_TOTAL_GOODS_BY_CATEGORY_PATH = "/category/{id}/detailed";
    public static final String CREATE_GOOD_BY_LOT_PATH = "/lot";

    @Autowired
    private GoodController goodController;

    public GoodResource(GoodController goodController) {
        this.goodController = goodController;
    }

    @GetMapping()
    public ResponseEntity<Object> getAllGoods() {
        return this.goodController.getAllGoods();

    }

    @PostMapping()
    public ResponseEntity<Object> createGood(@RequestBody Map<String, Object> jsonBody) {
        return this.goodController.create(jsonBody);

    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteGoods(@RequestBody List<String> goodIds) {
        return this.goodController.delete(goodIds);

    }

    @PostMapping(CREATE_GOOD_BY_LOT_PATH)
    public ResponseEntity<Object> createGoods(@RequestBody List<Map<String, Object>> jsonBody) {
        return this.goodController.create(jsonBody);

    }

    @GetMapping(TOTAL_GOODS_BY_CATEGORY_PATH)
    public ResponseEntity<Object> getTotalGoodsByCategory(@PathVariable("id") String categoryId) {
        return this.goodController.getTotalByCategory(categoryId);

    }

    @GetMapping(DETAILED_TOTAL_GOODS_BY_CATEGORY_PATH)
    public ResponseEntity<Object> getDetailedTotalGoodsByCategory(@PathVariable("id") String categoryId) {
        return this.goodController.getDetailedTotalByCategory(categoryId);

    }

    @PutMapping(EDIT_GOOD_STATE)
    public ResponseEntity<Object> editGoodState(@PathVariable("id") String goodId,
            @RequestBody Map<String, Boolean> jsonBody) {
        return this.goodController.editState(goodId, jsonBody);

    }

}
