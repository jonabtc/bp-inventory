package com.example.apistock.models;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category {

    @Id
    private String id;
    private String name;
    private String description;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> toMap() {
        return Map.of("id", id, "name", name, "description", description);
    }
    
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }


}
