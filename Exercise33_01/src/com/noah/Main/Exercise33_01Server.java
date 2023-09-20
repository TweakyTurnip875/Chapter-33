package com.noah.Main;

// Exercise31_01Server.java: The server can communicate with
// multiple clients concurrently using the multiple threads
import java.util.*;
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Exercise33_01Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    ta.setWrapText(true);
   
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 400, 200);
    primaryStage.setTitle("Exercise31_01Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    new Thread(() -> {
  	  try {
		ServerSocket serverSocket = new ServerSocket(8000);
		Platform.runLater(() -> ta.appendText("Server started at: " + new Date() + "\n"));
		
		Socket socket = serverSocket.accept();
		
		Platform.runLater(() -> ta.appendText("Client connected at: " + new Date() + "\n"));
		
		DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
		DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
		
		while(true) {
			double annualInterestRate = inputFromClient.readDouble();
			int numOfYears = inputFromClient.readInt();
			double loanAmount = inputFromClient.readDouble();
			
			System.out.println(numOfYears);
			
			Platform.runLater(() -> {
				ta.appendText("Annual Interest Rate: " + annualInterestRate + "\n");
				ta.appendText("Number of Years: " + numOfYears + "\n");
				ta.appendText("Loan Amount: " + loanAmount + "\n");
			});
			
			Loan loan = new Loan(annualInterestRate, numOfYears, loanAmount);
			double monthlyPayment = loan.getMonthlyPayment();
			double totalPayment = loan.getTotalPayment();
			
			outputToClient.writeDouble(monthlyPayment);
			outputToClient.writeDouble(totalPayment);
			outputToClient.flush();
		}
  	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
