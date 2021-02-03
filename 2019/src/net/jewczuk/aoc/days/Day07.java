package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;
import net.jewczuk.aoc.utils.Outputter;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Day07 extends DayRunner {

    BlockingQueue<Integer> finalOutput = new LinkedBlockingQueue<>();

    public Day07() {
        this.dayNumber = 7;
        this.className = this.getClass().getSimpleName();
    }

    @Override
    public void runDay() {
        List<Integer> additionalData1 = transformInputDataToList("d07_additional_1.txt");
        List<Integer> additionalData2 = transformInputDataToList("d07_additional_2.txt");
        List<Integer> additionalData3 = transformInputDataToList("d07_additional_3.txt");
        List<Integer> additionalData4 = transformInputDataToList("d07_additional_4.txt");
        exampleNumericData = transformInputDataToList("d07_example.txt");
        exerciseNumericData = transformInputDataToList("d07_exercise.txt");

        Outputter.INSTANCE.displayBreakLine(dayNumber);
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(exampleNumericData), ResultType.EXAMPLE.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(additionalData1), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(additionalData2), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 1, solvePart1(exerciseNumericData), ResultType.EXERCISE.toString());

        Outputter.INSTANCE.displayResuls(className, 2, solvePart2(additionalData3), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 2, solvePart2(additionalData4), ResultType.ADDITIONAL.toString());
        Outputter.INSTANCE.displayResuls(className, 2, solvePart2(exerciseNumericData), ResultType.EXERCISE.toString());

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
        List<List<Integer>> sequences = generateSequences(Arrays.asList(1,2,3,4,5));
        int maxValue = 0;
        List<Integer> maxSeq = null;
        int limit = sequences.size();
        for (int i = 0; i < limit; i++) {
            int seq = runSingleMode(sequences.get(i), initialData);
            if (seq > maxValue) {
                maxValue = seq;
                maxSeq = sequences.get(i);
            }
        }
        System.out.println(maxSeq);
        return maxValue;
    }

    private int solvePart2(List<Integer> initialData) {
        List<List<Integer>> sequences = generateSequences(Arrays.asList(5,6,7,8,9));

        int maxValue = 0;
        List<Integer> maxSeq = null;
        int limit = sequences.size();
        for (int i = 0; i < limit; i++) {
            int seq = runLoopMode(sequences.get(i), initialData);
            if (seq > maxValue) {
                maxValue = seq;
                maxSeq = sequences.get(i);
            }
        }
        System.out.println(maxSeq);
        return maxValue;
    }

    private int runLoopMode(List<Integer> sequence, List<Integer> data) {
        List<Integer> stateA = new ArrayList<>(data);
        List<Integer> stateB = new ArrayList<>(data);
        List<Integer> stateC = new ArrayList<>(data);
        List<Integer> stateD = new ArrayList<>(data);
        List<Integer> stateE = new ArrayList<>(data);
        BlockingQueue<Integer> inputsA = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> inputsB = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> inputsC = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> inputsD = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> inputsE = new LinkedBlockingQueue<>();
        inputsA.add(0);

        Thread tA = new Thread(() -> {runAmplifier(stateA, sequence.get(0), inputsA, inputsB, "A");});
        Thread tB = new Thread(() -> {runAmplifier(stateB, sequence.get(1), inputsB, inputsC, "B");});
        Thread tC = new Thread(() -> {runAmplifier(stateC, sequence.get(2), inputsC, inputsD, "C");});
        Thread tD = new Thread(() -> {runAmplifier(stateD, sequence.get(3), inputsD, inputsE, "D");});
        Thread tE = new Thread(() -> {runAmplifier(stateE, sequence.get(4), inputsE, inputsA, "E");});
        tA.start();
        tB.start();
        tC.start();
        tD.start();
        tE.start();

        try {
            return finalOutput.take();
        } catch (InterruptedException e) {
            return -1;
        }

    }

    private int runAmplifier(List<Integer> state, int phase, BlockingQueue<Integer> inputs, BlockingQueue<Integer> outputs, String tag) {
        int idx = 0;
        int output = 0;
        int inputCount = 0;

        try {
            while (true) {
                int oppCode = state.get(idx);

                if (oppCode == 99) {
                    if (tag.equals("E")) {
                        finalOutput.add(output);
                    }
                    break;
                } else if (oppCode == 4) {
                    output = applyOutputOperation(oppCode, idx, state);
                    outputs.put(output);
                    idx += 2;
                } else if (oppCode == 3) {
                    int value = inputCount > 0 ? inputs.take() : phase;
                    state.set(state.get(idx + 1), value);
                    inputCount++;
                    idx += 2;
                } else {
                    idx = applyOperation(oppCode, idx, state);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Nobody expects Spanish Inquisition!");
        }

        return -1;
    }

    private List<List<Integer>> generateSequences(List<Integer> elements) {
        List<List<Integer>> sequences = new ArrayList<>();
        int count = elements.size();

        for (int i = 0; i < count; i++) {
            sequences.add(Arrays.asList(elements.get(i)));
        }
        for (int i = 1; i < count; i++) {
            sequences = extendSequence(sequences, elements);
        }

        return sequences;
    }

    private List<List<Integer>> extendSequence(List<List<Integer>> current, List<Integer> elements) {
        List<List<Integer>> newSeq = new ArrayList<>();
        current.forEach(curr -> {
            elements.forEach(el -> {
                if (!curr.contains(el)) {
                    List<Integer> newEl = new ArrayList<>(curr);
                    newEl.add(el);
                    newSeq.add(newEl);
                }
            });
        });

        return newSeq;
    }

    private int runSingleMode(List<Integer> sequence, List<Integer> data) {
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
