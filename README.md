# PostScript Simulator

Simple java program that interprets postscript

Postscript is a stack-based language (stack: first in, last out), which is designed to describe
graphical images. PostScript printers interpret this language to render images.

This program implements the basic operations (pstack, add, sub, mul, div, dup, exch, eq, ne, def, pop, quit,
and ptable) as well as procedure definitions and calls, and the if instruction.

## Usage
```bash
$ javac Interpreter.java Reader.java SymbolTable.java Token.java
$ java Interpreter < mytext.ps
```
You must input a PostScript text the run the interpreter

## Fun things you can do
```
/fact { dup 0 eq { pop 1 } { dup 1 sub fact mul } ifelse } def => creates a "method" fact that returns the number factorial
5 fact => returns 5!
```


