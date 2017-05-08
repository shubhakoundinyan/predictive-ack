package pack;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class DBCon{
    private static Connection con;
public static Connection getCon()throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost/pack","root","root");
     return con;
}
public static String register(String[] input)throws Exception{
    String msg="no";
    boolean flag=false;
    boolean flag1=false;
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select uname from register where uname='"+input[0]+"'");
    if(rs.next()){
        flag=true;
        msg = "Username already exist";
    }
    stmt=con.createStatement();
    rs=stmt.executeQuery("select pass from register where pass='"+input[1]+"'");
    if(rs.next() && !flag){
        flag1=true;
        msg = "Password already exist";
    }
    if(!flag && !flag1){
    PreparedStatement stat=con.prepareStatement("insert into register values(?,?,?)");
    stat.setString(1,input[0]);
    stat.setString(2,input[1]);
    stat.setString(3,input[2]);
    int i=stat.executeUpdate();
    if(i > 0)
        msg = "Registration completed";
    }
    return msg;
}
public static String login(String input[])throws Exception{
    String msg="invalid login";
    con = getCon();
    System.out.println(input[0]);
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select uname,pass from register where uname='"+input[0]+"' && pass='"+input[1]+"'");
    if(rs.next()){
        msg = "valid login";
    }
    System.out.println(msg);
    return msg;
}
}
