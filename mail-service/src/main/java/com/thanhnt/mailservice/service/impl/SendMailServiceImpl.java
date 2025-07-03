package com.thanhnt.mailservice.service.impl;

import com.thanhnt.mailservice.global.enums.OTPType;
import com.thanhnt.mailservice.global.enums.VerificationStatus;
import com.thanhnt.mailservice.global.message.OTPMailMessage;
import com.thanhnt.mailservice.global.message.StoreRegistrationMessage;
import com.thanhnt.mailservice.service.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {
  private final JavaMailSender mailSender;
  private static final String TITLE_STORE_REGISTRATION = "THÔNG BÁO VỀ QUÁ TRÌNH ĐĂNG KÍ SHOP";

  @Override
  public void sendOTPMail(OTPMailMessage mailMessage) {
    String subject = getOtpSubject(mailMessage.getType());

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

      String htmlContent = loadHtmlTemplate("templates/otp_template.html");
      // Replace placeholders with actual values
      htmlContent = htmlContent.replace("{{OTP_CODE}}", mailMessage.getOtpCode());
      htmlContent = htmlContent.replace("{{OTP_PURPOSE}}", getOtpPurpose(mailMessage.getType()));

      helper.setTo(mailMessage.getReceiverMail());
      helper.setSubject(subject);
      helper.setText(htmlContent, true); // true indicates HTML

      mailSender.send(message);
    } catch (MessagingException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendStoreRegistrationMail(StoreRegistrationMessage storeRegistrationMessage) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
      String location = getTemplateLocation(storeRegistrationMessage.getVerificationStatus());
      String htmlContent = loadHtmlTemplate(location);

      htmlContent = htmlContent.replace("{{USER_NAME}}", storeRegistrationMessage.getOwnerName());
      htmlContent = htmlContent.replace("{{REASON}}", storeRegistrationMessage.getReason());

      helper.setTo(storeRegistrationMessage.getReceiverMail());
      helper.setSubject(TITLE_STORE_REGISTRATION);
      helper.setText(htmlContent, true); // true indicates HTML

      mailSender.send(message);
    } catch (MessagingException | IOException e) {
      e.printStackTrace();
    }
  }

  private String loadHtmlTemplate(String path) throws IOException {
    try (InputStream inputStream = new ClassPathResource(path).getInputStream();
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

      return reader.lines().collect(Collectors.joining("\n"));
    }
  }

  private String getTemplateLocation(VerificationStatus status) {
    return switch (status) {
      case VERIFIED -> "templates/store_registration_approved.html";
      case REJECTED -> "templates/store_registration_rejected.html";
      case SUSPENDED -> "templates/store_registration_suspended.html";
      case NEED_MORE_INFO -> "templates/store_registration_need_more_info.html";
      default -> throw new IllegalArgumentException("Trạng thái không hợp lệ!");
    };
  }

  private String getOtpSubject(OTPType type) {
    return switch (type) {
      case REGISTER -> "OTP Verify Your Email";
      case FORGOT_PASSWORD -> "OTP For Reset Password";
      case TWO_FA -> "OTP Verification 2FA";
      default -> "OTP Verification";
    };
  }

  private String getOtpPurpose(OTPType type) {
    return switch (type) {
      case REGISTER -> "Sign Up process";
      case FORGOT_PASSWORD -> "Password Reset process";
      case TWO_FA -> "Two-Factor Authentication";
      default -> "verification";
    };
  }
}
