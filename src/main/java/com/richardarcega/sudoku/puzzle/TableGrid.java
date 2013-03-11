package com.richardarcega.sudoku.puzzle;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Table;

/**
 * A table based implementation of the Sudoku grid.
 */
public class TableGrid extends Grid
{
	/**
	 * The Sudoku grid is modeled using a Table that associates an ordered pair of keys, the
	 * row key and the column key, with a single value. The row and column keys correspond to
	 * the row and cell indices (coordinates) and the value to that of the digit contained
	 * within the cell.
	 */
	private final Table<Integer, Integer, Integer> grid;

	/**
	 * The solution to the puzzle
	 */
	private final Table<Integer, Integer, Integer> solution;

	public TableGrid(final int numRows, final int numCols)
	{
		checkArgument( numRows > 0, "Number of grid rows must be > 0. Specified: " + numRows );
		checkArgument( numCols > 0, "Number of grid cols must be > 0. Specified: " + numCols );
		checkArgument( numRows ==  numCols, "Number of grid rows must be equal to number of grid columns. Rows: " + numRows + " =/= " + "Cols: " + numCols );

		final ContiguousSet<Integer> rowKeys = ContiguousSet.create( Range.closedOpen(0, numRows), DiscreteDomain.integers() );
	    final ContiguousSet<Integer> colKeys = ContiguousSet.create( Range.closedOpen(0, numCols), DiscreteDomain.integers() );

	    grid     = ArrayTable.create( rowKeys, colKeys );
		solution = ArrayTable.create( rowKeys, colKeys );
	}

	/**
	 * Returns the digit contained within the specified cell.
	 *
	 * @param x		the x coordinate of the cell
	 * @param y		the y coordinate of the cell
	 *
	 * @return the digit within the cell
	 */
	@Override
	public int getCellValue(final int x, final int y)
	{
		checkArgument( x >= 0 && x < grid.rowKeySet().size(), "Invalid x coordinate: " + x );
		checkArgument( y >= 0 && y < grid.columnKeySet().size(), "Invalid y coordinate: " + y );

		return grid.get( x, y );
	}

	/**
	 * Loads a puzzle grid from an InputStream (typically a text file),
	 * and stores in a Table-based representation.
	 *
	 * See src/main/resources/format.txt for the format of the puzzle
	 * text file.
	 *
	 * @param puzzleFile	the file containing the sudoku puzzle
	 */
	@Override
	public void load(final InputStream puzzleFile) throws IOException
	{
		try ( BufferedReader reader = new BufferedReader( new InputStreamReader( puzzleFile ) ) )
		{
			String line = null;

			// Iterate through the file line-by-line, capturing the digits
			for ( int y = 0, isSolution = 0; ( line = reader.readLine() ) != null;  )
			{
				// split and store the digits within the current line
				final String[] digits = Iterables.toArray(
					Splitter
					    .on( CharMatcher.anyOf(" |") )
						.trimResults()
					    .omitEmptyStrings()
					    .split( line )
			    , String.class);

				// treat a line with nine valid tokens as a valid sudoku row
				if ( digits.length == 9)
				{
					for ( int x = 0; x < 9; x++)
					{
						// set the value of cell if it known
						if ( Character.isDigit( digits[x].charAt(0) ) )
						{
							if ( isSolution == 0 )
							{
								grid.put( x, y, Integer.valueOf( digits[x] ) );
							}
							else
							{
								solution.put( x, y, Integer.valueOf( digits[x] ) );
							}
						}
						else
						{
							// unsolved cell
							grid.put( x, y, 0 );
						}
					}

					// only increment for actual rows--ignore the dotted separators between boxes
					y++;
				}

				// a blank line serves as the separator between unsolved puzzle and its solution
				if ( digits.length == 0 )
				{
					y = 0; 			// reset row counter for solution grid
					isSolution = 1; // start populating the solution grid
				}
			}
		}
	}

	/**
	 * Returns the empty cells in the grid.
	 *
	 * @return a List of Cell objects
	 */
	@Override
	public List<Cell> getEmptyCells()
	{
		final List<Cell> emptyCells = Lists.newArrayList();

		final int numRows = grid.rowKeySet().size();
		final int numCols = grid.columnKeySet().size();

		for ( int y = 0; y < numRows; y++ )
		{
			for ( int x = 0; x < numCols; x++ )
			{
				final Integer digit = grid.get(x, y);

				if ( digit == 0 )
				{
					emptyCells.add( new Cell( x, y, 0 ) );
				}
			}
		}

		return emptyCells;
	}


	@Override
	public String toString()
	{
		final int numRows = grid.rowKeySet().size();
		final int numCols = grid.columnKeySet().size();

		final StringBuilder sb = new StringBuilder();

		for ( int y = 0; y < numRows; y++ )
		{
			if ( y != 0 && y % 3 == 0)
			{
				sb.append( "------+------+------\n" );
			}

			for ( int x = 0; x < numCols; x++ )
			{
				if ( x != 0 && x % 3 == 0)
				{
					sb.append( "|" );
				}

				final Integer digit = grid.get(x, y);

				if ( digit == 0 )
				{
					sb.append( ". " );
				}
				else
				{
					sb.append( digit + " " );
				}
			}

			sb.append( '\n' );
		}

		return sb.toString();
	}


	public Table<Integer, Integer, Integer> getGrid()
	{
		return grid;
	}

	public Table<Integer, Integer, Integer> getSolution()
	{
		return solution;
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
		final TableGrid other = (TableGrid) obj;
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
