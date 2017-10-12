package Server;


/**
* Server/ServerInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/java/workspace/0805/src/Server/Server.idl
* Sunday, August 6, 2017 5:16:50 o'clock PM EDT
*/

public interface ServerInterfaceOperations 
{
  boolean createTRecord (String managerID, String firstName, String lastName, String address, String phone, String specialization, String location);
  boolean createSRecord (String managerID, String firstName, String lastName, String courseRegisted, String status, String statusDate);
  String getRecordCounts (String managerID);
  boolean editRecord (String managerID, String recordID, String fieldName, String newValue);
  boolean transferRecord (String managerID, String recordID, String serverName);
} // interface ServerInterfaceOperations
