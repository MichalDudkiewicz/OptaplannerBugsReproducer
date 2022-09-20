package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    private static void serialize(ShiftsSolution solution) {
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("src/test/resources/testSolution.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(solution);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public static void main(String... args) {
        List<HalfHourTimeslot> slots = createDummySlots();
        List<Team> teams = createDummyTeams();

        ShiftsSolution problem = new ShiftsSolution(slots, teams);

        String solverConfigPath = "solverConfig.xml";
        ShiftsSolver shiftsSolver = new ShiftsSolver(solverConfigPath);
        ShiftsSolution solution = shiftsSolver.solve(problem);

        System.out.println(solution);

        serialize(solution);
    }
}