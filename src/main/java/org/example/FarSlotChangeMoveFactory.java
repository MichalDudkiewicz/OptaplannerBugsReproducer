package org.example;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class FarSlotChangeMoveFactory implements MoveListFactory<ShiftsSolution> {

    private static Predicate<HalfHourTimeslot> equalTime(HalfHourTimeslot slot2) {
        return slot -> slot.compareTo(slot2) == 0;
    }

    private static Predicate<HalfHourTimeslot> sameDayAndTeam(DayOfWeek day, Team team) {
        return slot -> slot.getDayOfWeek().equals(day) && slot.getTeam().equals(team);
    }

    @Override
    public List<? extends Move<ShiftsSolution>> createMoveList(ShiftsSolution solution) {
        List<FarSlotChangeMove> moves = new ArrayList<>();
        List<HalfHourTimeslot> slots = solution.getSlots();
        List<Team> teams = solution.getTeams();
        for (Team fromTeam : teams) {
            for (Team toTeam : teams) {
                if (fromTeam != toTeam) {
                    for (DayOfWeek day : DayOfWeek.values()) {
                        List<HalfHourTimeslot> fromTeamSlots = slots.stream().filter(sameDayAndTeam(day, fromTeam)).collect(Collectors.toList());
                        List<HalfHourTimeslot> toTeamSlots = slots.stream().filter(sameDayAndTeam(day, toTeam)).collect(Collectors.toList());

                        Optional<HalfHourTimeslot> fromMinOpt = fromTeamSlots.stream().min(Comparator.naturalOrder());
                        Optional<HalfHourTimeslot> fromMaxOpt = fromTeamSlots.stream().max(Comparator.naturalOrder());
                        Optional<HalfHourTimeslot> toMaxOpt = toTeamSlots.stream().max(Comparator.naturalOrder());
                        Optional<HalfHourTimeslot> toMinOpt = toTeamSlots.stream().min(Comparator.naturalOrder());

                        if (fromMinOpt.isPresent() && fromMaxOpt.isPresent() && toMaxOpt.isPresent() && toMinOpt.isPresent())
                        {
                            HalfHourTimeslot fromMin = fromMinOpt.get();
                            HalfHourTimeslot fromMax = fromMaxOpt.get();
                            HalfHourTimeslot toMax = toMaxOpt.get();
                            HalfHourTimeslot toMin = toMinOpt.get();

                            if (toMin.compareTo(fromMin) < 0 && toMax.compareTo(fromMin) > 0 && toTeamSlots.stream().noneMatch(equalTime(fromMin)))
                            {
                                moves.add(new FarSlotChangeMove(fromMin, toTeam));
                            }

                            if (toMin.compareTo(fromMax) < 0 && toMax.compareTo(fromMax) > 0 && toTeamSlots.stream().noneMatch(equalTime(fromMax)))
                            {
                                moves.add(new FarSlotChangeMove(fromMax, toTeam));
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }
}
