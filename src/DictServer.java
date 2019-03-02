/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DictServer implements ActionListener {
	private final static boolean RUNNING = true;
	private static Dictionary dictionary;//
	private int port = 2018;
	private ServerSocket serverSocket;//
	private JFrame frame;
	private JTextArea textArea;
	private int numOfClient = 0;
	
	public DictServer() {
		initialize();
	}
	
	public static void main(String[] args) {
		
		DictServer server = new DictServer();
		server.frame.setVisible(true);
		
		server.setPort(args);
		server.setDictionary(args);	//initialize dictionary
		
		try {
			server.serverSocket = new ServerSocket(server.port);//
			server.printInitialStats();			
			server.connect(server.serverSocket, dictionary);		
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	private void printInitialStats() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());
		System.out.println("Port = " + port);	
		textArea.append("Current IP address : " + ip.getHostAddress() + "\n");
		textArea.append("Port = " + port + "\n");
		
	}

	private void setPort(String[] args) {
		try {
			port = Integer.parseInt(args[0]);
			if (port > 65535 || port <= 0)
				throw new PortNumberNotInRange();
			System.out.println("Using port: " + port);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Didn't enter port number!");
			System.out.println("Using default port: 2018");
			textArea.append("Didn't enter port number!\n");
			textArea.append("Using default port: 2018\n");
		} catch (NumberFormatException e) {
			System.out.println("Wrong port number format!");
			System.out.println("Using default port: 2018");
			textArea.append("Wrong port number format!\n");
			textArea.append("Using default port: 2018\n");
		} catch (PortNumberNotInRange e) {
			System.out.println(e.getMessage());
			port = 2018;
			System.out.println("Using default port: 2018");
			textArea.append("Port number not in range!\n");
			textArea.append("Using default port: 2018\n");
		}
		
	}
	private void setDictionary(String[] args) {
		String path = null;
		try {
			path = args[1];
			File dictFile = new File(path);
			if (!dictFile.exists())
				throw new FileNotFoundException();
			dictionary = new Dictionary(path, this);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Didn't enter file path!");
			textArea.append("Didn't enter file path!\n");
			useDefaultDict();
		} catch (FileNotFoundException e) {
			System.out.println("File not found in path: " + path);
			textArea.append("File not found in path: " + path + "\n");
			useDefaultDict();
		}		
	}
	private void useDefaultDict() {
		System.out.println("Use default dictionary");
		textArea.append("Use default dictionary\n");
		dictionary = new Dictionary("dictionary.dat", this);
	}

	private void connect(ServerSocket serverSocket, Dictionary dictionary) {
		while (RUNNING) {
			System.out.println("Listening");
			Socket client;
			try {
				client = serverSocket.accept();
				numOfClient++;
				textArea.append("A client has connected.\n");
				DictThread dictThread = new DictThread(dictionary, client, this);
				new Thread(dictThread).start();	
			} catch (IOException e) {
				//e.printStackTrace();
				break;	//when serverSocket is closed break from while loop
			}
			textArea.append("Number of clients: " + numOfClient + "\n");
			System.out.println("connecting");
		
		}
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 548, 607);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton close = new JButton("Close Server");
		close.setBounds(200, 527, 116, 25);
		frame.getContentPane().add(close);
		close.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 506, 509);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		scrollPane.setViewportView(textArea);
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	public JTextArea getJTextArea() {
		return textArea;
	}
	
	public void clientDisconnect() {
		textArea.append("A client has disconnected.\n");
		numOfClient--;
		textArea.append("Number of clients: " + numOfClient + "\n");	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			serverSocket.close();
			
			//close server until all clients are disconnected
			if (numOfClient == 0) {
				ObjectOutputStream oos = 
						new ObjectOutputStream
						(new FileOutputStream("dictionary.dat"));
				oos.writeObject(dictionary.getDict());
				oos.close();			
				System.exit(0);
			} else {
				textArea.append("Server socket closed\n");
				textArea.append("Waiting for all clients to disconnect\n");
				textArea.append(numOfClient + " clients still connected\n");
				textArea.append("Try again later\n");
			}			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
}
