package mips.InstructionTypes;

import java.util.HashMap;

public class TypeR extends Instruction {
  
  String $rs;
  String $rt;
  String $rd;
  int shamt;
  String funct;
  int code;

  HashMap<Integer, String> functs = new HashMap<Integer, String>();

  public TypeR(String address, String binary){
    super(address, binary);

    //move this to an import so it isnt doing it on every new instruction
    functs.put(0, "sll");
    functs.put(2, "srl");
    functs.put(3, "sra");
    functs.put(4, "sllv");
    functs.put(6, "srlv");
    functs.put(7, "srav");
    functs.put(8, "jr");
    functs.put(9, "jalr");
    functs.put(12, "syscall");
    functs.put(16, "mfhi");
    functs.put(17, "mthi");
    functs.put(18, "mflo");
    functs.put(19, "mtlo");
    functs.put(24, "mult");
    functs.put(25, "multu");
    functs.put(26, "div");
    functs.put(27, "divu");
    functs.put(32, "add");
    functs.put(33, "addu");
    functs.put(34, "sub");
    functs.put(35, "subu");
    functs.put(36, "and");
    functs.put(37, "or");
    functs.put(38, "xor");
    functs.put(39, "nor");
    functs.put(42, "slt");
    functs.put(43, "sltu");



    parseBinary();
  }

  private void parseBinary() {
    $rs = parseRegister(toDecimal(6,11));
    $rt = parseRegister(toDecimal(11, 16));
    $rd = parseRegister(toDecimal(16,21 ));
    shamt = toDecimal(21, 26);
    code = toDecimal(26,32);
    funct = functs.get(code);

  }

  public String toString() {

    switch(code) {
      case 0, 1, 3:
        return funct + " " + $rd + ", " + $rt + ", " + shamt;
      case 4, 6, 7, 32, 33, 34, 35, 36, 37, 38, 39, 42, 43:
        return funct + " " + $rd + ", " + $rs + ", " + $rt;
      case 8, 9, 17, 19:
        return funct + " " + $rs;
      case 12:
        return funct;
      case 16, 18:
        return funct + " " + $rd;
      default:
        return "error";
    }

  }


}
