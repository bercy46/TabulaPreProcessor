package ca.jpti.SuiviBudget.Externe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jacques.poulin@gmail.com");
//        message.setTo("jacques.poulin@gmail.com");
        message.setTo(new String[]{"jacques.poulin@gmail.com", "nadstaubin@gmail.com"});
        message.setSubject("Suivi Budget");
        message.setText("http://172.24.101.14/suivibudget");
        javaMailSender.send(message);
        log.info("Email sent");
    }
}
