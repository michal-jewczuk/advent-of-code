package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;

import java.util.*;
import java.util.stream.Collectors;

public class Day06 extends DayRunner {

    public Day06() {
        this.dayNumber = 6;
        this.className = this.getClass().getSimpleName();
        populateStringData();
    }

    @Override
    public void runDay() {
        populateStringData();

        addResults(1, ResultType.EXAMPLE, solvePart1(exampleStringData));
        addResults(1, ResultType.EXERCISE, solvePart1(exerciseStringData));
        addResults(2, ResultType.EXERCISE, solvePart2(exerciseStringData));

        displayResults();
    }

    private int solvePart1(List<String> data) {
        Map<String, String> orbits = createOrbitsMap(data);

        return calculateOrbitCount(orbits);
    }

    private int solvePart2(List<String> data) {
        Map<String, String> orbits = createOrbitsMap(data);

        return calculateOrbitJump(orbits.get("YOU"), orbits.get("SAN"));
    }

    private Map<String, String> createOrbitsMap(List<String> data) {
        Map<String, String> orbits = new HashMap<>();
        int limit = data.size();
        while (orbits.size() < limit) {
            data.forEach(s -> populateOrbit(s, orbits));
        }

        return orbits;
    }

    private void populateOrbit(String entry, Map<String, String> orbits) {
        String[] parts = entry.split("[)]");
        if (parts[0].equals("COM")) {
            orbits.put(parts[1], parts[1]);
        } else {
            String value = orbits.get(parts[0]);
            if (value != null) {
                orbits.put(parts[1], extendOrbit(value, parts[1]));
            }
        }
    }

    private String extendOrbit(String current, String point) {
        StringBuilder sb = new StringBuilder(current);
        sb.append("-").append(point);
        return sb.toString();
    }

    private int calculateOrbitCount(Map<String, String> orbits) {
        return orbits.values().stream()
                .mapToInt(v -> v.split("-").length)
                .sum();
    }

    private int calculateOrbitJump(String you, String santa) {
        List<String> partsYou = Arrays.asList(you.split("-"));
        List<String> partsSanta = Arrays.asList(santa.split("-"));
        List<String> cleanYou = partsYou.stream()
                .filter(el -> !partsSanta.contains(el))
                .collect(Collectors.toList());
        List<String> cleanSanta = partsSanta.stream()
                .filter(el -> !partsYou.contains(el))
                .collect(Collectors.toList());

        return cleanYou.size() + cleanSanta.size() - 2;
    }

}
