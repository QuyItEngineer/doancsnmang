package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class ServerForm extends JFrame{

	
	public JTextField ipAddressTextField;
    public JTextField portTextField;
    public JTextArea messageTextAreaServer;
    public JTextField filePathTextField;
    private String ipAddress;
    private String sourceFilePath;
    private String fileName;
    private Label lblTitle;
    public static Vector vecConnectionSockets = null;
	
	public ServerForm() {
        setTitle("Chuong trinh truyen File theo giao thuc TCP");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        
        add(buildInputPanel(), BorderLayout.NORTH);
        messageTextAreaServer= new JTextArea(30, 30);
        messageTextAreaServer.setWrapStyleWord(true);
        messageTextAreaServer.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageTextAreaServer);
        add(scrollPane, BorderLayout.CENTER);
        
        vecConnectionSockets = new Vector();
    }
	
	 public JPanel buildInputPanel() {
		 JPanel panel = new JPanel();
	        panel.setLayout(new GridBagLayout());
	        GridBagConstraints c = new GridBagConstraints();
	        c.insets = new Insets(5, 5, 0, 5);
	        Dimension minDimension = new Dimension(100, 20);

	        c.gridx = 3;
	        c.gridy = 0;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Khoa Cong nghe thong tin"), c);
	        
	        c.gridx = 2;
	        c.gridy = 1;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("FORM NHAN FILE CUA SERVER TU CLIENT"), c);
	        
	        c.gridx = 0;
	        c.gridy = 2;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("GVHD: "), c);
	        
	        c.gridx = 1;
	        c.gridy = 2;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("ThS. Mai Van Ha "), c);
	        
	        c.gridx = 0;
	        c.gridy = 3;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Sinh vien: "), c);
	        
	        c.gridx = 1;
	        c.gridy = 3;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Hoang Bui Ngoc Quy "), c);
	        
	        c.gridx = 1;
	        c.gridy = 4;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Nguyen Duc Dang Quang"), c);
	        
	        c.gridx = 0;
	        c.gridy = 5;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Lop: "), c);
	        
	        c.gridx = 1;
	        c.gridy = 5;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("14TCLC2"), c);
	        
	       
	        c.gridx = 2;
	        c.gridy = 6;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("_____________________"), c);
	        
	        c.gridx = 1;
	        c.gridy = 7;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("IP Address of Server: "), c);

	        c.gridx = 1;
	        c.gridy = 8;
	        c.anchor = GridBagConstraints.LINE_START;
	        ipAddressTextField = new JTextField(15);
	        ipAddressTextField.setText(getIpAddress());
	        ipAddressTextField.setMinimumSize(minDimension);
	        ipAddressTextField.setEditable(false);
	        panel.add(ipAddressTextField, c);
	        

	        c.gridx = 2;
	        c.gridy = 7;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Port: "), c);

	        c.gridx = 2;
	        c.gridy = 8;
	        c.anchor = GridBagConstraints.LINE_START;
	        portTextField = new JTextField(10);
	        portTextField.setMinimumSize(minDimension);
	        panel.add(portTextField, c);
	        
	        c.gridx = 3;
	        c.gridy = 8;
	        c.anchor = GridBagConstraints.WEST;
	        JButton startBt = new JButton("Start");
	        startBt.addActionListener((ActionEvent e) -> {
	        	startBt();
	        });
	        panel.add(startBt, c);
	        
	        c.gridx = 1;
	        c.gridy = 9;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Save folder: "), c);

	        c.gridx = 1;
	        c.gridy = 10;
	        c.gridwidth = 2;
	        c.anchor = GridBagConstraints.CENTER;
	        filePathTextField = new JTextField(20);
	        filePathTextField.setMinimumSize(minDimension);
	        panel.add(filePathTextField, c);
	        	        
	       
	        c.gridx = 1;
	        c.gridy = 11;
	        c.anchor = GridBagConstraints.LINE_START;
	        panel.add(new JLabel("Notification: "), c);

	        return panel;
		 
	 }
	 private String getIpAddress() {
	        try {
	            ipAddress = InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        return ipAddress;
	    }
	    
	    private void doChooseBt(){
	        FileDialog fd = new FileDialog(new JFrame(),"Select File...",FileDialog.LOAD);
	        fd.show();
	        String filePath =fd.getDirectory()+fd.getFile();
	        fileName=fd.getFile();
	        filePathTextField.setText(filePath);
	    }
	    
	    private void startBt() {
	    	 int port = Integer.parseInt(portTextField.getText());
	    	try {
	    		ServerSocket serverSocket = new ServerSocket(port);
	            String message = "Server started at " + port + "\n";
	            AppendNewTextThread txt = new AppendNewTextThread(messageTextAreaServer, message);
	            txt.appendNewText();
				System.out.println("Server started at" + port +". \n");

				System.out.println("Server started, Waitting Client connect... ");
				while (true) {
					vecConnectionSockets.addElement(new ThreadedConnectionSocket());
					Thread.yield();
				}
			} 
			catch (IOException ioe) {
				System.out.println(ioe);
			}
	    }
	    
	    public void selectSaveFolder() {
//	        int st = JOptionPane.showConfirmDialog(this, "Server gui 1 file, ban co muon nhan?",
//	                "Message", JOptionPane.OK_CANCEL_OPTION);
//	        if (st == 0) {
	            //chon noi luu file
	            JFileChooser chooser = new JFileChooser();
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.showOpenDialog(null);
	            File f = chooser.getSelectedFile();
	            String filePath = f.getPath();
	            filePathTextField.setText(filePath);
//	        }
	    }
	    
//	    private void doSendBt(){
//	        int port = Integer.parseInt(portTextField.getText());
//	        try {
//	            ServerSocket serverSocket = new ServerSocket(port);
//	            String message = "Server started at " + port + "\n";
//	            AppendNewTextThread txt = new AppendNewTextThread(messageTextAreaServer, message);
//	            txt.appendNewText();
//	            Socket soc = serverSocket.accept();
//	            DataInputStream dis = new DataInputStream(soc.getInputStream());
//	            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
//	            dos.writeUTF(fileName);
//	            dos.flush();
//
//	            String filePath = filePathTextField.getText();
//
//	            //Specify the file
//	            File file = new File(filePath);
//	            FileInputStream fis = new FileInputStream(file);
//	            BufferedInputStream bis = new BufferedInputStream(fis);
//	            OutputStream os = soc.getOutputStream();
//
//	            //Read File Contents into contents array 
//	            byte[] contents;
//	            long fileLength = file.length();
//	            long current = 0;
//
//	            long start = System.nanoTime();
//	            while (current != fileLength) {
//	                int size = 10000;
//	                if (fileLength - current >= size) {
//	                    current += size;
//	                } else {
//	                    size = (int) (fileLength - current);
//	                    current = fileLength;
//	                }
//	                contents = new byte[size];
//	                bis.read(contents, 0, size);
//	                os.write(contents);
//	                String message1 = "Sending file ... " + (current * 100) / fileLength + "% complete!\n";
//	                AppendNewTextThread txt1 = new AppendNewTextThread(messageTextAreaServer, message1);
//	                txt1.appendNewText();
//	                //dos.writeUTF(message);
//	            }
//	            //dos.close();
//	            os.flush();
//	            //File transfer done. Close the socket connection!
//	            soc.close();
//	            serverSocket.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        
//	        String message= "File transfer complete";
//	        AppendNewTextThread txt= new AppendNewTextThread(messageTextAreaServer, message);
//	        txt.appendNewText();
//	    }
}

