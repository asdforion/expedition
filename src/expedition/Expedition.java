package expedition;

import static expedition.Other.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expedition {

	public static int numChars, textSpeed, z;

	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner sysIn = new Scanner(System.in);
		numChars = 3;
		textSpeed = 3;
		z = 0;
		/*
		 * print("\n\n\n\n\n\n\n\n"); print("START"); ellipsis(3); println("?");
		 * Thread.sleep((textSpeed + 2) * 100); println("      y/n\n");
		 * 
		 * if (inputAffirm(sysIn)) {
		 */

		updateSettings();

		Frame state = new Frame(sysIn);
		state.begin();

		// }
		Thread.sleep(1000);
		println("\n     Thanks for playing.");
		// input(sysIn, false);
		sysIn.close();

	}

	public static String ellipsis(int length) throws InterruptedException {
		Thread.sleep(textSpeed * 100);
		for (int i = 0; i < length; i++) {
			print(".");
			Thread.sleep((textSpeed + 2) * 100);
		}
		return "";
	}

	public static void enter(Scanner scan) throws InterruptedException {
		print("...");
		scan.nextLine();
	}
	
	public static String input(Scanner scan) throws InterruptedException {
		print("> ");
		String text = scan.nextLine();
		return text;
	}

	public static int inputInt(Scanner scan) {
		print("> ");
		Integer in = null;
		String text = scan.nextLine();
		try {
			in = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			println("//  // That isn't a number! Try again! //  //");
			return inputInt(scan);
		}
		return in;
	}

	public static String input(Scanner scan, Boolean symb) throws InterruptedException {
		if (symb) {
			print("> ");
		}
		String text = scan.nextLine();
		return text;
	}

	public static void updateSettings() throws InterruptedException {
		Scanner setScan = null;
		File setFile = new File("settings.txt");

		while (setScan == null) {
			try {
				setScan = new Scanner(setFile);
			} catch (FileNotFoundException ex) {
				print("No settings.txt file found! Creating one");
				ellipsis(3);
				print("\n");
				writeNewSettings(setFile);
			}
		}

		String[] currLine;
		while (setScan.hasNext()) {
			currLine = setScan.nextLine().split(":");
			switch (currLine[0]) {
			case "startingCharacters":
				numChars = Integer.parseInt(currLine[1]);
			case "textSpeed":
				textSpeed = Integer.parseInt(currLine[1]);
			case "z":
				z = Integer.parseInt(currLine[1]);
			}
		}

		setScan.close();
	}

	public static void writeNewSettings(File setFile) throws InterruptedException {
		if (setFile.exists()) {
			println("Oops! This method should not have been called; the 'settings.txt' file already exists!");
		} else {
			try {
				String a = System.getProperty("line.separator");
				setFile.createNewFile();
				FileWriter out = new FileWriter(setFile, false);
				out.write("--/settings/--" + a);
				out.write("startingCharacters:3\n" + a);
				out.write("textSpeed:3\n" + a);
				out.write("z:0\n" + a);
				out.write("--/end/--" + a);

				out.close();
			} catch (IOException ex1) {
				Logger.getLogger(Expedition.class.getName()).log(Level.SEVERE, null, ex1);
				println("Unable to create a new settings file. Exiting");
				ellipsis(3);
			}
		}

	}

	public static Boolean inputAffirm(Scanner scan) throws InterruptedException {
		String text = input(scan);
		if (text.equalsIgnoreCase("y") || text.equalsIgnoreCase("yes") || text.equalsIgnoreCase("affirmative")
				|| text.equalsIgnoreCase("of course") || text.equalsIgnoreCase("ok") || text.equalsIgnoreCase("okay")
				|| text.equalsIgnoreCase("sure") || text.equalsIgnoreCase("why not")
				|| text.equalsIgnoreCase("why not?") || text.equalsIgnoreCase(">nods") || text.equalsIgnoreCase(">nod")
				|| text.equalsIgnoreCase("without a doubt") || text.equalsIgnoreCase("yeah")
				|| text.equalsIgnoreCase("yep") || text.equalsIgnoreCase("yeppers") || text.equalsIgnoreCase("mmhm")) {
			return true;
		} else {
			return false;
		}
	}

}
