package drive;

import Servercore.ReplicaManager;

public class Replica3 {
	
	public static void main(String[] args) {
		
		ReplicaManager RM = new ReplicaManager(2, 7000);
		
		Thread t = new Thread(RM);
		t.start();
		
	}

}
