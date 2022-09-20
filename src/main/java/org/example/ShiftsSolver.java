package org.example;

import org.optaplanner.core.api.solver.SolverFactory;

public class ShiftsSolver {
    private final SolverFactory<ShiftsSolution> solverFactory;

    public ShiftsSolver(String solverConfigPath)
    {
        solverFactory = SolverFactory.createFromXmlResource(solverConfigPath);
    }

    public ShiftsSolution solve(ShiftsSolution problem) {
        var solver = solverFactory.buildSolver();

        return solver.solve(problem);
    }
}
