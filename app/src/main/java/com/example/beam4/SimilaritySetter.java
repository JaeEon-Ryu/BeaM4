package com.example.beam4;

public class SimilaritySetter {
    private static int similarity = 20;

    public static void setSimilarity(int similarity) {
        SimilaritySetter.similarity = similarity;
    }

    public static int getSimilarity() {
        return similarity;
    }
}
