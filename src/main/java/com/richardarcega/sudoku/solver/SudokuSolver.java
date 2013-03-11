package com.richardarcega.sudoku.solver;

import com.richardarcega.sudoku.puzzle.Puzzle;

/**
 * A class to solve sudoku puzzles.
 */
public abstract class SudokuSolver implements Strategy
{
	/**
	 * Solves the specified puzzle.
	 */
	@Override
	public abstract void solve(Puzzle puzzle) throws UnsolvableException;
}
