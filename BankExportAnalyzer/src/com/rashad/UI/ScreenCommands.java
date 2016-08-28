package com.rashad.UI;


import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.UID;


public class ScreenCommands {
	
	private Map<Integer, Method> commandMap = new HashMap<Integer, Method>();
	private Screens userInterface = new Screens();
	
	public ScreenCommands() {
		super();
		init();
	}

	private void init() {
		Method pickTransactionType = null;
		Method pickDebitTransactionCategory = null;
		Method pickCreditTransactionCategory = null;
		Method pickTransactionView = null;
		Method goodBye = null;
		Method start = null;
		
		try {
			start = userInterface.getClass().getMethod("start");
			pickTransactionType = userInterface.getClass().getMethod("pickTransactionType");
			pickDebitTransactionCategory = userInterface.getClass().getMethod("pickDebitTransactionCategory");
			pickCreditTransactionCategory = userInterface.getClass().getMethod("pickCreditTransactionCategory");
			pickTransactionView = userInterface.getClass().getMethod("pickTransactionView");
			goodBye = userInterface.getClass().getMethod("goodBye");
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Error creating UI methods.");
			e.printStackTrace();
		}
		commandMap.put(0, start);
		commandMap.put(1, pickTransactionType);
		commandMap.put(2, pickDebitTransactionCategory);
		commandMap.put(3, pickTransactionView);
		commandMap.put(4, pickCreditTransactionCategory);
		commandMap.put(666, goodBye);
	}

	public int executeCommand(int commandId) {
		int nextScreen = 666;
		Method method = commandMap.get(commandId);
		try {
			nextScreen = (int) method.invoke(userInterface);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Failed to execute UI method.");
			e.printStackTrace();
		}
		return nextScreen;
	}
	
	
	
}
