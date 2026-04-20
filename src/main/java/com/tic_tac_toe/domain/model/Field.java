package com.tic_tac_toe.domain.model;

public class Field {
    private int[][] field;

    /**
     *
     * @param field field matrix of the game, can be empty(null), or filled with values
     * if empty - will initialize to default values
     */
    public Field(int[][] field) {
        if (field == null)
            this.field = new int[3][3];
        else this.field = field;
    }

    public int[][] getField() {
        int[][] copy = new int[field.length][];
        for (int i = 0; i < field.length; i++) {
            copy[i] = field[i].clone();
        }
        return copy;
    }

    public void setField(int[][] field) {
        this.field = field;
    }
}
