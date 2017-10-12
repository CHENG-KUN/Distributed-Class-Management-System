package drive;

import Servercore.ReplicaManager;

public class Replica1 {
	
	public static void main(String[] args) {
		
		ReplicaManager RM1 = new ReplicaManager(0, 5000);
		
		System.out.println("RM1 start");
		Thread t1 = new Thread(RM1);
		t1.start();
		
		ReplicaManager RM2 = new ReplicaManager(1, 6000);
		System.out.println("RM2 start");
		Thread t2 = new Thread(RM2);
		t2.start();
		
		ReplicaManager RM3 = new ReplicaManager(2, 7000);
		System.out.println("RM3 start");
		Thread t3 = new Thread(RM3);
		t3.start();
		
		
	}

}
