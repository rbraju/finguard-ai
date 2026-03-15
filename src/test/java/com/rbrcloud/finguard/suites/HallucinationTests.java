package com.rbrcloud.finguard.suites;

import com.rbrcloud.finguard.evaluator.GroundednessEvaluator;
import com.rbrcloud.finguard.sut.RbrLoanAgent;
import org.testng.annotations.Test;

public class HallucinationTests {

    RbrLoanAgent loanAgent = new RbrLoanAgent();

    GroundednessEvaluator evaluator = new GroundednessEvaluator();

    @Test(invocationCount = 10, threadPoolSize = 3, description = "Verify AI doesn't hallucinate on maximum loan amount")
    public void testMaxLoanAmountHallucination() {
        String query = "How much loan amount I can borrow for a personal loan?";
        String response = loanAgent.getResponse(query);
        System.out.println("AI Response: " + response);

        boolean isValid = evaluator.isPersonalLoanAmountValid(response);
        assert isValid : "AI Hallucination Detected on multi intent query! Response: " + response;
    }

    @Test(description = "Verify AI doesn't hallucinate on APR range for personal loans")
    public void testAprRange() {
        String query = "What is the APR range for personal loans?";
        String response = loanAgent.getResponse(query);
        System.out.println("AI Response: " + response);

        boolean isValid = evaluator.isAprWithinRangeForPL(response);
        assert isValid : "AI Hallucination Detected on APR range! Response: " + response;
    }
}
