package com.bajajfinserv.qualifier.service;

import com.bajajfinserv.qualifier.model.WebhookRequest;
import com.bajajfinserv.qualifier.model.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public WebhookResponse generateWebhook(String name, String regNo, String email) {
        WebhookRequest request = new WebhookRequest(name, regNo, email);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
        
        WebhookResponse response = restTemplate.postForObject(WEBHOOK_URL, entity, WebhookResponse.class);
        
        return response;
    }
}