package Servercore;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import log.Log;

public class Server implements Serializable, Runnable {

	private static final long serialVersionUID = 1L;
	
	HashMap <String, String> recordA = new HashMap <String, String>();
	HashMap <String, String> recordB = new HashMap <String, String>();
	HashMap <String, String> recordC = new HashMap <String, String>();
	HashMap <String, String> recordD = new HashMap <String, String>();
	HashMap <String, String> recordE = new HashMap <String, String>();
	HashMap <String, String> recordF = new HashMap <String, String>();
	HashMap <String, String> recordG = new HashMap <String, String>();
	HashMap <String, String> recordH = new HashMap <String, String>();
	HashMap <String, String> recordI = new HashMap <String, String>();
	HashMap <String, String> recordJ = new HashMap <String, String>();
	HashMap <String, String> recordK = new HashMap <String, String>();
	HashMap <String, String> recordL = new HashMap <String, String>();
	HashMap <String, String> recordM = new HashMap <String, String>();
	HashMap <String, String> recordN = new HashMap <String, String>();
	HashMap <String, String> recordO = new HashMap <String, String>();
	HashMap <String, String> recordP = new HashMap <String, String>();
	HashMap <String, String> recordQ = new HashMap <String, String>();
	HashMap <String, String> recordR = new HashMap <String, String>();
	HashMap <String, String> recordS = new HashMap <String, String>();
	HashMap <String, String> recordT = new HashMap <String, String>();
	HashMap <String, String> recordU = new HashMap <String, String>();
	HashMap <String, String> recordV = new HashMap <String, String>();
	HashMap <String, String> recordW = new HashMap <String, String>();
	HashMap <String, String> recordX = new HashMap <String, String>();
	HashMap <String, String> recordY = new HashMap <String, String>();
	HashMap <String, String> recordZ = new HashMap <String, String>();
	
	HashMap <Character, HashMap <String, String>> Records = new HashMap <Character, HashMap <String, String>>();
	
	HashMap <Integer, String> CommandList = new HashMap <Integer, String>();
	int currentCmd = 1;
	
	String serverType;
	String serverFilePath = "LogServer/";
	int localPort;  
	
	int countTotal = 0;
	int countOfTeacher = 0;
	int countOfStudent = 0;
	
	public Server(String serverType, int localPort) {
		
		Records.put('A', recordA);
		Records.put('B', recordB);
		Records.put('C', recordC);
		Records.put('D', recordD);
		Records.put('E', recordE);
		Records.put('F', recordF);
		Records.put('G', recordG);
		Records.put('H', recordH);
		Records.put('I', recordI);
		Records.put('J', recordJ);
		Records.put('K', recordK);
		Records.put('L', recordL);
		Records.put('M', recordM);
		Records.put('N', recordN);
		Records.put('O', recordO);
		Records.put('P', recordP);
		Records.put('Q', recordQ);
		Records.put('R', recordR);
		Records.put('S', recordS);
		Records.put('T', recordT);
		Records.put('U', recordU);
		Records.put('V', recordV);
		Records.put('W', recordW);
		Records.put('X', recordX);
		Records.put('Y', recordY);
		Records.put('Z', recordZ);
		
		this.serverType = serverType;
		serverFilePath += localPort + "/" + serverType + "Server.txt";
		this.localPort = localPort;

		System.out.println(serverFilePath);
	}
	
	public String execute(String s) {
		
		String[] cmd = s.split(",");
		String reply = "";
		
		System.out.println("here in execute");
		switch(cmd[0]) {
		case "CTR" : 
			reply = String.valueOf(createTRecord(cmd[1], cmd[2], cmd[3], cmd[4], cmd[5], cmd[6], cmd[7]));
			break;
		case "CSR" : 
			reply = String.valueOf(createSRecord(cmd[1], cmd[2], cmd[3], cmd[4], cmd[5], cmd[6])); 
			break;
		case "GCN" : 
			reply = String.valueOf(getRecordCounts(cmd[1])); 
			break;
		case "EDR" :
			reply = String.valueOf(editRecord(cmd[1], cmd[2], cmd[3], cmd[4])); 
			break;
		case "TFR" : 
			reply = String.valueOf(transferRecord(cmd[1], cmd[2], cmd[3])); 
			break;
		}
	
		return reply;
		
	}
	
