package com.example.config;

public record BranchParams(
        double initialLength,
        double lengthDecay,
        double widthDecay,
        int maxDepth,
        double angleVariance,
        int branchesPerNode
) {}
