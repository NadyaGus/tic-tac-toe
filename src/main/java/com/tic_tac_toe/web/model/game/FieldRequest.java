package com.tic_tac_toe.web.model.game;

import java.util.Arrays;

public class FieldRequest {
    private int[][] field;

    public FieldRequest(int[][] field) {
        this.field = copyField(field);
    }

    public int[][] copyField(int[][] orig) {
        if (orig == null) return null;
        int[][] copy = new int[orig.length][];
        for (int i = 0; i < orig.length; i++) {
            copy[i] = Arrays.copyOf(orig[i], orig[i].length);
        }
        return copy;
    }

    public int[][] getField() {
        return field;
    }
}
