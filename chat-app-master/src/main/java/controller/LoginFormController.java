package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFormController {
    public JFXTextField txtName;

    public void initialize() {
        // Initialization logic, if needed
    }

    public void logInButtonOnAction(ActionEvent actionEvent) throws IOException {
        String username = txtName.getText();

        if (!username.isEmpty() && username.matches("[A-Za-z0-9]+")) {
            try (Connection connection = DBConnection.getConnection()) {
                
                String query = "SELECT * FROM users WHERE name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                   
                    Stage primaryStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ClientForm.fxml"));

                    ClientFormController controller = new ClientFormController();
                    controller.setClientName(username); 
                    fxmlLoader.setController(controller);

                    primaryStage.setScene(new Scene(fxmlLoader.load()));
                    primaryStage.setTitle(username);
                    primaryStage.setResizable(false);
                    primaryStage.centerOnScreen();
                    primaryStage.setOnCloseRequest(windowEvent -> controller.shutdown());
                    primaryStage.show();

                    txtName.clear();
                } else {
                    
                    new Alert(Alert.AlertType.ERROR, "User not found. Please register first.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        } else {
            // Invalid input
            new Alert(Alert.AlertType.ERROR, "Please enter a valid username.").show();
        }
    }
}
