package org.example;

import lombok.Getter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.io.Serializable;
import java.util.List;

@PlanningSolution
public class ShiftsSolution implements Serializable {

    @Getter
    @PlanningEntityCollectionProperty
    private List<HalfHourTimeslot> slots;

    @Getter
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "teamRange")
    private List<Team> teams;

    @Getter
    @PlanningScore
    private transient HardMediumSoftScore score;

    public ShiftsSolution() {
    }

    public ShiftsSolution(List<HalfHourTimeslot> slots, List<Team> teams) {
        this.slots = slots;
        this.teams = teams;
    }

    @Override
    public String toString() {
        return slots.toString();
    }
}
