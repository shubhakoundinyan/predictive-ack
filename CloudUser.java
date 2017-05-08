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
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
public class CloudUser extends JFrame implements Runnable
{
	JPanel p1,p2,p3,p4;
	JLabel l1,l2;
	ImageIcon icon;
	Font f1,f2;
	LineBorder line;
	TitledBorder title;
	Thread thread;
	JButton b1,b2;
public CloudUser(){
	super("PACK");
	f1 = new Font("Calibri",Font.ITALIC,18);
	p1 = new JPanel();
	p1.setBackground(new Color(120,120,255));
	l1 = new JLabel("<html><body<center>PACK: PREDICTION-BASED CLOUD BANDWIDTH AND COST<br/>REDUCTION SYSTEM</center></body></html>");
	l1.setFont(f1);
	l1.setForeground(new Color(140,54,2));
	p1.add(l1);
	getContentPane().add(p1,BorderLayout.NORTH);

	f2 = new Font("Courier New",Font.BOLD,14);
	line = new LineBorder(new Color(35, 84, 146),3,true);
	title = new TitledBorder(line,"TRAFFIC REDUNDANCY ELIMINATION SYSTEM",TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION, new Font("Arial",Font.BOLD,14 ),new Color(49, 120, 206));
	
	p2 = new JPanel();
	p2.setForeground(Color.white);
	p2.setLayout(new BorderLayout());
	p2.setBorder(title);
	p3 = new JPanel();
	p3.setForeground(Color.green);
	b1 = new JButton("Existing User");
	b1.setFont(f2);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Login log = new Login();
			log.setVisible(true);
			log.pack();
			log.setLocationRelativeTo(null);
		}
	});

	b2 = new JButton("New User");
	b2.setFont(f2);
	p3.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			NewUser nu = new NewUser();
			nu.setVisible(true);
			nu.pack();
			nu.setLocationRelativeTo(null);
		}
	});

	p2.add(p3,BorderLayout.NORTH);

	p4 = new JPanel();
	p4.setForeground(Color.white);
	icon = new ImageIcon("img/chain.jpg");
	l2 = new JLabel(icon);
	p4.add(l2);
	p2.add(p4,BorderLayout.CENTER);

	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
	thread = new Thread(this);
	thread.start();
}
public void run(){
	try{
		while(true){
			icon = new ImageIcon("img/chain.jpg");
			l2.setIcon(icon);
			l1.setForeground(Color.white);
			thread.sleep(500);
			l1.setForeground(new Color(125,54,2));
			thread.sleep(500);
			icon = new ImageIcon("img/cloud.jpg");
			l2.setIcon(icon);
			l1.setForeground(Color.black);
			thread.sleep(500);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}


public static void main(String a[])throws Exception{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	CloudUser cu = new CloudUser();
	cu.setVisible(true);
	cu.setSize(800,600);	
}
}