package com.example.expensetracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;

    private double dailyLimit;

    private List<String> expenseIds = new ArrayList<>();

    // Constructors, getters, and setters
    public User() {}

    public User(String username, String password, String email, double dailyLimit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dailyLimit=dailyLimit;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public List<String> getExpenseIds() {
        return expenseIds;
    }

    public void setExpenseIds(List<String> expenseIds) {
        this.expenseIds = expenseIds;
    }

    public void addExpenseId(String expenseId){
        this.expenseIds.add(expenseId);
    }

}
