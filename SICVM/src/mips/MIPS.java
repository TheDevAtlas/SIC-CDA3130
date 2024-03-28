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
        
        EditorJFrame.ChangeCodeText(fileAsString);
    }
    
}
