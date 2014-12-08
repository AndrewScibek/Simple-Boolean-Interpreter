import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ExpressionGetter {
	File file;
	String expression;

	/**
	 * 
	 * @param filepath
	 *            string of path to file
	 * @return returns true if the input string is a file and not a directory
	 */
	boolean checkFile(String filepath) {
		file = new File(filepath);
		if (file.exists() && !file.isDirectory()) {
			return true;
		} else {
			System.out.println("Incorrect filepath");
			return false;
		}
	}

	/**
	 * Reads string that is to be parsed
	 * 
	 * @return returns true if string is not empty
	 */
	boolean readInput() {
		try {
			Scanner read = new Scanner(new FileReader(file)); //does;
			expression = read.nextLine();
			if (expression.length() < 1) {
				System.out.println("Input file cannot be empty.");
				return false;

			}
		}

		catch (Exception e) {
			return false;
		}

		return true;
	}

	String readInput(String filepath) {
		try {
			File file = new File(filepath);
			Scanner read = new Scanner(new FileReader(file)); //does;
			expression = read.nextLine();
		}

		catch (FileNotFoundException e) {
			// e.printStackTrace();
		}

		return expression;
	}
}
