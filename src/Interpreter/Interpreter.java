package Interpreter;

import Interpreter.Exceptions.IllegalAddressException;
import Interpreter.Exceptions.InvalidRegisterException;
import Interpreter.Exceptions.UnfinishedInstructionException;

public class Interpreter {
	// 1 byte = 1 int value
	
	// 128 bytes of stack
	public static final int STACK_SIZE = 128;
	// 64 bytes of call stack
	public static final int CALL_STACK_SIZE = 64;
	// 64k of memory
	public static final int MEM_SIZE = 65536;
	
	private int programCounter;
	private final Stack stack;
	private final Stack callStack;
	private final Memory memory;
	
	// Interpreter.Register array (for A to H registers)
	private final Register[] registers = new Register[8];
	public static final int MAX_REGISTERS = 8;
	
	// Flags register
	private final Register[] flags = new Register[4];
	
	// Instruction IDs
	public static final int OUT = 0b00000000;
	public static final int HLT = 0b00000001;
	public static final int MOV = 0b00000010; // MOV r1, r2
	public static final int LDM = 0b00000011; // LDM r1, addr
	public static final int LDI = 0b00000100; // LDI r1, val
	public static final int STV = 0b00000101; // STV r1, addr
	public static final int POP = 0b00000110; // POP r1
	public static final int PSH = 0b00000111; // PSH r1
	
	private final int[] progMem;
	
	public Interpreter() {
		// Initialize memory, stack and call stack
		
		stack = new Stack(STACK_SIZE);
		callStack = new Stack(CALL_STACK_SIZE);
		memory = new Memory(MEM_SIZE);
		
		// Initialize each data register
		for (int i = 0; i < registers.length; i++) {
			registers[i] = new Register(0);
		}
		
		// Initialize the flags register
		for (int i = 0; i < flags.length; i++) {
			flags[i] = new Register(0);
		}
		
		// Initialize program memory
		// Each 8 bit value represents a possible instruction/argument
		
		// This is a test program to test each instruction
		progMem = new int[] {
				LDI, 0x0, 0x123,
				MOV, 0x0, 0x1,
				OUT, 0x1,
				STV, 0x1, 0x0,
				LDM, 0x2, 0x0,
				OUT, 0x2,
				LDI, 0x3, 0x1,
				PSH, 0x3,
				POP, 0x4,
				OUT, 0x4,
		};
	}
	
	public void beginInterpreter() throws UnfinishedInstructionException, InvalidRegisterException, IllegalAddressException {
		long msStartTime = System.currentTimeMillis();
		System.out.println("Beginning execution.");
		
		for (programCounter = 0; programCounter < progMem.length;) {
			int lastOperationVal = 0;
			
			switch (progMem[programCounter]) {
				// Each instruction is expected to advance the program counter by itself.
				// I couldn't find a simple way to automatically advance the program counter,
				// so I had to settle for this instead.
				
				// Each instruction should be added here as its own separate switch case
				case OUT -> {
					if (progMem.length - programCounter < 2) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Output from register " + register + ": " + registers[register].read());
					lastOperationVal = registers[register].read();
					
					programCounter += 2;
				}
				// Halt program
				case HLT -> {
					long msEndTime = System.currentTimeMillis();
					
					System.out.println("Finished execution in " + (msEndTime - msStartTime) + "ms.");
					
					return;
				}
				// Move value from first register to second register
				case MOV -> {
					if (progMem.length - programCounter < 3) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register1 = progMem[programCounter + 1];
					int register2 = progMem[programCounter + 2];
					
					if (register1 < 0 || register1 > MAX_REGISTERS) {
						throw new InvalidRegisterException(register1);
					}
					
					if (register2 < 0 || register2 > MAX_REGISTERS) {
						throw new InvalidRegisterException(register2);
					}
					
					System.out.println(programCounter + " Moving value from register " + register1 + " to register " + register2);
					
					registers[register2].write(registers[register1].read());
					
					lastOperationVal = registers[register2].read();
					
					programCounter += 3;
				}
				// Load from memory
				case LDM -> {
					if (progMem.length - programCounter < 3) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					int address = progMem[programCounter + 2];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Loading value from address " + address + " to register " + register);
					
					registers[register].write(memory.read(address));
					lastOperationVal = registers[register].read();
					
					programCounter += 3;
				}
				case LDI -> {
					if (progMem.length - programCounter < 2) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					int value = progMem[programCounter + 2];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Writing value " + value + " to register " + register);
					
					registers[register].write(value);
					lastOperationVal = registers[register].read();
					
					programCounter += 3;
				}
				case STV -> {
					if (progMem.length - programCounter < 3) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					int address = progMem[programCounter + 2];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Writing value " + registers[register].read() + " from register " + register + " to memory at address " + address);
					
					memory.write(address, registers[register].read());
					
					programCounter += 3;
				}
				case POP -> {
					if (progMem.length - programCounter < 2) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Popping value from stack to register " + register);
					
					registers[register].write(stack.pop());
					
					lastOperationVal = registers[register].read();
					programCounter += 2;
				}
				case PSH -> {
					if (progMem.length - programCounter < 2) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Pushing value " + registers[register].read() + " to stack");
					
					stack.push(registers[register].read());
					
					lastOperationVal = registers[register].read();
					programCounter += 2;
				}
			}
			
			updateFlags(lastOperationVal);
		}
		
		long msEndTime = System.currentTimeMillis();
		
		System.out.println("Finished execution in " + (msEndTime - msStartTime) + "ms.");
	}
	
	private void updateFlags(int lastReturn) {
		int ZERO_FLAG = 0;
		if (lastReturn == 0) {
			flags[ZERO_FLAG].write(1);
		} else {
			flags[ZERO_FLAG].write(0);
		}
		
		// TODO: implement carry
//		int CARRY_FLAG = 1;
		
		int SIGN_FLAG = 2;
		if (lastReturn < 0) {
			flags[SIGN_FLAG].write(1);
		} else {
			flags[SIGN_FLAG].write(0);
		}
	}
}