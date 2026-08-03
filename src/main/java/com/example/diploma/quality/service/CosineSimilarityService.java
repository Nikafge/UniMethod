package com.example.diploma.quality.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CosineSimilarityService {

    public double cosineSimilarity(
            Map<String, Double> firstVector,
            Map<String, Double> secondVector
    ) {
        if (firstVector == null || secondVector == null
                || firstVector.isEmpty() || secondVector.isEmpty()) {
            return 0.0;
        }

        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(firstVector.keySet());
        allTerms.addAll(secondVector.keySet());

        double dotProduct = 0.0;
        double firstNorm = 0.0;
        double secondNorm = 0.0;

        for (String term : allTerms) {
            double firstValue = firstVector.getOrDefault(term, 0.0);
            double secondValue = secondVector.getOrDefault(term, 0.0);

            dotProduct += firstValue * secondValue;
            firstNorm += firstValue * firstValue;
            secondNorm += secondValue * secondValue;
        }

        if (firstNorm == 0.0 || secondNorm == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(firstNorm) * Math.sqrt(secondNorm));
    }
}