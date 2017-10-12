package client;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import org.omg.CosNaming.NamingContextExt;

import Server.*;
import log.Log;

public class ManagerClient {
	
	static ArrayList<String> managerList = new ArrayList<String>();

	public NamingContextExt ncRef;
	Server.ServerInterface service;
	
	String managerID;
	String managerFilePath = "LogManager/";

	
	/**
	 * Constructor based on the managerID of manager.
	 * @param managerID
	 * The list that store the ID of manager clients
	 */
	public ManagerClient(String managerID)  {
		
		
		if(managerList.contains(managerID)) {
			System.out.println(managerID + "Welcome back to manager system.");
			Log.write(managerID + " Log in.", managerFilePath);
		} else {
			managerList.add(managerID);
			System.out.println(managerID + "New user");
			//Log.write(managerID + " Log in.", managerFilePath);
			
		}	
		this.managerID = managerID;
		managerFilePath = managerFilePath + managerID + ".txt";
	}
	
	/**
	 * the main menu of operations.
	 */
	public void menu() {

		System.out.println("The Manager System\n");
		System.out.println("1.Create Teacher Record");
		System.out.println("2.Create Student Record");
		System.out.println("3.Count of Record");
		System.out.println("4.Edit exist Record");
		System.out.println("5.transfer Record");
		
		Scanner in = new Scanner(System.in);
		String a = "";
		a = in.nextLine();

		switch(a) {
		case "1" : createTeacherRecord();break;
		case "2" : createStudentRecord();break;
		case "3" : getCount();break;
		case "4" : recordEdit();break;
		case "5" : transferRecord();break;
		default: System.out.println("Invalid input.\n");
		}
	}
	
	public void transferRecord() {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter: RecordID");
		String RecordID = in.next();
		System.out.println("Please enter:  Destination server name");
		String serverName = in.next();
		if(!serverName.equals("DDO")&&!serverName.equals("LVL")&&!serverName.equals("MTL"))
		{
			System.out.println("Wrong location, please select again!");
			return;
		}

		if(service.transferRecord(managerID, RecordID, serverName)) {
			System.out.println("transfer success.");
			Log.write(managerID + "\t\ttransfer success", managerFilePath);
		} else {
			System.out.println("transfer failed.");
			Log.write(managerID + "\t\ttransfer fail", managerFilePath);
		}
	}
	
	/**
	 * bind the port of the server via Corba
	 */
	public boolean connect() throws Exception {
		
		System.out.println(managerID + "Connecting...");

		service = ServerInterfaceHelper.narrow(ncRef.resolve_str("FE"));
		Log.write("Connect to server successful", managerFilePath);
		return true;	
	}
	

	/**
	 * show the total count of each server.
	 */
	public void getCount(){	
		System.out.println(service.getRecordCounts(managerID));
	}
	
	/**
	 * create teacher record by call the server's createTRecord method via RMI
	 */
	public void createTeacherRecord() {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter: firstName");
		String firstName = in.next();
		System.out.println("Please enter: lastName");
		String lastName = in.next();
		System.out.println("Please enter: address");
		String address = in.next();
		System.out.println("Please enter: phone");
		String phone = in.next();
		System.out.println("Please enter: specialization");
		String specialization = in.next();
		System.out.println("Please enter: location");
		String location = in.next();
		if(Character.isLowerCase(firstName.charAt(0))) {
			System.out.println("Creatation failed.");
			System.out.println("invalid firstName, please input again!");
			return;
		};
		if(Character.isLowerCase(lastName.charAt(0))) {
			System.out.println("Creatation failed.");
			System.out.println("invalid lastName, please input again!");
			return;
		};
		if(!phone.matches("[0-9]*"))
		{
			System.out.println("Creatation failed.");
			System.out.println("invalid phone number, please input again!");
			return;
		}
		if(!location.equals("DDO")&&!location.equals("LVL")&&!location.equals("MTL"))
		{	System.out.println("Creatation failed.");
			System.out.println("Wrong location, please select again!");
			return;
		}

		Log.write(managerID + "\t\tCreate Teacher Record :   " + "\n" + firstName 
				+ "   " + lastName + "   " + address + "   " + phone + "   " + specialization 
				+ "   " + location, managerFilePath);
		
		if(service.createTRecord(managerID, firstName, lastName, address, phone, 
				specialization, location)) {
			System.out.println("Create success.");
			Log.write(managerID + "\t\tCreate Teacher Record successful", managerFilePath);
		} else {
			System.out.println("Create fail.");
			Log.write(managerID + "\t\tCreate Teacher Record fail", managerFilePath);
		}
			
	}
	
	/**
	 * create student record by call the server's createSRecord method via RMI
	 */
	public void createStudentRecord() {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter: firstName");
		String firstName = in.next();
		System.out.println("Please enter: lastName");
		String lastName = in.next();
		System.out.println("Please enter: courseRegistered");
		String courseRegistered = in.next();
		System.out.println("Please enter: status");
		String status = in.next();
		System.out.println("Please enter: statusDate");
		String statusDate = in.next();
		
		if(Character.isLowerCase(firstName.charAt(0))) {
			System.out.println("Creatation failed.");
			System.out.println("invalid firstName, please input again!");
			return;
		};
		if(Character.isLowerCase(lastName.charAt(0))) {
			System.out.println("Creatation failed.");
			System.out.println("invalid lastName, please input again!");
			return;
		};

		Log.write(managerID + "\t\tCreate Student Record :   " + "\n" + firstName 
				+ "   " + lastName + "   " + courseRegistered + "   " + status + "   " 
				+ statusDate , managerFilePath);
		
		if(service.createSRecord(managerID, firstName, lastName, courseRegistered, 
				status, statusDate)) {
			System.out.println("Create success.");
			Log.write(managerID + "\t\tCreate Student Record successful", managerFilePath);
		} else {
			System.out.println("Create fail.");
			Log.write(managerID + "\t\tCreate Student Record fail", managerFilePath);
		}	
	}
	
	/**
	 * edit record by call the server's editRecord method via RMI
	 */
	public void recordEdit() {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter: recordID");
		String recordID = in.next();
		System.out.println("Please enter: fieldName");
		String fieldName = in.next();
		System.out.println("Please enter: newValue");
		String newValue = in.next();	
		if(fieldName.equals("location")&&!newValue.equals("DDO")&&!newValue.equals("LVL")&&!newValue.equals("MTL"))
		{
			System.out.println("Wrong location!!");
			return;
		}

		Log.write(managerID + "---Record edit---" + recordID + " " + fieldName 
				+ " " + newValue , managerFilePath);

		if(service.editRecord(managerID, recordID, fieldName, newValue)) {
			System.out.println("Edit success.");
			Log.write(managerID + "---Record edit success---" + recordID + "\n" + fieldName + 
					" : " + newValue , managerFilePath);
		} else {
			System.out.println("Edit fail.");
			Log.write(managerID + "---Record edit fail---" + recordID + "\n" + fieldName + 
					" : " + newValue , managerFilePath);
		}	
	}
	
}
