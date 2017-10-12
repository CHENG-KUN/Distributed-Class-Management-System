package client;

import java.util.Properties;

import Server.*;

import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExtHelper;

import client.ManagerClient;

public class DriveManager {
	
	public static void main(String[] args) {
	    try {
	    	
	    	System.out.println("Please enter managerID : ");
			Scanner input = new Scanner(System.in);
			ManagerClient ma = new ManagerClient(input.nextLine()) ;
			
	    	Properties props = new Properties();
		    props.put("org.omg.CORBA.ORBInitialPort", "1050");    
		    props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1"); 
		    ORB orb = ORB.init(args, props);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			ma.ncRef = NamingContextExtHelper.narrow(objRef);
			ma.connect();
			while(true) {
				ma.menu();
			}	
			
		} catch (InvalidName e) {
			
		} catch (Exception e) {
			
		}
	

	}       

}