	public void returnFE(String s) {
		
		try {
			
			DatagramSocket UDP = new DatagramSocket();
			InetAddress host = InetAddress.getByName("localhost");
			DatagramPacket request;
		    //DatagramPacket reply;
			
			byte[] message = s.getBytes();
			//byte[] buffer;
			
			request = new DatagramPacket(message, message.length, host, 6666);
			UDP.send(request);
			//buffer = new byte[50];
			//reply = new DatagramPacket(buffer, buffer.length);
			//UDP.receive(reply);
			//String inform = new String(buffer, 0, reply.getLength());
			
	
		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (UnknownHostException e) {
			System.out.println("Can not find IP address");
		} catch (IOException e) {
			System.out.println("Internet error.");
		}
		
	}
	
	@Override
	public void run() {
		
		System.out.println("server thread start");
		try {
			
			DatagramSocket UDP = null;
			switch(serverType) {
			case "MTL": UDP = new DatagramSocket(localPort + 1); break;
			case "LVL": UDP = new DatagramSocket(localPort + 2); break;
			case "DDO": UDP = new DatagramSocket(localPort + 3); break;
			}
			
			byte[] receiveBuffer = new byte[100];
			DatagramPacket request = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			
			while(true) {
				
				UDP.receive(request);	
				String cmd = new String(receiveBuffer, 0, request.getLength());
				
				currentCmd = Integer.parseInt(cmd.substring(0, 1));
				
				if(cmd.equals("count")) {	
					
					String countOfServer = String.valueOf(countTotal);
					byte[] sendBuffer = new byte[50];
					sendBuffer = countOfServer.getBytes();
					DatagramPacket reply = new DatagramPacket(sendBuffer, sendBuffer.length, 
							request.getAddress(), request.getPort());
					UDP.send(reply);
							
				} else if(cmd.substring(0, 8).equals("transfer")) {		
					
					String[] s = cmd.substring(9).split(",");
					if(s[0].substring(0, 2).equals("TR")) {
						createTRecord(s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
					} else {
						createSRecord(s[1], s[2], s[3], s[4], s[5], s[6]);
					}	
					byte[] sendBuffer = new byte[50];
					sendBuffer = "Record transfer success.".getBytes();
					DatagramPacket success = new DatagramPacket(sendBuffer, sendBuffer.length, 
							request.getAddress(), request.getPort());
					UDP.send(success);
					
				} else {
					
					System.out.println("here in server receive thread");
					System.out.println(cmd);
					CommandList.put(Integer.parseInt(cmd.substring(0, 1)), cmd.substring(2));	
					String s = CommandList.get(currentCmd++);
					System.out.println(s);
					returnFE(execute(s));
					
				}
				
			}

		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (IOException e) {
			System.out.println("Internet error.");
		} 

		
	}
	

	public synchronized boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		
		String teacherRecord = firstName + ',' + lastName + ',' + address + ',' + phone + ',' + specialization + ',' + location;
		String RecordID = "TR" + String.valueOf(10001 + countOfTeacher);
		
		switch(lastName.charAt(0)) {
		
		case 'A':
			recordA.put(RecordID, teacherRecord);
			break;
		case 'B':
			recordB.put(RecordID, teacherRecord);
			break;
		case 'C':
			recordC.put(RecordID, teacherRecord);
			break;
		case 'D':
			recordD.put(RecordID, teacherRecord);
			break;
		case 'E':
			recordE.put(RecordID, teacherRecord);
			break;
		case 'F':
			recordF.put(RecordID, teacherRecord);
			break;
		case 'G':
			recordG.put(RecordID, teacherRecord);
			break;
		case 'H':
			recordH.put(RecordID, teacherRecord);
			break;
		case 'I':
			recordI.put(RecordID, teacherRecord);
			break;
		case 'J':
			recordJ.put(RecordID, teacherRecord);
			break;
		case 'K':
			recordK.put(RecordID, teacherRecord);
			break;
		case 'L':
			recordL.put(RecordID, teacherRecord);
			break;
		case 'M':
			recordM.put(RecordID, teacherRecord);
			break;
		case 'N':
			recordN.put(RecordID, teacherRecord);
			break;
		case 'O':
			recordO.put(RecordID, teacherRecord);
			break;
		case 'P':
			recordP.put(RecordID, teacherRecord);
			break;
		case 'Q':
			recordQ.put(RecordID, teacherRecord);
			break;
		case 'R':
			recordR.put(RecordID, teacherRecord);
			break;
		case 'S':
			recordS.put(RecordID, teacherRecord);
			break;
		case 'T':
			recordT.put(RecordID, teacherRecord);
			break;
		case 'U':
			recordU.put(RecordID, teacherRecord);
			break;
		case 'V':
			recordV.put(RecordID, teacherRecord);
			break;
		case 'W':
			recordW.put(RecordID, teacherRecord);
			break;
		case 'X':
			recordX.put(RecordID, teacherRecord);
			break;
		case 'Y':
			recordY.put(RecordID, teacherRecord);
			break;
		case 'Z':
			recordZ.put(RecordID, teacherRecord);
			break;
		}
		
		countTotal++;
		countOfTeacher++;
		
		Log.write(managerID + "\t\tCreate Teacher Record :   " + RecordID + "\n\r" + teacherRecord, serverFilePath);
		
		return true;
	}

	public synchronized boolean createSRecord(String managerID, String firstName, String lastName, String courseRegistered,
			String status, String statusDate) {
		
		String studentRecord = firstName + ',' + lastName + ',' + courseRegistered + ',' + status + ',' + statusDate;
		String RecordID = "SR" + String.valueOf(10001 + countOfStudent);	
		
		switch(lastName.charAt(0)) {
		
		case 'A':
			recordA.put(RecordID, studentRecord);
			break;
		case 'B':
			recordB.put(RecordID, studentRecord);
			break;
		case 'C':
			recordC.put(RecordID, studentRecord);
			break;
		case 'D':
			recordD.put(RecordID, studentRecord);
			break;
		case 'E':
			recordE.put(RecordID, studentRecord);
			break;
		case 'F':
			recordF.put(RecordID, studentRecord);
			break;
		case 'G':
			recordG.put(RecordID, studentRecord);
			break;
		case 'H':
			recordH.put(RecordID, studentRecord);
			break;
		case 'I':
			recordI.put(RecordID, studentRecord);
			break;
		case 'J':
			recordJ.put(RecordID, studentRecord);
			break;
		case 'K':
			recordK.put(RecordID, studentRecord);
			break;
		case 'L':
			recordL.put(RecordID, studentRecord);
			break;
		case 'M':
			recordM.put(RecordID, studentRecord);
			break;
		case 'N':
			recordN.put(RecordID, studentRecord);
			break;
		case 'O':
			recordO.put(RecordID, studentRecord);
			break;
		case 'P':
			recordP.put(RecordID, studentRecord);
			break;
		case 'Q':
			recordQ.put(RecordID, studentRecord);
			break;
		case 'R':
			recordR.put(RecordID, studentRecord);
			break;
		case 'S':
			recordS.put(RecordID, studentRecord);
			break;
		case 'T':
			recordT.put(RecordID, studentRecord);
			break;
		case 'U':
			recordU.put(RecordID, studentRecord);
			break;
		case 'V':
			recordV.put(RecordID, studentRecord);
			break;
		case 'W':
			recordW.put(RecordID, studentRecord);
			break;
		case 'X':
			recordX.put(RecordID, studentRecord);
			break;
		case 'Y':
			recordY.put(RecordID, studentRecord);
			break;
		case 'Z':
			recordZ.put(RecordID, studentRecord);
			break;
		}
		
		countTotal++;
		countOfStudent++;

		Log.write(managerID + "\t\tCreate Student Record :   " + RecordID + "\n\r" + studentRecord, serverFilePath);
		
		return true;
	}

	public synchronized String getRecordCounts(String managerID) {
		
		String count;
		
		final int MTLPORT = localPort + 1;
		final int LVLPORT = localPort + 2;
		final int DDOPORT = localPort + 3;
		
		System.out.println("main ");
		
		try {
			
			DatagramSocket UDP = new DatagramSocket();
			InetAddress host = InetAddress.getByName("localhost");
			DatagramPacket request;
			DatagramPacket reply;
			
			byte[] message = "count".getBytes();
			byte[] buffer;
			
			String countOfMTL = "";
			String countOfLVL = "";
			String countOfDDO = "";
			
			if(serverType != "MTL") {
				request = new DatagramPacket(message, message.length, host, MTLPORT);
				UDP.send(request);
				buffer = new byte[100];
				reply = new DatagramPacket(buffer, buffer.length);
				System.out.println("before");
				UDP.receive(reply);
				System.out.println("end");
				countOfMTL = "MTL: " + new String(reply.getData());
			} else 
				countOfMTL = "MTL: " + String.valueOf(countTotal);
			
			if(serverType != "LVL") {
				request = new DatagramPacket(message, message.length, host, LVLPORT);
				UDP.send(request);
				buffer = new byte[100];
				reply = new DatagramPacket(buffer, buffer.length);
				System.out.println("before");
				UDP.receive(reply);
				System.out.println("end");
				countOfLVL = "LVL: " + new String(reply.getData());
			} else 
				countOfLVL = "LVL: " + String.valueOf(countTotal);

			if(serverType != "DDO") {
				request = new DatagramPacket(message, message.length, host, DDOPORT);
				UDP.send(request);
				buffer = new byte[100];
				reply = new DatagramPacket(buffer, buffer.length);
				System.out.println("before");
				UDP.receive(reply);
				System.out.println("end");
				countOfDDO = "DDO: " + new String(reply.getData());
			} else 
				countOfDDO = "DDO: " + String.valueOf(countTotal);

			
			
			
			count = countOfMTL.trim() + "\n" + countOfLVL.trim() + "\n" + countOfDDO.trim();
			
			Log.write(managerID + "\t\tCheck Records Count\n\r" + count, serverFilePath);
			
			return count;
	
		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (UnknownHostException e) {
			System.out.println("Can not find IP address");
		} catch (IOException e) {
			System.out.println("Internet error.");
		}
		
		return null;
	}


	public synchronized boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {
		
		for(char i = 'A'; i <= 'Z'; i++) {

			String record = Records.get(i).get(recordID);
			
			if(record != null) {
				
				String[] recordPart = record.split(",");
				
				switch(fieldName) {
				
				case "address": recordPart[2] = newValue; break;
				case "phone": recordPart[3] = newValue; break;
				case "location": recordPart[5] = newValue; break;
				case "courseRegistered": recordPart[2] = newValue; break;
				case "status": recordPart[3] = newValue; break;
				case "statusDate": recordPart[4] = newValue; break;
				default: 
					Log.write(managerID + "\t\tfail to edit---FieldName cannot match.", serverFilePath);
					return false;
				
				}
				
				String newRecord = "";	
				for(String s : recordPart)
					newRecord += s + ",";
				newRecord = newRecord.substring(0, newRecord.length() - 1);
				
				Records.get(i).replace(recordID, newRecord);
				
				break;
				
			} else {
				if(i < 'Z')
					continue;
				else {
					Log.write(managerID + "\t\tfail to edit---Invalid RecordID.", serverFilePath);
					return false;
				}
			}	
		}
		
		Log.write(managerID + "\t\tRecord edit successful : " + recordID + "\n\r" + fieldName + " : " + newValue, serverFilePath);
		return true;
	}

	public synchronized boolean transferRecord(String managerID, String recordID, String serverName) {
		
		String transfer = "transfer," + recordID + ",";
		
		for(char i = 'A'; i <= 'Z'; i++) {

			String record = Records.get(i).get(recordID);
			
			if(record != null) {
				
				transfer += record;
				Records.get(i).remove(recordID);
				break;
				
			} else {
				if(i < 'Z')
					continue;
				else {
					Log.write(managerID + "\t\tfail to transfer---Invalid RecordID", serverFilePath);
					return false;
				}
			}	
		}
		
		int port = localPort;
		switch(serverName) {
		case "MTL" : port = localPort + 1; break;
		case "LVL" : port = localPort + 2; break;
		case "DDO" : port = localPort + 3; break;
		}
		
		try {
			
			DatagramSocket UDP = new DatagramSocket();
			InetAddress host = InetAddress.getByName("localhost");
			DatagramPacket request;
			DatagramPacket reply;
			
			byte[] message = transfer.getBytes();
			byte[] buffer;
			
			request = new DatagramPacket(message, message.length, host, port);
			UDP.send(request);
			buffer = new byte[50];
			reply = new DatagramPacket(buffer, buffer.length);
			UDP.receive(reply);
			String inform = new String(buffer, 0, reply.getLength());
			
			if(inform.equals("Record transfer success.")) {
				Log.write(managerID + "\t\tRecord transfer successful : " + recordID + " to " + serverName, serverFilePath);
				return true;
			}
	
		} catch (SocketException e) {
			System.out.println("This port is already used.");
		} catch (UnknownHostException e) {
			System.out.println("Can not find IP address");
		} catch (IOException e) {
			System.out.println("Internet error.");
		}
			
		return false;
	}

}
