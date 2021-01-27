package net.jewczuk.aoc.utils;

public enum Outputter {
    INSTANCE;

    public void displayBreakLine(Integer dayNumber) {
        System.out.format("===== DAY %d ================================ \n", dayNumber);
    }

    public void displayResuls(String clsName, int part, Integer result, String type) {
        System.out.format("[%s - part %d - %s]: %d \n", clsName, part, type, result);
    }
}
