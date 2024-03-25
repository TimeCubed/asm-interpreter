package Interpreter.Exceptions;

public class InvalidRegisterException extends Exception {
	public InvalidRegisterException(int id) {
		super("Cannot find register with id " + id);
	}
}
