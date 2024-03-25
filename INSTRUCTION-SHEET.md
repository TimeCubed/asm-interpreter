# REGISTERS
<hr/>
Flag register, contains flags like zero flag, carry flag and
A -> H: General purpose data registers

# INSTRUCTIONS:
<hr/>

## Miscellaneous
HLT -> HaLT execution

## Data Instructions
MOV -> MOVe value from register to register
LDM -> LoaD value from Memory to register
LDI -> LoaD Immediate value to register
STV -> STore Value from register in memory
POP -> POP value from stack and move it to a register
PSH -> PuSH value to stack from a register

## Arithmetic and logic instructions
ADM -> ADd value from Memory to register
SBM -> SuBtract from register by a value from memory
MLM -> MuLtiply register by value from Memory
DVM -> DiVide register by value from Memory

ADR -> ADd value from Register to register
SBR -> SuBtract from register by value from Register
MLR -> MuLtiply register by value from Register
DVR -> DiVide register by value from Register

AND -> bitwise AND operator between two registers
NOT -> bitwise NOT operator between two registers
OR  -> bitwise OR operator between two registers
XOR -> bitwise XOR operator between two registers
SHR -> SHift register value Right
SHL -> SHift register value Left
ROR -> ROtate register value Right
ROL -> ROtate register value Left

CMP -> CoMPare two registers (equal = 1, not equal = 0)

## Jump Instructions (for all the turing completeness)
JMP -> JuMP unconditionally
JNZ -> Jump if Not Zero
JPZ -> JumP if Zero
JPC -> JumP if Carry
JPP -> JumP if Positive
JSR -> Jump to SubRoutine
RET -> RETurn from subroutine

## Currently implemented instructions:
OUT, HLT, MOV, LDM, LDI, STV, POP, PSH