package sicvm;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class OpenMIPSFile {
    public static int Open() throws IOException
    {
        // Set Up File Explorer //
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*.txt, *.asm, *.ckpt";
            }

            @Override
            public boolean accept(File f) {
                return true;
            }
        });
        
        // Open A File Explorer To Find MIPS File //
        fileChooser.showOpenDialog(null);
        
        // Get The File //
        File file = fileChooser.getSelectedFile();
        
        // Check The Type Of File, Then Proccess That File //
        if(file.getName().contains(".txt"))
        {
            // This File Is Assembled Code //
            System.out.println("File Is .txt");
            
            CPU.instructionList = Decoder.decode(file);
            
            return 1;
        }
        else
        if(file.getName().contains(".asm"))
        {
            // This File Is Writen Out Assembly //
            System.out.println("File Is .asm");
            
            return 1;
        }
        else
        if(file.getName().contains(".ckpt"))
        {
            // This File Is Our Custom Checkpoint File //
            System.out.println("File Is .ckpt");
            
            // Set The Values Of CPU To The Checkpoint Values //
            // This Includes Registers and Instructions //
            Checkpoint.ReadCheckpoint(file.getName());
            
            return 1;
        }
        else
        {
            // File Can Not Be Proccessed, Do Not Open Editor //
            System.out.println("Incorrect File Type");
            
            return -1;
        }
        
        
    }
}
