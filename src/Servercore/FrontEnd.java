package Servercore;

import java.io.*;
import java.net.*;
import java.util.*;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import Server.ServerInterface;
import Server.ServerInterfaceHelper;
import Server.ServerInterfacePOA;
import log.Log;

public class FrontEnd extends ServerInterfacePOA{
	
	int id = 1;
	
	int error = 0;
	
	ServerInterface service;
	public NamingContextExt ncRef;
	
	ORB orb;
	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) {

		System.out.println("here CTR in front end");
		String cmd = "CTR," + managerID + ',' + firstName + ',' + lastName + ',' + address + ',' + phone + ',' + specialization + ',' + location;
		
		String receive = sequencer(cmd);
		
		return receive.equals("true") ? true : false;

	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, String courseRegistered,
			String status, String statusDate) {
		
		String cmd = "CSR," + managerID + ',' + firstName + ',' + lastName + ',' + courseRegistered + ',' + status + ',' + statusDate;
		
		String receive = sequencer(cmd);
		
		return receive.equals("true") ? true : false;
	}

	@Override
	public String getRecordCounts(String managerID) {
		
		System.out.println("here getcount in front end");
		String cmd = "GCN," + managerID;
		return sequencer(cmd);
		
	}

	@Override
	public boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {
		
		String cmd = "EDR," + managerID + ',' + recordID + ',' + fieldName + ',' + newValue;

		String receive = sequencer(cmd);
		
		return receive.equals("true") ? true : false;
	}

	@Override
	public boolean transferRecord(String managerID, String recordID, String serverName) {
		
		String cmd = "TFR," + managerID + ',' + recordID + ',' + serverName;

		String receive = sequencer(cmd);

		return receive.equals("true") ? true : false;
	}
	
	private String sequencer(String cmd) {
		
		error++;
		
		System.out.println("here  in sequence");
		String requestServer = cmd.substring(4, 7);
		
		String[] solution = new String[3];
		
		cmd = String.valueOf(id++) + ',' + cmd;
		System.out.println(cmd);
		int portOffset = 0;
		switch(requestServer) {
		case "MTL" : portOffset += 1; break;
		case "LVL" : portOffset += 2; break;
		case "DDO" : portOffset += 3; break;
		}
		
		try {
			
			DatagramSocket UDP = new DatagramSocket(6666);
			InetAddress host = InetAddress.getByName("localhost");

			DatagramPacket request;
			DatagramPacket reply;
			
			byte[] command;
			byte[] buffer;
			
			for(int i = 0; i < 3; i++) {
				
				System.out.print("udp to s ");
				System.out.println(i);
				command = cmd.getBytes();
				request = new DatagramPacket(command, command.length, host, portOffset + i * 1000 + 5000);
				UDP.send(request);
				buffer = new byte[100];
				reply = new DatagramPacket(buffer, buffer.length);
				UDP.receive(reply);
				
				solution[i] = new String(buffer, 0, reply.getLength());
				
			}
			
			UDP.close();
	
		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (UnknownHostException e) {
			System.out.println("Can not find IP address");
		} catch (IOException e) {
			System.out.println("Internet error.");
		}

		if(error % 3 == 0) {
			solution[2] = "false";
		}
		
		if(solution[0].equals(solution[1])) {	
			
			if(solution[1].equals(solution[2]))
				return solution[0];
			else {
				warn(2, portOffset - 1, cmd);
				return solution[0];
			}		
			
		} else if (solution[0].equals(solution[2])) {			
			warn(1, portOffset - 1, cmd);
			return solution[0];		
		} else {
			warn(0, portOffset - 1, cmd);
			return solution[1];
		}

	}
	
	private void warn(int replica, int type, String cmd) {

		try {
			
			DatagramSocket UDP = new DatagramSocket();
			InetAddress host = InetAddress.getByName("localhost");

			DatagramPacket request;
			//DatagramPacket reply;
			
			byte[] warnMessage;
			//byte[] buffer;
			
			warnMessage = ("" + type + ',' + cmd).getBytes();
			request = new DatagramPacket(warnMessage, warnMessage.length, host, replica * 1000 + 5000);
			UDP.send(request);
			//buffer = new byte[100];
			//reply = new DatagramPacket(buffer, buffer.length);
			//UDP.receive(reply);
			
			UDP.close();
	
		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (UnknownHostException e) {
			System.out.println("Can not find IP address");
		} catch (IOException e) {
			System.out.println("Internet error.");
		}
		
	}

	public static void main(String[] args) {
		
		try{
			
			FrontEnd FE = new FrontEnd();
					
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "1050");    
		    props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1"); 
		    ORB orb = ORB.init(args, props);
		    POA rootpoa =(POA)orb.resolve_initial_references("RootPOA");
			rootpoa.the_POAManager().activate();
			FE.setORB(orb);
			
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(FE);
			ServerInterface href = ServerInterfaceHelper.narrow(ref);
			Log.write("Server start", "DDOServer.txt");
			
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			FE.ncRef = NamingContextExtHelper.narrow(objRef);
			String name = "FE";
			NameComponent path[] = FE.ncRef.to_name(name);
			FE.ncRef.rebind(path, href);
			System.out.println("Front End ready and waiting ...");
			orb.run();
	
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
