package com.example.apistock.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.apistock.models.Category;
import com.example.apistock.models.Good;
import com.example.apistock.repository.GoodDao;
import com.example.apistock.utils.DataUtils;

@Controller
public class GoodController {

    private GoodDao goodDao;
    private CategoryController categoryController;

    @Autowired
    public GoodController(GoodDao goodDao, CategoryController categoryController) {
        this.goodDao = goodDao;
        this.categoryController = categoryController;
    }

    public ResponseEntity<Object> create(List<Map<String, Object>> data) {
        if (DataUtils.dataIsNotValid(data)) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }

        if (categoryNotFoud(data)) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        List<Good> goods = formatGoodList(data);
        
        List<Map<String,String>> response = saveLotAndReturnGoodIds(goods);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private List<Map<String,String>> saveLotAndReturnGoodIds(List<Good> goods) {

        List<Good> responseList = goodDao.saveAll(goods);

        List<Map<String,String>> goodIds = new ArrayList<>();
        responseList.forEach(item -> goodIds.add(Map.of("id", item.getId().toString())));

        return goodIds;
    }

    public ResponseEntity<Object> create(Map<String, Object> data) {
        if (DataUtils.hasNullValue(data) && !data.containsKey("categoryId")) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }
        String categoryId = (String) data.get("categoryId");
        if (categoryNoExists(categoryId)) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Good good = formatGood(data);
        goodDao.save(good);
        Map<String, String> response = Map.of("id", good.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private boolean categoryNotFoud(List<Map<String, Object>> data) {
        Set<String> categoryIds = getNonRepeatedCategoryIds(data);

        for (String categoryId : categoryIds) {
            if (categoryNoExists(categoryId)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getNonRepeatedCategoryIds(List<Map<String, Object>> data) {
        Set<String> categoryIds = new HashSet<>();
        for (Map<String, Object> item : data) {
            categoryIds.add(item.get("categoryId").toString());
        }

        return categoryIds;
    }

    private boolean categoryNoExists(String categoryId) {
        return !categoryController.existsById(categoryId);
    }

    private Good formatGood(Map<String, Object> data) {
        Good good = new Good();

        String name = (String) data.get("name");
        String description = (String) data.get("description");
        String categoryId = (String) data.get("categoryId");

        Optional<Category> category = categoryController.findById(categoryId);

        good.setName(name);
        good.setDescription(description);
        category.ifPresent(good::setCategory);
        return good;
    }

    private List<Good> formatGoodList(List<Map<String, Object>> data) {
        List<Good> goods = new ArrayList<>();

        for (Map<String, Object> item : data) {
            Good good = formatGood(item);
            goods.add(good);
        }

        return goods;
    }

    public ResponseEntity<Object> delete(List<String> goodIds) {
        if (DataUtils.hasNullValue(goodIds)) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }
        goodDao.deleteAllById(goodIds);
        String response = "Goods deleted: " + goodIds.size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getTotalByCategory(String categoryId) {
        if (categoryNoExists(categoryId)) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }
        Category category = categoryController.findById(categoryId).get();
        Long total = goodDao.countByCategoryAndIsActive(category, true);
        Map<String, Long> response = Map.of("total", total);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getDetailedTotalByCategory(String categoryId) {
        if (categoryNoExists(categoryId)) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }
        Category category = categoryController.findById(categoryId).get();
        Long totalActives = goodDao.countByCategoryAndIsActive(category, true);
        Long totalInactives = goodDao.countByCategoryAndIsActive(category, false);

        Map<String, Long> response = Map.of("totalActives", totalActives, "totalInactives", totalInactives);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> editState(String goodId, Map<String, Boolean> jsonBody) {
        if (isNoValidStateData(goodId, jsonBody)) {
            return new ResponseEntity<>("Bad data", HttpStatus.BAD_REQUEST);
        }

        if (!goodDao.existsById(goodId)) {
            return new ResponseEntity<>("Good not found", HttpStatus.NOT_FOUND);
        }

        goodDao.findById(goodId).ifPresent(good -> {
            good.setActive(jsonBody.get("state").toString().equals("true"));
            goodDao.save(good);
        });

        return new ResponseEntity<>("Good updated", HttpStatus.OK);
    }

    private boolean isNoValidStateData(String goodId, Map<String, Boolean> jsonBody) {
        if (goodId == null) {
            return true;
        }
        if (goodId.isEmpty()) {
            return true;
        }

        if (DataUtils.hasNullValue(jsonBody)) {
            return true;
        }
        if (!jsonBody.containsKey("state")) {
            return true;
        }
        return false;

    }

    public ResponseEntity<Object> getAllGoods() {
        List<Good> goods = goodDao.findAll();
        return new ResponseEntity<>(goods, HttpStatus.OK);
    }

}
