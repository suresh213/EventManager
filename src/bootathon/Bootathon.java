package bootathon;

import homepage.LoginPage;
import javax.swing.JFrame;

public class Bootathon extends JFrame {
    
    public Bootathon(){
        
        new LoginPage(this);
        
        // set properties of main frame
        setTitle("College Event Manager");
        setSize(1200,650);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        
        
    }
    public static void main(String[] args) {
        new Bootathon();
    }
}
