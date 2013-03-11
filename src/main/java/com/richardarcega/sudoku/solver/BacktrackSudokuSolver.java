package com.richardarcega.sudoku.solver;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.richardarcega.sudoku.puzzle.Cell;
import com.richardarcega.sudoku.puzzle.Grid;
import com.richardarcega.sudoku.puzzle.Puzzle;
import com.richardarcega.sudoku.puzzle.TableGrid;

/**
 * Solves a sudoku puzzle using a backtracking algorithm.
 *
 * Using brute-force methods, an exhaustive search is made
 * to find a solution. Candidates are incrementally built
 * and are abandoned as soon they prove to be a dead end--
 * when they cannot possibly lead to a valid solution.
 * Once this occurs, the algorithm backtracks to an earlier
 * state to try a different guess.
 */
public class BacktrackSudokuSolver extends SudokuSolver
{
	/**
	 * The sudoku puzzle to be solved.
	 */
	private Puzzle puzzle;

	private static final Logger log = LoggerFactory.getLogger( BacktrackSudokuSolver.class );

	@Override
	public void solve(final Puzzle puzzle) throws UnsolvableException
	{
		this.puzzle = puzzle;

		for ( final ListIterator<Cell> it = getEmptyCells().listIterator(); it.hasNext(); )
		{
			final Cell cell = it.next();

			if ( nextValidGuess(cell) )
			{
				// assign the guess to the empty cell
				getGrid().put( cell.getX(), cell.getY(), cell.getIterator().next() );
			}
			else
			{
				// cannot possibly lead to a viable solution
				// empty the current cell
				getGrid().put( cell.getX(), cell.getY(), 0 );

				// reset the iterator to the first candidate
				cell.setIterator( cell.getCandidates().listIterator() );

				// move the cursor position to before the current cell
				it.previous();

				if ( it.hasPrevious() )
				{
					// "backtrack" to the previous cell
					// move the cursor position to before the previous cell
					it.previous();
				}
				else
				{
					log.info( "The puzzle is unsolvable!" );
					throw new UnsolvableException( "The puzzle could not be solved using " + this.getClass().getSimpleName() );
				}
			}
		}
	}

	/**
	 * Returns the empty cells in the grid.
	 *
	 * Removes cells which only have a single candidate
	 * and sorts the returned list in ascending order of
	 * the number of candidates held by each cell.
	 *
	 * @return a list of empty cells
	 */
	public List<Cell> getEmptyCells()
	{
		final List<Cell> emptyCells = puzzle.getGrid().getEmptyCells();

		// obtain the list of possible candidate digits for each empty cell
		for ( ListIterator<Cell> it = emptyCells.listIterator(); it.hasNext(); )
		{
			final Cell cell = it.next();
			final List<Integer> candidates = getCandidates( cell.getX(), cell.getY() );

			// eliminate single candidates
			if ( candidates.size() == 1 )
			{
				getGrid().set( cell.getX(), cell.getY(), candidates.get( 0 ) );
				it.remove();
				it = emptyCells.listIterator(); // reset to the first element
			}

			cell.setCandidates( candidates );
		}

		// Sorts the empty cells according to the number of candidates.
		// The cells with the least amount candidates are chosen first so as to
		// improve the likelihood of being the correct guess.
		Collections.sort( emptyCells, Cell.NumCandidatesComparator );

		return emptyCells;
	}

	/**
	 * Returns the potential candidates for a given cell.
	 *
	 * @param cellX 	the cell's row
	 * @param cellY 	the cell's column
	 *
	 * @return	a collection of the potential candidates
	 */
	public List<Integer> getCandidates(final int cellX, final int cellY)
	{
		final Set<Integer> givens = Sets.newHashSet();

		// row
		for ( int x = 0; x < getGrid().columnKeySet().size(); x++ )
		{
			givens.add( getGrid().get( x, cellY) );
		}

		// column
		for ( int y = 0; y < getGrid().rowKeySet().size(); y++ )
		{
			givens.add( getGrid().get( cellX, y ) );
		}


		final int offset = (int) Math.pow( getGrid().size(), 1.0 / 4.0 );

		// coordinates of the top left cell of the containing block
		final int topLeftX = cellX / (offset) * (offset);
		final int topLeftY = cellY / (offset) * (offset);

		// block
		for ( int y = topLeftY; y < topLeftY + 3; y++ )
		{
			for ( int x = topLeftX; x < topLeftX + 3; x++)
			{
				givens.add( getGrid().get( x, y ) );
			}
		}

		return Lists.newArrayList( ( Sets.difference( Grid.DIGITS, givens ).copyInto( new HashSet<Integer>() ) ) );
	}

	/**
	 * Determines whether a valid guess exists among the available
	 * candidates. If one is found, true is returned and the candidate
	 * list's cursor is moved to corresponding element for retrieval.
	 *
	 * @param cell		the empty cell to be solved
	 *
	 * @return			true if a valid guess was found, false otherwise.
	 */
	public boolean nextValidGuess(final Cell cell)
	{
		if ( cell.getCandidates() != null && cell.getCandidates().size() > 0 )
		{
			for ( final ListIterator<Integer> it = cell.getIterator();  it.hasNext(); )
			{
				final Integer candidateGuess = it.next();

				if ( isValidGuess( cell.getX(), cell.getY(), candidateGuess ) )
				{
					it.previous();
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * Returns true if the guess is valid, false otherwise.
	 *
	 * @param cellX	the candidate cell's x coordinate
	 * @param cellY	the candidate cell's y coordinate
	 * @param guess	the current candidate guess
	 *
	 * @return	true if the guess does not violate the rules, false otherwise.
	 */
	public boolean isValidGuess(final int cellX, final int cellY, final int guess)
	{
		// row
		for ( int x = 0; x < getGrid().columnKeySet().size(); x++ )
		{
			if ( getGrid().get(x, cellY) == guess)
			{
				return false;
			}
		}

		// column
		for ( int y = 0; y < getGrid().rowKeySet().size(); y++ )
		{
			if ( getGrid().get(cellX, y) == guess)
			{
				return false;
			}
		}

		final int offset = (int) Math.pow( getGrid().size(), 1.0 / 4.0 );

		// coordinates of the top left cell of the containing block
		final int topLeftX = cellX / offset * offset;
		final int topLeftY = cellY / offset * offset;

		// block
		for ( int y = topLeftY; y < topLeftY + 3; y++ )
		{
			for ( int x = topLeftX; x < topLeftX + 3; x++ )
			{
				if ( getGrid().get(x, y) == guess)
				{
					return false;
				}
			}
		}

		return true;
	}

	public ArrayTable<Integer, Integer, Integer> getGrid()
	{
		return (ArrayTable<Integer, Integer, Integer>) ( (TableGrid) puzzle.getGrid() ).getGrid();
	}
}
