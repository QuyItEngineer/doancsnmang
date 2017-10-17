package filetransfer;

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

public class FileTransferServer extends Frame{
	
	public static String strHostAddress = "";
	public static int intPortNumber = 0, intMaxClients = 0;
	public static Vector vecConnectionSockets = null;
	public static FileTransferServer objFileTransfer;
	public static String strFileName = "",strFilePath = "";
	public static Socket clientSocket = null;
	public static ObjectOutputStream outToServer = null;
	public static ObjectInputStream inFromServer = null;
	
	public static void main (String [] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Chon so hieu cong de ket noi: ");
		System.out.flush();
		
		intPortNumber = Integer.parseInt(stdin.readLine());
		
		System.out.print("So luong may Client co the ket noi: ");
		System.out.flush();
		
		intMaxClients = Integer.parseInt(stdin.readLine());
		objFileTransfer = new FileTransferServer();
	}
	public Label lblSelectFile;
	public Label lblTitle;
	public Label lblStudentName;
	public Label lblStudentClass;
	public TextField tfFile;
	public Button btnBrowse;
	public Button btnSend;
	public Button btnReset;
	
	public FileTransferServer () {
		setTitle("Chuong trinh truyen File theo giao thuc TCP");
		setSize(600 , 400);
		setLayout(null);
		addWindowListener(new WindowAdapter () { 
			public void windowClosing (WindowEvent e) { 
				System.exit(0); 
				} 
			} );
		
		lblTitle = new Label("Form truyen File tu Server. ");
		add(lblTitle);
		lblTitle.setBounds(50,30,450,50);
		
		lblSelectFile = new Label("Duong dan file can truyen:");
		add(lblSelectFile);
		lblSelectFile.setBounds(50,100,200,20);

		tfFile = new TextField("");
		add(tfFile);
		tfFile.setBounds(50,134,200,20);
		
		btnBrowse = new Button("Chon File");
		btnBrowse.addActionListener(new buttonListener());
		add(btnBrowse);
		btnBrowse.setBounds(283,133,70,20);
		
		lblStudentName = new Label("Giao vien huong dan: Mai Van Ha.");
		add(lblStudentName);
		lblStudentName.setBounds(70,230,300,20);
		
		lblStudentName = new Label("Sinh vien thuc hien: Hoang Bui Ngoc Quy.");
		add(lblStudentName);
		lblStudentName.setBounds(70,270,300,20);
		
		lblStudentName = new Label("Nguyen Duc Dang Quang.");
		add(lblStudentName);
		lblStudentName.setBounds(180,290,300,20);
		
		lblStudentClass = new Label("Lop : 14TCLC2");
		add(lblStudentClass);
		lblStudentClass.setBounds(70,320,100,20);
		
		
		
		btnSend = new Button("Gui");
		btnSend.addActionListener(new buttonListener());
		add(btnSend);
		btnSend.setBounds(70,180,50,20);
		
		btnReset = new Button("Xoa");
		btnReset.addActionListener(new buttonListener());
		add(btnReset);
		btnReset.setBounds(140,180,50,20);
		
		setVisible(true);
		vecConnectionSockets = new Vector();
		
		try {
			InetAddress ip = InetAddress.getByName("192.168.1.100");
			ServerSocket welcomeSocket = new ServerSocket(intPortNumber,intMaxClients,ip);
			
			System.out.println("Address server: "+welcomeSocket.getInetAddress());
			while (true) {
				vecConnectionSockets.addElement(new ThreadedConnectionSocket(welcomeSocket.accept()));
				Thread.yield();
			}
		} 
		catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	public static String showDialog () {
		FileDialog fd = new FileDialog(new Frame(),"Select File...", FileDialog.LOAD);
		fd.setVisible(true);
		return fd.getDirectory()+fd.getFile();
	}
	
	private class buttonListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			byte[] arrByteOfSentFile = null;
			if (ae.getSource() == btnBrowse) {
				strFilePath = showDialog();
				tfFile.setText(strFilePath);
				int intIndex = strFilePath.lastIndexOf("\\");
				strFileName = strFilePath.substring(intIndex+1);
			}
			if (ae.getSource() == btnSend) {
				try {
					FileInputStream inFromHardDisk = new FileInputStream (strFilePath);
					int size = inFromHardDisk.available();
					arrByteOfSentFile = new byte[size];
					inFromHardDisk.read(arrByteOfSentFile,0,size);
					for (int i=0;i<vecConnectionSockets.size();i++)
					{
						ThreadedConnectionSocket tempConnectionSocket = (ThreadedConnectionSocket)vecConnectionSockets.elementAt(i);
						tempConnectionSocket.outToClient.writeObject("IsFileTransfered");
						tempConnectionSocket.outToClient.flush();
						tempConnectionSocket.outToClient.writeObject(strFileName);
						tempConnectionSocket.outToClient.flush();
						tempConnectionSocket.outToClient.writeObject(arrByteOfSentFile);
						tempConnectionSocket.outToClient.flush();
					}
					
					JOptionPane.showMessageDialog(null,"Ban da gui thanh cong file toi Client","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (Exception ex) {}
			}
			if (ae.getSource() == btnReset) {
				tfFile.setText("");
			}
		}
	}
	class ThreadedConnectionSocket extends Thread {
		public Socket connectionSocket;
		public ObjectInputStream inFromClient;
		public ObjectOutputStream outToClient;
		public ThreadedConnectionSocket (Socket s) {
			connectionSocket = s;
				try {
					outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
					
					outToClient.flush();
					inFromClient = new ObjectInputStream(connectionSocket.getInputStream( ));
				} catch (Exception e) {
					System.out.println(e);
				}
				start();
			}
		
		public void run () {
			try {
				int intFlag = 0;
				String strFileName = "";
				while (true) {
					Object objRecieved = inFromClient.readObject();
					
					switch (intFlag) {
					case 0:
						if (objRecieved.equals("IsFileTransfered")) {
							intFlag++;
						}
						break;
					case 1:
						strFileName = (String) objRecieved;
						int intOption = JOptionPane.showConfirmDialog(null,connectionSocket.getInetAddress().getHostName()+" dang gui "+strFileName+"!\nBan co chac chan nhan khong?","Thong bao",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						
						System.out.println("\nTen nguon da gui File: "+connectionSocket.getInetAddress().getLocalHost());
						System.out.println("Dia chi Internet nguon cua File: "+connectionSocket.getInetAddress().getHostAddress());
						
						if (intOption == JOptionPane.YES_OPTION) {
							intFlag++;
						} else {
							intFlag = 0;
						}
						break;
					case 2:
						byte[] arrByteOfReceivedFile = (byte[])objRecieved;
						FileOutputStream outToHardDisk = new FileOutputStream(strFileName);
						outToHardDisk.write(arrByteOfReceivedFile);
						System.out.println("Ten File da gui: "+strFileName);
						intFlag = 0;
						JOptionPane.showMessageDialog(null,"Ban da nhan thanh cong file tu Client","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				Thread.yield();
				}
			} 
			catch (Exception e) {
				System.out.println(e);
			}
		}
	} 
	


}
