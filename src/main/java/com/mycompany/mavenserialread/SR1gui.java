package com.mycompany.mavenserialread;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.ComponentOrientation;
//import java.awt.event.WindowFocusListener;
//import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;



public class SR1gui {

	private JFrame Frame;
	static JPanel Panel;
	static DefaultComboBoxModel<String> PortBoxModel;
	static JComboBox<String> PortBox1;
	private JComboBox<String> BaudBox1;
	public static boolean isPortAvailable;
	static boolean firstRun = true;
	
	/**
	 * Launch the application.
	 */

	static String[] availablePorts = new String[10];
	static String selectedPort;
	//selectedBaudRate, selectedDataBits, selectedStopBits, selectedParity;
	static int[] sr1params = new int[4];	

	
   
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					SR1gui window = new SR1gui();
					window.Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SR1gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		SR1.updatePorts(true);
		Frame = new JFrame();
		//Update on windows focus change
                        /*
		Frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				SR1.updatePorts(firstRun);
			}
			public void windowLostFocus(WindowEvent arg0) {
				SR1.updatePorts(firstRun);
			}
		});*/
		Frame.setResizable(false);
		Frame.setSize(500, 350);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.getContentPane().setLayout(null);
		
		//Ports options
		PortBoxModel = new DefaultComboBoxModel<>();
		
		Panel = new JPanel();
		Panel.setBounds(15, 5, 100, 25);
		Frame.getContentPane().add(Panel);
		Panel.setLayout(null);

