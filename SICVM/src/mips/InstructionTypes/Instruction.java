package mips.InstructionTypes;

public class Instruction {
  public String address;
  public String binary;

  public Instruction(String address, String binary) {
    this.address = address;
    this.binary = binary;
  }
  
  public int toDecimal( int start, int end) {
    return Integer.parseInt(binary.substring(start, end), 2);
  }
}
