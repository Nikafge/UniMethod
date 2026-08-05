package com.example.diploma.quality.dto;

import com.example.diploma.model.Publication;

public class DuplicateCandidateDto {

    private Publication first;
    private Publication second;

    private double levenshteinSimilarity;
    private double cosineSimilarity;
    private double finalScore;

    private SimilarityLevel level;

    public DuplicateCandidateDto(
            Publication first,
            Publication second,
            double levenshteinSimilarity,
            double cosineSimilarity,
            double finalScore,
            SimilarityLevel level
    ) {
        this.first = first;
        this.second = second;
        this.levenshteinSimilarity = levenshteinSimilarity;
        this.cosineSimilarity = cosineSimilarity;
        this.finalScore = finalScore;
        this.level = level;
    }

    public Publication getFirst() {
        return first;
    }

    public Publication getSecond() {
        return second;
    }

    public double getLevenshteinSimilarity() {
        return levenshteinSimilarity;
    }

    public double getCosineSimilarity() {
        return cosineSimilarity;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public SimilarityLevel getLevel() {
        return level;
    }
}