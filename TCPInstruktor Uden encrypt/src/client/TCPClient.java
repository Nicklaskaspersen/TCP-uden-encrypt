package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import shared.Deposit;
import shared.Login;
import shared.SerializableData;
import shared.Use;

class TCPClient 
{
	public static void main(String argv[]) throws Exception 
	{
		
		while(true){
		String pin;
		String username;
		

		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		Scanner in = new Scanner(System.in);
		Socket serverSocket = new Socket("localhost", 6789);
		System.out.println("Connected to server.");
		Login login = new Login();
		ObjectOutputStream oos = new ObjectOutputStream(serverSocket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
		while (!login.isUserAuthenticated()){
		System.out.println("Please input a username");
		username = inFromUser.readLine();
		login.setUsername(username);
		
		System.out.println("Please input your password");
		pin = inFromUser.readLine();
		login.setPin(pin);
		
		oos.writeObject(login);
		
		login = (Login) ois.readObject();
		
		}
		
		System.out.println("1. deposit bitcoins to your account.");
		System.out.println("2. spend bitcoins.");
		System.out.println("3. exit.");
		
		int k = in.nextInt();
		
		switch(k){
		case 1:
			System.out.println("Enter the amount you want to deposit.");
			double amount = in.nextDouble();
			Deposit dep = new Deposit();
			dep.setAmount(amount);
			dep.setUsername(login.getUsername());
			oos.writeObject(dep);
			
			dep = (Deposit) ois.readObject();
			System.out.println("Your account now have: " + dep.getBalance());
			break;
		case 2:
			System.out.println("Enter the amount you want to spend.");
			amount = in.nextDouble();
			Use use = new Use();
			use.setAmount(amount);
			use.setUsername(login.getUsername());
			oos.writeObject(use);
			
			use = (Use) ois.readObject();
			if(use.getAcceptedUse()){
				System.out.println("The transaction was accepted.\nYour account now have: " + use.getBalance());
			}
			else{
				System.out.println("The transaction was not accepted because you have insufficient funds.");
			}
			break;
		case 3:
			return;
			
		}
		
		serverSocket.close();
		
		}
	}
}