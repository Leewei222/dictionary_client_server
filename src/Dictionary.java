/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;


public class Dictionary {
	
	private Map<String, String[]> dictionary = new HashMap<String, String[]>();
	private DictServer server;
	
	@SuppressWarnings("unchecked")
	public Dictionary(String path, DictServer server) {
		this.server = server;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			dictionary = (HashMap<String, String[]>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Wrong dictionary file!");
			server.getJTextArea().append("Wrong dictionary file!\n");
			useEmptyDict();		
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			server.getJTextArea().append("File not Found!\n");
			useEmptyDict();
		} catch (IOException e) {
			System.out.println("Wrong path!");
			server.getJTextArea().append("Wrong path!\n");
			useEmptyDict();
		}		
	}
	
	private void useEmptyDict() {
		System.out.println("Use empty dictionary");
		server.getJTextArea().append("Use empty dictionary!\n");
		dictionary = new HashMap<String, String[]>();
	}
	
	public synchronized boolean containsWord(String word) {
		return dictionary.containsKey(word);
	}
	public synchronized String[] getDefinition(String word) {
		return dictionary.get(word);
	}
	
	public synchronized void update(String word, String[] definition) {
		dictionary.put(word, definition);
	}
	
	public synchronized void removeWord(String word) {
		dictionary.remove(word);
	}
	public Map<String, String[]> getDict(){
		return dictionary;
	}
	
}
