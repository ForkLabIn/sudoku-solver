package com.richardarcega.sudoku.puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.collect.ImmutableSet;

/**
 * This abstract class defines the grid representation of a sudoku puzzle.
 */
public abstract class Grid
{
	/**
	 * The possible set of digits within a cell.
	 */
	public static final ImmutableSet<Integer> DIGITS = ImmutableSet.of( 1, 2, 3, 4, 5, 6, 7, 8, 9 );

	/**
	 * Returns the digit contained within the specified cell.
	 *
	 * @param x		the x coordinate of the cell
	 * @param y		the y coordinate of the cell
	 * @return		the digit
	 */
	public abstract int getCellValue(int x, int y);

	/**
	 * Populates the grid with the known digits.
	 *
	 * @param puzzleFile	the puzzle file
	 * @throws IOException
	 */
	public abstract void load(InputStream puzzleFile) throws IOException;

	/**
	 * Returns the cells which do not contain any digits.
	 *
	 * @return	a list of cells
	 */
	public abstract List<Cell> getEmptyCells();

}
