/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DictThread implements Runnable {
	private final int QUERY = 1;
	private final int DELETE = 2;
	private final int UPDATE = 3;
	private final int ADD = 4;
	private final int EXIT = 5;
	
	//need to be consistent with client side
	private final int TEXT_AREA_ROW = 10;
	
	private Dictionary dictionary;
	private InputStreamReader is;
	PrintWriter writer = null;
	BufferedReader reader = null;
	private Socket client;
	DictServer server;
	private final boolean RUNNING = true;
	
	
	public DictThread(Dictionary dictionary, Socket client, DictServer server) {
		this.dictionary = dictionary;
		this.client = client;
		this.server = server;
		try {
			writer = new PrintWriter(client.getOutputStream(), true);
			is = new InputStreamReader(client.getInputStream());
			reader = new BufferedReader(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		while (RUNNING) {				
			
			int act = 0;
			String command = null;
			String word = null;
			try {
				command = reader.readLine();
				word = reader.readLine();
			} catch (IOException e) {				
				e.printStackTrace();
			} 		
			act = Integer.parseInt(command);				
			String[] lines;
			switch (act) {
			case QUERY:
				if (dictionary.containsWord(word)) {			
					for (int i = 0; i < TEXT_AREA_ROW; i++) {
						writer.println(dictionary.getDefinition(word)[i]);
					}
					System.out.println("Client look up for " + word);	//change
					server.getJTextArea().append("A client looked up for " + word + "\n");
				} else {
					
					//do this TEXT_AREA_ROW times to prevent strange bug
					for (int i = 0; i < TEXT_AREA_ROW; i++) {
						if (i == 0) {
							writer.println("Word not in dictionary!");
						} else {
							writer.println(" ");
						}
					}				
				}			
				
				break;
				
			case DELETE:
				if (dictionary.containsWord(word)) {
					dictionary.removeWord(word);
					writer.println("Delete " + word + " successfully!");
					System.out.println("Client deleted " + word);	//change
					server.getJTextArea().append("A client deleted " + word + "\n");
				} else {
					writer.println("No such word!");
				}			
				
				break;
	
			
			case UPDATE:
				
				lines = new String[TEXT_AREA_ROW];

				try {
					for (int i = 0; i < TEXT_AREA_ROW; i++) {
						lines[i] = reader.readLine();
					}
					//definition = reader.readLine();	//problem here!! need to read many line
					//System.out.println(definition);					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (dictionary.containsWord(word)) {
					writer.println("Updating the definition of " + word);
				} else {
					writer.println("Adding new word: " + word);
				}
				
				dictionary.update(word, lines);
				System.out.println("Client updated " + word);	//change
				server.getJTextArea().append("A client updated " + word + "\n");
									
				break;	
				
			case ADD:				
				lines = new String[TEXT_AREA_ROW];
				try {
					for (int i = 0; i < TEXT_AREA_ROW; i++) {
						lines[i] = reader.readLine();
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				if (dictionary.containsWord(word)) {
					writer.println(word + " already in dictionary!");
				} else {
					writer.println("Adding new word: " + word);
					dictionary.update(word, lines);
					System.out.println("Client added " + word);	//change
					server.getJTextArea().append("A client added " + word + "\n");
				}
										
				break;
			
			case EXIT:
				server.clientDisconnect();
				writer.close();
				try {
					reader.close();
					is.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
				Thread.currentThread().interrupt();
				return;
			}		
		}

	}
}