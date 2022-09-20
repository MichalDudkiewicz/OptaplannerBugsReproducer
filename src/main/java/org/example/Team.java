package org.example;

import lombok.Getter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;

public class Team implements Comparable<Team>, Serializable {

    @PlanningId
    @Getter
    private String name;

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(Team other) {
        return name.compareTo(other.name);
    }
}
