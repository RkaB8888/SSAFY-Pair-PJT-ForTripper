package com.travel.demo.users.model.service;

import jakarta.mail.MessagingException;

public  interface EmailService {
	void sendVerificationEmail(String toEmail, String subject, String body) throws MessagingException;
}
