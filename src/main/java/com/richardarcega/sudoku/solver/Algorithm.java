package com.richardarcega.sudoku.solver;

/**
 * This class defines the constants that are used to identify the
 * algorithms that are available to solve sudoku puzzles.
 */
public enum Algorithm
{
	BACKTRACK
	{

	},
	NORVIG
	{

	},
	DLX
	{

	};

	@Override
	public String toString()
	{
		switch ( this )
		{
			case BACKTRACK:
				System.out.println( "Backtracking with forward-checking" );
				break;
			case NORVIG:
				System.out.println( "Algorithm X implemention - \"Dancing Links (DLX)\" by Donald Knuth" );
				break;
			case DLX:
				System.out.println( "Peter Norvig's Sudoku Solution using CPS" );
				break;
		}

		return super.toString();
	}

}
