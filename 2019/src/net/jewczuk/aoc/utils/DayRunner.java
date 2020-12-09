package net.jewczuk.aoc.utils;

import java.util.ArrayList;
import java.util.List;

public class DayRunner {

    protected int dayNumber;
    protected String className;
    protected List<Integer> exampleNumericData;
    protected List<Integer> exerciseNumericData;
    protected List<String> exampleStringData;
    protected List<String> exerciseStringData;
    protected List<Integer> results = new ArrayList<>();

    public void runDay() {}

    protected void displayResults() {
        Outputter.INSTANCE.displayBreakLine(dayNumber);
        int size = results.size();
        for (int part = 1; part * 2 <= size; part++) {
            int idx = (part - 1) * 2;
            Outputter.INSTANCE.displayExampleResults(className, part, results.get(idx));
            Outputter.INSTANCE.displayExerciseResults(className, part, results.get(idx + 1));
        }
        Outputter.INSTANCE.displayBreakLine(dayNumber);
    }

    protected void populateNumericData() {
        exampleNumericData = FileReader.INSTANCE.loadNumericData(getFilename(true));
        exerciseNumericData = FileReader.INSTANCE.loadNumericData(getFilename(false));
    }

    protected void populateStringData() {
        exampleStringData = FileReader.INSTANCE.loadStringData(getFilename(true));
        exerciseStringData = FileReader.INSTANCE.loadStringData(getFilename(false));
    }

    private String getFilename(boolean isExample) {
        String dayPrefix = dayNumber < 10 ? "d0" : "d";
        dayPrefix += dayNumber;
        return isExample ? dayPrefix + "_example.txt" : dayPrefix + "_exercise.txt";
    }

}
