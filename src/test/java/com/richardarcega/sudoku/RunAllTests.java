package com.richardarcega.sudoku;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BacktrackSolverTest.class })
public class RunAllTests
{
	public static void main(final String[] args)
	{
		// Run all JUnit tests programmatically
		final Result result = JUnitCore.runClasses( BacktrackSolverTest.class );
		for ( final Failure failure : result.getFailures() )
		{
			System.out.println( failure.toString() );
		}
	}

}