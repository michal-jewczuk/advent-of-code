package net.jewczuk.aoc;

import net.jewczuk.aoc.d01.Day01;
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
        runners.add(new Day01());

        return runners;
    }
}
