package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;
import net.jewczuk.aoc.utils.Outputter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day07 extends DayRunner {

    public Day07() {
        this.dayNumber = 7;
        this.className = this.getClass().getSimpleName();
    }

    @Override
    public void runDay() {
        List<Integer> additionalData1 = transformInputDataToList("d07_additional_1.txt");
        List<Integer> additionalData2 = transformInputDataToList("d07_additional_2.txt");
        exampleNumericData = transformInputDataToList("d07_example.txt");
        exerciseNumericData = transformInputDataToList("d07_exercise.txt");

        Outputter.INSTANCE.displayBreakLine(dayNumber);
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(exampleNumericData), ResultType.EXAMPLE.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(additionalData1), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(additionalData2), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(exerciseNumericData), ResultType.EXERCISE.toString());

        Outputter.INSTANCE.displayBreakLine(dayNumber);
    }

    private List<Integer> transformInputDataToList(String fileName) {
        return Arrays.asList(
                FileReader.INSTANCE.loadStringData(fileName).get(0).split(",")
        ).stream()
                .map(el -> Integer.parseInt(el))
                .collect(Collectors.toList());
    }

    private int solvePart1(List<Integer> initialData) {
        List<List<Integer>> sequences = generateSequences(5);
        int maxValue = 0;
        List<Integer> maxSeq = null;
        int limit = sequences.size();
        for (int i = 0; i < limit; i++) {
            int seq = runForSequence(sequences.get(i), initialData);
            if (seq > maxValue) {
                maxValue = seq;
                maxSeq = sequences.get(i);
            }
        }
        System.out.println(maxSeq);
        return maxValue;
    }

    private List<List<Integer>> generateSequences(int thrustersCount) {
        List<List<Integer>> sequences = new ArrayList<>();

        for (int i = 0; i < thrustersCount; i++) {
            sequences.add(Arrays.asList(i));
        }
        for (int i = 1; i < thrustersCount; i++) {
            sequences = extendSequence(sequences, thrustersCount);
        }

        return sequences;
    }

    private List<List<Integer>> extendSequence(List<List<Integer>> current, int count) {
        List<List<Integer>> newSeq = new ArrayList<>();
        current.forEach(el -> {
            for (int i = 0; i < count; i++) {
                if (!el.contains(i)) {
                    List<Integer> newEl = new ArrayList<>(el);
                    newEl.add(i);
                    newSeq.add(newEl);
                }
            }
        });

        return newSeq;
    }

    private int runForSequence(List<Integer> sequence, List<Integer> data) {
        List<Integer> allowedCodes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        List<Integer> inputs = Arrays.asList(sequence.get(0), 0);
        int output = calculateAmplifier(data, inputs, allowedCodes);
        inputs = Arrays.asList(sequence.get(1), output);
        output = calculateAmplifier(data, inputs, allowedCodes);
        inputs = Arrays.asList(sequence.get(2), output);
        output = calculateAmplifier(data, inputs, allowedCodes);
        inputs = Arrays.asList(sequence.get(3), output);
        output = calculateAmplifier(data, inputs, allowedCodes);
        inputs = Arrays.asList(sequence.get(4), output);

        return calculateAmplifier(data, inputs, allowedCodes);
    }

    private int calculateAmplifier(List<Integer> initialData, List<Integer> inputs, List<Integer> allowedCodes) {
        int dataSize = initialData.size();
        List<Integer> data = new ArrayList<>(initialData);

        int currInputNr = 0;
        int i = 0;
        while (i < dataSize) {
            int oppCode = data.get(i);

            if (oppCode == 4) {
                return applyOutputOperation(oppCode, i, data);
            } else if (oppCode == 99) {
                break;
            } else if (!allowedCodes.contains(oppCode % 10)) {
                throw new RuntimeException("Invalid opp code! " + oppCode);
            }

            if (oppCode == 3) {
                data.set(data.get(i + 1), inputs.get(currInputNr++));
                i = i + 2;
            } else {
                i = applyOperation(oppCode, i, data);
            }
        }

        return -1;
    }

    private int applyOperation(int oppCode, int idx, List<Integer> output) {
        int step = getInstructionLength(oppCode % 10);
        int singleDigit = oppCode % 10;

        if (singleDigit == 5) {
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

    private int applyOutputOperation(int oppCode, int idx, List<Integer> output) {
        int value = output.get(idx + 1);
        if (oppCode == 4) {
            value = output.get(value);
        }
        return value;
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
