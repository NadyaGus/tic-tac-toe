package com.tic_tac_toe.datasource.model;

import java.util.Arrays;

public class FieldEntity {
    private final int[][] field;

    private FieldEntity() {
        this.field = null;
    }

    public FieldEntity(int[][] field) {
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
