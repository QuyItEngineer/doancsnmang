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
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class ClientForm extends JFrame{
	public JTextField ipAddressTextField;
	public JTextField portTextField;
	public JTextArea messageTextAreaClient;
	public JTextField filePathTextField;
	public String ipAddress;
	public String sourceFilePath;
	public String fileName;
    private Label lblTitle;
	
	public ClientForm() {
        setTitle("Chuong trinh truyen File theo giao thuc TCP");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        
        add(buildInputPanel(), BorderLayout.NORTH);
        messageTextAreaClient= new JTextArea(30, 30);
        messageTextAreaClient.setWrapStyleWord(true);
        messageTextAreaClient.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageTextAreaClient);
        add(scrollPane, BorderLayout.CENTER);
    }
	
	 private JPanel buildInputPanel() {
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
	        panel.add(new JLabel("FORM TRUYEN FILE TU CLIENT"), c);
	        
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
	        panel.add(new JLabel("Address: "), c);

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
	        JButton connectBt = new JButton("Connect");
	        connectBt.addActionListener((ActionEvent e) -> {
	        	doConnectBt();
	        });
	        panel.add(connectBt, c);
	        
	        
	        c.gridx = 1;
	        c.gridy = 9;
	        c.anchor = GridBagConstraints.LINE_START;
	        filePathTextField = new JTextField(20);
	        filePathTextField.setMinimumSize(minDimension);
	        panel.add(filePathTextField, c);

	        c.gridx = 2;
	        c.gridy = 9;
	        c.anchor = GridBagConstraints.LINE_START;
	        JButton chooseBt = new JButton("Choose file");
	        chooseBt.addActionListener((ActionEvent e) -> {
	        	doChooseBt();
	        });
	        panel.add(chooseBt,c);
	        
	        c.gridx = 3;
	        c.gridy = 9;
	        c.anchor = GridBagConstraints.LINE_START;
	        JButton sendBt = new JButton("Sent");
	        sendBt.addActionListener((ActionEvent e) -> {
	        	doSendBt();
	        });
	        panel.add(sendBt,c);



	        c.gridx = 1;
	        c.gridy = 10;
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
	 private void doConnectBt() {
	        String fileName;
	        String filePathDestination;
	        int port=  Integer.parseInt(portTextField.getText());
	        try {
	            InetAddress ip = InetAddress.getByName(ipAddressTextField.getText());
	            //Initialize socket
	            Socket soc = new Socket(ip.getHostAddress(), port);
	            String message = "Having connected with " + ipAddressTextField.getText() + "\n";
	            AppendNewTextThread txt = new AppendNewTextThread(messageTextAreaClient, message);
	            txt.appendNewText();
	            DataInputStream dis = new DataInputStream(soc.getInputStream());
	            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
	           
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	    
	    private void doChooseBt(){
	        FileDialog fd = new FileDialog(new JFrame(),"Select File...",FileDialog.LOAD);
	        fd.show();
	        String filePath =fd.getDirectory()+fd.getFile();
	        fileName=fd.getFile();
	        filePathTextField.setText(filePath);
	    }
	    private void doSendBt(){
	        int port = Integer.parseInt(portTextField.getText());
	        try {
	            ServerSocket serverSocket = new ServerSocket(port);
	            String message = "Server started at " + port + "\n";
	            AppendNewTextThread txt = new AppendNewTextThread(messageTextAreaClient, message);
	            txt.appendNewText();
	            Socket soc = serverSocket.accept();
	            DataInputStream dis = new DataInputStream(soc.getInputStream());
	            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
	            dos.writeUTF(fileName);
	            dos.flush();

	            String filePath = filePathTextField.getText();

	            //Specify the file
	            File file = new File(filePath);
	            FileInputStream fis = new FileInputStream(file);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	            OutputStream os = soc.getOutputStream();

	            //Read File Contents into contents array 
	            byte[] contents;
	            long fileLength = file.length();
	            long current = 0;

	            long start = System.nanoTime();
	            while (current != fileLength) {
	                int size = 10000;
	                if (fileLength - current >= size) {
	                    current += size;
	                } else {
	                    size = (int) (fileLength - current);
	                    current = fileLength;
	                }
	                contents = new byte[size];
	                bis.read(contents, 0, size);
	                os.write(contents);
	                String message1 = "Sending file ... " + (current * 100) / fileLength + "% complete!\n";
	                AppendNewTextThread txt1 = new AppendNewTextThread(messageTextAreaClient, message1);
	                txt1.appendNewText();
	                //dos.writeUTF(message);
	            }
	            //dos.close();
	            os.flush();
	            //File transfer done. Close the socket connection!
	            soc.close();
	            serverSocket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        String message= "File transfer complete";
	        AppendNewTextThread txt= new AppendNewTextThread(messageTextAreaClient, message);
	        txt.appendNewText();
	    }
}
