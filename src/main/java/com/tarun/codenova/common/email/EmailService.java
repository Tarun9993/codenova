package com.tarun.codenova.common.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(
            String email,
            String username,
            String temporaryPassword,
            String cohortName) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Welcome to CodeNova"
        );

        message.setText(
                "Hello " + username + ",\n\n" +

                        "Your CodeNova account has been created.\n\n" +

                        "Username: " + username + "\n" +
                        "Email: " + email + "\n" +
                        "Temporary Password: "
                        + temporaryPassword + "\n" +
                        "Cohort: " + cohortName + "\n\n" +

                        "Please log in using these credentials " +
                        "and change your password after your first login.\n\n" +

                        "Regards,\n" +
                        "CodeNova Team"
        );

        mailSender.send(message);
    }
}