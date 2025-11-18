package com.example.config;

public record GrowthParams(
        double initialLength,
        double lengthDecay,
        double widthDecay,
        int maxDepth,
        double angleVariance,
        int branchesPerNode
) {}
