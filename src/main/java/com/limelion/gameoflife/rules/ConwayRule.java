package com.limelion.gameoflife.rules;

public class ConwayRule implements Rule {

    @Override
    public boolean apply(boolean cell, int neighboursCount) {
        return !cell ? neighboursCount == 3 : !(neighboursCount > 3 || neighboursCount < 2);
    }
}
