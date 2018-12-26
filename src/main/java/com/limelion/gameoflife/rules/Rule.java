package com.limelion.gameoflife.rules;

@FunctionalInterface
public interface Rule {

    boolean apply(boolean cell, int neighboursCount);
}
