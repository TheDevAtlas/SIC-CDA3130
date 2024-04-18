/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mips;

import java.util.LinkedList;
import mips.InstructionTypes.Instruction;

public class MIPS {
    
    static MainMenu MenuJFrame;
    static EditorMenu EditorJFrame;

    
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
    
    public static void OpenMIPSEditor(String fileAsString, LinkedList<Instruction> stream)
    {
        // Close Menu Frame //
        MenuJFrame.setVisible(false);
        
        // Open The Editor Frame //
        EditorJFrame = new EditorMenu();
        EditorJFrame.setLocationRelativeTo(null);
        EditorJFrame.setVisible(true);
        
        // Sets A Text Box To Show Code //
        // Will Change Later To Create The Steps and Micro Steps //
        EditorJFrame.ChangeCodeText(fileAsString, stream);
    }

    
    public static void setRegister(String register, int value) {

        switch(register) {
            case "at":
                at = value;
                break;
            case "v0":
                v0 = value;
                break;
            case "v1":
                v1 = value;
                break;
            case "a0":
                a0 = value;
                break;
            case "a1":
                a1 = value;
                break;
            case "a2":
                a2 = value;
                break;
            case "a3":
                a3 = value;
                break;
            case "t0":
                t0 = value;
                break;
            case "t1":
                t1 = value;
                break;
            case "t2":
                t2 = value;
                break;
            case "t3":
                t3 = value;
                break;
            case "t4":
                t4 = value;
                break;
            case "t5":
                t5 = value;
                break;
            case "t6":
                t6 = value;
                break;
            case "t7":
                t7 = value;
                break;
            case "s0":
                s0 = value;
                break;
            case "s1":
                s1 = value;
                break;
            case "s2":
                s2 = value;
                break;
            case "s3":
                s3 = value;
                break;
            case "s4":
                s4 = value;
                break;
            case "s5":
                s5 = value;
                break;
            case "s6":
                s6 = value;
                break;
            case "s7":
                s7 = value;
                break;
            case "t8":
                t8 = value;
                break;
            case "t9":
                t9 = value;
                break;
            case "gp":
                gp = value;
                break;
            case "sp":
                sp = value;
                break;
            case "fp":
                fp = value;
                break;
            case "ra":
                ra = value;
                break;
            default:
                System.out.println("register does not exist");

        }   
    }

    public static int getRegister(String register) {

        switch(register) {
            case "at":
                return at;
            case "v0":
                return v0;
            case "v1":
            return v1; 
            case "a0":
            return a0;
            case "a1":
            return a1 ;
            case "a2":
            return a2 ;
            case "a3":
            return a3 ;
            case "t0":
            return t0 ;
            case "t1":
            return t1 ;
            case "t2":
            return t2 ;
            case "t3":
            return t3 ;
            case "t4":
            return t4 ;
            case "t5":
            return t5 ;
            case "t6":
            return t6 ;
            case "t7":
            return t7 ;
            case "s0":
            return s0 ;
            case "s1":
            return s1 ;
            case "s2":
            return s2 ;
            case "s3":
            return s3 ;
            case "s4":
            return s4 ;
            case "s5":
            return s5 ;
            case "s6":
            return s6 ;
            case "s7":
            return s7 ;
            case "t8":
            return t8 ;
            case "t9":
            return t9 ;
            case "gp":
            return gp ;
            case "sp":
            return sp ;
            case "fp":
                return fp ;
            case "ra":
                return ra;
            default:
                System.out.println("register does not exist");
                return -1;

        }   
    }

    
    public static void setRegister(String register, int value) {

        switch(register) {
            case "at":
                at = value;
                break;
            case "v0":
                v0 = value;
                break;
            case "v1":
                v1 = value;
                break;
            case "a0":
                a0 = value;
                break;
            case "a1":
                a1 = value;
                break;
            case "a2":
                a2 = value;
                break;
            case "a3":
                a3 = value;
                break;
            case "t0":
                t0 = value;
                break;
            case "t1":
                t1 = value;
                break;
            case "t2":
                t2 = value;
                break;
            case "t3":
                t3 = value;
                break;
            case "t4":
                t4 = value;
                break;
            case "t5":
                t5 = value;
                break;
            case "t6":
                t6 = value;
                break;
            case "t7":
                t7 = value;
                break;
            case "s0":
                s0 = value;
                break;
            case "s1":
                s1 = value;
                break;
            case "s2":
                s2 = value;
                break;
            case "s3":
                s3 = value;
                break;
            case "s4":
                s4 = value;
                break;
            case "s5":
                s5 = value;
                break;
            case "s6":
                s6 = value;
                break;
            case "s7":
                s7 = value;
                break;
            case "t8":
                t8 = value;
                break;
            case "t9":
                t9 = value;
                break;
            case "gp":
                gp = value;
                break;
            case "sp":
                sp = value;
                break;
            case "fp":
                fp = value;
                break;
            case "ra":
                ra = value;
                break;
            default:
                System.out.println("register does not exist");

        }   
    }

    public static int getRegister(String register) {

        switch(register) {
            case "at":
                return at;
            case "v0":
                return v0;
            case "v1":
            return v1; 
            case "a0":
            return a0;
            case "a1":
            return a1 ;
            case "a2":
            return a2 ;
            case "a3":
            return a3 ;
            case "t0":
            return t0 ;
            case "t1":
            return t1 ;
            case "t2":
            return t2 ;
            case "t3":
            return t3 ;
            case "t4":
            return t4 ;
            case "t5":
            return t5 ;
            case "t6":
            return t6 ;
            case "t7":
            return t7 ;
            case "s0":
            return s0 ;
            case "s1":
            return s1 ;
            case "s2":
            return s2 ;
            case "s3":
            return s3 ;
            case "s4":
            return s4 ;
            case "s5":
            return s5 ;
            case "s6":
            return s6 ;
            case "s7":
            return s7 ;
            case "t8":
            return t8 ;
            case "t9":
            return t9 ;
            case "gp":
            return gp ;
            case "sp":
            return sp ;
            case "fp":
                return fp ;
            case "ra":
                return ra;
            default:
                System.out.println("register does not exist");
                return -1;

        }   
    }
    
}
