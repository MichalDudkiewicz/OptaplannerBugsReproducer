package org.example;

import lombok.Getter;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@PlanningEntity
public class HalfHourTimeslot implements Comparable<HalfHourTimeslot>, Serializable {

    @Getter
    @PlanningId
    private UUID id;

    @Getter
    private DayOfWeek dayOfWeek;

    @Getter
    private DayOfWeek realDayOfWeek;

    @Getter
    private LocalTime startTime;

    @Getter
    @Setter
    @PlanningVariable(valueRangeProviderRefs = "teamRange")
    private Team team;

    public HalfHourTimeslot() {
    }

    public HalfHourTimeslot(DayOfWeek dayOfWeek, LocalTime startTime, DayOfWeek realDayOfWeek) {
        this.id = UUID.randomUUID();
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.realDayOfWeek = realDayOfWeek;
    }

    public LocalTime getEndTime()
    {
        return startTime.plusMinutes(30);
    }

    @Override
    public String toString()
    {
        return (team == null ? "no team" : team.toString()) + ": " + realDayOfWeek.toString() + ", " + startTime.toString() + "-" + getEndTime().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HalfHourTimeslot)) {
            return false;
        }

        HalfHourTimeslot slot = (HalfHourTimeslot) o;
        return id == slot.id;
    }

    @Override
    public int hashCode() {
        return dayOfWeek.hashCode() * realDayOfWeek.hashCode() * startTime.hashCode();
    }

    @Override
    public int compareTo(HalfHourTimeslot halfHourTimeslot) {
        int dayOfWeekComparison = dayOfWeek.compareTo(halfHourTimeslot.dayOfWeek);
        if (dayOfWeekComparison == 0) {
            int realDayOfWeekComparison = realDayOfWeek.compareTo(halfHourTimeslot.realDayOfWeek);
            if (realDayOfWeekComparison == 0) {
                return startTime.compareTo(halfHourTimeslot.startTime);
            }
            else {
                if (dayOfWeek == DayOfWeek.SUNDAY && halfHourTimeslot.dayOfWeek == DayOfWeek.SUNDAY) {
                    return -realDayOfWeekComparison;
                }
                return realDayOfWeekComparison;
            }
        }
        else {
            return dayOfWeekComparison;
        }
    }
}
