package com.richardarcega.sudoku.solver;

import com.richardarcega.sudoku.puzzle.Puzzle;

interface Strategy
{
	void solve(Puzzle puzzle) throws UnsolvableException;
}
