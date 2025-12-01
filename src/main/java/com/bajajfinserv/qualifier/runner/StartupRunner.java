package com.bajajfinserv.qualifier.runner;

import com.bajajfinserv.qualifier.model.WebhookResponse;
import com.bajajfinserv.qualifier.service.SqlSolverService;
import com.bajajfinserv.qualifier.service.SubmissionService;
import com.bajajfinserv.qualifier.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private SqlSolverService sqlSolverService;

    @Autowired
    private SubmissionService submissionService;

    @Value("${app.name}")
    private String name;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.email}")
    private String email;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Bajaj Finserv Qualifier Application...");
        
        // Step 1: Generate webhook
        System.out.println("Generating webhook...");
        WebhookResponse webhookResponse = webhookService.generateWebhook(name, regNo, email);
        
        System.out.println("Webhook URL: " + webhookResponse.getWebhook());
        System.out.println("Access Token: " + webhookResponse.getAccessToken());
        
        // Step 2: Solve SQL problem
        System.out.println("Solving SQL problem...");
        String sqlSolution = sqlSolverService.getSqlSolution(regNo);
        
        System.out.println("SQL Solution: " + sqlSolution);
        
        // Step 3: Submit solution
        System.out.println("Submitting solution...");
        submissionService.submitSolution(
            webhookResponse.getWebhook(),
            webhookResponse.getAccessToken(),
            sqlSolution
        );
        
        System.out.println("Process completed successfully!");
    }
}