package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day03 extends DayRunner {

    public Day03() {
        this.dayNumber = 3;
        this.className = this.getClass().getSimpleName();
        populateStringData();
    }

    @Override
    public void runDay() {
        results.add(solvePart1(exampleStringData));
        results.add(solvePart1(exerciseStringData));
        displayResults();
    }

    private int solvePart1(List<String> data) {
        Tuple start = new Tuple(0,0);
        Set<Tuple> path1 = generateCoordsSet(extractDirections(data.get(0)), start);
        Set<Tuple> path2 = generateCoordsSet(extractDirections(data.get(1)), start);

        return findManhatanDistance(path1, path2);
    }

    private int solvePart2(List<String> data) {
        return -1;
    }

    private List<String> extractDirections(String input) {
        return Arrays.asList(input.split(","));
    }

    private Set<Tuple> generateCoordsSet(List<String> values, Tuple start) {
        Set<Tuple> result = new HashSet<>();
        Tuple current = new Tuple(start);
        int size = values.size();
        for(int i = 0; i < size; i++) {
            current = appendNewCoords(result, current, values.get(i));
        }

        return result;
    }

    private Tuple appendNewCoords(Set<Tuple> all, Tuple start, String coords) {
        String direction = coords.substring(0,1);
        int times = Integer.parseInt(coords.substring(1));
        for(int i = 1; i <= times; i++) {
            start = new Tuple(start, direction);
            all.add(start);
        }

        return start;
    }

    private int findManhatanDistance(Set<Tuple> first, Set<Tuple> second) {
        return first.stream()
                .filter(e -> second.contains(e))
                .mapToInt(t -> abs(t.getFirst()) + abs(t.getSecond()))
                .min().orElse(-1);
    }


    private class Tuple {
        int first;
        int second;

        Tuple(int first, int second) {
            this.first = first;
            this.second = second;
        }

        Tuple(Tuple old) {
            this.first = old.getFirst();
            this.second = old.getSecond();
        }

        Tuple(Tuple old, String direction) {
            if (direction.equals("R")  || direction.equals("L")) {
                this.first = old.getFirst() + transformDirection(direction);
                this.second = old.getSecond();
            } else {
                this.first = old.getFirst();
                this.second = old.getSecond() + transformDirection(direction);
            }
        }

        private int transformDirection(String direction) {
            if (direction.equals("R")  || direction.equals("U")) {
                return 1;
            } else {
                return -1;
            }
        }

        public int getFirst() {
            return first;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return first == tuple.first &&
                    second == tuple.second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + "," + second + ')';
        }
    }

}
