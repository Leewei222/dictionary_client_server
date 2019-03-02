/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

/**
 * This exception is thrown when a user didn't enter definition
 */
public class NoDefinitionException extends Exception {
	public NoDefinitionException() {
		super("Please enter definition!");
	}
}
