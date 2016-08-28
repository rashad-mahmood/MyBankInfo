package com.rashad.UI;

public class Temp {
	
	public static void main(String[] args) {
		ScreenCommands controller = new ScreenCommands();
		
		int nextScreen = 0;
		while (true) {
			nextScreen = controller.executeCommand(nextScreen);
		}
	}
//	private ScreenComma=nds controller = new ScreenCommands();
	
	/*public void showScreen() {
		int nextScreen = 1;
		while (nextScreen != 666) {
			nextScreen = controller.executeCommand(nextScreen);
		}
	} */

}
