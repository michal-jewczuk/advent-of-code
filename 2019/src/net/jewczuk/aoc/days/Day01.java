package net.jewczuk.aoc.days;

import net.jewczuk.aoc.utils.DayRunner;

import java.util.List;

public class Day01 extends DayRunner {

    public Day01() {
        this.dayNumber = 1;
        this.className = this.getClass().getSimpleName();
        populateNumericData();
    }

    @Override
    public void runDay() {
        results.add(calculateNeededFuel(exampleNumericData));
        results.add(calculateNeededFuel(exerciseNumericData));
        results.add(calculateNeededFuelWithFuel(exampleNumericData));
        results.add(calculateNeededFuelWithFuel(exerciseNumericData));
        displayResults();
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
