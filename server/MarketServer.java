package server;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import entities.CredentialsCouple;
import entities.Product;
import entities.SingleMessage;

import java.net.*;
import java.util.*;



/*
 * The {@code MyServer} gives all the services to the client {@see MyClient}
 * Every time a server object is created:
 * 1. Accepts a connection from a client
 * 2. Asks client for authentication (username - password)
 * 3. Asks client for action to take
 * 		> List of products
 * 		> Product bought from client {@see Product}
 * 		> closing message
 * 
 * @author Francesco Carboni
 * 
 * */

public class MarketServer {
	
	private static List<CredentialsCouple> credentials = new ArrayList<>();
	private static List<Product> products = new ArrayList<>();
	private static boolean connectionState = false;
	private static boolean authenticationState = false;
	private static ServerSocket server = null;
	private static Socket client = null;
	private static ObjectOutputStream socketOutput = null;
	private static ObjectInputStream socketInput = null;
	private static CredentialsCouple userCredentials = null;
	
	private static final int PORTNO = 5001;
	private static final String credentialsFileName = "credentials.txt";
	private static final String storageFileName = "storage.txt";

	private static final Logger logger = Logger.getLogger("ServerMarketLogger");
	private static FileHandler logFile = null;

	public MarketServer() {
		
		logger.log(Level.FINE,"MarketServer initialized");
		
	}
	
	
	
	
	/**
	 * This method opens the socket port and waits for a connection from 
	 * client. When the client connects, this method notifies that using
	 * an object;
	 * 
	 * */
	private static void openConnection(){
		try {
			//socket creation 
			server = new ServerSocket(PORTNO);
			
			//client connection accept
			client = server.accept();
			
			//Initialising I/O streams
			socketInput = new ObjectInputStream(client.getInputStream());
			socketOutput = new ObjectOutputStream(client.getOutputStream());
			
			//Check correct connection
			if(client == null) {
				logger.log(Level.WARNING, "unable to open connection - client is null");
				return;
			} 
			
			//Notification of connection state
			connectionState = true;
			socketOutput.writeObject(new SingleMessage(connectionState,false));
			socketOutput.flush();
			
			
		} catch(IOException e) {
			logger.log(Level.SEVERE,"Connection setup failed! - "+e.getLocalizedMessage());
		}
		
	}
	
	
	
	
	
	/**
	 * This method gets the client's credentials from socket and
	 * checks whether they're on DB (credentials.txt).
	 * @return boolean - status of authentication
	 * */
	private static SingleMessage checkLogin() {
		try {
			//initialising the message
			SingleMessage msg = new SingleMessage(connectionState,false);
			
			//getting credentials from client
			userCredentials = (CredentialsCouple) socketInput.readObject();
			
			//client name tracking on server
			System.out.println(userCredentials.getUsr());
			System.out.println(userCredentials.getPasswd());
			
			//check value
			boolean checkValue = checkCredentials(userCredentials);
			
			if(checkValue) {
				msg.setAuthentication(true);
				System.out.println("Authenticated - "+ userCredentials.getUsr());
				

			} else {
				msg.setAuthentication(false);
				System.out.println("Not authenticated");
			}
				
			return msg;
			
	
		} catch (IOException e) {
			logger.log(Level.SEVERE,"loginCheck error - "+e.getLocalizedMessage());
			return null;
		} catch(ClassNotFoundException cnf) {
			logger.log(Level.SEVERE,"loginCheck error - "+cnf.getLocalizedMessage());
			return null;
		}
	}
	
	
	
	
	/**
	 * The {@code connection} method establishes a connection
	 * between client and server at the specified port number.
	 * The client-server communication works with an exchange
	 * of a {@code MarketMessage} object, that contains 
	 * */
	private static void requestHandler(SingleMessage inputMsg) {
		
		
		try {
			
			//message to be 
			SingleMessage msg = new SingleMessage(connectionState,authenticationState);
			
			//SENDING OK FOR AUTHENTICATION
			socketOutput.writeObject(msg);
			socketOutput.flush();
			
			
			//loop used to communicate with the client
//			while(connectionState == true) {
//				
//				msg = (SingleMessage)socketInput.readObject();
//				int req = msg.getRequest();
//				System.out.println(req);
//				/* At this point the server is accepting requests from client:
//				 * 1. List of products:
//				 * 		Send the list
//				 * 
//				 * 2. Purchase of a product:
//				 * 		check if the product exists --> status
//				 * 		check if the product is available --> send a product
//				 * 
//				 * 3. Upload a product:
//				 * 		Product object expected
//				 * 
//				 * 4. Return a product:
//				 * 		Product object expected
//				 * 		Check product ownership on 'products'
//				 * */
//				SingleMessage outputMessage = new SingleMessage(connectionState);
//				
//				switch(req) {
//				case 1:
//					System.out.println("Servig request n.1");
//					socketOutput.writeObject(products);
//					socketOutput.flush();
//					break;
//					
//				case 2:
//					Product myProduct = (Product)socketInput.readObject();
//					if(myProduct.getName() == "" || myProduct.getId() == "") {
//						
//						outputMessage.setStatus(false);
//						socketOutput.writeObject(outputMessage);
//						socketOutput.flush();
//					
//					} else {
//						
//						int index = products.indexOf(myProduct);
//						
//						if(index == -1 || products.get(index).getOwnerName() != "") {
//							
//							outputMessage.setStatus(false);
//							socketOutput.writeObject(outputMessage);
//							socketOutput.flush();
//						
//						} else {
//							
//							products.get(index).setOwnerName(userCredentials.getUsr());
//							socketOutput.writeObject(products.get(index));
//							socketOutput.flush();
//						
//						}
//					}
//						
//					break;
//						
//					case 5:
//						System.out.println("Closing connection...");
//						connectionState = false;
//						break;
//					}
//				}
//			
			
			
			server.close();
			client.close();
			
					
		} catch (IOException e){
			System.out.println("connection error - " + e.getMessage());
		}
//		catch (ClassNotFoundException cnfe) {
//			System.out.println("comunication message error - " + cnfe.getMessage());
//		}
//		
		
	}
	
	
	
