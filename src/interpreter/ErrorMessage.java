package interpreter;
/**
 * 
 * @author Andrew Scibek
 *
 */
public class ErrorMessage {
	String expression;
	int location;
	String errormessage;
	public ErrorMessage(String expression, int currentLocation, String errormessage) 
	{
		this.expression = expression;
		location = currentLocation;
		this.errormessage = errormessage;
		printError();
		//System.exit(0);
	}
	private void printError() {
		System.out.println(expression);
		location--;
		String spaces ="";
		if(location!=0)spaces = String.format("%"+location+"s", "");//string with correct amount of spaces to error
		System.out.print(spaces);
		System.out.println("^");
		System.out.println(errormessage);
	}

}
