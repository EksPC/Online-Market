package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import entities.CredentialsCouple;
import entities.Product;
import entities.SingleMessage;
import javafx.scene.Scene;

public class MarketClient {
	
	private static final int PORTNO = 5001;
	private static final String ADDRESS = "localhost";
	
	static private boolean connectionState = false;
	static private boolean authenticationState = false;
	static private Socket server = null;
	static private ObjectOutputStream socketOutput = null;
	static private ObjectInputStream socketInput = null;
	static private CredentialsCouple credentials = null;
	static private List<Product> products = null;
	
	private static Object response = null;
	private static Scene marketScene = null;
	
	private static final Logger logger = Logger.getLogger("MarketClientLogger");
	private static FileHandler logFile = null;
	
	public boolean isConnected() {
		return connectionState;
	}
	
	public boolean isAuthenticated() {
		return authenticationState;
	}
	

	
	public MarketClient() {
		setLogger();
		setConnection();
		
	}
	
	
	private static boolean setLogger() {
		
		System.out.println("Creatig logger");
		LogManager.getLogManager().reset();
		logger.setLevel(Level.ALL);
		
		try {
			logFile = new FileHandler("serverLogger.log");
			logFile.setLevel(Level.FINE);
			logger.addHandler(logFile);
			logFile.setFormatter(new SimpleFormatter());
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Logger not initialized");
			return false;
		}
		return true;
	}
	
	
	/**
	 * This method gets the credentials from input {@code market.view}
	 * and send them to server by a {@code CredentialsCouple} object.
	 * */
	public int checkCredentials(String usr,String psw) {
		//CREDENTIALS INPUT
		try {
			credentials = new CredentialsCouple(usr, psw);
			SingleMessage inputObject = (SingleMessage) response;
			socketOutput.writeObject(credentials);
			socketOutput.flush();
			
			//CREDENTIALS VERIFICATION
			inputObject = (SingleMessage) socketInput.readObject();
			
			//Checking server authentication
			if(inputObject.getAuthentication() == true) {
				
				System.out.println("Authenticated, welcome back " + usr);
				authenticationState = true;
				return 1;
			} else {
				
				System.out.println("Authentication failed, try again.");
				return 0;
			}
		} catch (IOException ioe) {
			System.out.println("Credentials check error - " + ioe.getLocalizedMessage());
			return -1;
		} catch(ClassNotFoundException cnf) {
			System.out.println("Credentials check error- " + cnf.getLocalizedMessage());
			return -2;
		}
	}
	
	public static void startCommunication() {
		//Communication with server
	}
	
	
	/**
	 * This method requests a connection from server and returns the status 
	 * of the connection: 1 - connection accepted, 0 - connection denied, -1 IOException,
	 * -2 - ClassNotFoundException.
	 * 
	 * @return status of connection
	 * */
	private static int setConnection() {
		//TODO implement a logger
		try {
			//connectionState METHODS
			server = new Socket(ADDRESS,PORTNO);
			socketOutput = new ObjectOutputStream(server.getOutputStream());
			socketInput = new ObjectInputStream(server.getInputStream());
			
			//Reading connection status from server
			response = socketInput.readObject();
			
			//Check state of connection
			if((response == null) || !(response instanceof SingleMessage)){
				
				System.out.println("Connection failed");
				return 0;
			}
			
			System.out.println("\nConnection enstablished.\n");
			connectionState = true;
			return 1;
			
		} catch (IOException ioe) {
			System.out.println("Connection setting error - " + ioe.getLocalizedMessage());
			return -1;
		
		} catch(ClassNotFoundException cnf) {
			System.out.println("Connection setting error - " + cnf.getLocalizedMessage());
			return -2;
		} 
		
	}
	
	
	
//	public void start(Stage primaryStage) {
//		
//		
//		if(setConnection() <= 0) {
//			System.out.println("Connection failed;");
//		};
//		
//		try {
//			
//			
////			StackPane root = loginScene.getRoot();
//			
//			
//			//Scene
//			Scene scene = new Scene(root,1200,1200);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			//Stage
//			primaryStage.setScene(scene);
//			primaryStage.show();
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		
	}
	
}
