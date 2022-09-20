package org.example;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class Main {

    private static List<HalfHourTimeslot> createDummySlots()
    {
        HalfHourTimeslot slot1 = new HalfHourTimeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), DayOfWeek.MONDAY);
        HalfHourTimeslot slot2 = new HalfHourTimeslot(DayOfWeek.MONDAY, LocalTime.of(12, 30), DayOfWeek.MONDAY);
        HalfHourTimeslot slot3 = new HalfHourTimeslot(DayOfWeek.MONDAY, LocalTime.of(15, 30), DayOfWeek.MONDAY);
        return Arrays.asList(slot1, slot2, slot3);
    }

    private static List<Team> createDummyTeams()
    {
        Team team1 = new Team("Anything");
        Team team2 = new Team("Whatever");
        return Arrays.asList(team1, team2);
    }

    public static void main(String... args) {
        List<HalfHourTimeslot> slots = createDummySlots();
        List<Team> teams = createDummyTeams();

        ShiftsSolution problem = new ShiftsSolution(slots, teams);

        String solverConfigPath = "solverConfig.xml";
        ShiftsSolver shiftsSolver = new ShiftsSolver(solverConfigPath);
        ShiftsSolution solution = shiftsSolver.solve(problem);

        System.out.println(solution);
    }
}