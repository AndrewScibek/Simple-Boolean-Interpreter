package util;
import interpreter.Interpreter;

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

	private static Scanner input;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String userInput;
		String expression;
		input = new Scanner(System.in);
		Interpreter inter = new Interpreter();
		ExpressionGetter eg = new ExpressionGetter();
		System.out.println("**SBI accepts both t/f and true/false**");
		while(true){
				inter.reset();
				System.out.print("> ");
				userInput = input.nextLine();
				if(userInput.startsWith("file"))
				{
					if(eg.checkFile(userInput.substring(5, userInput.length()))){
						expression = eg.readInput(userInput.substring(5, userInput.length()));
						inter.check(expression);
					}
					else
					{
						System.out.println("Invalid file location");
					}
						
				}
				else if(userInput.equals("--help"))
				{
					System.out.println();
					System.out.println("To interprete a file use syntax: file -filename");
					System.out.println("Otherwise input will be interpreted\n");
				}
				else
				{
					inter.check(userInput);
				}
			//inter.reset();
		}
	}

}
