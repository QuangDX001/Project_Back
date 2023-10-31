package com.example.backend.security.service.users;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.config.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Admin on 10/27/2023
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void sendMail(String sendTo) {
        StringUtil stringUtil = new StringUtil();
        //BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        String randomPass = stringUtil.randomString();
        User existUser = userRepository.getUserByEmail(sendTo);
        existUser.setPassword(passwordEncoder.encode(randomPass));
        userRepository.save(existUser);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("wibucompany41@gmail.com");
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject("This is an email for reset password");
        simpleMailMessage.setText("\n" +
                "Hi,\n" +
                "\n" +
                "There was a request to change your password!\n" +
                "\n" +
                "If you did not make this request then please ignore this email.\n" +
                "\n" +
                "Otherwise, please use this new password to log in and change your password: "+randomPass);
        mailSender.send(simpleMailMessage);
    }
}
