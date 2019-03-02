/*
 * Subject: COMP90015
 * Name: Leewei Kuo
 * Student ID: 932975
 * Tutor: Lakshmi Jagathamma Mohan
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;


public class DictGUI implements ActionListener {
	private final int QUERY = 1;
	private final int DELETE = 2;
	private final int UPDATE = 3;
	private final int ADD = 4;
	private final int EXIT = 5;
	
	//need to be consistent with server side
	private final int TEXT_AREA_ROW = 10;
	
	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private PrintWriter writer;
	private BufferedReader reader;
	private Socket client;

	

	/**
	 * Create the application.
	 */
	public DictGUI(BufferedReader reader, PrintWriter writer, Socket client) {
		initialize();
		this.reader = reader;
		this.writer = writer;
		this.client = client;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 951, 558);		
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				writer.println(EXIT);
			    writer.println("");
			    writer.close();
			    try {			    	
					reader.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    System.exit(0);
			}
		});
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 23));
		textField.setBounds(53, 29, 191, 45);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lookUp();	
			}			
		});	
		textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if(textField.getText().trim().equals("Enter Word")){
		            textField.setText("");
		            textField.setForeground(Color.BLACK);
		        }
			}
			public void focusLost(FocusEvent arg0) {
				if(textField.getText().trim().equals("")){
		            textField.setText("Enter Word");
		            textField.setForeground(Color.GRAY);
		        }				
			}
			
		});
		
		JButton lookUpButton = new JButton("");
		lookUpButton.setToolTipText("Search");
		lookUpButton.setActionCommand("Look up");
		lookUpButton.setBackground(new Color(240,240,240));
		Image img1 = new ImageIcon(this.getClass().getResource("/search.png")).getImage();
		lookUpButton.setIcon(new ImageIcon(img1));
		lookUpButton.setBounds(100, 417, 97, 66);
		frame.getContentPane().add(lookUpButton);
		lookUpButton.addActionListener(this);
		
		JButton deleteButton = new JButton("");
		deleteButton.setToolTipText("Delete word");
		deleteButton.setActionCommand("Delete");
		deleteButton.setBackground(new Color(240,240,240));
		Image img2 = new ImageIcon(this.getClass().getResource("/minus.png")).getImage();
		deleteButton.setIcon(new ImageIcon(img2));
		deleteButton.setBounds(500, 417, 97, 66);
		frame.getContentPane().add(deleteButton);
		deleteButton.addActionListener(this);
		
		JButton updateButton = new JButton("");
		updateButton.setToolTipText("Update a word");
		updateButton.setActionCommand("Update");
		updateButton.setBackground(new Color(240,240,240));
		Image img3 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		updateButton.setIcon(new ImageIcon(img3));
		updateButton.setBounds(700, 417, 97, 66);
		frame.getContentPane().add(updateButton);
		updateButton.addActionListener(this);
		
		JButton addButton = new JButton("");
		addButton.setToolTipText("Add new word");
		addButton.setActionCommand("Add");
		addButton.setBackground(new Color(240,240,240));
		Image img4 = new ImageIcon(this.getClass().getResource("/plus.png")).getImage();
		addButton.setIcon(new ImageIcon(img4));
		addButton.addActionListener(this);
		addButton.setBounds(300, 417, 97, 66);
		frame.getContentPane().add(addButton);
		
		JButton clearButton = new JButton("");
		clearButton.setToolTipText("CLEAR ALL");
		clearButton.setActionCommand("Clear");
		clearButton.setBackground(new Color(240,240,240));
		Image img5 = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		clearButton.setIcon(new ImageIcon(img5));
		clearButton.setBounds(256, 29, 48, 50);
		frame.getContentPane().add(clearButton);
		clearButton.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 87, 809, 315);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setRows(TEXT_AREA_ROW);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 23));
		textArea.setLineWrap(true);
		textArea.setForeground(Color.BLACK);
		
		JLabel lblDailyQuotes = new JLabel("Daily Quotes:");
		lblDailyQuotes.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDailyQuotes.setBounds(395, 29, 123, 35);
		frame.getContentPane().add(lblDailyQuotes);
		
		JLabel lblGoodGoodStudy = new JLabel("Good good study, day day up!");
		lblGoodGoodStudy.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblGoodGoodStudy.setBounds(517, 29, 308, 35);
		frame.getContentPane().add(lblGoodGoodStudy);		
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Look up")) {
			lookUp();		
		} else if(action.equals("Delete")) {
			delete();			
		} else if(action.equals("Update")) {
			update();		
		} else if(action.equals("Clear")) {
			textArea.setText("");
			textField.setText("Enter Word");
			textField.setForeground(Color.GRAY);
		} else if(action.equals("Add")) {
			add();
		}
	}
	
	private void lookUp() {
		try {
			String word = textField.getText().trim().toLowerCase();
			if(word.equals("") || word.equals("enter word")) {
				throw new NoWordException();
			}
			textArea.setText("");
			
			writer.println(QUERY);
			writer.println(word);	//report //getText return "" when no value
			for (int i = 0; i < TEXT_AREA_ROW; i++) {
				String line = reader.readLine();
				textArea.append(line + "\n");				
			}				
		} catch (IOException e) {				
			e.printStackTrace();
		} catch (NoWordException e) {			
			textArea.setText(e.getMessage() + "\n");
		}
	}
	
	private void delete() {
		int response = JOptionPane.showConfirmDialog(frame, "Do you want to delete?", "Delete",
		        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				String word = textField.getText().trim().toLowerCase();
				if(word.equals("") || word.equals("enter word")) {
					throw new NoWordException();
				}
				writer.println(DELETE);
				writer.println(word);	//report				
				String line = reader.readLine();
				textArea.setText(line);	
			} catch (IOException e1) {				
				e1.printStackTrace();
			} catch (NoWordException e) {
				textArea.setText(e.getMessage() + "\n");
			}	
		}
	}
	
	private void update() {
		int response = JOptionPane.showConfirmDialog(frame, "Do you want to continue update?", "Update",
		        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				String word = textField.getText().trim().toLowerCase();
				if(word.equals("") || word.equals("enter word")) {
					throw new NoWordException();
				}
				String[] temp = textArea.getText().trim().split("\\n");	//report
				if (temp.length > TEXT_AREA_ROW)
					throw new TooManyRowsException(TEXT_AREA_ROW);
				if(temp[0].equals(""))
					throw new NoDefinitionException();
				
				String[] lines = new String[TEXT_AREA_ROW];
				writer.println(UPDATE);
				writer.println(word);	//report

				for (int i = 0; i < TEXT_AREA_ROW; i++) {
					if (i < temp.length) {
						lines[i] = temp[i];
					} else {
						lines[i] = " ";
					}
				}
				for (int i = 0; i < TEXT_AREA_ROW; i++) {
					writer.println(lines[i]);
				}
				String line = reader.readLine();
				textArea.setText(line);	
			} catch (IOException e1) {				
				e1.printStackTrace();
			} catch (TooManyRowsException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} catch (NoDefinitionException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} catch (NoWordException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} 
		} 
	}	
	private void add() {
		int response = JOptionPane.showConfirmDialog(frame, "Do you want to add?", "Update",
		        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				String word = textField.getText().trim().toLowerCase();
				if(word.equals("") || word.equals("enter word")) {
					throw new NoWordException();
				}
				String[] temp = textArea.getText().trim().split("\\n");	//report
				if (temp.length > TEXT_AREA_ROW)
					throw new TooManyRowsException(TEXT_AREA_ROW);
				if(temp[0].equals(""))
					throw new NoDefinitionException();
				String[] lines = new String[TEXT_AREA_ROW];
				writer.println(ADD);
				writer.println(word);	//report

				for (int i = 0; i < TEXT_AREA_ROW; i++) {
					if (i < temp.length) {
						lines[i] = temp[i];
					} else {
						lines[i] = " ";
					}
				}
				for (int i = 0; i < TEXT_AREA_ROW; i++) {
					writer.println(lines[i]);
				}
				String line = reader.readLine();
				textArea.setText(line);	
			} catch (IOException e1) {				
				e1.printStackTrace();
			} catch (TooManyRowsException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} catch (NoDefinitionException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} catch (NoWordException e) {
				String message = e.getMessage();
				textArea.setText(message);
			} 
		}		
	}
}
