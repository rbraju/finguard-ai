package com.rbrcloud.finguard.evaluator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

/**
 * The Data Access Layer that provides an interface to access the grounded data about RBR products.
 * Loads RBR products data from a JSON file and provides access to it.
 */
public class KnowledgeBase {

    private final JsonNode products;

    public KnowledgeBase() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/product-spec-truth-source.json");
            this.products = mapper.readTree(inputStream).get("products");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RBR products knowledge base", e);
        }
    }

    public JsonNode getProductData(String productName) {
        for (JsonNode product : products) {
            if (product.get("name").asText().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }
}
