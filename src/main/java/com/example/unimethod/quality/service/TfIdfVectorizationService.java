package com.example.unimethod.quality.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TfIdfVectorizationService {

    private final TextNormalizationService normalizationService;

    public TfIdfVectorizationService(TextNormalizationService normalizationService) {
        this.normalizationService = normalizationService;
    }

    public Map<Long, Map<String, Double>> buildTfIdfVectors(Map<Long, String> documents) {
        Map<Long, List<String>> tokenizedDocuments = new HashMap<>();

        for (Map.Entry<Long, String> entry : documents.entrySet()) {
            tokenizedDocuments.put(
                    entry.getKey(),
                    normalizationService.tokenize(entry.getValue())
            );
        }

        Map<String, Integer> documentFrequency = calculateDocumentFrequency(tokenizedDocuments);
        int totalDocuments = tokenizedDocuments.size();

        Map<Long, Map<String, Double>> vectors = new HashMap<>();

        for (Map.Entry<Long, List<String>> entry : tokenizedDocuments.entrySet()) {
            Long documentId = entry.getKey();
            List<String> tokens = entry.getValue();

            Map<String, Double> tf = calculateTermFrequency(tokens);
            Map<String, Double> tfIdf = new HashMap<>();

            for (Map.Entry<String, Double> tfEntry : tf.entrySet()) {
                String term = tfEntry.getKey();

                int df = documentFrequency.getOrDefault(term, 0);

                double idf = Math.log((double) (totalDocuments + 1) / (df + 1)) + 1.0;
                double value = tfEntry.getValue() * idf;

                tfIdf.put(term, value);
            }

            vectors.put(documentId, tfIdf);
        }

        return vectors;
    }

    private Map<String, Integer> calculateDocumentFrequency(Map<Long, List<String>> tokenizedDocuments) {
        Map<String, Integer> documentFrequency = new HashMap<>();

        for (List<String> tokens : tokenizedDocuments.values()) {
            Set<String> uniqueTokens = new HashSet<>(tokens);

            for (String token : uniqueTokens) {
                documentFrequency.put(
                        token,
                        documentFrequency.getOrDefault(token, 0) + 1
                );
            }
        }

        return documentFrequency;
    }

    private Map<String, Double> calculateTermFrequency(List<String> tokens) {
        Map<String, Double> frequency = new HashMap<>();

        if (tokens == null || tokens.isEmpty()) {
            return frequency;
        }

        for (String token : tokens) {
            frequency.put(token, frequency.getOrDefault(token, 0.0) + 1.0);
        }

        int totalTerms = tokens.size();

        for (String token : new ArrayList<>(frequency.keySet())) {
            frequency.put(token, frequency.get(token) / totalTerms);
        }

        return frequency;
    }
}