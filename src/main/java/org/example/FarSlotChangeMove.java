package org.example;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class FarSlotChangeMove extends AbstractMove<ShiftsSolution> {

    private final HalfHourTimeslot slot;
    private final Team toTeam;

    private final Team fromTeam;

    public FarSlotChangeMove(HalfHourTimeslot slot, Team toTeam) {
        this.slot = slot;
        this.toTeam = toTeam;
        this.fromTeam = slot.getTeam();
    }

    @Override
    protected AbstractMove<ShiftsSolution> createUndoMove(ScoreDirector<ShiftsSolution> scoreDirector) {
        return new FarSlotChangeMove(slot, fromTeam);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<ShiftsSolution> scoreDirector) {
        scoreDirector.beforeVariableChanged(slot, "team");
        slot.setTeam(toTeam);
        scoreDirector.afterVariableChanged(slot, "team");
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<ShiftsSolution> scoreDirector) {
        // we don't check overlaps here, because it is already checked by the move factory
        return !toTeam.equals(fromTeam);
    }

    @Override
    public FarSlotChangeMove rebase(ScoreDirector<ShiftsSolution> destinationScoreDirector) {
        return new FarSlotChangeMove(destinationScoreDirector.lookUpWorkingObject(slot), destinationScoreDirector.lookUpWorkingObject(toTeam));
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return Collections.singletonList(slot);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Collections.singletonList(toTeam);
    }

    @Override
    public String toString() {
        return slot + " {" + fromTeam + "->" + toTeam + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FarSlotChangeMove other = (FarSlotChangeMove) o;
        return Objects.equals(fromTeam, other.fromTeam) &&
                Objects.equals(toTeam, other.toTeam) &&
                Objects.equals(slot, other.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot, toTeam, fromTeam);
    }
}
