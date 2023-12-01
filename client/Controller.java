package client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class Controller implements Initializable{

	@FXML
	private PasswordField password_input;
	
	@FXML
	private TextField username_input;
	
	@FXML
	private Button submit_btn;

	@FXML
	private Text warning_text;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("Initialization");
		System.out.println(warning_text == null);
		System.out.println(submit_btn == null);
		System.out.println(username_input == null);
		System.out.println(password_input == null);
	}
	
	
	public Controller() {
		System.out.println("Controller on:");
		System.out.println(warning_text == null);
		System.out.println(submit_btn == null);
		System.out.println(username_input == null);
		System.out.println(password_input == null);
	}	
	
	/**
	 * Getter methods: submit button - username text field - password password field.
	 * */
	public Button getSubmitBtn() {
		return this.submit_btn;
	}
	
	public String getUsername() {
		return this.username_input.getText().trim();
	}
	
	public String getPassword() {
		return this.password_input.getText().trim();
	}
	
	public Text getWarningText() {
		return this.warning_text;
	}
	
	
	public void setWarningText(String msg) {
		warning_text.setText(msg);
	}
	
	
	
	
}
