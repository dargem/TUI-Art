package com.example.model_layer;

public record Params(
        double initialLength,
        double lengthDecay,
        int maxDepth,
        double angleVariance,
        int branchesPerNode,
        long seed
) {}
