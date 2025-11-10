package de.ait.training.controller;

import de.ait.training.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/email")
@Slf4j
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/service")
    public ResponseEntity<String> sendEmailToService(@RequestParam @Email String emailTo) {

        Map<String, Object> map = Map.of(
                "title", "Welcome to our service Car Service",
                "userName", "Vio красотка",
                "promoCode", "AIT-TR 2706");
        emailService.sendTemplateEmail(emailTo, "Car service time", "email/CarService",
                map);

        return ResponseEntity.ok(STR."Email sent to \{emailTo}");
    }


}
