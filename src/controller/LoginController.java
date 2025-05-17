package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import util.AdminAuth;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty");
            return;
        }
        
        if (AdminAuth.authenticate(username, password)) {
            try {
                // Load the student management view
                Parent root = FXMLLoader.load(getClass().getResource("/view/student.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("Student Management");
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                messageLabel.setText("Error loading student management view");
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }
}