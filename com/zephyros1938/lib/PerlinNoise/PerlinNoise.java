package com.zephyros1938.lib.PerlinNoise;

/*
 * RESOURCES:
 * - https://jaysmito101.hashnode.dev/perlins-noise-algorithm
 */

import java.util.Random;

public class PerlinNoise {
    private static final int GRADIENT_SIZE = 256;
    private final int[] perm = new int[512];

    private long S = 1; // seed

    private static final double[][] grad2 = {
            { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 },
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
    };

    public PerlinNoise(long seed) {
        S = seed;

        int[] p = new int[GRADIENT_SIZE];
        for (int i = 0; i < GRADIENT_SIZE; i++)
            p[i] = i;

        Random random = new Random(S);
        for (int i = GRADIENT_SIZE - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
        }
    }

    public void changeSeed(long changeTo) {
        S = changeTo;
    }

    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255; // GET UNIT SQUARE CONTAINING POINT
        int Y = (int) Math.floor(y) & 255;

        double xCoord = Math.floor(x); // RELATIVE XY COORDS WITHIN THE SQUARE
        double yCoord = Math.floor(y);

        x -= xCoord; // RELATIVE XY COORDS WITHIN THE SQUARE
        y -= yCoord;

        double u = fade(x);
        double v = fade(y);

        // HASH 4 CORNERS
        int aa = perm[X + perm[Y]] % 4;
        int ab = perm[X + perm[Y + 1]] % 4;
        int ba = perm[X + 1 + perm[Y]] % 4;
        int bb = perm[X + 1 + perm[Y + 1]] % 4;

        // ADD BLEND RESULTS FROM 4 CORNERS OF SQUARE
        double gradAA = dot(grad2[aa], x, y);
        double gradBA = dot(grad2[ba], x - 1, y);
        double gradAB = dot(grad2[ab], x, y - 1);
        double gradBB = dot(grad2[bb], x - 1, y - 1);

        double lerpX1 = lerp(u, gradAA, gradBA);
        double lerpX2 = lerp(u, gradAB, gradBB);

        double lerpedValue = lerp(v, lerpX1, lerpX2);

        /* // DEBUG
         * System.out.println(
         * "X: " + X + ", Y: " + Y +
         * "\nxCoord: " + xCoord + ", yCoord: " + yCoord +
         * "\nu: " + u + ", v: " + v +
         * "\naa: " + aa + ", ab: " + ab + ", ba: " + ba + ", bb: " + bb +
         * "\ngradAA: " + gradAA + ", gradBA: " + gradBA + ", gradAB: " + gradAB +
         * ", gradBB: " + gradBB +
         * "\nlerpX1: " + lerpX1 + ", lerpX2: " + lerpX2 +
         * "\nResult: " + lerpedValue + "\n");
         */

        return lerpedValue;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double dot(double[] g, double x, double y) {
        return g[0] * x + g[1] * y;
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
}