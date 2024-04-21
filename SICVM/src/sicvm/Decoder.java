package sicvm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import mips.InstructionTypes.Instruction;
import mips.InstructionTypes.TypeI;
import mips.InstructionTypes.TypeJ;
import mips.InstructionTypes.TypeR;

public class Decoder {
 
  public Decoder() {
  }

  public static LinkedList<Instruction> decode(File file) throws FileNotFoundException, IOException {
    LinkedList<Instruction> stream = new LinkedList<>();

    BufferedReader reader = new BufferedReader(new FileReader(file));

    String line = reader.readLine();

    while(line != null) {
      String[] split = line.split(" ");
      String address = split[0].substring(0, split[0].length() - 2);
      String binary = split[1];

      Instruction instruction;


      int type = getType(binary);
      switch(type) {
        case 0 -> {
            instruction = new TypeR(address, binary);
            stream.add(instruction);
            }
        case 1 -> {
            instruction = new TypeJ(address, binary);
            stream.add(instruction);
            }
        case 2 -> {
            instruction = new TypeI(address, binary);
            stream.add(instruction);
            }
      }
      line = reader.readLine();
    }

    printStream(stream);
    reader.close();
    return stream;
  }

  // 0 = R, 1 = J, 2 = I
  public static int getType(String instruction) {
    //giving the wrong code
    int code = Integer.parseInt(instruction.substring(0, 6), 2);
    if ( code == 0) return 0;
    else if (code == 2 || code == 3) return 1;
    else return 2;

  }

  public static void printStream(LinkedList<Instruction> stream) {

    ListIterator<Instruction> iterator = stream.listIterator(0);

    while( iterator.hasNext()) {
      System.out.println(iterator.next().toString());
    }
  }
}