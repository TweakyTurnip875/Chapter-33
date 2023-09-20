package com.noah.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Exercise33_09Client extends Application {
  private TextArea taServer = new TextArea();
  private TextArea taClient = new TextArea();
  
  DataInputStream inputFromServer = null;
  DataOutputStream outputToServer = null;
 
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    taServer.setWrapText(true);
    taClient.setWrapText(true);
    taServer.setDisable(true);
    taServer.getStyleClass().add("taServer");
    taServer.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

    BorderPane pane1 = new BorderPane();
    pane1.setTop(new Label("History"));
    pane1.setCenter(new ScrollPane(taServer));
    BorderPane pane2 = new BorderPane();
    pane2.setTop(new Label("New Message"));
    pane2.setCenter(new ScrollPane(taClient));
    
    VBox vBox = new VBox(5);
    vBox.getChildren().addAll(pane1, pane2);

    // Create a scene and place it in the stage
    Scene scene = new Scene(vBox, 200, 200);
    primaryStage.setTitle("Exercise31_09Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    // To complete later
    
    // Probably not the best way of storing input, but I can't directly change variables in event listeners
    TextArea message = new TextArea();
    message.appendText("C: ");
    taClient.setOnKeyPressed(e -> {
    	message.appendText(e.getText());
    	if(e.getCode() == e.getCode().ENTER) {
    		
    		try {
    			
				outputToServer.writeUTF(message.getText());
				outputToServer.flush();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		taServer.appendText(message.getText() + "\n");
    		taClient.clear();
    		message.clear();
    		message.appendText("C: ");
    	}
    });
    new Thread(() -> {
	    try {
	    	Socket socket = new Socket("localhost", 8000);
	    	Platform.runLater(() -> taServer.appendText("[User connected]" + "\n"));
	    	
	    	inputFromServer = new DataInputStream(socket.getInputStream());
	    	outputToServer = new DataOutputStream(socket.getOutputStream());
	    	
	    	while(true) {
	    		String userMessage = inputFromServer.readUTF();
	    		
	    		Platform.runLater(() -> taServer.appendText(userMessage + "\n"));
	    	}
	    	
	    } catch(IOException ex) {
	    	ex.printStackTrace();
	    }
    }).start();
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
