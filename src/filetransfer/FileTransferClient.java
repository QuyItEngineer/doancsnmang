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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

public class FileTransferClient extends Frame{
	public static String strHostAddress = "";
	public static int intPortNumber = 0, intMaxClients = 0;
	public static Vector vecConnectionSockets = null;
	public static FileTransferClient objFileTransfer;
	public static String strFileName = "",strFilePath = "";
	public static Socket clientSocket = null;
	public static ObjectOutputStream outToServer = null;
	public static ObjectInputStream inFromServer = null;
	
	public static void main (String [] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
//		System.out.print("Nhap dia chi cua may server de ket noi: ");
//		System.out.flush();
//		
//		strHostAddress = stdin.readLine();
		
		System.out.print("Nhap dia chi cong de ket noi voi may server: ");
		System.out.flush();
		
		intPortNumber = Integer.parseInt(stdin.readLine());
		objFileTransfer = new FileTransferClient();
	}
	public Label lblSelectFile;
	public Label lblTitle;
	public Label lblStudentName;
	public Label lblStudentClass;
	public TextField tfFile;
	public Button btnBrowse;
	public Button btnSend;
	public Button btnReset;
	public FileTransferClient () {
		setTitle("Chuong trinh truyen File theo giao thuc TCP");
		setSize(600 , 400);
		setLayout(null);
		
		addWindowListener(new WindowAdapter () { public void windowClosing
		(WindowEvent e) { System.exit(0); } } );
		
		lblTitle = new Label("Form truyen File tu Client. ");
		add(lblTitle);
		lblTitle.setBounds(50,30,450,50);
		
		lblSelectFile = new Label("Duong dan file can truyen:");
		add(lblSelectFile);
		lblSelectFile.setBounds(50,100,200,20);
		
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
		
		tfFile = new TextField("");
		add(tfFile);
		tfFile.setBounds(50,134,200,20);
		
		btnBrowse = new Button("Chon File");
		btnBrowse.addActionListener(new buttonListener());
		add(btnBrowse);
		btnBrowse.setBounds(283,133,70,20);
		
		btnSend = new Button("Gui");
		btnSend.addActionListener(new buttonListener());
		add(btnSend);
		btnSend.setBounds(70,180,50,20);
		
		btnReset = new Button("Xoa");
		btnReset.addActionListener(new buttonListener());
		add(btnReset);
		btnReset.setBounds(140,180,50,20);
		
		setVisible(true);
		try {
			InetAddress test = InetAddress.getLocalHost();
			clientSocket = new Socket (test,intPortNumber);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			outToServer.flush();
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Client connected to Server.");
			int intFlag = 0;
			
				while (true) {
					Object objRecieved = inFromServer.readObject();
					switch (intFlag) {
					case 0:
						if (objRecieved.equals("IsFileTransfered")) {
							intFlag++;
						}
						break;
					case 1:
						strFileName = (String) objRecieved;
						int intOption =
								JOptionPane.showConfirmDialog(this,clientSocket.getInetAddress().getAddress()+" dang gui "+strFileName+"!\nBan co chac chan nhan khong?","Thong bao",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
								
								System.out.println("\nTen nguon da gui File: "+clientSocket.getInetAddress().getLocalHost());
								System.out.println("Dia chi Internet nguon cua File: "+clientSocket.getInetAddress().getHostAddress());
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
						JOptionPane.showMessageDialog(this,"Ban dong y nhan file nay tu Server","Thong bao",JOptionPane.INFORMATION_MESSAGE);
//						clientSocket.close();
						break;
					}
					Thread.yield();
				}
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	public static String showDialog () {
		FileDialog fd = new FileDialog(new Frame(),"Select File...",FileDialog.LOAD);
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
				System.out.println(strFileName);
			}
			if (ae.getSource() == btnSend) {
				try {
					FileInputStream inFromHardDisk = new FileInputStream (strFilePath);
					int size = inFromHardDisk.available();
					arrByteOfSentFile = new byte[size];
					inFromHardDisk.read(arrByteOfSentFile,0,size);
					outToServer.writeObject("IsFileTransfered");
					outToServer.flush();
					outToServer.writeObject(strFileName);
					outToServer.flush();
//					outToServer.writeObject(arrByteOfSentFile);
//					outToServer.flush();
					
					FileInputStream fileInputStream = new FileInputStream(strFilePath);
					System.out.println(fileInputStream);
//					outToServer.writeObject("IsFileTransfered");
//					outToServer.flush();
					outToServer.writeObject(strFileName);
					outToServer.flush();
					
			        byte [] buffer = new byte[192*1024]; 
			        int bytesRead = 0;
			        long totalSent = 0;
			        long time = System.currentTimeMillis();
			        OutputStream out = clientSocket.getOutputStream();
			        System.out.println("01...................00");
			        while ( (bytesRead = fileInputStream.read(buffer)) != -1)
			        {
			            if (bytesRead > 0)
			            {   System.out.println("01...");
			                out.write(buffer, 0, bytesRead);
			                totalSent += bytesRead;
			                System.out.println("sent " + totalSent);
			            }   
			        }

			        

			        System.out.println("Sent " + totalSent + " bytes in "
			                + (System.currentTimeMillis() - time) + "ms.");
			        clientSocket.close();
			        JOptionPane.showMessageDialog(null,"Ban da gui thanh cong file toi Server","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
			        
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
			if (ae.getSource() == btnReset) {
				tfFile.setText("");
			}
		}
	}
	
}
