package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender javaMailSender;

	public String sendEmail(User user, String mode) {

		String subject = "Unlock IES Account";
		String send = "<html><body>" + "Hi " + user.getFirstName() + ", " + user.getLastName() + " :<br><br>"
				+ "Welcome to IES family, your registration is almost complete."
				+ "<br>Please unlock your account using below details." + "<p>Temporary Password: "
				+ user.getTempPassword() + "</p>" + "<a href='http://localhost:8081/unlock/" + user.getId()
				+ "'>Click Here To Unlock your Account</a>" + "<br><br>" + "Thanks," + "<br>IES Team"
				+ "</body></html>";

		String resend = "<html><body>" + "Hi " + user.getFirstName() + ", " + user.getLastName() + " :<br><br>"
				+ "Based on the request initiated by you to resend the password."
				+ "<br>Please note the password for further reference" + "<p>Login Password: "
				+ user.getConfirmPassword() + "</p>"
				+ "<a href='http://localhost:8081/'>Click Here To Login your Account</a>" + "<br><br>" + "Thanks,"
				+ "<br>IES Team" + "</body></html>";

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(user.getEmail());
			if (("send").equalsIgnoreCase(mode)) {
				mimeMessageHelper.setText(send, true);
			} else {
				mimeMessageHelper.setText(resend, true);
			}
			mimeMessageHelper.setSubject(subject);
			javaMailSender.send(mimeMessage);
			return "Y";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "N";
		}
	}

}
