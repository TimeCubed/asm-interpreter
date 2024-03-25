package Interpreter;

import Interpreter.Exceptions.IllegalAddressException;

public class Memory {
	private final int[] internalMemory;
	private final int memSize;
	
	public Memory(int memSize) {
		this.memSize = memSize;
		internalMemory = new int[this.memSize];
	}
	
	public int read(int address) throws IllegalAddressException {
		if (address < 0 || address > memSize - 1) {
			throw new IllegalAddressException(address, memSize);
		}
		
		return internalMemory[address];
	}
	
	public void write(int address, int value) throws IllegalAddressException {
		if (address < 0 || address > memSize - 1) {
			throw new IllegalAddressException(address, memSize);
		}
		
		internalMemory[address] = value;
	}
}
