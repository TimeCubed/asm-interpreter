package Interpreter;

import Interpreter.Exceptions.IllegalAddressException;
import Interpreter.Exceptions.InvalidRegisterException;
import Interpreter.Exceptions.UnfinishedInstructionException;

public class Main {
	public static void main(String[] args) {
		Interpreter interpreter = new Interpreter();
		
		try {
			interpreter.beginInterpreter();
		} catch (InvalidRegisterException e) {
			System.err.println("Program attempted to access invalid register: ");
			e.printStackTrace(System.err);
		} catch (UnfinishedInstructionException e) {
			System.err.println("Program had an unfinished instruction: ");
			e.printStackTrace(System.err);
		} catch (IllegalAddressException e) {
			System.err.println("Program attempted to access invalid memory address: ");
			e.printStackTrace(System.err);
		}
	}
}