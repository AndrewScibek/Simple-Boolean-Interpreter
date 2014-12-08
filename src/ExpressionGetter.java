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
		if (file.isFile()) {
			return true;
		} else {
			System.out.println("Incorrect filepath");
			return false;
		}
	}

	/**
	 * Reads string that is to be parsed
	 * 
	 * @return returns string that is in file
	 */

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
