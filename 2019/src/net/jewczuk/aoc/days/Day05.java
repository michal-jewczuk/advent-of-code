package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;
import net.jewczuk.aoc.utils.Outputter;

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

        Outputter.INSTANCE.displayBreakLine(5);
        Outputter.INSTANCE.displayResuls(className, 1, 0, ResultType.EXERCISE.toString());
        solvePart1(exerciseNumericData);
        Outputter.INSTANCE.displayResuls(className, 2, 0, ResultType.EXERCISE.toString());
        solvePart2(exerciseNumericData);
        Outputter.INSTANCE.displayBreakLine(5);
    }

    private void transformInputDataToList() {
        exerciseNumericData = Arrays.asList(
                FileReader.INSTANCE.loadStringData("d05_exercise.txt").get(0).split(",")
        ).stream()
                .map(el -> Integer.parseInt(el))
                .collect(Collectors.toList());
    }

    private void solvePart1(List<Integer> initialData) {
        List<Integer> allowedCodes = Arrays.asList(1, 2, 3, 4);
        int initialInput = 1;

        applyOpcodes(initialData, initialInput, allowedCodes);
    }

    private void solvePart2(List<Integer> initialData) {
        List<Integer> allowedCodes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        int initialInput = 5;

        applyOpcodes(initialData, initialInput, allowedCodes);
    }

    private List<Integer> applyOpcodes(List<Integer> initialData, int initialInput, List<Integer> allowedCodes) {
        int dataSize = initialData.size();
        List<Integer> data = new ArrayList<>(initialData);

        int i = 0;
        while (i < dataSize) {
            int oppCode = data.get(i);

            if (oppCode == 99) {
                break;
            } else if (!allowedCodes.contains(oppCode % 10)) {
                throw new RuntimeException("Invalid opp code! " + oppCode);
            }

            i = applyOperation(oppCode, i, data, initialInput);
        }

        return data;
    }

    private int applyOperation(int oppCode, int idx, List<Integer> output, int initialInput) {
        int step = getInstructionLength(oppCode % 10);
        int singleDigit = oppCode % 10;

        List<Integer> elements = output.subList(idx+1, idx+step);
//        System.out.format("Applying opp code of %d on values %s \n", oppCode, elements);
        if (singleDigit == 3) {
            applyInputOperation(idx, output, initialInput);
        } else if (singleDigit == 4) {
            applyOutputOperation(oppCode, idx, output);
        } else if (singleDigit == 5) {
            return applyJumpIfTrueOperation(oppCode, idx, output);
        } else if (singleDigit == 6) {
            return applyJumpIfFalseOperation(oppCode, idx, output);
        } else if (singleDigit == 7) {
            applyLessThanOperation(oppCode, idx, output);
        } else if (singleDigit == 8) {
            applyEqualsOperation(oppCode, idx, output);
        } else {
            applyReplaceOperation(oppCode, idx, output);
        }

        return idx + step;
    }

    private int getInstructionLength(int oppCode) {
        if (oppCode == 3 || oppCode == 4) {
            return 2;
        } else if (oppCode == 5 || oppCode == 6) {
            return 3;
        } else {
            return 4;
        }
    }

    private void applyInputOperation(int idx, List<Integer> output, int input) {
        System.out.println("Providing input value: " + input);
        output.set(output.get(idx + 1), input);
    }

    private void applyOutputOperation(int oppCode, int idx, List<Integer> output) {
        int value = output.get(idx + 1);
        if (oppCode == 4) {
            value = output.get(value);
        }
        if (value != 0) {
            System.out.println("Current output is: " + value);
        }
    }

    private void applyReplaceOperation(int oppCode, int idx, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], output.get(idx + 1), output);
        int secondElement = getValueByMode(modes[1], output.get(idx + 2), output);

        int result = firstElement + secondElement;
        if (oppCode % 10 == 2) {
            result = firstElement * secondElement;
        }

        output.set(output.get(idx + 3), result);
    }

    private int applyJumpIfTrueOperation(int oppCode, int idx, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], output.get(idx + 1), output);
        int secondElement = getValueByMode(modes[1], output.get(idx + 2), output);

        if (firstElement != 0) {
            return secondElement;
        } else {
            return idx + 3;
        }
    }

    private int applyJumpIfFalseOperation(int oppCode, int idx, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], output.get(idx + 1), output);
        int secondElement = getValueByMode(modes[1], output.get(idx + 2), output);

        if (firstElement == 0) {
            return secondElement;
        } else {
            return idx + 3;
        }
    }

    private void applyLessThanOperation(int oppCode, int idx, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], output.get(idx + 1), output);
        int secondElement = getValueByMode(modes[1], output.get(idx + 2), output);

        int result = 0;
        if (firstElement < secondElement) {
            result = 1;
        }

        output.set(output.get(idx + 3), result);
    }

    private void applyEqualsOperation(int oppCode, int idx, List<Integer> output) {
        int [] modes = extractModes(oppCode);
        int firstElement = getValueByMode(modes[0], output.get(idx + 1), output);
        int secondElement = getValueByMode(modes[1], output.get(idx + 2), output);

        int result = 0;
        if (firstElement == secondElement) {
            result = 1;
        }

        output.set(output.get(idx + 3), result);
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
