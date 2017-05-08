package pack;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.File;
public class BandwidthProcessThread extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	DefaultTableModel dtm;
	byte b1[] = null;
	byte b2[] = null;
	RandomAccessFile random;
	int tot_win;
	Object window[][];
public BandwidthProcessThread(Socket soc,DefaultTableModel dtm,Object window[][]){
    socket=soc;
	this.dtm=dtm;
	this.window = window;
	try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}
@Override
public void run(){
    try{
		Object input[]=(Object[])in.readObject();
		if(input != null){
			String request = (String)input[0];
			if(request.equals("register")){
				String uname = (String)input[1];
				String pass = (String)input[2];
				String cpass = (String)input[3];
				String req[] = {uname,pass,cpass};
				String msg=DBCon.register(req);
				Object res[]={msg};
				out.writeObject(res);
				out.flush();
				if(msg.equals("Registration completed")){
					Object row[] = {uname+" Registered successfully"};
					dtm.addRow(row);
					File file = new File("D:/pack_cloud/"+uname.trim());
					if(!file.exists())
						file.mkdirs();
				}
			}
			if(request.equals("login")){
				String user = input[1].toString();
				String pass = input[2].toString();
				String req[] = {user,pass};
				String msg = DBCon.login(req);
				Object res[]={msg};
				out.writeObject(res);
				out.flush();
				if(msg.equals("valid login")){
					Object row[] = {user +" Login"};
					dtm.addRow(row);
				}
			}
			if(request.equals("upload")){
				String user = input[1].toString();
				String file = input[2].toString();
				byte b[] = (byte[])input[3];
				FileOutputStream fout = new FileOutputStream("D:/pack_cloud/"+user+"/"+file);
				fout.write(b,0,b.length);
				fout.close();
				Object res[]={"success"};
				out.writeObject(res);
				out.flush();
				Object row[] = {file +" successfully stored at user storage "+user};
				dtm.addRow(row);
			}
			if(request.equals("filename")){
				String user = input[1].toString().trim();
				File file = new File("D:/pack_cloud/"+user);
				File list[] = file.listFiles();
				String s[]=new String[list.length];
				for(int i=0;i<list.length;i++){
					s[i] = list[i].getName();
				}
				Object res[]={s};
				out.writeObject(res);
				out.flush();
				Object row[] = {"Sent all available file names to user "+user};
				dtm.addRow(row);
			}
			if(request.equals("download")){
				String user = input[1].toString();
				String file = input[2].toString();
				String ack = input[3].toString();
				int start = Integer.parseInt(input[4].toString());
				String hash = input[5].toString();
				if(start == 0){
					File fname= new File("D:/pack_cloud/"+user+"/"+file);
					long window_size = getWindowSize(fname);
					random = new RandomAccessFile(fname.getPath(),"r");
					window = getWindow(window_size,fname.getName());
					random.close();
					Bandwidth.setWindow(window);
				}
				if(start < window.length){
					if(ack.equals("none")){
						Bandwidth.setUnique(((byte[])window[start][0]).length);
						Object req[] = {"window",window[start][0],window[start][1]};
						out.writeObject(req);
						out.flush();
						Object row[] = {"Window Unmatched. window sent to client"};
						dtm.addRow(row);
					}
					if(ack.equals("matched")){
						String hashcode = SHA.ShaSignature((byte[])window[start][0]);
						if(hash.equals(hashcode)){
							Bandwidth.setRedundancy(((byte[])window[start][0]).length);
							Object req[] = {"copy"};
							out.writeObject(req);
							out.flush();
							Object row[] = {"Window Matched. Request sent to copy"};
							dtm.addRow(row);
						}else{
							Object req[] = {"window",window[start][0],window[start][1]};
							out.writeObject(req);
							out.flush();
							Bandwidth.setUnique(((byte[])window[start][0]).length);
							Object row[] = {"Window Unmatched. window sent to client"};
							dtm.addRow(row);
						}
					}
				}else{
					Object req[] = {"over"};
					out.writeObject(req);
					out.flush();
					Object row[] = {"All windows sent to client successfully"};
					dtm.addRow(row);
				}
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
}
public long getWindowSize(File file){
	long length = file.length();
	tot_win=0;
	long size = 0;
	if(length >= 1000){
		size = length/10;
		tot_win = 10;
	}
	if(length < 1000 && length > 500){
		size = length/5;
		tot_win = 5;
	}
	if(length < 500 && length > 1){
		size = length/3;
		tot_win = 3;
	}
	return size;
}
public Object[][] getWindow(long window_size,String name){
	Object row[][] = new Object[tot_win][2];
	try{
		for(int i=0;i<tot_win;i++){
			byte window[] = new byte[(int)window_size];
			random.read(window);
			random.seek(random.getFilePointer());
			row[i][0]=window;
			row[i][1]=i+"_"+name;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return row;
}
}
