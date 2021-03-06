package net.jewczuk.aoc;

import net.jewczuk.aoc.days.*;
import net.jewczuk.aoc.utils.DayRunner;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	    List<DayRunner> runners = getListOfRunners();
	    runners.stream().forEach(r -> r.runDay());
    }

    private static List<DayRunner> getListOfRunners() {
        List<DayRunner> runners = new ArrayList<>();
//        runners.add(new Day01());
//        runners.add(new Day02());
//        runners.add(new Day03());
//        runners.add(new Day04());
//        runners.add(new Day05());
//        runners.add(new Day06());
        runners.add(new Day07());

        return runners;
    }
}
