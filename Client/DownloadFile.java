package pack;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.FlowLayout;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import javax.swing.JTextArea;
import java.awt.Color;
import java.io.File;
import java.awt.Font;
import java.util.ArrayList;
public class DownloadFile extends JFrame
{
	JLabel l1;
	JButton b1;
	JComboBox c1;
	String user;
	JTextArea area;
	Font f1;
	int start = 0;
	String download_data;
	String ack;
public void setUser(String user){
    this.user=user;
}
public DownloadFile(JTextArea ar){
	area = ar;
	setTitle("Download File");
	getContentPane().setBackground(Color.white);
	getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

	f1 = new Font("Monospaced",Font.BOLD,14);
	l1 = new JLabel("File Name");
	l1.setFont(f1);
	getContentPane().add(l1);

	c1 = new JComboBox();
	c1.setFont(f1);
	getContentPane().add(c1);

	b1 = new JButton("Download");
	b1.setFont(f1);
	getContentPane().add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			try{
				start = 0;
				String file = c1.getSelectedItem().toString();
				FileOutputStream rec = new FileOutputStream("received/"+file);
				StringBuilder sb = new StringBuilder();
				boolean flag = true;
				while(flag){
					if(start == 0){
						Socket socket=new Socket("localhost",1111);
						ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
						Object req[]={"download",user,file,"none","0","0"};
						out.writeObject(req);
						out.flush();
						Object res[]=(Object[])in.readObject();
						String response = res[0].toString();
						String win_name = res[2].toString();
						if(response.equals("window")){
							byte fdata[] = (byte[])res[1];
							sb.append(new String(fdata));
							area.append(new String(fdata));
							download_data = new String(fdata);
							FileOutputStream fout = new FileOutputStream("local/"+win_name);
							fout.write(fdata,0,fdata.length);
							fout.close();
							start = start + 1;
						}
					}else{
						ack = "none";
						String match = checkMatch(file);
						String hash = SHA.ShaSignature(match.getBytes());
						Socket socket=new Socket("localhost",1111);
						ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
						Object req[]={"download",user,file,ack,Integer.toString(start),hash};
						out.writeObject(req);
						out.flush();
						Object res[]=(Object[])in.readObject();
						String response = res[0].toString();
						if(response.equals("window")){
							String win_name = res[2].toString();
							byte fdata[] = (byte[])res[1];
							sb.append(new String(fdata));
							area.append(new String(fdata)+"\nno copy=======\n");
							download_data = new String(fdata);
							FileOutputStream fout = new FileOutputStream("local/"+win_name);
							fout.write(fdata,0,fdata.length);
							fout.close();
							start = start + 1;
						}
						else if(response.equals("copy")){
							area.append(match+"\ncopy===\n");
							download_data = match;
							start = start + 1;
							byte fdata[] = match.getBytes();
							sb.append(new String(fdata));
						}
						else if(response.equals("over")){
							byte b[] = sb.toString().getBytes();
							rec.write(b,0,b.length);
							rec.close();
							start = 0;
							flag = false;
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
}
public void getFileName(){
	try{
		c1.removeAllItems();
		 Socket socket=new Socket("localhost",1111);
         ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		 ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
		 Object req[]={"filename",user};
		 out.writeObject(req);
		 out.flush();
		 Object res[]=(Object[])in.readObject();
		 String files[] =(String[])res[0];
		 for(int i=0;i<files.length;i++){
			 c1.addItem(files[i]);
		 }
		 out.close();
		 in.close();
		 socket.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public String checkMatch(String file_name){
	String value = "none";
	try{
		File files = new File("local");
		File list[] = files.listFiles();
		ArrayList<File> arr = new ArrayList<File>();
		for(int i=0;i<list.length;i++){
			String name = list[i].getName();
			String ar[] = name.split("_");
			if(ar[1].equals(file_name))
				arr.add(list[i]);
		}
		for(int i=0;i<arr.size();i++){
			File fill = arr.get(i);
			FileInputStream fin = new FileInputStream(fill.getPath());
			byte b[] = new byte[fin.available()];
			fin.read(b,0,b.length);
			fin.close();
			String check = new String(b);
			if(check.toString().equals(download_data.toString())){
				String name = fill.getName();
				String str[] = name.split("_");
				String val = (i+1)+"_"+str[1];
				File ff = new File("local/"+val);
				System.out.println(check+"===="+download_data);
				if(ff.exists()){
					System.out.println("value "+val+" "+name);
					ack = "matched";
					fin = new FileInputStream(ff);
					byte b1[] = new byte[fin.available()];
					fin.read(b1,0,b1.length);
					fin.close();
					value = new String(b1);
					i = list.length;
					break;
				}
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return value;
}
}
