package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;

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
        List<String> additionalData1 = FileReader.INSTANCE.loadStringData("d03_additional_1.txt");
        List<String> additionalData2 = FileReader.INSTANCE.loadStringData("d03_additional_2.txt");
        addResults(1, ResultType.EXAMPLE, solvePart1(exampleStringData));
        addResults(1, ResultType.ADDITIONAL, solvePart1(additionalData1));
        addResults(1, ResultType.ADDITIONAL, solvePart1(additionalData2));
        addResults(1, ResultType.EXERCISE, solvePart1(exerciseStringData));

        addResults(2, ResultType.EXAMPLE, solvePart2(exampleStringData));
        addResults(2, ResultType.ADDITIONAL, solvePart2(additionalData1));
        addResults(2, ResultType.ADDITIONAL, solvePart2(additionalData2));
        addResults(2, ResultType.EXERCISE, solvePart2(exerciseStringData));
        displayResults();
    }

    private int solvePart1(List<String> data) {
        List<Set<Tuple>> paths = createPaths(data);

        return findManhatanDistance(paths.get(0), paths.get(1));
    }

    private int solvePart2(List<String> data) {
        List<Set<Tuple>> paths = createPaths(data);
        List<Tuple> path1 = findJointPoints(paths.get(0), paths.get(1));
        List<Tuple> path2 = findJointPoints(paths.get(1), paths.get(0));

        return findFewestCombinedSteps(path1, path2);
    }

    private List<Set<Tuple>> createPaths(List<String> data) {
        Tuple start = new Tuple(0,0);
        return data.stream()
                .map(el -> generateCoordsSet(extractDirections(el), start))
                .collect(Collectors.toList());
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

    private List<Tuple> findJointPoints(Set<Tuple> target, Set<Tuple> toLookIn) {
        return target.stream().filter(el -> toLookIn.contains(el)).collect(Collectors.toList());
    }

    private int findFewestCombinedSteps(List<Tuple> first, List<Tuple> second) {
        Collections.sort(first);
        Collections.sort(second);
        int size = first.size();
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < size; i++) {
            int tmp = first.get(i).getLength() + second.get(i).getLength();
            if(tmp < min) {
                min = tmp;
            }
        }
        return min;
    }


    private class Tuple implements Comparable<Tuple> {
        int first;
        int second;
        int length;

        Tuple(int first, int second) {
            this.first = first;
            this.second = second;
            this.length = 0;
        }

        Tuple(Tuple old) {
            this.first = old.getFirst();
            this.second = old.getSecond();
            this.length = old.getLength();
        }

        Tuple(Tuple old, String direction) {
            if (direction.equals("R")  || direction.equals("L")) {
                this.first = old.getFirst() + transformDirection(direction);
                this.second = old.getSecond();
            } else {
                this.first = old.getFirst();
                this.second = old.getSecond() + transformDirection(direction);
            }
            this.length = old.getLength() + 1;
        }

        private int transformDirection(String direction) {
            if (direction.equals("R")  || direction.equals("U")) {
                return 1;
            } else {
                return -1;
            }
        }

        @Override
        public int compareTo(Tuple o) {
            if (this.getFirst() != o.getFirst()) {
                return this.getFirst() - o.getFirst();
            } else {
                return this.getSecond() - o.getSecond();
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

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
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
            return "(" + first + "," + second + ")[" + length + "]";
        }
    }

}
