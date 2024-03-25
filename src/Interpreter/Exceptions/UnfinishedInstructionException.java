package Interpreter.Exceptions;

public class UnfinishedInstructionException extends Exception {
	public UnfinishedInstructionException(int address) {
		super("Instruction at address " + address + " was unfinished");
	}
}
