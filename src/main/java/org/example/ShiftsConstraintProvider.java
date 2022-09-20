package org.example;

import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.*;

import java.util.SortedSet;

public class ShiftsConstraintProvider implements ConstraintProvider {
    public static final int kMaximumShiftLength = 780; // hard inclusive [min]

    private int shiftLength(SortedSet<HalfHourTimeslot> sameDaySlots) {
        if (sameDaySlots.isEmpty()) {
            return 0;
        }
        else if (sameDaySlots.first().getDayOfWeek() != sameDaySlots.last().getRealDayOfWeek() || sameDaySlots.last().getEndTime().getHour() == 0) {
            return (1440 - (sameDaySlots.first().getStartTime().getHour() * 60 + sameDaySlots.first().getStartTime().getMinute())) + (sameDaySlots.last().getEndTime().getHour() * 60 + sameDaySlots.last().getEndTime().getMinute());
        }
        else {
            return sameDaySlots.last().getEndTime().getHour() * 60 + sameDaySlots.last().getEndTime().getMinute() - (sameDaySlots.first().getStartTime().getHour() * 60 + sameDaySlots.first().getStartTime().getMinute());
        }
    }

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard
                maxShiftLength(constraintFactory),
                noOverlaps(constraintFactory),

                // Medium
                freeDayOfWeek(constraintFactory),
                minimizeShiftLength(constraintFactory),
        };
    }

    public Constraint maxShiftLength(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(HalfHourTimeslot.class)
                .groupBy(HalfHourTimeslot::getTeam, HalfHourTimeslot::getDayOfWeek, ConstraintCollectors.toSortedSet())
                .filter((team, dayOfWeek, daySlots) -> shiftLength(daySlots) > kMaximumShiftLength)
                .penalize("maximumShiftLength", HardMediumSoftScore.ONE_HARD, (team, dayOfWeek, daySlots) -> shiftLength(daySlots) - kMaximumShiftLength);
    }

    public Constraint noOverlaps(ConstraintFactory constraintFactory) {
        return constraintFactory.forEachUniquePair(HalfHourTimeslot.class, Joiners.equal(HalfHourTimeslot::getTeam), Joiners.equal(HalfHourTimeslot::getRealDayOfWeek), Joiners.equal(HalfHourTimeslot::getStartTime))
                .penalize("noOverlaps", HardMediumSoftScore.ONE_HARD, (slot1, slot2) -> 100);
    }

    public Constraint freeDayOfWeek(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(HalfHourTimeslot.class)
                .groupBy(HalfHourTimeslot::getTeam, HalfHourTimeslot::getDayOfWeek, ConstraintCollectors.toList())
                .filter((team, dayOfWeek, daySlots) -> !daySlots.isEmpty())
                .penalize("freeDayOfWeek", HardMediumSoftScore.ONE_MEDIUM, (team, dayOfWeek, daySlots) -> 500000);
    }

    public Constraint minimizeShiftLength(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(HalfHourTimeslot.class)
                .groupBy(HalfHourTimeslot::getTeam, HalfHourTimeslot::getDayOfWeek, ConstraintCollectors.toSortedSet())
                .penalize("minimizeShiftLength", HardMediumSoftScore.ONE_MEDIUM, (team, dayOfWeek, daySlots) ->
                        shiftLength(daySlots) / 30);
    }
}
