// Stephanie Liu CS136


import structure5.*;
import java.util.Iterator;

public class Interpreter {

    StackList<Token> stack = new StackList<Token>();
    SymbolTable list = new SymbolTable();
    StackList<Token> tokenList;


    public Interpreter() {

	

    }

    public void interpret(Reader reads) {

	Reader r = reads;
	Token next;
	String theString;
	
	while (r.hasNext()) {
	    next = r.next();

	    if (next.isNumber()) {
		stack.add(next);
	    }
	    else if (next.isBoolean()) {
		stack.add(next);
	    }
	    else if (next.isSymbol() && list.contains(next.getSymbol())) {
		stack.add(list.get(next.getSymbol()));
	    }
		    

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
	    else if (next.toString().equals("dup")) {
		Token a = stack.get();
		stack.add(a);
	    }
	    else if (next.toString().equals("exch")) {
		Token a = stack.get();
		stack.remove();

		Token b = stack.get();
		stack.remove();

		stack.add(a);
		stack.add(b);
	    }
	    else if (next.toString().equals("eq")) {
		Token a = stack.get();
		stack.remove();

		Token b = stack.get();
		stack.remove();

		stack.add(new Token(a.equals(b)));
	    }
	    else if (next.toString().equals("def")) {
		Token a = stack.get();
		stack.remove();
		
		Token b = stack.get();
		Assert.condition(b.getSymbol().substring(0,1).equals("/"), "Precede symbols with '/'");
		stack.remove();

		list.add(b.getSymbol().substring(1), a);
	    }

	    else if (next.toString().equals("pop")) {
		stack.remove();
	    }
	    else if (next.toString().equals("pstack")) {
		for (Token element : stack) {
		    System.out.println(element.toString());
		}
	    }
	    else if (next.toString().equals("ne")) {
		Token a = stack.get();
		stack.remove();
		stack.add(new Token(-1 * a.getNumber()));
	    }
	    else if (next.toString().equals("quit")) {
		System.exit(0);
	    }
	    else if (next.toString().equals("ptable")) {
		System.out.println(list.toString());
	    }
	    else if (next.toString().equals("}")) {
		tokenList = new StackList<Token>();
		while (stack.get().toString() != "{") {
		    tokenList.add(stack.get());
		    stack.remove();
		}
		System.out.println(tokenList.toString());
	    }
	    else {
		stack.add(next);
        
	    }
	    
	   
	}
	
    }

    

    public static void main(String[] args) {

	Reader reads = new Reader();
	Interpreter theInterpreter = new Interpreter();
	theInterpreter.interpret(reads);

    }

}
