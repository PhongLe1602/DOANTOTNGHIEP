package doan.ptit.programmingtrainingcenter.service;


import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
