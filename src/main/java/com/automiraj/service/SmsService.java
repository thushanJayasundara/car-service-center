package com.automiraj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    private final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    @Value("${sms.secure.baseURL}")
    private String smsSecureBaseURL;

    @Value("${sms.username}")
    private String smsUsername;

    @Value("${sms.password}")
    private String smsPassword;

    @Value("${sms.signature}")
    private String smsSignature;

    @Value("${sms.send}")
    private boolean isSmsSend;

    @Value("${sms.message.body}")
    private String smsBody;

    final RestTemplate restTemplate;

    @Autowired
    public SmsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpStatus sendMessage(String mobileNum, String serviceDate) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        try {
            final String sendingMessage = serviceDate;
            final String mobileNumber = "94-"+mobileNum;

            final String uri = smsSecureBaseURL +   "&username=" + smsUsername + "&password="
                    + smsPassword + "&recipient=" + mobileNumber + "&messagedata=" + sendingMessage + "&signature=" + smsSignature;

            if (isSmsSend)
                httpStatus = restTemplate.getForEntity(uri, String.class).getStatusCode();

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in SmsService -> sendMessage()" + e);
        }
        return httpStatus;
    }
}
