// Stephanie Liu CS136

import structure5.*;
import java.util.Iterator;

public class Interpreter {

    StackList<Token> stack; // The stack
    SymbolTable list; // The symbol table
    Token next; // Each token the reader parses through


    public Interpreter() {

	stack = new StackList<Token>();
	list = new SymbolTable();

    }

    public void interpret(Reader reads) {

	int arrayTr = 0;
	boolean define = false;
	Reader r = reads;

    	while (r.hasNext()) {
	    
	    // Advance to the next Token
	    next = r.next();

	    // Add the number to the stack
	    if (next.isNumber()) {
		stack.add(next);
	    }
	    // Add the boolean
	    else if (next.isBoolean()) {
		stack.add(next);
	    }
	    // If we're trying to define this procedure, don't run it, just add it to the stack
	    else if (next.isProcedure() && define ) {
		stack.add(next);
	    }
	    // Run the procedure
	    else if (next.isProcedure() && !define ) {
		if (r.next().toString().equals("for")) {
		    forLoop(next);
		}
		else {
		    runProcedure(next);
		}
	    }
	    // Add the array
	    else if (next.isArray()) {
		stack.add(next);
	    }
	    // If the token is a symbol, and that symbol has been defined, use it
	    else if (next.isSymbol() && list.contains(next.getSymbol())) {
		runProcedure(list.get(next.getSymbol()));
	    }
		
	    // Define this new symbol 
	    else if (next.toString().startsWith("/")) {
	        define = true;
		stack.add(next);
		
	    }
	    // Add the definition
	    else if (next.toString().equals("def") && define ) {
		Token def = stack.get();
		stack.remove();
		Token symbol = stack.get();
		list.add(symbol.toString().substring(1), def);
		define = false;
		stack.remove();
	    }
	    // Add the previous two numbers
	    else if (next.toString().equals("add")) {
		Token a  = stack.get();
		Assert.condition( a.isNumber(), "Add two numbers!");
		stack.remove();
		Token b = stack.get();
		Assert.condition( b.isNumber(), "Add two numbers!");
		stack.remove();
		Token sum = new Token(a.getNumber() + b.getNumber());
		stack.add(sum);
	    }
	    // Subtract the previous number from the previous-previous number
	    else if (next.toString().equals("sub")) {
		Token a  = stack.get();
		Assert.condition( a.isNumber(), "Subtract two numbers!");
		stack.remove();
		Token b = stack.get();
		Assert.condition( b.isNumber(), "Subtract two numbers!");
		stack.remove();
		Token subtract = new Token(b.getNumber() - a.getNumber());
		stack.add(subtract);
	    }
	    // Multiply the previous two numbers
	    else if (next.toString().equals("mul")) {
		Token a  = stack.get();
		Assert.condition( a.isNumber(), "Multiply two numbers!");
		stack.remove();
		Token b = stack.get();
		Assert.condition( b.isNumber(), "Multiply two numbers!");
		stack.remove();
		Token prod = new Token(a.getNumber() * b.getNumber());
		stack.add(prod);		
	    }
	    // Divide the previous number by the previous-previous number
	    else if (next.toString().equals("div")) {
		Token a  = stack.get();
		Assert.condition( a.isNumber(), "Divide two numbers!");
		stack.remove();
		Token b = stack.get();
		Assert.condition( b.isNumber(), "Divide two numbers!");
		stack.remove();
		Token divide = new Token(b.getNumber() / a.getNumber());
		stack.add(divide);
	    }
	    // Duplicate the previous entry
	    else if (next.toString().equals("dup")) {
		Token a = stack.get();
		stack.add(a);
	    }
	    // Exchange two entries
	    else if (next.toString().equals("exch")) {
		Token a = stack.get();
		stack.remove();
		Token b = stack.get();
		stack.remove();
		stack.add(a);
		stack.add(b);
	    }
	    // Check if the previous two entries were equivalent
	    else if (next.toString().equals("eq")) {
		Token a = stack.get();
		stack.remove();
		Token b = stack.get();
		stack.remove();
		stack.add(new Token(a.equals(b)));
	    }
	    // Remove the last thing on the stack
	    else if (next.toString().equals("pop")) {
		stack.remove();
	    }
	    // Print the stack
	    else if (next.toString().equals("pstack")) {
		for (Token element : stack) {
		    System.out.println(element.toString());
		}
	    }
	    // Check if the previous two entries were not equivalent
	    else if (next.toString().equals("ne")) {
		Token a = stack.get();
		stack.remove();
		Token b = stack.get();
		stack.remove();
		stack.add(new Token(!a.equals(b)));
	    }
	    // Quit the program
	    else if (next.toString().equals("quit")) {
		System.exit(0);
	    }
	    // Print the symbol table
	    else if (next.toString().equals("ptable")) {
		System.out.println(list.toString());
	    }
	    // Check if the previous-previous entry is less than the previous entry
	    else if (next.toString().equals("lt")) {
		Token a = stack.get(); // First int
		stack.remove();
		Token b = stack.get(); // Second int
		stack.remove();
		Assert.condition(a.isNumber() && b.isNumber(), "Compare two numbers!");
		// Add the boolean
		stack.add(new Token(b.getNumber() < a.getNumber()));
	    }
	    // The array has started, keep track of this with "arrayTr" and add the "[" to
	    // the stack
	    else if (next.toString().equals("[")) {
		arrayTr++;
		stack.add(next);
	    }
	    // If the array has been closed, and it was opened before, then add all the elements
	    // between "[" and "]" to an array and make that a Token and add it to the stack
	    else if (next.toString().equals("]") && arrayTr > 0) {
		Vector<Token> tokenArr = new Vector<Token>();
		// Add the stuff on the stack to the array, one by one
		while (!stack.get().toString().equals("[")) {
		    tokenArr.add(0, stack.get());
		    stack.remove();
		}
		stack.remove();
		stack.add(new Token(tokenArr));
		// This array has been taken care of
		arrayTr--;
	    }
	    // If the "[" has been added to the stack, add all the Tokens after it
	    // to the stack (and we'll remove them later when we see the "]")
	    else if (arrayTr > 0) {
		stack.add(next);
	    }
	    // Get the sum of the elements in the array
	    else if (next.toString().equals("sum")) {
		Token a = stack.get(); // array
		stack.remove();
		// Ensure this array is an array of ints
		Assert.condition(a.isArray(), "Sum the entries of an array!");
		Vector<Token> arr = a.getArray();
		int sum = 0; // sum of the elements
		// Go through the vector and add up at each entry
		for (int i = 0; i<arr.size(); i++) {
		    Assert.condition(arr.get(i).isNumber(), "Sum numbers in an array!");
		    sum += arr.get(i).getNumber();
		}
		stack.add(new Token(sum));
	    }
	    // Get the length of the array
	    else if (next.toString().equals("length")) {
		Token a = stack.get(); // array
		stack.remove();
		Assert.condition(a.isArray(), "Find the length of an array!");
		Vector<Token> arr = a.getArray();
		int leng = arr.size(); // length
		stack.add(new Token(leng));
	    }
	    // Gets the value at the index specified in the array
	    else if (next.toString().equals("get")) {
		Token a = stack.get(); // index
		stack.remove();
		Assert.condition(a.isNumber(), "Include the index for the array (should be a number)!");
		int ind = (int)a.getNumber(); 
		Token b = stack.get(); // array
		stack.remove();
		Assert.condition(b.isArray(), "Find the value at an index for an array!");
		Vector<Token> arr = b.getArray(); 
		Assert.condition(ind < arr.size(), "That index is bigger than the size of the array!");
		// Add the value at that index in the array to the stack
		stack.add(arr.get(ind)); 
	    }
	  
	}	
    }
    
