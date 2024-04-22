package sicvm;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SICVM {
    
    public static MainMenu mainMenu;
    public static Editor editor;

    // Program Init //
    public static void main(String[] args) {
        // Open Main Menu //
        OpenMainMenu();
    }
    
    // Function To Open Main Menu Window //
    public static void OpenMainMenu()
    {
        // Create New Main Menu Window //
        mainMenu = new MainMenu();
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
        
        // Clean Up Extra Window //
        if(editor != null)
        {
            editor.dispose();
        }
    }
    
    // Function To Open Editor Window //
    public static void OpenEditor()
    {
        // Create New Editor Window //
        editor = new Editor();
        editor.setLocationRelativeTo(null);
        editor.setVisible(true);
        
        // Clean Up Extra Window //
        if(mainMenu != null)
        {
            mainMenu.dispose();
        }
    }
    
    // Function To Open A File //
    public static void OpenFile()
    {
        System.out.print("Open File");
        
        OpenEditor();
        
        CPU.UpdateMemoryFromCPU();
    }
    
    // Open Webpage To Manuel //
    public static void OpenManuelPage()
    {
        String url = "https://github.com/TheDevAtlas/SIC-CDA3130/tree/main/SICVM";
        
        try {
        Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
        }
    }
    
    // Open Webpage To GitHub // 
    public static void OpenGitHubPage()
    {
        String url = "https://github.com/TheDevAtlas/SIC-CDA3130";
        
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
        }
    }
    
}