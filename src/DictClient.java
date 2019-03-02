/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DictClient {
	private static final int DEFAULT_PORT = 2018;
	private static final String DEFAULT_HOST = "localhost";
	private static BufferedReader reader;
	private static PrintWriter writer;
	private static Socket client;
	
	public static void main(String[] args) {		
		connect(args);		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictGUI window = new DictGUI(reader, writer,client);
					window.getJFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
				
	}

	private static void connect(String[] args) {
		
		try {
			String serverAddress = args[0];
			int port = Integer.parseInt(args[1]);
			client = new Socket(serverAddress, port);
			System.out.println("connecting.....");
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream(), true);
		} catch(ArrayIndexOutOfBoundsException e) {
			//System.out.println("ArrayIndexOutOfBounds");
			defaultConnect();
		} catch(NumberFormatException e) {
			//System.out.println("NumberFormat");
			defaultConnect();
		} catch (UnknownHostException e) {
			//System.out.println("UnknownHost");
			defaultConnect();		
		} catch (IOException e) {
			//System.out.println("IOException");
			defaultConnect();
		}
		
	}
	
	private static void defaultConnect() {
		System.out.println("Wrong input address/port\nUsing default settings");
		System.out.println("start");
		try {
			client = new Socket(DEFAULT_HOST, DEFAULT_PORT);
			System.out.println("connecting.....");
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.getStackTrace();
			System.out.println("Connection failed, check server address and port");
			System.exit(0);
		}

	}
	
}
