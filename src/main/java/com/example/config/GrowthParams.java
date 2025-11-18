package com.example.config;

public record GrowthParams(
        double initialLength,
        double lengthDecay,
        int maxDepth,
        double angleVariance,
        int branchesPerNode,
        long seed
) {}
