/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

/**
 * This exception is thrown when a user didn't enter word
 */
public class NoWordException extends Exception {
	public NoWordException() {
		super("Please enter word!");
	}
}
