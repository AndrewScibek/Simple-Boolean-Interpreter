Interpreter
===========
***BETA***
A simple boolean expression interpreter

The syntax is as follows:				
P::=S
S::=V:=<E><S1>
 ::=read(V)<S1>
 ::=write(V)<S1>
 ::=if C do S od<S1>           
 ::=while C do S od S1                
S1::=;S              
  ::=                 
C::=a<C'>             
C'::= <<C2>              
  ::= ><C2>             
  ::= =E2                
  ::= !=E2             
C2::= E2                 
  ::= =E2              
E::=<T><E'>             
E'::=+<T><E'>            
  ::=-<T><E'>           
  ::=              
T::=<F><T'>               
T'::=*<F><T'>              
  ::=/<F><T'>               
F::=(<E>)               
 ::=cons              
 ::=V                    
