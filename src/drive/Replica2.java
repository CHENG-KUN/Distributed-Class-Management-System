package drive;

import Servercore.ReplicaManager;

public class Replica2 {
	
	public static void main(String[] args) {
		
		ReplicaManager RM = new ReplicaManager(1, 6000);
		
		Thread t = new Thread(RM);
		t.start();
		
	}

}
