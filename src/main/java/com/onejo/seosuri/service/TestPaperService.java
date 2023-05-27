package com.onejo.seosuri.service;

import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@RequiredArgsConstructor
public class TestPaperService {

    @Autowired
    JavaMailSenderImpl mailSender;
    private final String SUBJECT = "띵동-! 서수리가 시험지를 배달했어요\uD83E\uDD89";
    private final String CONTENT = "안녕하세요 고객님!\n아래 첨부된 pdf 파일을 다운로드 해주세요\uD83D\uDE04";

    public void sendEmail(String email, Long testPaperId){
        // 시험지 pdf 만들기

        // 이메일 발송
        try{
            // true: 첨부 파일 첨부하겠다
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

            if(email.equals("") || email.isEmpty()){
                throw new BusinessException(ErrorCode.EMPTY_DATA);
            }

            mimeMessageHelper.setFrom("seosuri2023@gmail.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setText(CONTENT, false);

            FileSystemResource file = new FileSystemResource(new File("C:\\Users\\신지수\\Pictures\\Saved Pictures\\image2.jpg"));
            mimeMessageHelper.addAttachment("image2.jpg", file);

            mailSender.send(mail);

        } catch(MessagingException e){
            throw new RuntimeException(e);
        } catch(BusinessException e){
            throw new BusinessException(ErrorCode.FAILED_TO_SEND_EMAIL);
        }
    }
}
