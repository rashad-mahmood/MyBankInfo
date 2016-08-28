package com.rashad.UI;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Screens {
	private String fileName = "C:/Users/Rashad/Downloads/accountactivity.csv";
	
	private File bills = new File("bills.txt");
	private File pays = new File("pays.txt");
	private Logic logic = new Logic();

	private String transactionCategory = "";
	private String transactionType = "";

	public void goodBye() {
		System.out.println("Goodbye.");
		System.exit(0);
	}
	
	public int start() {
		System.out.println("Hello.");
		try {
			logic.setBills(bills);
			logic.setPays(pays);
			logic.populateTransactionMaps(fileName);
			System.out.println("done");
		} catch (IOException | ParseException e) {
			System.out.println("Failed to parse data into maps.");
			e.printStackTrace();
			return 666;
		}
		System.out.println("Parsed date into maps.");
		return 1;
	}
	
	public int pickTransactionType() throws IOException {
		int nextScreen = 666;
		
		String option1 = "Debit";
		String option2 = "Credit";
		String option3 = "Temp";
		
		System.out.println("\n1 - " + option1 + "\n2 - " + option2 + "\n3 - " + option3 + "\n4 - Quit");
		System.out.print("\n> ");
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();
		
		if (userChoice.equals("1")) {
			this.transactionType = option1;
			nextScreen = 2;
		} else if (userChoice.equals("2")) {
			this.transactionType = option2;
			nextScreen = 4;
		} else if (userChoice.equals("3")) {
			this.transactionType = option3;
		} else if (userChoice.equals("4")) {
			nextScreen = 666;
		} 
		return nextScreen;
	}
	
	public int pickDebitTransactionCategory() throws IOException {
		int nextScreen = 3;
		
		String option1 = "OSAP";
		String option2 = "Bills";
		String option3 = "Fun";
		
		System.out.println("\n1 - " + option1 + "\n2 - " + option2 + "\n3 - " + option3 + "\n4 - Back");
		System.out.print("\n> ");
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();
		
		if (userChoice.equals("1")) {
			this.transactionCategory = option1;
		} else if (userChoice.equals("2")) {
			String billsStr = FileUtils.readFileToString(bills);
			System.out.println("List of bills\n-------------\nAdd more bills at " + bills.getAbsolutePath() + "\n" + billsStr +"\n");
			
			this.transactionCategory = option2;
		} else if (userChoice.equals("3")) {
			this.transactionCategory = option3;
		} else if (userChoice.equals("4")) {
			nextScreen = 1;
		}
		return nextScreen;
	}
	
	public int pickCreditTransactionCategory() throws IOException {
		int nextScreen = 3;
		
		String option1 = "Intact Insurance";
		String option2 = "Credit";
		
		System.out.println("\n1 - " + option1 + "\n2 - " + option2 + "\n3 - Back");
		System.out.print("\n> ");
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();
		
		if (userChoice.equals("1")) {
			this.transactionCategory = option1;
		} else if (userChoice.equals("2")) {
			this.transactionCategory = option2;
		} else if (userChoice.equals("3")) {
			nextScreen = 1;
		} 
		return nextScreen;
	}
	
	public int pickBudgetTransactionCategory() throws IOException {
		int nextScreen = 3;
		
		String option1 = "Intact Insurance";
		String option2 = "Credit";
		
		System.out.println("\n1 - " + option1 + "\n2 - " + option2 + "\n3 - Back");
		System.out.print("\n> ");
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();
		
		if (userChoice.equals("1")) {
			this.transactionCategory = option1;
		} else if (userChoice.equals("2")) {
			this.transactionCategory = option2;
		} else if (userChoice.equals("3")) {
			nextScreen = 1;
		} 
		return nextScreen;
	}
	
	public int pickTransactionView() {
		String mapName = this.transactionCategory;
		int nextScreen = 3;
		
		String option1 = "Month";
		String option2 = "Total";
		
		System.out.println("\n1 - " + option1  + "\n2 - " + option2 + "\n3 - Back");
		System.out.print("\n> ");
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();

		if (userChoice.equals("1")) {
			String inputMonth = userInputMonth();
			logic.printMonth(mapName, inputMonth);
		} else if (userChoice.equals("2")) {
			logic.printTotal(mapName);
		} else if (userChoice.equals("3")) {
			if (transactionType.equals("Debit"))
				nextScreen = 2;
			if (transactionType.equals("Credit"))
				nextScreen = 4;
		}
		return nextScreen;
	}
	
	private String userInputMonth() {
		System.out.println("Enter month [m(m)/yyyy]:\n");
		
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		
		return userInput;
	}
}
