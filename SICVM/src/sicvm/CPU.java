package sicvm;

import java.util.LinkedList;
import mips.InstructionTypes.Instruction;

public class CPU {
    
    // Instructions //
    public static LinkedList<Instruction> instructionList = new LinkedList<>();
    
    // All CPU Registers //
    public static int pc = 0; // Program Counter
    public static int hi = 0; // HI register for division and multiplication high result
    public static int lo = 0; // LO register for division and multiplication low result
    public static int zero = 0; // $0, always zero
    public static int at = 0; // $1, assembler temporary
    public static int v0 = 0, v1 = 0; // $2-$3, function return values
    public static int a0 = 0, a1 = 0, a2 = 0, a3 = 0; // $4-$7, function arguments
    public static int t0 = 0, t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0; // $8-$15, temporary registers
    public static int s0 = 0, s1 = 0, s2 = 0, s3 = 0, s4 = 0, s5 = 0, s6 = 0, s7 = 0; // $16-$23, saved registers
    public static int t8 = 0, t9 = 0; // $24-$25, more temporary registers
    public static int k0 = 0, k1 = 0; // $26-$27, reserved for OS kernel
    public static int gp = 0; // $28, global pointer
    public static int sp = 0; // $29, stack pointer
    public static int fp = 0; // $30, frame pointer
    public static int ra = 0; // $31, return address
    
    // Set All Registers To Zero //
    public void ResetMemoryValues()
    {
        pc = 0;
        hi = 0;
        lo = 0;
        zero = 0;
        at = 0;
        v0 = 0;
        v1 = 0;
        a0 = 0;
        a1 = 0;
        a2 = 0;
        a3 = 0;
        t0 = 0;
        t1 = 0;
        t2 = 0;
        t3 = 0;
        t4 = 0;
        t5 = 0;
        t6 = 0;
        t7 = 0;
        s0 = 0;
        s1 = 0;
        s2 = 0;
        s3 = 0;
        s4 = 0;
        s5 = 0;
        s6 = 0;
        s7 = 0;
        t8 = 0;
        t9 = 0;
        k0 = 0;
        k1 = 0;
        gp = 0;
        sp = 0;
        fp = 0;
        ra = 0;
    }
    
    // Set The Register Based On Input String And Value //
    public static void setRegister(String register, int value) {

        switch(register) {
            case "at" -> at = value;
            case "v1" -> v1 = value;
            case "v0" -> v0 = value;
            case "a0" -> a0 = value;
            case "a1" -> a1 = value;
            case "a2" -> a2 = value;
            case "a3" -> a3 = value;
            case "t0" -> t0 = value;
            case "t1" -> t1 = value;
            case "t2" -> t2 = value;
            case "t3" -> t3 = value;
            case "t4" -> t4 = value;
            case "t5" -> t5 = value;
            case "t6" -> t6 = value;
            case "t7" -> t7 = value;
            case "s0" -> s0 = value;
            case "s1" -> s1 = value;
            case "s2" -> s2 = value;
            case "s3" -> s3 = value;
            case "s4" -> s4 = value;
            case "s5" -> s5 = value;
            case "s6" -> s6 = value;
            case "s7" -> s7 = value;
            case "t8" -> t8 = value;
            case "t9" -> t9 = value;
            case "gp" -> gp = value;
            case "sp" -> sp = value;
            case "fp" -> fp = value;
            case "ra" -> ra = value;
            default -> System.out.println("register does not exist");
        }  
    }

    // Return The Register Value Based On Input String //
    public static int getRegister(String register) {
        switch(register) {
            case "at" -> {
                return at;
            }
            case "v0" -> {
                return v0;
            }
            case "v1" -> {
                return v1;
            }
            case "a0" -> {
                return a0;
            }
            case "a1" -> {
                return a1 ;
            }
            case "a2" -> {
                return a2 ;
            }
            case "a3" -> {
                return a3 ;
            }
            case "t0" -> {
                return t0 ;
            }
            case "t1" -> {
                return t1 ;
            }
            case "t2" -> {
                return t2 ;
            }
            case "t3" -> {
                return t3 ;
            }
            case "t4" -> {
                return t4 ;
            }
            case "t5" -> {
                return t5 ;
            }
            case "t6" -> {
                return t6 ;
            }
            case "t7" -> {
                return t7 ;
            }
            case "s0" -> {
                return s0 ;
            }
            case "s1" -> {
                return s1 ;
            }
            case "s2" -> {
                return s2 ;
            }
            case "s3" -> {
                return s3 ;
            }
            case "s4" -> {
                return s4 ;
            }
            case "s5" -> {
                return s5 ;
            }
            case "s6" -> {
                return s6 ;
            }
            case "s7" -> {
                return s7 ;
            }
            case "t8" -> {
                return t8 ;
            }
            case "t9" -> {
                return t9 ;
            }
            case "gp" -> {
                return gp ;
            }
            case "sp" -> {
                return sp ;
            }
            case "fp" -> {
                return fp ;
            }
            case "ra" -> {
                return ra;
            }
            default -> {
                System.out.println("register does not exist");
                return -1;
            }
        }  
    }
    
    // When The CPU Does Something, Show That On The User Side //
    public static void UpdateMemoryFromCPU()
    {
        String memText = "";
        
        memText += "at " + Integer.toHexString(at) + "\n";
        memText += "v1 " + v1 + "\n";
        memText += "v0 " + v0 + "\n";
        memText += "a0 " + a0 + "\n";
        memText += "a1 " + a1 + "\n";
        memText += "a2 " + a2 + "\n";
        memText += "a3 " + a3 + "\n";
        memText += "t0 " + t0 + "\n";
        memText += "t1 " + t1 + "\n";
        memText += "t2 " + t2 + "\n";
        memText += "t3 " + t3 + "\n";
        memText += "t4 " + t4 + "\n";
        memText += "t5 " + t5 + "\n";
        memText += "t6 " + t6 + "\n";
        memText += "t7 " + t7 + "\n";
        memText += "s0 " + s0 + "\n";
        memText += "s1 " + s1 + "\n";
        memText += "s2 " + s2 + "\n";
        memText += "s3 " + s3 + "\n";
        memText += "s4 " + s4 + "\n";
        memText += "s5 " + s5 + "\n";
        memText += "s6 " + s6 + "\n";
        memText += "s7 " + s7 + "\n";
        memText += "t8 " + t8 + "\n";
        memText += "t9 " + t9 + "\n";
        memText += "gp " + gp + "\n";
        memText += "sp " + sp + "\n";
        memText += "fp " + fp + "\n";
        memText += "ra " + ra + "\n";
        
        SICVM.editor.MemInputOutput.setText(memText);
    }
    
    // When The User Does Something, Change The CPU To Do That //
    public void UpdateCPUFromMemory()
    {
        
    }
}
