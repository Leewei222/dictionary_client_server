/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */
public class PortNumberNotInRange extends Exception {
	public PortNumberNotInRange() {
		super("Port number not in range(0 < port <= 65535)");
	}
}
