package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 extends DayRunner {

    public Day05() {
        this.dayNumber = 5;
        this.className = this.getClass().getSimpleName();
    }

    @Override
    public void runDay() {
        transformInputDataToList();
        addResults(1, ResultType.EXERCISE, applyOpcodes(exerciseNumericData).get(0));

        displayResults();
    }

    private void transformInputDataToList() {
        exerciseNumericData = Arrays.asList(FileReader.INSTANCE.loadStringData("d05_exercise.txt").get(0).split(",")).stream()
                .map(el -> Integer.parseInt(el))
                .collect(Collectors.toList());
    }

    private List<Integer> applyOpcodes(List<Integer> initialData) {
        int dataSize = initialData.size();
        List<Integer> data = new ArrayList<>(initialData);
        int step = 2;

        for (int i = 0; i < dataSize; i += step) {
            int oppCode = data.get(i);

            if (oppCode == 99) {
                break;
            } else if (isInvalid(oppCode)) {
                throw new RuntimeException("Invalid opp code! " + oppCode);
            }

            step = applyOperation(oppCode, i, data);
        }

        return data;
    }

    private boolean isInvalid(int code) {
        List<Integer> allowed = Arrays.asList(1, 2, 3, 4);

        return !allowed.contains(code % 10);
    }

    private int applyOperation(int oppCode, int i, List<Integer> output) {
        int step = getInstructionLength(oppCode % 10);
        List<Integer> elements = output.subList(i+1, i+step);
        System.out.format("Applying opp code of %d on values %s \n", oppCode, elements);
        if (oppCode % 10 == 3) {
            applyInputOperation(elements, output);
        } else if (oppCode % 10 == 4) {
            applyOutputOperation(oppCode, elements, output);
        } else {
            applyReplaceOperation(oppCode, elements, output);
        }

        return step;
    }

    private int getInstructionLength(int oppCoode) {
        if (oppCoode == 3 || oppCoode == 4) {
            return 2;
        } else {
            return 4;
        }
    }

    private void applyInputOperation(List<Integer> elements, List<Integer> output) {
        int input = 1;
        System.out.println("Providing input value: " + input);
        output.set(elements.get(0), input);
    }

    private void applyOutputOperation(int oppCode, List<Integer> elements, List<Integer> output) {
        int value = elements.get(0);
        if (oppCode == 4) {
            value = output.get(value);
        }
        System.out.println("Output for current test is: " + value);
    }

    private void applyReplaceOperation(int oppCode, List<Integer> elements, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], elements.get(0), output);
        int secondElement = getValueByMode(modes[1], elements.get(1), output);

        int result = firstElement + secondElement;
        if (oppCode % 10 == 2) {
            result = firstElement * secondElement;
        }

        output.set(elements.get(2), result);
    }

    private int[] extractModes(int code) {
        if (code > 100) {
            String tmp = String.valueOf(code);
            if (tmp.length() < 4) {
                tmp = "0" + tmp;
            }
            String pos1 = String.valueOf(tmp.charAt(1));
            String pos2 = String.valueOf(tmp.charAt(0));
            return new int[]{Integer.parseInt(pos1), Integer.parseInt(pos2)};
        } else {
            return new int[]{0,0};
        }
    }

    private int getValueByMode(int mode, int value, List<Integer> output) {
        return mode == 0 ? output.get(value) : value;
    }

}
