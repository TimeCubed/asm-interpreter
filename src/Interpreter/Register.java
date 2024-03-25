package Interpreter;

public class Register {
	private int value;
	
	public Register(int initialValue) {
		value = initialValue;
	}
	
	public void write(int value) {
		this.value = value;
	}
	
	public int read() {
		return value;
	}
	
	public void add(int value) {
		this.value += value;
	}
	public void sub(int value) {
		this.value -= value;
	}
	public void mul(int value) {
		this.value *= value;
	}
	public void div(int value) {
		this.value /= value;
	}
}
