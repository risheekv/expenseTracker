package com.example.expensetracker.controller;

import com.example.expensetracker.service.EmailService;
import com.example.expensetracker.utils.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String email,
                            @RequestParam String subject,
                            @RequestParam String message) {
        try {
            notificationService.sendNotification(email, subject, message);
            return "Notification sent successfully to: " + email;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
