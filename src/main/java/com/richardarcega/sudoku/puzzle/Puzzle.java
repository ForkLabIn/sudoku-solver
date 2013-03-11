package com.richardarcega.sudoku.puzzle;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a sudoku puzzle.
 */
public class Puzzle
{
	private Grid	grid;
	private Grid	solution;

	public Puzzle(final Grid grid)
	{
		this.grid = grid;
	}

	public Grid getGrid()
	{
		return grid;
	}

	public void setGrid(final Grid grid)
	{
		this.grid = grid;
	}

	public Grid getSolution()
	{
		return solution;
	}

	public void setSolution(final Grid solution)
	{
		this.solution = solution;
	}

	public int at(final int x, final int y)
	{
		return grid.getCellValue( x, y );
	}

	public void load(final InputStream puzzleFile) throws IOException
	{
		grid.load(puzzleFile);
	}

	@Override
	public String toString()
	{
		return grid.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( grid == null ) ? 0 : grid.hashCode() );
		result = prime * result + ( ( solution == null ) ? 0 : solution.hashCode() );
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		final Puzzle other = (Puzzle) obj;
		if ( grid == null )
		{
			if ( other.grid != null )
				return false;
		}
		else if ( !grid.equals( other.grid ) )
			return false;
		if ( solution == null )
		{
			if ( other.solution != null )
				return false;
		}
		else if ( !solution.equals( other.solution ) )
			return false;
		return true;
	}


}
