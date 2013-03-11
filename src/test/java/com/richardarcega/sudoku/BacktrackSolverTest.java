package com.richardarcega.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.richardarcega.sudoku.puzzle.Cell;
import com.richardarcega.sudoku.puzzle.Puzzle;
import com.richardarcega.sudoku.puzzle.TableGrid;
import com.richardarcega.sudoku.solver.Algorithm;
import com.richardarcega.sudoku.solver.SolverFactory;
import com.richardarcega.sudoku.solver.SudokuSolver;
import com.richardarcega.sudoku.solver.UnsolvableException;

/**
 * Unit tests for testing the backtracking algorithm
 */
public class BacktrackSolverTest
{
	private static Puzzle		puzzle;
	private static SudokuSolver	solver;

	@BeforeClass
	public static void setUp() throws IOException
	{
		solver = SolverFactory.getSolver( Algorithm.BACKTRACK );
	}

	@AfterClass
	public static void tearDown() {}

	public static void loadPuzzle(final String puzzleFile)
	{
		puzzle = new Puzzle( new TableGrid( 9, 9 ) );

		final String puzzleFilePath = ( new File( "src/test/resources/" + puzzleFile ).exists() ) ? "src/test/resources/" + puzzleFile:
			                                                                                        "src/main/resources/" + puzzleFile;
		try
		{
			puzzle.load( new FileInputStream( puzzleFilePath ) );
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testCellValue()
	{
		loadPuzzle( "easy.txt" );
		assertTrue( puzzle.at( 0, 0 ) == 0 );
		assertEquals( puzzle.at( 1, 0 ), 4 );
	}

	@Test
	public void testEasyPuzzleSolve()  throws UnsolvableException
	{
		loadPuzzle( "easy.txt" );
		solver.solve( puzzle );

		final ArrayTable<Integer, Integer, Integer> grid = (ArrayTable<Integer, Integer, Integer>) ( (TableGrid) puzzle.getGrid() ).getGrid();

		final int[][] solution = {
				{2, 4, 5, 8, 9, 3, 7, 1, 6},
				{8, 1, 3, 5, 7, 6, 9, 2, 4},
				{7, 6, 9, 2, 1, 4, 5, 3, 8},
				{5, 3, 6, 9, 8, 7, 1, 4, 2},
				{4, 9, 2, 1, 6, 5, 8, 7, 3},
				{1, 7, 8, 4, 3, 2, 6, 5, 9},
				{6, 8, 4, 7, 2, 1, 3, 9, 5},
				{3, 2, 1, 6, 5, 9, 4, 8, 7},
				{9, 5, 7, 3, 4, 8, 2, 6, 1}
		};

		for ( int y = 0; y < 9; y++ )
		{
			for ( int x = 0; x < 9; x++ )
			{
				assertTrue( (double) grid.get(x,y).intValue() == (double) solution[y][x] );
			}
		}
	}

	@Test
	public void testMediumPuzzleSolve()  throws UnsolvableException
	{
		loadPuzzle( "medium.txt" );
		solver.solve( puzzle );

		final ArrayTable<Integer, Integer, Integer> grid = (ArrayTable<Integer, Integer, Integer>) ( (TableGrid) puzzle.getGrid() ).getGrid();

		final int[][] solution = {
				{1, 4, 9, 8, 7, 5, 6, 2, 3},
				{7, 3, 2, 4, 6, 1, 9, 5, 8},
				{6, 8, 5, 3, 2, 9, 4, 1, 7},
				{9, 2, 3, 7, 1, 8, 5, 4, 6},
				{5, 7, 8, 2, 4, 6, 1, 3, 9},
				{4, 1, 6, 9, 5, 3, 8, 7, 2},
				{8, 9, 4, 1, 3, 2, 7, 6, 5},
				{3, 6, 7, 5, 9, 4, 2, 8, 1},
				{2, 5, 1, 6, 8, 7, 3, 9, 4}
		};

		for ( int y = 0; y < 9; y++ )
		{
			for ( int x = 0; x < 9; x++ )
			{
				assertTrue( (double) grid.get(x,y).intValue() == (double) solution[y][x] );
			}
		}
	}

	@Ignore
	@Test
	public void testHardPuzzleSolve() throws UnsolvableException
	{
		loadPuzzle( "hard.txt" );
		solver.solve( puzzle );

		final ArrayTable<Integer, Integer, Integer> grid = (ArrayTable<Integer, Integer, Integer>) ( (TableGrid) puzzle.getGrid() ).getGrid();

		final int[][] solution = {
				{4, 1, 7, 3, 6, 9, 8, 2, 5},
				{6, 3, 2, 1, 5, 8, 9, 4, 7},
				{9, 5, 8, 7, 2, 4, 3, 1, 6},
				{8, 2, 5, 4, 3, 7, 1, 6, 9},
				{7, 9, 1, 5, 8, 6, 4, 3, 2},
				{3, 4, 6, 9, 1, 2, 7, 5, 8},
				{2, 8, 9, 6, 4, 3, 5, 7, 1},
				{5, 7, 3, 2, 9, 1, 6, 8, 4},
				{1, 6, 4, 8, 7, 5, 2, 9, 3}
		};

		for ( int y = 0; y < 9; y++ )
		{
			for ( int x = 0; x < 9; x++ )
			{
				assertTrue( (double) grid.get(x,y).intValue() == (double) solution[y][x] );
			}
		}
	}

	@Test(expected=UnsolvableException.class)
	public void testUnsolvablePuzzle()  throws UnsolvableException
	{
		loadPuzzle( "unsolvable.txt" );
		solver.solve( puzzle );
	}

	@Test
	public void testSortCandidates()
	{
		final List<Cell> list = Lists.newArrayList();

		final Cell c3 = new Cell( 0, 0, 0, Ints.asList(new int[] {1, 2, 3}) );
		final Cell c1 = new Cell( 1, 1, 1, Ints.asList(new int[] {1,}) );
		final Cell c2 = new Cell( 1, 1, 1, Ints.asList(new int[] {1, 2}) );

		list.add( c3 );
		list.add( c1 );
		list.add( c2 );

		Collections.sort( list, Cell.NumCandidatesComparator );

		assertTrue( list.get(0).equals( c1 ) && list.get(1).equals( c2 ) && list.get(2).equals( c3 ));
	}
}
