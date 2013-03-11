package com.richardarcega.sudoku.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.richardarcega.sudoku.puzzle.Puzzle;
import com.richardarcega.sudoku.puzzle.TableGrid;
import com.richardarcega.sudoku.solver.Algorithm;
import com.richardarcega.sudoku.solver.SolverFactory;
import com.richardarcega.sudoku.solver.SudokuSolver;
import com.richardarcega.sudoku.solver.UnsolvableException;

/**
 * Runs the application to solve Sudoku puzzles.
 */
public class AppRunner
{
	private static Logger	log	= LoggerFactory.getLogger( AppRunner.class );

	public static void main(final String[] args) throws IOException
	{
		log.info( "Started Sudoku Puzzle Solver" );

		play();
	}

	public static void play()
	{
		final Puzzle puzzle       = new Puzzle( new TableGrid(9, 9) );
		final SudokuSolver solver = SolverFactory.getSolver( Algorithm.BACKTRACK );

		for ( final String puzzleFile : new String[]{ "expert.txt" } )
		{
			try
			{
				puzzle.load( AppRunner.class.getClassLoader().getResourceAsStream( puzzleFile ) );
			}
			catch ( final IOException e )
			{
				log.error( "Could not load puzzle file", e );
			}

			System.out.println( "PUZZLE:\n" + puzzle );

			try
			{
				final Stopwatch stopwatch = new Stopwatch().start();

				// Solve the puzzle!
				solver.solve( puzzle );

				stopwatch.stop();
				System.out.println( "SOLUTION:\n" + puzzle + "(" + stopwatch.toString() + ")\n");
			}
			catch (final UnsolvableException e)
			{
				log.warn( "Failed to solve puzzle", e );
			}
		}
	}

}
