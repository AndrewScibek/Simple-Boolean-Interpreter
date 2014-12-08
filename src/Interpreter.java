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
	boolean debug = false; // true - verbose (all steps) false -quiet(Just errors and final)
	boolean alreadySolved = false;
	String expression;
	int currentLocation = 0;
	boolean correct = false;
	int numOfOpen = 0;
	int numOfClosed = 0;
	Stack<String> SAS = new Stack<String>();
	Stack<Integer> pS = new Stack<Integer>();
	String curChar;
	boolean endOfExpression = false;
	boolean tempB1, tempB2;
	String tempS1,tempS2;
	public void reset(){
		debug = false; // true - verbose (all steps) false -quiet(Just errors and final)
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
		if(debug) System.out.println("Getting next char");
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
	public void check(String expression , boolean verbose) 
	{
		debug = verbose;
		this.expression = expression;
		correct = E();		
	}
	/**
	 * E::=B.
	 * @return if expression is a Expression of the rules
	 */
	boolean E() 
	{
		if(debug) System.out.println("At E()");
		accept();
		if(B())
		{
			if(debug) System.out.println("B() is true...curChar: "+curChar);
			if(curChar.equals("."))
			{
				if(debug) System.out.println("Found end of expression '.'");
				endOfExpression = true;
				correct = true;
				solve();
				return true;
			}
			else
			{
					if(debug) 
					{
						new ErrorMessage(expression,currentLocation,
							"Expecting: . at this location in E() failed inside B()");
					}
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
		if(debug) System.out.println("Inside B()");
		if(L())
		{
			if(debug) System.out.println("L() true inside of B()");
			if(T())
			{
				if(debug) System.out.println("T() true inside of B()");
				return true;
			}
			else
			{
				if(debug) 
				{
					new ErrorMessage(expression,currentLocation,
						"Expecting: ^,v,.,) at this location in B() failed T()");
				}
				new ErrorMessage(expression,currentLocation,
						"Expecting: ^,v,.,) at this location");				
			}
		}
		else
		{
			if(debug) 
			{	
				new ErrorMessage(expression,currentLocation,
					"Expecting: ~,t,true,f,false,( at this location in B() failed L()");
			}
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
		if(debug) System.out.println("Inside T()");
		if(curChar.equals("^"))
		{
			if(debug) System.out.println("Found AND in T()");
			accept();
			if(L())
			{
				if(debug) System.out.println("Doing AND math");
				tempS1 = SAS.pop();
				tempS2 = SAS.pop();
				if(debug) System.out.println("TempS1: "+tempS1+" TempS2: "+tempS2);				
				tempB1 = Boolean.parseBoolean(tempS1);
				tempB2 = Boolean.parseBoolean(tempS2);
				if(debug) System.out.println(tempB1+" AND "+ tempB2);
				SAS.push(String.valueOf(tempB1&&tempB2));
				if(debug) printStack();
				if(T())
					return true;
				return true;
			}
		}
		else if(curChar.equals("v"))
		{
			if(debug) System.out.println("Found OR in T()");
			accept();
			if(L())
			{
				if(debug) System.out.println("Doing OR math");
				tempS1 = SAS.pop();
				tempS2 = SAS.pop();				
				if(debug) System.out.println("TempS1: "+tempS1+" TempS2: "+tempS2);				
				tempB1 = Boolean.parseBoolean(tempS1);
				tempB2 = Boolean.parseBoolean(tempS2);
				if(debug) System.out.println(tempB1+" OR "+ tempB2);			
				SAS.push(String.valueOf(tempB1||tempB2));
				if(debug) printStack();
				if(T())
					return true;				
				return true;
			}			
		}
		else if(curChar.equals(")"))
		{
			if(!pS.empty())
				pS.pop();
			else
				new ErrorMessage(expression,currentLocation,
						"This ')' has no matching '(' It is most likely an extra.");
			if(debug) System.out.println(pS);
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
			if(debug) 
			{
				new ErrorMessage(expression,currentLocation,
					"Expecting: ^,v,),. at this location failed T()");
			}
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
		if(debug) System.out.println("Inside L()");
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
			if(debug) 
			{
				new ErrorMessage(expression,currentLocation,
					"Expecting: ~,t,true,f,false,( at this location failed L()");
			}
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
		if(debug) System.out.println("Inside A()");
		if(curChar.equals("t"))
		{
			if(debug) System.out.println("Found true statement");
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
			if(debug) printStack();
			return true;
		}
		else if(curChar.equals("f"))
		{
			if(debug) System.out.println("Found false statement");
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
			if(debug) printStack();
			return true;
		}
		else if(curChar.equals("("))
		{
			pS.push(currentLocation);
			if(debug) System.out.println(pS);
			numOfOpen++;
			accept();
			if(B())
			{
				return true;
			}
			if(curChar.equals(")"))
			{
				if(!pS.empty())
					pS.pop();
				else
					new ErrorMessage(expression,currentLocation,
							"This ')' has no matching '(' It is most likely an extra.");
				if(debug) System.out.println(pS);
				numOfClosed++;
				accept();
				return true;
			}
		}
		else
		{
			if(debug) 
			{
				new ErrorMessage(expression,currentLocation,
					"Expecting: t,true,f,false,( at this location");
			}
			new ErrorMessage(expression,currentLocation,
					"Expecting: t,true,f,false,( at this location");			
		}		
		return false;
	}
	
	private void solve() {
		if(debug) System.out.println("\n\n\nInside solve():\n");
		if(debug) printStack();
		if(debug) System.out.println("Correct: "+correct);
		if(debug) System.out.println("End of Expression: "+endOfExpression);
		if(alreadySolved)
			return;
		if((numOfOpen!=numOfClosed))
		{
			new ErrorMessage(expression,(int)pS.pop(),
					"Must close this open parenthesis before the .");
		}
		if(correct && SAS.size()==1 && endOfExpression && (numOfOpen==numOfClosed))
		{
			System.out.println("The expression is syntaxtically correct");
			System.out.println("The value of the expression is "+SAS.pop());
		}
		else
		{
			if(debug) 
			{
				new ErrorMessage(expression,currentLocation+1,
					"Expecting . at end of expression");
			}
			new ErrorMessage(expression,currentLocation+1,
					"Expecting . at end of expression");			
		}
		System.exit(0);
	}
	private void printStack() {
		if(debug) System.out.println(SAS.toString());
		
	}
}
