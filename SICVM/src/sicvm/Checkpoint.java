package sicvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import mips.InstructionTypes.Instruction;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Checkpoint {
    public static void CreateNewCheckpoint()
    {
        // Create New Checkpoint File //
        JSONObject ckpt = new JSONObject();
        
        // Add All Registrrs //
        ckpt.put("pc", CPU.pc);
        ckpt.put("hi", CPU.hi);
        ckpt.put("lo", CPU.lo);
        ckpt.put("zero", CPU.zero);
        ckpt.put("at", CPU.at);
        ckpt.put("v0", CPU.v0);
        ckpt.put("v1", CPU.v1);
        ckpt.put("a0", CPU.a0);
        ckpt.put("a1", CPU.a1);
        ckpt.put("a2", CPU.a2);
        ckpt.put("a3", CPU.a3);
        ckpt.put("t0", CPU.t0);
        ckpt.put("t1", CPU.t1);
        ckpt.put("t2", CPU.t2);
        ckpt.put("t3", CPU.t3);
        ckpt.put("t4", CPU.t4);
        ckpt.put("t5", CPU.t5);
        ckpt.put("t6", CPU.t6);
        ckpt.put("t7", CPU.t7);
        ckpt.put("s0", CPU.s0);
        ckpt.put("s1", CPU.s1);
        ckpt.put("s2", CPU.s2);
        ckpt.put("s3", CPU.s3);
        ckpt.put("s4", CPU.s4);
        ckpt.put("s5", CPU.s5);
        ckpt.put("s6", CPU.s6);
        ckpt.put("s7", CPU.s7);
        ckpt.put("t8", CPU.t8);
        ckpt.put("t9", CPU.t9);
        ckpt.put("k0", CPU.k0);
        ckpt.put("k1", CPU.k1);
        ckpt.put("gp", CPU.gp);
        ckpt.put("sp", CPU.sp);
        ckpt.put("fp", CPU.fp);
        ckpt.put("ra", CPU.ra);
        
        // Add All Instructions // Instruction i in CPU.instructionList
        for (Instruction instructionList : CPU.instructionList) {
            ckpt.put("instruction", instructionList);
        }
        
        // Write To File //
         LocalDateTime dateTime = LocalDateTime.now();
        // Updated to use valid filename characters
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String formattedDateTime = dateTime.format(formatter);
        
        try (FileWriter file = new FileWriter(formattedDateTime + ".ckpt")) {
            file.write(ckpt.toString(4)); // Writing JSON with indentation for readability
            file.flush();
            
            System.out.println("Checkpoint Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void ReadCheckpoint(String filename)
    {
        try (FileReader reader = new FileReader(filename)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject ckpt = new JSONObject(tokener);
            
            CPU.pc = ckpt.getInt("pc");
            CPU.hi = ckpt.getInt("hi");
            CPU.lo = ckpt.getInt("lo");
            CPU.zero = ckpt.getInt("zero");
            CPU.at = ckpt.getInt("at");
            CPU.v0 = ckpt.getInt("v0");
            CPU.v1 = ckpt.getInt("v1");
            CPU.a0 = ckpt.getInt("a0");
            CPU.a1 = ckpt.getInt("a1");
            CPU.a2 = ckpt.getInt("a2");
            CPU.a3 = ckpt.getInt("a3");
            CPU.t0 = ckpt.getInt("t0");
            CPU.t1 = ckpt.getInt("t1");
            CPU.t2 = ckpt.getInt("t2");
            CPU.t3 = ckpt.getInt("t3");
            CPU.t4 = ckpt.getInt("t4");
            CPU.t5 = ckpt.getInt("t5");
            CPU.t6 = ckpt.getInt("t6");
            CPU.t7 = ckpt.getInt("t7");
            CPU.s0 = ckpt.getInt("s0");
            CPU.s1 = ckpt.getInt("s1");
            CPU.s2 = ckpt.getInt("s2");
            CPU.s3 = ckpt.getInt("s3");
            CPU.s4 = ckpt.getInt("s4");
            CPU.s5 = ckpt.getInt("s5");
            CPU.s6 = ckpt.getInt("s6");
            CPU.s7 = ckpt.getInt("s7");
            CPU.t8 = ckpt.getInt("t8");
            CPU.t9 = ckpt.getInt("t9");
            CPU.k0 = ckpt.getInt("k0");
            CPU.k1 = ckpt.getInt("k1");
            CPU.gp = ckpt.getInt("gp");
            CPU.sp = ckpt.getInt("sp");
            CPU.fp = ckpt.getInt("fp");
            CPU.ra = ckpt.getInt("ra");
            
            
        } catch (Exception e) {
            System.err.println("Error parsing JSON from the file: " + filename);
            e.printStackTrace();
        }
    }
}
