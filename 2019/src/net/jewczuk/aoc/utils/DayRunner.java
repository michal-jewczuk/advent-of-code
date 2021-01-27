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
    private List<Result> results = new ArrayList<>();

    public void runDay() {}

    protected void displayResults() {
        Outputter.INSTANCE.displayBreakLine(dayNumber);
        results.forEach(result -> {
            Outputter.INSTANCE.displayResuls(className, result.getPart(), result.getScore(), result.getType().toString());
        });
        Outputter.INSTANCE.displayBreakLine(dayNumber);
    }

    protected void addResults(int part, ResultType type, int result) {
        results.add(new Result(part, type, result));
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

    public enum ResultType {
        EXERCISE,
        EXAMPLE,
        ADDITIONAL
    }

    private class Result {
        private int part;
        private ResultType type;
        private int score;

        Result(int part, ResultType type, int score) {
            this.part = part;
            this.type = type;
            this.score = score;
        }

        public int getPart() {
            return part;
        }

        public ResultType getType() {
            return type;
        }

        public int getScore() {
            return score;
        }
    }

}
