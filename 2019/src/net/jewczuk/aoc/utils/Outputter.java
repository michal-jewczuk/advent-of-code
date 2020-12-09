package net.jewczuk.aoc.utils;

public enum Outputter {
    INSTANCE;

    public void displayExampleResults(String clsName, int part, Integer result) {
        displayResuls(clsName, part, result, "example_");
    }

    public void displayExerciseResults(String clsName, int part, Integer result) {
        displayResuls(clsName, part, result, "exercise");
    }

    public void displayBreakLine(Integer dayNumber) {
        System.out.format("===== DAY %d ================================ \n", dayNumber);
    }

    private void displayResuls(String clsName, int part, Integer result, String type) {
        System.out.format("[%s - part %d - %s]: %d \n", clsName, part, type, result);
    }
}
