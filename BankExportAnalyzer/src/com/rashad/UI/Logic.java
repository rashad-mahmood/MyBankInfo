package com.rashad.UI;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Logic {
	
	// Transaction maps for debit
	private SortedMap<String, ArrayList<Transaction>> osapPayments = new TreeMap<String, ArrayList<Transaction>>();
	private SortedMap<String, ArrayList<Transaction>> funPayments = new TreeMap<String, ArrayList<Transaction>>();
	private SortedMap<String, ArrayList<Transaction>> billPayments = new TreeMap<String, ArrayList<Transaction>>();
	
	// Transaction maps for credit
	private SortedMap<String, ArrayList<Transaction>> jobPayments = new TreeMap<String, ArrayList<Transaction>>();
	private SortedMap<String, ArrayList<Transaction>> creditPayments = new TreeMap<String, ArrayList<Transaction>>();
	
	// Totals
	private double totalOsap = 0.0;
	private double totalFun = 0.0;
	private double totalBill = 0.0;
	private double totalCredit = 0.0;
	private double totalSpendable = 0.0;
	private double totalSpendableIntact = 0.0;
	
	// List of transaction names that are considered bills
	private ArrayList<String> bills = new ArrayList<String>();
	private ArrayList<String> pays = new ArrayList<String>();
 	
	// Set the array list containing the names of all bill transactions
	public void setBills(File bills) throws IOException {
		ArrayList<String> billList = null;
		if(!bills.exists()) {
			bills.createNewFile();
		}
		billList = (ArrayList<String>) FileUtils.readLines(bills);
		this.bills = billList;
	}
	
	// Set the array list containing the names of all payments
	public void setPays(File payFile) throws IOException {
		ArrayList<String> pays = null;
		if(!payFile.exists()) {
			payFile.createNewFile();
		}
		pays = (ArrayList<String>) FileUtils.readLines(payFile);
		this.pays = pays;
	}
	
	public ArrayList<String> getBills() {
		return this.bills;
	}
	
	public Double getAvailableSpendings() {
		Double total = 0.0;
/*		int payCount = 0;
		
		if (creditPayments.size() > 0) {
			Set<String> monthSet = creditPayments.keySet();
			for (String month : monthSet) {
				System.out.println(month);
				ArrayList<Transaction> transactionMap = creditPayments.get(month);
				Set<String> transactionSet = transactionMap.keySet();
				for (String transactionName : transactionSet) {
					Double transactionAmount = transactionMap.get(transactionName);
					System.out.println("\t" + transactionName + " = " + transactionAmount);
					if (pays.contains(transactionName)) {
						payCount++;
					}
				}
			}
		}
		*/
		return total;
	}
	
	public double getTotalSpendableIntact() {
		return this.totalSpendableIntact;
	}
	
	public double getCurrentSpendable() {
		return totalSpendableIntact - totalFun;
	}
	
	// Prints to screen all transactions for the month for given transaction type
	public Double printMonth(String mapName, String key) {
		Map<String, ArrayList<Transaction>> temp = new HashMap<String, ArrayList<Transaction>>();
		Double monthTotal = 0.0;
		
		if (mapName.equals("OSAP")) {
			temp = osapPayments;
		} else if (mapName.equals("Bills")) {
			temp = billPayments;
		} else if (mapName.equals("Fun")) {
			temp = funPayments;
		}
		
		if (temp.containsKey(key)) {
			ArrayList<Transaction> transactions = temp.get(key);
			for (Transaction t : transactions) {
				double amount = t.getAmount();
				monthTotal += amount;
				System.out.println(t.getName() + " = " + amount);
			}
			System.out.println("-------------");
			System.out.println("Total   => $" + monthTotal);
		} else {
			System.out.println("No data for month. See all tracked months below.");
			Set<String> keySet = temp.keySet();
			ArrayList<String> keySetList = new ArrayList<String>(keySet);
			Collections.sort(keySetList);
			for (String k : keySetList) {
				System.out.println(k);
			}
		}
		return monthTotal;
	}
	
	//Prints to screen all transactions for the given transaction type
	public void printTotal(String mapName) {
		Map<String, ArrayList<Transaction>> temp = new HashMap<String, ArrayList<Transaction>>();
		
		if (mapName.equals("OSAP")) {
			temp = osapPayments;
		} else if (mapName.equals("Bills")) {
			temp = billPayments;
		} else if (mapName.equals("Fun")) {
			temp = funPayments;
		} else if (mapName.equals("Credit")) {
			temp = creditPayments;
		} else if (mapName.equals("Intact Insurance")) {
			temp = jobPayments;
		}
		
		Double total = 0.0;
		Set<String> keySet = temp.keySet();
		for (String key : keySet) {
			Double monthTotal = 0.0;
			System.out.println(key);
			ArrayList<Transaction> transactions = temp.get(key);
			for (Transaction t : transactions) {
				System.out.println("\t" + t.getName() + " " + t.getAmount());
				monthTotal += t.getAmount();
				total += t.getAmount();
			}
			System.out.println("\t-------------");
			System.out.println("\tMonth Total   => $" + monthTotal);
		}
		System.out.println("-------------");
		System.out.println("Total   => $" + total);
	}
	
	private boolean putInMap(Map<String, ArrayList<Transaction>> map, String month, Double value, String name) {
		Transaction t = new Transaction(name, value);
		
		if (map.containsKey(month)) {
			ArrayList<Transaction> transaction = map.get(month);
			transaction.add(t);
			map.put(month, transaction);
		} else {
			ArrayList<Transaction> transaction = new ArrayList<Transaction>();
			transaction.add(t);
			map.put(month, transaction);
		}
		return true;
	}
	
	public void populateTransactionMaps(String fileName) throws IOException, ParseException {
		File file = new File(fileName);
		String readFileToString = FileUtils.readFileToString(file);
		CSVParser parser = CSVParser.parse(readFileToString, CSVFormat.EXCEL);
		
		for (CSVRecord csvRecord : parser) {
			String dateStr = csvRecord.get(0);
			String debit = csvRecord.get(2);
			String credit = csvRecord.get(3);
			String name = csvRecord.get(1);
			
			// Get date key from date field of record
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
			DateTime dt = formatter.parseDateTime(dateStr);
			String dateKey = dt.getMonthOfYear() + "/" + dt.getYear();
			
			if (debit.length() > 0) {
				if (name.contains("NSLSC") || name.contains("NTL")) {
					putInMap(osapPayments, dateKey, Double.parseDouble(debit), name);
					totalOsap += Double.parseDouble(debit);
				} else if (bills.contains(name)) {
					putInMap(billPayments, dateKey, Double.parseDouble(debit), name);
					totalBill += Double.parseDouble(debit);
				} else {
					putInMap(funPayments, dateKey, Double.parseDouble(debit), name);
					totalFun += Double.parseDouble(debit);
				}
			}
			
			if (credit.length() > 0) {
				if (name.contains("FDM GROUP LIMIT  PAY")) {
					this.totalSpendableIntact += Double.parseDouble(credit)*0.0625;
					putInMap(jobPayments, dateKey, Double.parseDouble(credit), name);
				} else {
					putInMap(creditPayments, dateKey, Double.parseDouble(credit), name);
				}
			}
		}
	}

}
