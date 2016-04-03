package controlInterface;

public class RemoteControlThread extends Thread{
	RemoteInterface client;
	
	public RemoteControlThread (RemoteInterface client){
		this.client = client;
	}
	
	public void run() {
		client.listen();
		
		client.disconnect();
	}
}
