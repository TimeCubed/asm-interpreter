package Interpreter;

public class Stack {
	private final int[] internalStack;
	private final int stackSize;
	private int stackPointer;
	
	public Stack(int stackSize) {
		this.stackSize = stackSize;
		internalStack = new int[this.stackSize];
		stackPointer = 0;
	}
	
	public void push(int number) {
		internalStack[stackPointer] = number;
		
		stackPointer++;
		
		// loop stackPointer to 0 if it is above the stackSize
		if (stackPointer >= stackSize - 1) {
			stackPointer = 0;
		}
	}
	
	public int pop() {
		stackPointer--;
		
		// loop stackPointer over if it is below index 0
		if (stackPointer < 0) {
			stackPointer = stackSize - 1;
		}
		
		return internalStack[stackPointer];
	}
}