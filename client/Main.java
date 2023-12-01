package client;


import entities.CredentialsCouple;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Main extends Application{

	private static MarketClient client;
	
	//FXML elements
	private static Button submitButton;
	
	private static FXMLLoader loader;
	private static Controller controller;
	
	private static CredentialsCouple credentials;
	
	private static boolean authenticationState = false;
	private static boolean connectionState = false;
	
	public static void main(String[] args) {
		 
		
		try {
			client = new MarketClient();
			loader = new FXMLLoader(Main.class.getResource("login.fxml"));
			controller = new Controller();
			loader.setController(controller);
			submitButton = controller.getSubmitBtn();
			initialise();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch();
		
	}
	
	
	
	

	@Override
	public void start(Stage stage) throws Exception {
		
		StackPane root = loader.load();
		
		Scene scene = new Scene(root,600,800);
		stage.setScene(scene);
	
		stage.show();
	}
	
	
	
	
	private static void initialise()throws Exception{
		
		if(!(connectionState = client.isConnected())) {
			controller.setWarningText("Connection failed, program is closing...");
			try {
				controller.wait(3);
				return;
			} catch(InterruptedException ie) {
				System.out.println("Wait interrupton - "+ie.getLocalizedMessage());
			}
		}
		
		setButtonHandler();
	}
	
	
	
	private static void setButtonHandler() {
		EventHandler<MouseEvent> submitClicked = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	checkLogin();
            }
        };
		
		submitButton.setOnMouseClicked(submitClicked);
	}
	
	
	
	private static void checkLogin() {
		//Get credentials from input fields
		String username, password;
		
		username = controller.getUsername();
		password = controller.getPassword();
		
		int response = client.checkCredentials(username, password);
		if(response > 0) {
			credentials = new CredentialsCouple(username, password);
			authenticationState = true;
			//TODO change view
			return;
		}
		
		handleLoginStatus(response);
	}
	
	
	private static void handleLoginStatus(int x) {
		switch(x) {
		case 0:
			controller.setWarningText("Wrong username or password, retry!");
			break;
			
		case -1:
			controller.setWarningText("Error - connection failed.");
			connectionState = false;
			break;
			
		case -2:
			controller.setWarningText("Error - connection failed.");
			connectionState = false;
			break;
		}
	}
	
	

}
