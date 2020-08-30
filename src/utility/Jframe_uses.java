package utility;
import javax.swing.*;

public class Jframe_uses {
    
    private static JButton jbutton;
    private static JLabel jlabel;
    private static JTextField textfield;
    private static JPanel jpanel;
    private static JTextArea textarea;
    private static JPasswordField jpass;
    
    // create button
    public static JButton getButton(int a,int b,int c,int d,String text){
        jbutton = new JButton(text);
        jbutton.setBounds(a,b,c,d);
        return jbutton;
    }
    
    // create label
    public static JLabel getLabel(int a,int b,int c,int d,String text){
        jlabel = new JLabel(text);
        jlabel.setBounds(a,b,c,d);
        return jlabel; 
    }

    // create textfield
    public static JTextField getTextField(int a,int b,int c,int d,String text){
        textfield = new JTextField();
        textfield.setBounds(a,b,c,d);
        textfield.setText(text);
        return textfield;
    }

    // create panel
    public static JPanel getPanel(int a,int b,int c,int d){
        jpanel = new JPanel();
        jpanel.setBounds(a,b,c,d);
        return jpanel;
    }

    // create textarea
    public static JTextArea getTextArea(int a,int b,int c,int d,String text){
        textarea = new JTextArea();
        textarea.setBounds(a,b,c,d);
        textarea.setText(text);
        return textarea;
    }

    // create passwordfield
    public static JPasswordField getPasswordField(int a,int b,int c,int d){
        jpass = new JPasswordField();
        jpass.setBounds(a,b,c,d);
        return jpass; 
    }
}
