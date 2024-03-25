package Interpreter.Exceptions;

public class IllegalAddressException extends Exception {
	public IllegalAddressException(int address, int memSize) {
		super("Illegal address " + address + ", address must be between 0 and memory size (" + memSize + " bytes)");
	}
}