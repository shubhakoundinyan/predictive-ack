package pack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
public class NewUser extends JFrame{
	LineBorder border;
	TitledBorder title;
	JLabel l1,l2,l3;
	JPanel p1;
	JButton b1,b2;
	JTextField tf1,tf2,tf3;
	Font f1,f2;
public NewUser(){
	super("New User Registration");
	f2 = new Font("Monospaced",Font.BOLD,16);
	p1 = new JPanel();
	p1.setLayout(new MigLayout("wrap 2")); 
	p1.setBackground(Color.white);
	border = new LineBorder(new Color(42,140,241),1,true);
	title = new TitledBorder (border,"New User Registration",TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION, new Font("Tahoma",Font.BOLD,16),Color.darkGray);
	p1.setBorder(title);
	l1 = new JLabel("Username");
	l1.setFont(f2);
	p1.add(l1);

	tf1 = new JTextField(20);
	tf1.setFont(f2);
	p1.add(tf1);

	l2 = new JLabel("Password");
	l2.setFont(f2);
	p1.add(l2);

	tf2 = new JPasswordField(20);
	tf2.setFont(f2);
	p1.add(tf2);

	l3 = new JLabel("Confirm Password");
	l3.setFont(f2);
	p1.add(l3);

	tf3 = new JTextField(20);
	tf3.setFont(f2);
	p1.add(tf3);


	b1 = new JButton("Register");
	b1.setFont(f2);
	p1.add(b1,"split 2,span");
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			register();
		}
	});

	b2 = new JButton("Reset");
	b2.setFont(f2);
	p1.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			clear();
		}
	});

	getContentPane().add(p1,BorderLayout.CENTER);
}
public void clear(){
	tf1.setText("");
	tf2.setText("");
	tf3.setText("");
}
public void register(){
    try{
        String uname=tf1.getText();
        String pass=tf2.getText();
        String cpass=tf3.getText();
        if(uname.length() <=0 || uname == null){
            JOptionPane.showMessageDialog(this,"Username must be enter");
            tf1.requestFocus();
            return;
        }
        if(pass.length() <=0 || pass == null){
            JOptionPane.showMessageDialog(this,"Password must be enter");
            tf2.requestFocus();
            return;
        }
        if(cpass.length() <=0 || cpass == null){
            JOptionPane.showMessageDialog(this,"Confirm password must be enter");
            tf3.requestFocus();
            return;
        }
        if(!pass.equals(cpass)){
            JOptionPane.showMessageDialog(this,"Password and confirm password must be same");
            tf3.requestFocus();
            return;
        }
        Socket socket=new Socket("localhost",1111);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"register",uname,pass,cpass};
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
		if(res[0].toString().equals("Registration completed")){
			setVisible(false);
			Login log = new Login();
			log.setVisible(true);
			log.pack();
			log.setLocationRelativeTo(null);
		}else{
			JOptionPane.showMessageDialog(this,res[0].toString());
		}
    }catch(Exception e){
        e.printStackTrace();
    }
}   
}
