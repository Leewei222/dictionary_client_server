/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

/**
 * This exception is thrown when a user types too many rows of definition
 */
public class TooManyRowsException extends Exception {
	public TooManyRowsException(int maxRows) {
		super("Definition no more than " + maxRows + " rows!");
	}
}