class ThreadedConnectionSocket extends Thread {
	ServerForm serverForm = new ServerForm();
	public Socket connectionSocket;
	public ObjectInputStream inFromClient;
	public ObjectOutputStream outToClient;
	public DataInputStream disMove;
	
    String filePathDestination;
    int port=  Integer.parseInt(serverForm.portTextField.getText());
	public ThreadedConnectionSocket () {
		
			try {
				InetAddress ip = InetAddress.getByName(serverForm.ipAddressTextField.getText());
	            //Initialize socket
	            Socket soc = new Socket(ip.getHostAddress(), port);
	            String message = "Having connected with " + serverForm.ipAddressTextField.getText() + "\n";
	            AppendNewTextThread txt = new AppendNewTextThread(serverForm.messageTextAreaServer, message);
	            txt.appendNewText();
	            DataInputStream dis = new DataInputStream(soc.getInputStream());
	            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
	            
	            inFromClient = new ObjectInputStream(soc.getInputStream( ));
	            
	            disMove = dis;
	            connectionSocket = soc;
			} catch (Exception e) {
				System.out.println(e);
			}
			start();
		}
	
	public void run () {
		try {
			int intFlag = 0;
			String strFileName = "";
			String fileName;
			while (true) {
				Object objRecieved = inFromClient.readObject();
				
				switch (intFlag) {
				case 0:
					if (objRecieved.equals("IsFileTransfered")) {
						intFlag++;
						System.out.println("file da nhan...");
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
					serverForm.selectSaveFolder();
					fileName= "/"+disMove.readUTF();
		            filePathDestination= serverForm.filePathTextField.getText()+fileName;
		            
		            byte[] contents = new byte[10000];
		            //Initialize the FileOutputStream to the output file's full path.
		            FileOutputStream fos = new FileOutputStream(filePathDestination);
		            BufferedOutputStream bos = new BufferedOutputStream(fos);
		            InputStream is = connectionSocket.getInputStream();
		            //No of bytes read in one read() call
		            int bytesRead = 0;

		            while ((bytesRead = is.read(contents)) != -1) {
		                bos.write(contents, 0, bytesRead);
		                //String message = "Sending file ... " + (current * 100) / fileLength + "% complete!\n";
		                //messageTextAreaClient.setText(dis.readUTF());
		                //System.out.print(dis.readUTF());
		            }
		            //dis.close();
		            bos.flush();
		            
					System.out.println("Ten File da gui: "+strFileName);
					intFlag = 0;
					JOptionPane.showMessageDialog(null,"Ban da nhan thanh cong file tu Client","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
					
					connectionSocket.close(); 
					
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

class AppendNewTextThread extends Thread{
    private JTextArea textArea;
    private String txt;
    public AppendNewTextThread(JTextArea textArea, String txt){
        this.textArea= textArea;
        this.txt=txt;
    }
    public void appendNewText() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(txt);
            }
        });
    }
}

