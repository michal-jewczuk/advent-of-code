package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 extends DayRunner {

    public Day02() {
        this.dayNumber = 2;
        this.className = this.getClass().getSimpleName();
        populateStringData();
    }

    @Override
    public void runDay() {
        transformInputDataToList();
        results.add(applyOpcodes(exampleNumericData).get(0));
        results.add(applyOpcodes(exerciseNumericData).get(0));
        results.add(calculateNounVerb(exampleNumericData));
        results.add(calculateNounVerb(exerciseNumericData));
        displayResults();
    }

    private void transformInputDataToList() {
        exampleNumericData = Arrays.asList(exampleStringData.get(0).split(",")).stream()
                .map(el -> Integer.parseInt(el))
                .collect(Collectors.toList());
        exerciseNumericData = Arrays.asList(exerciseStringData.get(0).split(",")).stream()
                .map(el -> Integer.parseInt(el))
                .collect(Collectors.toList());
    }

    private List<Integer> applyOpcodes(List<Integer> initialData) {
        int dataSize = initialData.size();
        List<Integer> data = new ArrayList<>(initialData);

        for (int i = 0; i < dataSize; i += 4) {
            int oppCode = data.get(i);

            if (oppCode == 99) {
                break;
            } else if (oppCode != 1 && oppCode != 2) {
                throw new RuntimeException("Invalid opp code!");
            }

            List<Integer> elements = data.subList(i+1, i+4);
            applyOperation(oppCode, elements, data);
        }

        return data;
    }

    private void applyOperation(int oppCode, List<Integer> elements, List<Integer> output) {
        int firstElement = output.get(elements.get(0));
        int secondElement = output.get(elements.get(1));

        int result = firstElement + secondElement;
        if (oppCode == 2) {
            result = firstElement * secondElement;
        }

        output.set(elements.get(2), result);
    }

    private int calculateNounVerb(List<Integer> data) {
        int dataSize = data.size();
        List<Integer> workingData;
        for (int i = 0; i < dataSize; i++) {
            for (int j = 0; j < dataSize; j++) {
                workingData = data.subList(0, data.size());
                workingData.set(1, i);
                workingData.set(2, j);
                List<Integer> result = applyOpcodes(workingData);
                if (result.get(0) == 19690720) {
                    return 100 * i + j;
                }
            }
        }

        return -1;
    }

}
