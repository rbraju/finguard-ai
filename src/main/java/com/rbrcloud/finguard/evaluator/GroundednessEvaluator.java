package com.rbrcloud.finguard.evaluator;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The model judge.
 * Pulls entities from unstructured AI responses and perform 'Groundedness checks' against the truth source.
 *
 * These are "Deterministic Evaluators" that can be implemented with traditional programming techniques.
 */
public class GroundednessEvaluator {

    // The source of truth for RBR products data
    private final KnowledgeBase kb = new KnowledgeBase();

    public boolean isPersonalLoanAmountValid(String aiResponse) {

        // Get the truth from the knowledge base
        JsonNode personalLoan = kb.getProductData("Personal Loan");
        if (personalLoan == null) {
            throw new RuntimeException("Personal Loan product data not found in knowledge base");
        }
        int maxLoanAmount = personalLoan.get("max_amount").asInt();

        // Extract the loan amount from the AI response
        Pattern pattern = Pattern.compile("\\$?([0-9]{1,3}(,?[0-9]{3})*)");
        Matcher matcher = pattern.matcher(aiResponse);

        if (matcher.find()) {
            String extractedAmount = matcher.group(1).replace(",", "");
            int amountFromAI = Integer.parseInt(extractedAmount);

            return amountFromAI <= maxLoanAmount;
        }

        return false; // Amount not found in AI response, or in invalid format
    }

    public boolean isAprWithinRangeForPL(String aiResponse) {
        JsonNode personalLoan = kb.getProductData("Personal Loan");
        if (personalLoan == null) {
            throw new RuntimeException("Personal Loan product data not found in knowledge base");
        }
        double minApr = personalLoan.get("min_apr").asDouble();
        double maxApr = personalLoan.get("max_apr").asDouble();

        // Extract APR values from the AI response
        Pattern pattern = Pattern.compile("(\\d{1,2}(?:\\.\\d{1,2})?)\\s?%");
        Matcher matcher = pattern.matcher(aiResponse);

        // Loop through all extracted numbers and check if any of them fall within the APR range
        while (matcher.find()) {
            try {
                double aprFromAI = Double.parseDouble(matcher.group(1));
                if (aprFromAI < minApr || aprFromAI > maxApr) {
                    return false; // Found an APR value that is outside the valid range
                }
            } catch (NumberFormatException e) {
                System.out.println("Warning: Unable to parse APR value from AI response: " + matcher.group(1));
            }
        }
        return true; // All extracted numbers are within the valid APR range, or no numbers found
    }
}
