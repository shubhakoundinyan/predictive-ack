package pack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.FileInputStream;
import java.io.FileOutputStream;
public class UserOpr extends JFrame
{
	JPanel p1,p2,p3,p4;
	JLabel l1;
	Font f1,f2;
	LineBorder line;
	TitledBorder title;
	JButton b1,b2;
	JTextArea area;
	JScrollPane jsp; 
	String uname;
	JFileChooser chooser;
public UserOpr(String un){
	super("PACK");
	uname = un;
	f1 = new Font("Courier New",Font.BOLD,18);
	p1 = new JPanel();
	p1.setBackground(new Color(140,150,180));
	l1 = new JLabel("<html><body<center>PACK: PREDICTION-BASED CLOUD BANDWIDTH AND COST<br/>REDUCTION SYSTEM</center></body></html>");
	l1.setFont(f1);
	l1.setForeground(new Color(125,54,2));
	p1.add(l1);
	getContentPane().add(p1,BorderLayout.NORTH);

	f2 = new Font("Courier New",Font.BOLD,14);
	line = new LineBorder(new Color(35, 84, 146),3,true);
	title = new TitledBorder(line,"USER OPERATION SCREEN",TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION, new Font("Arial",Font.BOLD,14 ),new Color(49, 120, 206));
	
	chooser = new JFileChooser();
	p2 = new JPanel();
	p2.setForeground(Color.white);
	p2.setLayout(new BorderLayout());
	p2.setBorder(title);
	p3 = new JPanel();
	p3.setForeground(Color.white);
	b1 = new JButton("Upload Data");
	b1.setFont(f2);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			int option = chooser.showOpenDialog(UserOpr.this);
			if(option == chooser.APPROVE_OPTION){
				File file = chooser.getSelectedFile();
				upload(file);
			}
		}
	});

	b2 = new JButton("Download Data");
	b2.setFont(f2);
	p3.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			area.setText("");
			DownloadFile df=new DownloadFile(area);
			df.setUser(uname);
			df.getFileName();
			df.pack();
			df.setLocationRelativeTo(null);
			df.setVisible(true);
		}
	});

	p2.add(p3,BorderLayout.NORTH);

	area = new JTextArea();
	area.setFont(f2);
	jsp = new JScrollPane(area);
	p2.add(jsp,BorderLayout.CENTER);

	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
}
public void upload(File file){
	try{
		area.setText("");
		FileInputStream fin = new FileInputStream(file);
		byte b[] = new byte[fin.available()];
		fin.read(b,0,b.length);
		fin.close();
		if(file.getName().endsWith(".txt") || file.getName().endsWith(".java")){
			area.append(new String(b));
		}
		Socket socket=new Socket("localhost",1111);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"upload",uname,file.getName(),b};
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
        if(res[0].toString().equals("success")){
			JOptionPane.showMessageDialog(this,"File sent to cloud server successfully");
		}else{
			JOptionPane.showMessageDialog(this,res[0].toString());
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
