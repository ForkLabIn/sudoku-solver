package com.richardarcega.sudoku.solver;

/**
 * SolverFactory manufactures SudokuSolver instances by algorithm.
 * Solver instances are obtained through the static SolverFactory.getSolver(Algorithm) method.
 */
public abstract class SolverFactory
{
	public static SudokuSolver getSolver(final Algorithm algorithm)
	{
		switch ( algorithm )
		{
			case BACKTRACK:
				return new BacktrackSudokuSolver();
			default:
				break;
		}

		throw new IllegalArgumentException( "Invalid sudoku algorithm specified: " + algorithm );
	}
}
