package pack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.net.ServerSocket;
import java.net.Socket;
import org.jfree.ui.RefineryUtilities;
public class Bandwidth extends JFrame implements Runnable
{
	JPanel p1,p2,p3;
	JLabel l1;
	JScrollPane jsp;
	JTable table;
	DefaultTableModel dtm;
	Font f1,f2;
	LineBorder line;
	TitledBorder title;
	Thread thread;
	ServerSocket server;
	Socket socket;
	JButton b1;
	static Object window[][];
	static long redundancy,unique;
public void start(){
	try{
		unique = 0;
		redundancy = 0;
		server = new ServerSocket(1111);
		Object row[] = {"Cloud Server Started"};
		dtm.addRow(row);
		while(true){
			socket = server.accept();
			thread=new BandwidthProcessThread(socket,dtm,window);
			thread.start();
		}
	}catch(Exception e){
				e.printStackTrace();
	}
}
public static void setRedundancy(long r){
	redundancy = redundancy+r;
}
public static void setUnique(long u){
	unique = unique + u;
}
public static void setWindow(Object win[][]){
	window = win;
}
public Bandwidth(){
	super("PACK");
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
	title = new TitledBorder(line,"TRAFFIC REDUNDANCY ELIMINATION SYSTEM",TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION, new Font("Arial",Font.BOLD,14 ),new Color(49, 120, 206));
	
	p2 = new JPanel();
	p2.setLayout(new BorderLayout());
	p2.setBorder(title);
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	jsp = new JScrollPane(table);
	table.setRowHeight(30);
	table.setFont(f2);
	dtm.addColumn("Request Processing Details"); 
	p2.add(jsp,BorderLayout.CENTER);

	p3 = new JPanel();
	b1 = new JButton("Traffic Volume And Detected Redundancy");
	b1.setFont(f2);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Chart chart1 = new Chart("Traffic Volume And Detected Redundancy",redundancy,unique);
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});
	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
	getContentPane().add(p3,BorderLayout.SOUTH);
	thread = new Thread(this);
	thread.start();
}
public void run(){
	try{
		while(true){
			l1.setForeground(Color.white);
			thread.sleep(500);
			l1.setForeground(new Color(125,54,2));
			thread.sleep(500);
			l1.setForeground(Color.black);
			thread.sleep(500);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}


public static void main(String a[])throws Exception{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	Bandwidth bw = new Bandwidth();
	bw.setVisible(true);
	bw.setSize(800,600);	
	new BandwidthThread(bw);
}
}