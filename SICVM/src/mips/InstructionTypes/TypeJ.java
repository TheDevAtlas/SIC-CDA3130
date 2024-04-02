package mips.InstructionTypes;

public class TypeJ extends Instruction {
  
  
  int address; //later make it actually convert it to hex value
  String funct;

  public TypeJ(String address, String binary){
    super(address, binary);
    parseBinary();
  }

  public void parseBinary() {
    int code = toDecimal(0, 6);
    if (code == 2) funct = "j";
    else funct = "jal"; 
    address = toDecimal(6, 32);
  }

  public String toString() {
    return funct + " " + address;
  }
}
