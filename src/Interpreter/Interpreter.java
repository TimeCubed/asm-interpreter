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
	
	// Register shorthands
	private static final int REG_1 = 0;
	private static final int REG_2 = 1;
	private static final int REG_3 = 2;
	private static final int REG_4 = 3;
	private static final int REG_5 = 4;
	private static final int REG_6 = 5;
	private static final int REG_7 = 6;
	private static final int REG_8 = 7;
	
	// Instruction IDs
	private static final int OUT = 0b00000000;
	private static final int HLT = 0b00000001;
	private static final int MOV = 0b00000010; // MOV r1, r2
	private static final int LDM = 0b00000011; // LDM r1, addr
	private static final int LDI = 0b00000100; // LDI r1, val
	private static final int STV = 0b00000101; // STV r1, addr
	private static final int POP = 0b00000110; // POP r1
	private static final int PSH = 0b00000111; // PSH r1
	private static final int ADM = 0b00001000;
	
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
				LDI, REG_1, 0x123,
				MOV, REG_1, REG_2,
				OUT, REG_1,
				STV, REG_2, 0x0,
				LDM, REG_3, 0x0,
				OUT, REG_3,
				LDI, REG_4, 0x1,
				PSH, REG_4,
				POP, REG_5,
				OUT, REG_5,
				ADM, REG_1, 0x0,
				OUT, REG_1,
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
					
					System.out.println(programCounter + " Output from register " + (register + 1) + ": " + registers[register].read());
					lastOperationVal = registers[register].read();
					
					programCounter += 2;
				}
				case HLT -> {
					long msEndTime = System.currentTimeMillis();
					
					System.out.println("Finished execution in " + (msEndTime - msStartTime) + "ms.");
					
					return;
				}
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
					
					System.out.println(programCounter + " Moving value from register " + (register1 + 1) + " to register " + (register2 + 1));
					
					registers[register2].write(registers[register1].read());
					
					lastOperationVal = registers[register2].read();
					
					programCounter += 3;
				}
				case LDM -> {
					if (progMem.length - programCounter < 3) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					int address = progMem[programCounter + 2];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Loading value from address " + address + " to register " + (register + 1));
					
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
					
					System.out.println(programCounter + " Writing value " + value + " to register " + (register + 1));
					
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
					
					System.out.println(programCounter + " Writing value " + registers[register].read() + " from register " + (register + 1) + " to memory at address " + address);
					
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
					
					System.out.println(programCounter + " Popping value from stack to register " + (register + 1));
					
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
				case ADM -> {
					if (progMem.length - programCounter < 3) {
						throw new UnfinishedInstructionException(programCounter);
					}
					
					int register = progMem[programCounter + 1];
					
					if (register < 0 || register > MAX_REGISTERS) {
						throw new InvalidRegisterException(register);
					}
					
					System.out.println(programCounter + " Adding memory value " + memory.read(progMem[programCounter + 2]) + " to register " + (register + 1));
					
					registers[register].add(memory.read(progMem[programCounter + 2]));
					
					lastOperationVal = registers[register].read();
					programCounter += 3;
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