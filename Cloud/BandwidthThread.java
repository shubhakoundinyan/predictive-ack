package pack;
public class BandwidthThread extends Thread
{
	Bandwidth server;
public BandwidthThread(Bandwidth server){
	this.server=server;
	start();
}
public void run(){
	server.start();
}
}
