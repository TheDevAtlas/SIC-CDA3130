/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mips;

/**
 *
 * @author jacob
 */
public class MIPS {
    
    static MainMenu MenuJFrame;
    static EditorMenu EditorJFrame;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Open The Menu Frame //
        MenuJFrame = new MainMenu();
        MenuJFrame.setLocationRelativeTo(null);
        MenuJFrame.setVisible(true);
    }
    
    public static void OpenMIPSEditor(String fileAsString)
    {
        // Close Menu Frame //
        MenuJFrame.setVisible(false);
        
        // Open The Editor Frame //
        EditorJFrame = new EditorMenu();
        EditorJFrame.setLocationRelativeTo(null);
        EditorJFrame.setVisible(true);
        
        // Sets A Text Box To Show Code //
        // Will Change Later To Create The Steps and Micro Steps //
        EditorJFrame.ChangeCodeText(fileAsString);
    }
    
}
