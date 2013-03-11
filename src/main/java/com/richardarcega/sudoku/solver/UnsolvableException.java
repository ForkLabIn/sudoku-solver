package com.richardarcega.sudoku.solver;

/**
 * Signals that a puzzle is unsolvable or there is no valid solution.
 */
public class UnsolvableException extends Exception
{
	private static final long	serialVersionUID	= 986479669675021740L;

	public UnsolvableException(final String message)
	{
		super(message);
	}

	public UnsolvableException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}
}
