package Servercore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ReplicaManager implements Runnable {
	
	int replicaID;
	int basePort;
	int[] serverStatus = new int[3];
	
	Server MTL;
	Server LVL;
	Server DDO;

	ArrayList<String> lostCmd = new ArrayList<String>();
	
	public ReplicaManager(int replicaID, int basePort) {
	
		this.replicaID = replicaID;
		this.basePort = basePort;
		
		serverStatus[0] = serverStatus[1] = serverStatus[2] = 0;
		
		MTL = new Server("MTL", basePort);
		LVL = new Server("LVL", basePort);
		DDO = new Server("DDO", basePort);
		
		Thread t1 = new Thread(MTL);
		t1.start();
		Thread t2 = new Thread(LVL);
		t2.start();
		Thread t3 = new Thread(DDO);
		t3.start();
			
	}
	
	@Override
	public void run() {
		
		DatagramSocket UDP = null;
		
		try {
			
			UDP = new DatagramSocket(replicaID * 1000 + 5000);
			
			byte[] receiveBuffer = new byte[10];
			DatagramPacket warnMessage = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			
			while(true) {
				
				UDP.receive(warnMessage);	
				String warn = new String(receiveBuffer, 0, warnMessage.getLength());
				
				update(warn);
				
			}

		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (IOException e) {
			System.out.println("Internet error.");
		} finally {
			UDP.close();
		}
		
	}
	
	private void update(String warnMessage) {
		
		String[] warn = warnMessage.split(",");
		int errorType = Integer.parseInt(warn[0]);
		++serverStatus[errorType];
		
		lostCmd.add(warn[1]);
		
		if(serverStatus[errorType] == 1) {

			File file = new File("temp.ser");
			try {
				
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				
				switch(errorType) {
				case 0 : out.writeObject(MTL); break;
				case 1 : out.writeObject(LVL); break;
				case 2 : out.writeObject(DDO); break;	
				}
				
				out.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("file not found.");
			} catch (IOException e) {
				System.out.println("io error.");
			}

		} else if(serverStatus[errorType] == 3) {

			try {
				
				FileInputStream fis = new FileInputStream("temp.ser");
				ObjectInputStream in = new ObjectInputStream(fis);
				
				switch(errorType) {
				case 0 : MTL = (Server)in.readObject(); break;
				case 1 : LVL = (Server)in.readObject(); break;
				case 2 : DDO = (Server)in.readObject(); break;	
				}
				
				in.close();
				
				while(!lostCmd.isEmpty()) {
					
					String s = lostCmd.get(0);
					
					switch(errorType) {
					case 0 : MTL.execute(s); break;
					case 1 : LVL.execute(s); break;
					case 2 : DDO.execute(s); break;	
					}
				
					lostCmd.remove(0);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("file not found.");
			} catch (IOException e) {
				System.out.println("io error.");
			} catch (ClassNotFoundException e) {
				System.out.println("class not found.");
			}
			
		}
		
	}

}
