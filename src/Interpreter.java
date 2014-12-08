/**
 * 
 * 
 */

import java.util.Stack;

/**
 * 
 * @author Andrew Scibek
 *
 */
public class Interpreter
{
	boolean alreadySolved = false;
	String expression;
	int currentLocation = 0;
	boolean correct = false;
	int numOfOpen = 0;
	int numOfClosed = 0;
	Stack<String> SAS = new Stack<String>();
	Stack<Integer> parenthesisStack = new Stack<Integer>();
	String curChar;
	boolean endOfExpression = false;
	boolean tempB1, tempB2;
	String tempS1,tempS2;
	public void reset(){
		currentLocation = 0;
		correct = false;
		numOfOpen = 0;
		numOfClosed = 0;
		SAS = new Stack<String>();
		endOfExpression = false;
		tempB1 = false;
		tempB2 = false;
		tempS1 = "";
		tempS2 = "";		
		alreadySolved = false;
	}
	/**
	 * This method moves the currentLocation to next character.
	 * It also moves curChar to next character
	 */
	public void accept()
	{
		if(currentLocation < expression.length())
		{
			curChar = expression.substring(currentLocation, currentLocation+1);
		}
		else
		{
			endOfExpression = true;
			solve();
		}
		//if(debug) System.out.println(curChar);
		currentLocation++;			
	}
	/**
	 * Starts the syntax checking algorithm
	 * @param expression String to check syntax and solve equation
	 */
	public void check(String expression) 
	{
		this.expression = expression;
		correct = E();		
	}
	/**
	 * E::=B.
	 * @return if expression is a Expression of the rules
	 */
	boolean E() 
	{
		accept();
		if(B())
		{
			if(curChar.equals("."))
			{
				endOfExpression = true;
				correct = true;
				solve();
				return true;
			}
			else
			{
					new ErrorMessage(expression,currentLocation,
						"Expecting: . at this location");
				return false;
			}
		}
		return false;
	}
	/**
	 * B::=LT
	 * @return true if B is a LT
	 */
	boolean B() {
		if(L())
		{
			if(T())
			{
				return true;
			}
			else
			{
				new ErrorMessage(expression,currentLocation,
						"Expecting: ^,v,.,) at this location");				
			}
		}
		else
		{
			new ErrorMessage(expression,currentLocation,
					"Expecting: ~,t,true,f,false,( at this location");			
		}
		return false;
	}
/**
 * T::=^LT
 * T::=vLT
 * T::=
 * 
 * @return true if T is a AND LT or OR LT or something in the follow set
 */
	boolean T() {
		if(curChar.equals("^"))
		{
			accept();
			if(L())
			{
				tempS1 = SAS.pop();
				tempS2 = SAS.pop();		
				tempB1 = Boolean.parseBoolean(tempS1);
				tempB2 = Boolean.parseBoolean(tempS2);
				SAS.push(String.valueOf(tempB1&&tempB2));
				if(T())
					return true;
				return true;
			}
		}
		else if(curChar.equals("v"))
		{
			accept();
			if(L())
			{
				tempS1 = SAS.pop();
				tempS2 = SAS.pop();						
				tempB1 = Boolean.parseBoolean(tempS1);
				tempB2 = Boolean.parseBoolean(tempS2);		
				SAS.push(String.valueOf(tempB1||tempB2));
				if(T())
					return true;				
				return true;
			}			
		}
		else if(curChar.equals(")"))
		{
			if(!parenthesisStack.empty())
				parenthesisStack.pop();
			else
				new ErrorMessage(expression,currentLocation,
						"This ')' has no matching '(' It is most likely an extra.");
			numOfClosed++;
			accept();
			return true;
		}
		else if(curChar.equals("."))
		{
			endOfExpression = true;
			correct = true;
			solve();
			return true;
		}
		else
		{
			new ErrorMessage(expression,currentLocation,
					"Expecting: ^,v,),. at this location\nalso beware of misspellings of (true) or (false)");			
		}		
		return false;
	}
/**
 * L::=A
 * L::=~A
 * 
 * @return true if L is an A or ~A
 */
	boolean L() {
		if(curChar.equals("~"))
		{
			SAS.push(curChar);
			accept();
			if(A())
			{
				return true;
			}
		}
		else if(A())
		{
			return true;
		}
		else
		{
			new ErrorMessage(expression,currentLocation,
					"Expecting: ~,t,true,f,false,( at this location");			
		}
		return false;
	}
	/**
	 * A::=t
	 * A::=true
	 * A::=f
	 * A::=false
	 * A::=(B)
	 * @return
	 */
	boolean A() {
		if(curChar.equals("t"))
		{
			if(currentLocation+3<=expression.length())
			{
				if(expression.substring(currentLocation-1,currentLocation+3).equals("true"))
				{
					accept();
					accept();
					accept();
				}
			}
			accept();
			if(!SAS.isEmpty()&&SAS.peek().equals("~"))
			{
				SAS.pop();
				SAS.push("FALSE");
				
			}
			else
			{
				SAS.push("TRUE");				
			}
			return true;
		}
		else if(curChar.equals("f"))
		{
			if(currentLocation+4<=expression.length())
			{
				if(expression.substring(currentLocation-1,currentLocation+4).equals("false"))
				{
					accept();
					accept();
					accept();
					accept();
				}
			}			
			accept();
			if(!SAS.isEmpty()&&SAS.peek().equals("~"))
			{
				SAS.pop();
				SAS.push("TRUE");
			}
			else
			{
				SAS.push("FALSE");				
			}
			return true;
		}
		else if(curChar.equals("("))
		{
			parenthesisStack.push(currentLocation);
			numOfOpen++;
			accept();
			if(B())
			{
				return true;
			}
			if(curChar.equals(")"))
			{
				if(!parenthesisStack.empty())
					parenthesisStack.pop();
				else
					new ErrorMessage(expression,currentLocation,
							"This ')' has no matching '(' It is most likely an extra.");
				numOfClosed++;
				accept();
				return true;
			}
		}
		else
		{
			new ErrorMessage(expression,currentLocation,
					"Expecting: t,true,f,false,( at this location");			
		}		
		return false;
	}
	
	private void solve() {
		if(alreadySolved)
			return;
		if((numOfOpen!=numOfClosed))
		{
			new ErrorMessage(expression,(int)parenthesisStack.pop(),
					"Must close this open parenthesis before the .");
		}
		if(correct && SAS.size()==1 && endOfExpression && (numOfOpen==numOfClosed))
		{
			System.out.println("The expression is syntaxtically correct");
			System.out.println("The value of the expression is "+SAS.pop());
		}
		else
		{
			new ErrorMessage(expression,currentLocation+1,
					"Expecting . at end of expression");			
		}
		System.exit(0);
	}
}
