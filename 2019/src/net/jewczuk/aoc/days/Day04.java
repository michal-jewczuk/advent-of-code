package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 extends DayRunner {

    public Day04() {
        this.dayNumber = 4;
        this.className = this.getClass().getSimpleName();
        populateNumericData();
    }

    @Override
    public void runDay() {
        addResults(1, ResultType.EXERCISE, solvePart1("145852-616942"));

        displayResults();
    }

    private int solvePart1(String data) {
        List<Integer> borders = extractBorders(data);
        List<Integer> passwords = generatePasswords(borders);

        return passwords.size();
    }

    private List<Integer> extractBorders(String data) {
        return Arrays.asList(data.split("-")).stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
    }

    private List<Integer> generatePasswords(List<Integer> borders) {
        List<Integer> passwords = new ArrayList<>();
        for(int i = borders.get(0); i < borders.get(1); i++) {
            if(isValid(i)) {
                passwords.add(i);
            }
        }

        return passwords;
    }

    private boolean isValid(int number) {
        boolean hasDouble = false;
        String toCheck = String.valueOf(number);
        for(int i = 0; i < 5; i++) {
            char ch1 = toCheck.charAt(i);
            char ch2 = toCheck.charAt(i+1);
            if(ch1 > ch2) {
                return false;
            }
            if(ch1 == ch2) {
                hasDouble = true;
            }
        }

        return hasDouble;
    }

}
