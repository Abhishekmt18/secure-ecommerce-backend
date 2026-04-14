package com.securecommerce.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private Map<String, String> otpStorage = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        System.out.println("Generated OTP for " + email + " = " + otp); // 👈 ADD

        otpStorage.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);
    }
        public boolean verifyOtp(String email, String otp) {

        String storedOtp = otpStorage.get(email);

        System.out.println("Stored OTP: " + storedOtp);
        System.out.println("Entered OTP: " + otp);

        if (storedOtp == null) {
            return false;
        }

        boolean isValid = storedOtp.equals(otp);

        if (isValid) {
            otpStorage.remove(email); //  important (cleanup)
        }

        return isValid;
    }
}