	/**
	 * This method gets the products from a file and stores them
	 * in a local list using the {@code makeProduct} method.
	 * 
	 * @see makeProduct
	 * */
	private static void getProductsFromFile() {
		
		String line;
		Scanner scanner =  null;
		
	    try {
	        // Initialise InputStreamReader and BufferedReader
	        scanner = new Scanner(new File("resource/storage.txt"));
	
	        // Read the content from the InputStream
	        while (scanner.hasNextLine()) {
	        	line = scanner.nextLine();
	        	products.add(makeProduct(line));
	        }
	    } catch (IOException e) {
	        logger.log(Level.SEVERE,"Products reading error - "+e.getLocalizedMessage());
	    } finally {
	        // Close resources in the finally block to ensure they are always closed
	    	if(scanner != null) {
	    		scanner.close();
	    	}
	        
	    }
	}
	




	/** 
	 * This method transforms a string line taken from the product file
	 * into a Product object.
	 * 
	 * @see Product
	 * */ 
	private static Product makeProduct(String line) {
		
		//Variables definition
		String name, id;
		String owner = "";
		int price = 0;
		
		//Delimiters for file-line handling
		int firstDelimiter = line.indexOf(';');
		int secondDelimiter = line.indexOf(';',firstDelimiter+1);
		int thirdDelimiter = line.indexOf(';',secondDelimiter+1);
			
		//file-line handling
		name = line.substring(0,firstDelimiter);
		id = line.substring(firstDelimiter+1,secondDelimiter);
		
		//If there's no owner:
		// price will be extracted from secondDelimiter to end of line
		if(thirdDelimiter == -1) {
			thirdDelimiter = line.length();
		} else {
			owner = line.substring(thirdDelimiter+1);
		}
		
		try {
			price = Integer.parseInt(line.substring(secondDelimiter+1,thirdDelimiter));
			
		} catch (NumberFormatException ne) {
			logger.log(Level.SEVERE,"Conversion error (parseInt) -> Message:\n" + ne.getMessage());
			return null;
		}
		
		Product prod = new Product(name,id,price);
		
		if(owner != "") {
			prod.setOwnerName(owner);
		}
		return prod;
	}
 	
	
	/**
	 * This function reads the credentials file and stores every couple (username - password) in
	 * an arrayList.
	 * */
	private static void getCredentialsFromFile() {	
		
		
		String usr,passwd,line;
		int delimiter;
		Scanner scanner = null;
		File file = null;
//		InputStreamReader inputStreamReader = null;
//		BufferedReader bufferedReader = null;
		
		try {
			file = new File("resource/credentials.txt");
			scanner = new Scanner(file);
//			inputStreamReader = null;
//	        bufferedReader = null;
        
            // Initialize InputStreamReader and BufferedReader
//            inputStreamReader = new InputStreamReader(input);
//            bufferedReader = new BufferedReader(inputStreamReader);

            // Read the content from the InputStream
            while (scanner.hasNextLine()) {
            	
            	line = scanner.nextLine().trim();
            	delimiter = line.indexOf(';');
				usr = line.substring(0,delimiter);
				passwd = line.substring(delimiter+1);
				
				credentials.add(new CredentialsCouple(usr,passwd));
            }
            
        } catch (IOException e) {
           logger.log(Level.SEVERE,"Credentials reading error - "+e.getLocalizedMessage());
           
        } finally {
            if (scanner != null) {
			    scanner.close();
			}
        } 
	}	
	
	
	/** 
	 * This method checks whether a user is in the database or not
	 * and returns true or false, respectively.
	 * 
	 * @param cred: {@code CredentialsCouple} object
	 * */
	private static boolean checkCredentials(CredentialsCouple cred) {

		System.out.println("DEBUG - checkCredentials");
		for(CredentialsCouple c : credentials) {
			if(c.getPasswd().equals(cred.getPasswd()) && c.getUsr().equals(cred.getUsr())) {
				return true;
			}
			System.out.println(cred.getPasswd() + "\t" + c.getPasswd());
		}
		
		return false;
	}
	
	
	/**
	 * This method creates the server logger and initialises it.
	 * */
	private static boolean setLogger() {
		
		System.out.println("Creatig logger");
		LogManager.getLogManager().reset();
		logger.setLevel(Level.ALL);
		
		try {
			logFile = new FileHandler("resource/serverLogger.log");
			logFile.setLevel(Level.FINE);
			logger.addHandler(logFile);
			logFile.setFormatter(new SimpleFormatter());
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Logger not initialized");
			return false;
		}
		return true;
	}
	
	
	
	
	
	public static void main(String args[]) {
		
		System.out.println("INIZIAMOOO");
		setLogger();
		logger.log(Level.FINE, "Logger on");
		
		getCredentialsFromFile();
		getProductsFromFile();
		
		openConnection();
		
		
		
//		try {
//			openConnection();
//			toCommunicate();
//		
//		}catch(ExceptionInInitializerError e) {
//			System.out.println("SERVER - " + e.getLocalizedMessage());
//		}
//		return;
		
	}

	
}