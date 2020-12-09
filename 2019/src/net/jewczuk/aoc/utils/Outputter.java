package net.jewczuk.aoc.utils;

public enum Outputter {
    INSTANCE;

    public void displayExampleResults(String clsName, int part, Integer result) {
        System.out.format("[%s - part %d]: The result for example is %d \n", clsName, part, result);
    }

    public void displayExerciseResults(String clsName, int part, Integer result) {
        System.out.format("[%s - part %d]: The result for exercise is %d \n", clsName, part, result);
    }
}