    public void runProcedure(Token proc) {

	if ( proc.toString().contains("ifelse")) {
	    
	    List<Token> theProc = proc.getProcedure();
	    
	    // Make a new copy of the procedure
	    DoublyLinkedList<Token> procCopy = new DoublyLinkedList<Token>();
	    for (int i = 0; i<theProc.size(); i ++) {
		procCopy.addLast(theProc.get(i));
	    }
	    
	    procCopy.removeLast();
	    // Store the statement that will be executed if the condition is not true
	    Token elseStatement = procCopy.getLast();
  
	    procCopy.removeLast();
	    
	    // Store the statement that will be executed if the condition is true
	    Token ifStatement = procCopy.getLast();
	    
	    procCopy.removeLast();

	    // Execute the condition
	    interpret(new Reader(procCopy));	 
	    
	    Assert.condition(stack.get().isBoolean(), "If statements must be preceded with a true/false condition");

	    // If the condition came out true, run the if statement
	    if (stack.get().getBoolean()) {
		stack.remove(); // Remove the true/false Token
		runProcedure(ifStatement);
	    }
	    // If the condition came out false, run the else statement
	    else {
		stack.remove(); // Remove the true/false Token
		runProcedure(elseStatement);
       	    }
		
	}
	
	else if (proc.toString().contains("if")) {
		
	    List<Token> theProc = proc.getProcedure();

	    // Make a new copy of the procedure
	    DoublyLinkedList<Token> procCopy = new DoublyLinkedList<Token>();
	    for (int i = 0; i<theProc.size(); i ++) {
		procCopy.addLast(theProc.get(i));
	    }
	    	    
	    procCopy.removeLast();
	    
	    // Store the procedure that will be executed if the condition is true
	    Token statement = procCopy.getLast();

	    procCopy.removeLast();

	    // Execute the condition
	    interpret(new Reader(procCopy));	 
	    
	    Assert.condition(stack.get().isBoolean(), "If statements must be preceded with a true/false condition");

	    // If the condition is true, execute the statement
	    if (stack.get().getBoolean()) {
		stack.remove(); // Remove the true/false Token from the stack
		runProcedure(statement);
	    }
	    else {
		stack.remove(); // Remove the true/false Token from the stack
	    }
	}
	// If the procedure does not contain an if/ifelse statement, or a for loop, just execute the procedure
	else {
	    interpret(new Reader(proc)); 
	}
    }

    // For loop implementation
    public void forLoop(Token proc) {

	// Store the numbers for the limit, the increment, and the starting index
	double limit = stack.get().getNumber();
	stack.remove();
	double incre = stack.get().getNumber();
	stack.remove();
	double index = stack.get().getNumber();
	stack.remove();

	// If index is less than the limit, run the loop until the index is greater than the limit
	if (index < limit) {
	    for (double i = index; i<=limit; i+=incre) {
		stack.add(new Token(i));
		runProcedure(proc);
	    }
	}
	// If index is greater than the limit, run the loop until the index is less than the limit
	else if (index > limit) {
	    for (double i = index; i>=limit; i+=incre) {
		stack.add(new Token(i));
		runProcedure(proc);
	    }
	}
	// If index is the limit, just add that number
	else {
	    stack.add(new Token(index));
	    runProcedure(proc);
	}
    }

    // Main method
    public static void main(String[] args) {

	Reader reads = new Reader();
	Interpreter theInterpreter = new Interpreter();
	theInterpreter.interpret(reads);

    }
}
