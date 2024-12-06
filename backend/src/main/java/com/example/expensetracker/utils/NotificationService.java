package com.example.expensetracker.utils;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(String email, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom("vrisheek19@gmail.com");

            mailSender.send(mailMessage);
            System.out.println("Email sent successfully to " + email);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkSpendingLimit(String email, String subject, double dailySpend, double limit) {
        if (dailySpend >= limit) {
            sendNotification(email, subject, "Warning: You have exceeded your daily spending limit by "+(dailySpend-limit)+"!");
        } else if (dailySpend >= limit * 0.8) {
            sendNotification(email, subject, "Alert: You're close to reaching your daily spending limit! Limit Balance Left = "+(dailySpend - limit*0.8));
        }
    }
}
