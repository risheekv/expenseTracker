package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.utils.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    @Autowired
    private final NotificationService notificationService;

    private final UserRepository userRepository;



    public ExpenseController(ExpenseService expenseService, NotificationService notificationService,UserRepository userRepository) {
        this.expenseService = expenseService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String email,
                            @RequestParam String subject,
                            @RequestParam double dailySpend,
                            @RequestParam double limit) {
        try {
            if(dailySpend >= limit){
                notificationService.sendNotification(email, subject, "Warning: You have exceeded your daily spending limit by "+(dailySpend-limit)+"!");
            }
            else if(dailySpend >= limit * 0.8){
                notificationService.sendNotification(email,subject,"Alert: You're close to reaching your daily spending limit! Limit Balance Left = "
                        +(dailySpend - limit*0.8));
            }
            return "Notification sent successfully to: " + email;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
    // Add a new expense
    @PostMapping
    public Expense addExpense(@RequestParam String category,@RequestParam String userId, @RequestParam String type, @RequestParam double amount,@RequestParam LocalDate date) {
        Expense expense = new Expense(category,type, amount, date, userId);
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            double dailySpend=0;
            List<Expense> expenses = getExpensesByDate(date);
            for(Expense e:expenses){
                if(e.getUserId().equals(user.getId())) dailySpend+=e.getAmount();
            }
            System.out.println("dailyspend = "+dailySpend);
            //dailySpend = 10000;
            System.out.println("limit = "+user.getDailyLimit());
            sendEmail(user.getEmail(),"Daily Spend Limit Notification!",dailySpend,user.getDailyLimit());
            return expenseService.addExpense(expense);
        }
        else return null;
    }

    // Get expenses for a specific date (daily tracking)

    @GetMapping("/user/{userId}")
    public List<Expense> getExpensesByUser(@PathVariable String userId) {
        return expenseService.getExpensesByUser(userId);
    }
    @GetMapping("/daily/{date}")
    public List<Expense> getExpensesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return expenseService.getExpensesByDate(date);
    }

    // Get expenses within a date range (weekly tracking)
    @GetMapping("/weekly")
    public List<Expense> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return expenseService.getExpensesByDateRange(startDate, endDate);
    }
}
