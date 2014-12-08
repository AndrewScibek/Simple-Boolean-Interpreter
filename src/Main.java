import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Andrew Scibek
 * This program reads a file path inputed by the user;
 * Reads the input from the file
 * The input is than lexed to see if it has correct syntax
 * And at the same time it is solved with respect to the
 * semantics of the expression
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String filepath = "   ";
		String expression;
		Scanner input = new Scanner(System.in);
		Interpreter inter = new Interpreter();
		ExpressionGetter eg = new ExpressionGetter();
		System.out.println("**The program accepts both t/f and true/false**");
		do
		{
			System.out.print("Enter file path: ");
			filepath = input.nextLine();
		}while(!eg.checkFile(filepath)||!eg.readInput());//while not a file and empty file
		expression = eg.readInput(filepath);
		/*
		 * Set boolean to true if you want verbose output
		 */
		inter.check(expression,false);
	}

}
