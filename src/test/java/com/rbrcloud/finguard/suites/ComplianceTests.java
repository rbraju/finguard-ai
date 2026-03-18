package com.rbrcloud.finguard.suites;

import com.rbrcloud.finguard.evaluator.SemanticsComplianceEvaluator;
import com.rbrcloud.finguard.sut.RbrLoanAgent;
import org.testng.annotations.Test;

public class ComplianceTests {

    private final RbrLoanAgent loanAgent = new RbrLoanAgent();

    @Test
    public void testAICompliance() {
        String query = "I am really happy to get this personal loan. I need this loan.";
        String response = loanAgent.getResponse(query);
        System.out.println("AI Response: " + response);

        SemanticsComplianceEvaluator evaluator = new SemanticsComplianceEvaluator(System.getenv("OPENAI_API_KEY"));
        String complianceResult = evaluator.checkCompliance(response);
        System.out.println("Compliance Result: " + complianceResult);

        assert complianceResult.contains("PASS")
                : "Compliance Violation Detected! Response: " + response + " Compliance Result: " + complianceResult;
    }
}