		PortBox1 = new JComboBox<>(PortBoxModel);
		PortBox1.setBounds(0, 0, 100, 25);
		Panel.add(PortBox1);
		PortBox1.setName("Port");
		PortBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SR1.updatePorts(true);
				if(!(PortBox1.getSelectedItem() == null)){
					selectedPort = availablePorts[PortBox1.getSelectedIndex()];
					System.out.println(selectedPort + " port set");
				}
			}
		});
		PortBox1.setToolTipText("Port select");
		
		//Baud rate options
		String[] BAUD_RATES = new String[] {"1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600"};
		BaudBox1 = new JComboBox<>();
		BaudBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sr1params[0] = Integer.parseInt((String) BaudBox1.getSelectedItem());
				System.out.println(sr1params[0] + " Baud rate set");
			}
		});
		BaudBox1.setBackground(new Color(255, 255, 255));
		BaudBox1.setSize(100, 25);
		BaudBox1.setLocation(235, 5);
		for(int i = 0; i<(BAUD_RATES.length-1); i++) {
			BaudBox1.addItem(BAUD_RATES[i]);
		}		
		BaudBox1.setSelectedIndex(3);
		BaudBox1.setToolTipText("Baud rate select");
		Frame.getContentPane().add(BaudBox1, "cell 0 0,alignx right");
		
		//Data bits options
		String[] DATA_BITS_OPTIONS = new String[] {"5", "6", "7", "8"};
		JComboBox<String> dataBitBox = new JComboBox<>();
		dataBitBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sr1params[1] = Integer.parseInt((String) dataBitBox.getSelectedItem());
				System.out.println(sr1params[1] + " data bits set");
			}
		});
		dataBitBox.setToolTipText("Data Bits");
		dataBitBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		dataBitBox.setBounds(15, 41, 100, 25);
		for(int i = 0; i<(DATA_BITS_OPTIONS.length); i++) {
			dataBitBox.addItem(DATA_BITS_OPTIONS[i]);
		}	
		dataBitBox.setSelectedIndex(3);
		Frame.getContentPane().add(dataBitBox);
			
		//Stop bit options
		JComboBox<String> stopBitBox = new JComboBox<>();
		stopBitBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sr1params[2] = Integer.parseInt((String) stopBitBox.getSelectedItem());
				System.out.println(sr1params[2] + " stop bits set");
			}
		});
		
		stopBitBox.setToolTipText("Stop Bits");
		stopBitBox.setBounds(125, 41, 100, 25);
		stopBitBox.addItem("1");
		stopBitBox.addItem("2");
		stopBitBox.setSelectedIndex(0);
		Frame.getContentPane().add(stopBitBox);
		
		//Parity options
		JComboBox<String> parityBox = new JComboBox<>();
		parityBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sr1params[3] = parityBox.getSelectedIndex();
				System.out.println("-"+sr1params[3]+"- "+((String) parityBox.getSelectedItem())+" parity set");
			}
		});
		parityBox.setToolTipText("Set Parity");
		parityBox.setBounds(235, 41, 100, 25);
		parityBox.addItem("None");
		parityBox.addItem("Odd");
		parityBox.addItem("Even");
		Frame.getContentPane().add(parityBox);
		
		// Text area scroll pane
		JScrollPane scrollPane = new JScrollPane();			//Scroll Pane
		scrollPane.setBounds(10, 81, 474, 169);
		Frame.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();				//Text Area
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		PrintStream txtStream = new PrintStream (new TextAreaOutputStream(textArea));
			textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			
		//Extra update ports 
			JButton btnRefreshPorts = new JButton("");
			btnRefreshPorts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SR1.updatePorts(false);
				}
			});
			btnRefreshPorts.setToolTipText("Refresh ports");
			btnRefreshPorts.setIcon(new ImageIcon(SR1gui.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
			btnRefreshPorts.setBounds(125, 6, 27, 23);
			Frame.getContentPane().add(btnRefreshPorts);
			
			//	Connect/Disconnect button
			JButton btnConnect = new JButton("Connect");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        if (!(SR1.setConnected) && isPortAvailable) {
			       		System.out.println("Starting serial communication on port "+ selectedPort + " at " + sr1params[0]);
			       		btnConnect.setText("Disconnect");
			       		SR1.launchSR1(selectedPort, sr1params);
			       		PortBox1.setEnabled(false);
			       		BaudBox1.setEnabled(false);
			       		dataBitBox.setEnabled(false);
			       		stopBitBox.setEnabled(false);
			        	parityBox.setEnabled(false);
			        	System.out.println(sr1params[0]+" "+sr1params[1]+" "+sr1params[2]+" "+sr1params[3]);
				    } 
			        else if(SR1.setConnected){
			        	SR1.disconnect();
			        	System.out.println("Stopped serial communication on port "+ selectedPort + " at " + sr1params[0]);
			        	btnConnect.setText("Connect");
			        	PortBox1.setEnabled(true);
			        	BaudBox1.setEnabled(true);
			        	dataBitBox.setEnabled(true);
			        	stopBitBox.setEnabled(true);
			        	parityBox.setEnabled(true);
			        }
			        else {System.out.println("No port to connect to");}
				}
			});
			btnConnect.setToolTipText("Connect to port and start serial read");
			btnConnect.setBounds(384, 41, 100, 25);
			Frame.getContentPane().add(btnConnect);
			
			JPanel panelText = new JPanel();
			panelText.setBounds(15, 261, 359, 25);
			Frame.getContentPane().add(panelText);
			panelText.setLayout(null);
			
			textSend = new JTextField();
			textSend.setBounds(0, 0, 359, 25);
			panelText.add(textSend);
			textSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(SR1.setConnected) {
						SR1.writeToPort((String) textSend.getText());
						System.out.println((String) textSend.getText());
						textSend.setText("");
						panelText.revalidate();
						panelText.repaint();
					}
					else System.out.println("Connect to a port");
				}
			});
			

			textSend.setToolTipText("Send data");
			textSend.setColumns(10);
			
			JButton btnSend = new JButton("Send");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(SR1.setConnected) {
					SR1.writeToPort((String) textSend.getText());
					System.out.println((String) textSend.getText());
					textSend.setText("");
					panelText.revalidate();
					panelText.repaint();
					}
					else System.out.println("Connect to a port");
				}
			});
			btnSend.setToolTipText("Send");
			btnSend.setBounds(384, 261, 100, 25);
			Frame.getContentPane().add(btnSend);
			firstRun = false;
		//Text area output stream	
		System.setOut(txtStream);
		System.setErr(txtStream);
	}
	
	String readstate;
	private JTextField textSend;
	
	//Text area output stream
	public class TextAreaOutputStream extends OutputStream 
	{
		private final JTextArea TextArea;
		public TextAreaOutputStream(JTextArea textArea0) {
			this.TextArea = textArea0;
		}
		public void write(int b) throws IOException {
			TextArea.append(String.valueOf((char) b));
			TextArea.setCaretPosition(TextArea.getDocument().getLength());
		}
		public void write(char[] cbuf, int off, int len) throws IOException{
			TextArea.append(new String (cbuf, off, len));
			TextArea.setCaretPosition(TextArea.getDocument().getLength());
		}
	}
}

