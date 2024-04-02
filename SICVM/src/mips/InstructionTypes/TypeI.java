package mips.InstructionTypes;

import java.util.HashMap;

public class TypeI extends Instruction {
  
  String funct;
  int code;
  int $rs;
  int $rt;
  int inm;

    //move this somewhere to import it in
  HashMap<Integer, String> functs = new HashMap<>(); 

  public TypeI(String address, String binary){
    super(address, binary);

    functs.put(4,"beq");
    functs.put(5,"bne");
    functs.put(6,"blez");
    functs.put(7,"bgtz");
    functs.put(8,"addi");
    functs.put(9,"addiu");
    functs.put(10,"slti");
    functs.put(11,"sltiu");
    functs.put(12,"andi");
    functs.put(13,"ori");
    functs.put(14,"xori");
    functs.put(15,"lui");
    functs.put(32,"lb");
    functs.put(33,"lh");
    functs.put(34,"lw");
    functs.put(36,"lbu");
    functs.put(37,"lhu");
    functs.put(40,"sb");
    functs.put(41,"sh");
    functs.put(43,"sw");

    parseBinary();
  }

  public void parseBinary() {

    code = toDecimal(0,6);
    funct = functs.get(code);
    $rs = toDecimal(6, 11);
    $rt = toDecimal(11, 16);
    inm = parseSignedInt(binary.substring(16,32));
  }

  private int parseSignedInt(String s) {
    int sign = Integer.parseInt(s.substring(0, 1));

    int num = toDecimal(17, 32);

    if (sign == 0) return num;

    return num * -1;
  }

  public String toString() {

    if (code == 6 || code == 7 || code == 15) return funct + " " + $rt + ", " + inm;
    else if (code >= 32) return funct + " " + $rt + ", " + inm + "(" + $rs + ")";  
   return funct + " " + $rs + ", " + $rt + ", " + inm;
  }
  
}
