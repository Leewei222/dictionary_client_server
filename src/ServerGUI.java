/**
 * this class is only used for generating DictServer class' gui
 */
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;

public class ServerGUI implements ActionListener {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public ServerGUI(ServerSocket serverSocket, Dictionary dictionary) {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 548, 607);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton close = new JButton("Close Server");
		close.setBounds(200, 527, 116, 25);
		frame.getContentPane().add(close);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 506, 509);
		frame.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 23));
		scrollPane.setViewportView(textArea);
	}
	
	public JFrame getJFrame() {
		return frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}
