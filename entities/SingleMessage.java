package entities;

import java.io.Serializable;

/**
 * The {@code MyMessage} class represents a message that is sent from server to client
 * It helps 
 * */
public class SingleMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private boolean connection = false;
	private boolean authentication = false;
	private boolean requestStatus;
	private int request;
	
	
	public SingleMessage(boolean connection, boolean authentication){
		this.connection = connection;
	}
	
	public boolean getConnection() {
		return this.connection;
	}
	
	
	public void setRequest(int choice) {
		this.request = choice;
	}
	
	public int getRequest() {
		return this.request;
	}
	
	public void setResponse() {
		
	}	
	
	public void setAuthentication(boolean val) {
		this.authentication = val;
	}
	
	public boolean getAuthentication() {
		return this.authentication;
	}
	
	public void setRequestStatus(boolean val) {
		this.requestStatus = val;
	}
	
	public boolean getRequestStatus() {
		return this.requestStatus;
	}
	
	/**
	 * The {@code getListOfCommands} function allows the server to send a client the 
	 * */
	
}