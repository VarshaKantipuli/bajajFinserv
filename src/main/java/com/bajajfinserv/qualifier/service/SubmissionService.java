package com.bajajfinserv.qualifier.service;

import com.bajajfinserv.qualifier.model.SolutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubmissionService {

    @Autowired
    private RestTemplate restTemplate;

    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SolutionRequest request = new SolutionRequest(sqlQuery);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        
        HttpEntity<SolutionRequest> entity = new HttpEntity<>(request, headers);
        
        String response = restTemplate.postForObject(webhookUrl, entity, String.class);
        
        System.out.println("Submission Response: " + response);
    }
}