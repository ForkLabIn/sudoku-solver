package com.richardarcega.sudoku.puzzle;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a single cell in the sudoku grid.
 *
 * Used to keep track of guesses and potential candidates
 * to the solution.
 */
public class Cell
{
	private int				x;
	private int				y;
	private int             digit;

	/**
	 * A list of possible candidate solutions to this cell.
	 */
	private List<Integer>	candidates;

	/**
	 * Used to keep track of the current guess.
	 */
	private ListIterator<Integer> iterator;

	public Cell(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public Cell(final int x, final int y, final int digit)
	{
		this.x = x;
		this.y = y;
		this.digit = digit;
	}

	public Cell(final int x, final int y, final int digit, final List<Integer> candidates)
	{
		this.x = x;
		this.y = y;
		this.digit = digit;
		this.candidates = candidates;
		iterator = candidates.listIterator();
	}

	public int getX()
	{
		return x;
	}

	public void setX(final int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(final int y)
	{
		this.y = y;
	}

	public int getDigit()
	{
		return digit;
	}

	public void setDigit(final int digit)
	{
		this.digit = digit;
	}

	public List<Integer> getCandidates()
	{
		return candidates;
	}

	public void setCandidates(final List<Integer> candidates)
	{
		this.candidates = candidates;
		iterator = candidates.listIterator();
	}

	public ListIterator<Integer> getIterator()
	{
		return iterator;
	}

	public void setIterator(final ListIterator<Integer> iterator)
	{
		this.iterator = iterator;
	}

	/**
	 * Sorts Cells in ascending order according to the number of candidates.
	 */
	public static Comparator<Cell>	NumCandidatesComparator	= new Comparator<Cell>()
	{
		@Override
		public int compare(final Cell cell1, final Cell cell2)
		{
			return cell1.getCandidates().size() - cell2.getCandidates().size();
		}
	};

	@Override
	public String toString()
	{
		return "Cell [x=" + x + ", y=" + y + ", digit=" + digit + ", candidates=" + candidates + ", iterator=" + iterator + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( candidates == null ) ? 0 : candidates.hashCode() );
		result = prime * result + digit;
		result = prime * result + ( ( iterator == null ) ? 0 : iterator.hashCode() );
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( getClass() != obj.getClass() )
		{
			return false;
		}
		final Cell other = (Cell) obj;
		if ( candidates == null )
		{
			if ( other.candidates != null )
			{
				return false;
			}
		}
		else if ( !candidates.equals( other.candidates ) )
		{
			return false;
		}
		if ( digit != other.digit )
		{
			return false;
		}
		if ( iterator == null )
		{
			if ( other.iterator != null )
			{
				return false;
			}
		}
		else if ( !iterator.equals( other.iterator ) )
		{
			return false;
		}
		if ( x != other.x )
		{
			return false;
		}
		if ( y != other.y )
		{
			return false;
		}
		return true;
	}


}
