package net.jewczuk.aoc.d01;

import net.jewczuk.aoc.utils.DayRunner;
import net.jewczuk.aoc.utils.FileReader;
import net.jewczuk.aoc.utils.Outputter;

import java.util.List;

public class Day01 implements DayRunner {

    @Override
    public void runDay() {
        List<Integer> exampleData = FileReader.INSTANCE.loadNumericData("d01_example.txt");
        List<Integer> exerciseData = FileReader.INSTANCE.loadNumericData("d01_exercise.txt");

        Integer example1 = calculateNeededFuel(exampleData);
        Outputter.INSTANCE.displayExampleResults(this.getClass().getSimpleName(), 1, example1);

        Integer exercise1 = calculateNeededFuel(exerciseData);
        Outputter.INSTANCE.displayExerciseResults(this.getClass().getSimpleName(), 1, exercise1);

        Integer example2 = calculateNeededFuelWithFuel(exampleData);
        Outputter.INSTANCE.displayExampleResults(this.getClass().getSimpleName(), 2, example2);

        Integer exercise2 = calculateNeededFuelWithFuel(exerciseData);
        Outputter.INSTANCE.displayExerciseResults(this.getClass().getSimpleName(), 2, exercise2);
    }

    private Integer calculateNeededFuel(List<Integer> data) {
        return data.stream()
                .map(mass -> getFuelForMass(mass))
                .reduce(0, Integer::sum);
    }

    private Integer getFuelForMass(Integer mass) {
        Integer result = Math.floorDiv(mass, 3);

        return result <= 2 ? 0 : result - 2;
    }

    private Integer calculateNeededFuelWithFuel(List<Integer> data) {
        return data.stream()
                .map(mass -> getFuelForMassAndFuel(mass))
                .reduce(0, Integer::sum);
    }

    private Integer getFuelForMassAndFuel(Integer mass) {
        int tmpFuel = getFuelForMass(mass);
        int sum = tmpFuel;

        while(true) {
            tmpFuel = getFuelForMass(tmpFuel);
            if(tmpFuel == 0) {
                return sum;
            } else {
                sum += tmpFuel;
            }
        }
    }
}
