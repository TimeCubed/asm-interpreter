# REGISTERS
<hr/>
Flag register, contains flags like zero flag, carry flag and
A -> H: General purpose data registers

# INSTRUCTIONS:
<hr/>

## Miscellaneous
HLT -> HaLT execution
OUT r -> OUTput register

## Data Instructions
MOV r1, r2  -> MOVe value from register to register
LDM r, addr -> LoaD value from Memory to register
LDI r, val  -> LoaD Immediate value to register
STV r, addr -> STore Value from register in memory
POP r       -> POP value from stack and move it to a register
PSH r       -> PuSH value to stack from a register

## Arithmetic and logic instructions
ADM r, addr -> ADd value from Memory to register
SBM r, addr -> SuBtract from register by a value from memory
MLM r, addr -> MuLtiply register by value from Memory
DVM r, addr -> DiVide register by value from Memory

ADR r1, r2  -> ADd value from Register to register
SBR r1, r2  -> SuBtract from register by value from Register
MLR r1, r2  -> MuLtiply register by value from Register
DVR r1, r2  -> DiVide register by value from Register

AND r1, r2  -> bitwise AND operator between two registers
NOT r1, r2  -> bitwise NOT operator between two registers
OR  r1, r2  -> bitwise OR operator between two registers
XOR r1, r2  -> bitwise XOR operator between two registers
SHR r1, r2  -> SHift register value Right
SHL r1, r2  -> SHift register value Left
ROR r       -> ROtate register value Right
ROL r       -> ROtate register value Left

CMP -> CoMPare two registers (equal = 1, not equal = 0)

## Jump Instructions (for all the turing completeness)
JMP addr    -> JuMP unconditionally
JNZ addr    -> Jump if Not Zero
JPZ addr    -> JumP if Zero
JPC addr    -> JumP if Carry
JPP addr    -> JumP if Positive
JSR addr    -> Jump to SubRoutine
RET addr    -> RETurn from subroutine

## Currently implemented instructions:
OUT, HLT, MOV, LDM, LDI, STV, POP, PSH