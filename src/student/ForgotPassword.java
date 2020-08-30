package student;

import dbpackage.DBConnection;
import homepage.LoginPage;
import homepage.SignupPage;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;
import utility.HashPassword;
import utility.Jframe_uses;
import utility.Mail;
import utility.RegexValidation;
import utility.Validation;

public class ForgotPassword extends JPanel {
    
    private  int otp = 0;
    
    private JTextField email_field;
    private JPasswordField change_password_field;
    private JPasswordField confirm_password_field;
    private Connection con;
    
    private JFrame main_frame;
    
    public ForgotPassword(JFrame main_frame){
    
        this.main_frame = main_frame;
        
        // get connection obj
        con = new DBConnection().getConnection();
     
        //Generating OTP
        String numbers = "1234567890";
        Random random = new Random();
        for(int i = 0; i< 5 ; i++) {
           otp = (otp*10)+numbers.charAt(random.nextInt(numbers.length()));
        }

        // changing values panel
        JPanel change_panel = Jframe_uses.getPanel(650,140,350,300);
        change_panel.setBackground(Color.white);
        change_panel.setLayout(null); 
        add(change_panel);

        // title label
        JLabel title = Jframe_uses.getLabel(450,30,250,40, "Forgot Password");
        title.setFont(new Font("Segoe UI",Font.PLAIN,30));
        add(title);

        // email label
        JLabel email_label = Jframe_uses.getLabel(150,180,250,30, "Enter Email to send OTP");
        email_label.setFont(new Font("Segoe",Font.PLAIN,20));
        add(email_label);

        // email field
        email_field = Jframe_uses.getTextField(150,250,250,25, "");
        email_field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        add(email_field);

        // send otp button
        ImageIcon send_otp_icon = new ImageIcon("src\\images\\otp_blue.png");
        JLabel send_otp_button = Jframe_uses.getLabel(150,300,102,40, "");
        send_otp_button.setIcon(send_otp_icon);
        send_otp_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(send_otp_button);

        // success label
        JLabel success_label = Jframe_uses.getLabel(150,380,250,30, "");
        success_label.setFont(new Font("Segoe",Font.PLAIN,20));
        add(success_label);

        // otp label
        JLabel otp_label = Jframe_uses.getLabel(40,40,200,20, "Enter OTP :  ");
        otp_label.setFont(new Font("Segoe UI",Font.PLAIN,22));
        change_panel.add(otp_label);

        // change password label
        JLabel change_password_label = Jframe_uses.getLabel(40,120,230,20, "Enter new Password :  ");
        change_password_label.setFont(new Font("Segoe UI",Font.PLAIN,22));
        change_panel.add(change_password_label);

        // confirm password label
        JLabel confirm_password_label = Jframe_uses.getLabel(40,195,245,20, "Confirm new Password :  ");
        confirm_password_label.setFont(new Font("Segoe UI",Font.PLAIN,22));
        change_panel.add(confirm_password_label);

        // otp textfield
        JTextField otp_field = Jframe_uses.getTextField(40,75,250,30, "");
        otp_field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        change_panel.add(otp_field);

        // change password textfield
        change_password_field = Jframe_uses.getPasswordField(40,155,250,30);
        change_password_field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        change_panel.add(change_password_field);

        // confirm password textfield
        confirm_password_field = Jframe_uses.getPasswordField(40,230,250,30);
        confirm_password_field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        change_panel.add(confirm_password_field);

        //save button
        ImageIcon save_button_icon = new ImageIcon("src\\images\\save_blue.png");
        JLabel save_button = Jframe_uses.getLabel(550,500,86,40, "");
        save_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        save_button.setIcon(save_button_icon);
        add(save_button);

        // send otp mouse listener
        send_otp_button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            ImageIcon user_Icon1 = new ImageIcon("src\\images\\otp_white.png");
            send_otp_button.setIcon(user_Icon1);
        }
        @Override
        public void mouseExited(MouseEvent evt) {
            ImageIcon user_Icon1 = new ImageIcon("src\\images\\otp_blue.png");
            send_otp_button.setIcon(user_Icon1);
        }
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt ){
                try {
                    String email = email_field.getText();
                
                    if (email.length() == 0)
                        throw new Validation();
                    
                    // check email exists
                    if (SignupPage.emailExists(con.createStatement(), email)) {
                        System.out.println(otp);
                       new Thread(new Mail( "Your OTP!", otp + " is your otp!",email)).start();
                        
                        success_label.setText("OTP has been sent !");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Email id does not exist !", "Sorry", JOptionPane.INFORMATION_MESSAGE);
                } catch (Validation e) {
                    e.fillAllDetailsMsg(null);
                } catch (SQLException er) {
                    System.out.println(er);
                }
                
            }
        });

        // save button mouse listener
        save_button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {

            if("".equals(otp_field.getText()))
            {
                JOptionPane.showMessageDialog(null,"Please enter OTP");
            }

            else{
                if(otp_field.getText().equals(Integer.toString(otp)))
                {
                     if(("".equals(change_password_field.getText()))||("".equals(confirm_password_field.getText())))
                     {
                         JOptionPane.showMessageDialog(null,"Credentials are empty");
                     }
                     else if(change_password_field.getText().equals(confirm_password_field.getText()))
                     {
                         // check regex validation
                          if (!new RegexValidation().isPassValid(change_password_field.getText()))
                              JOptionPane.showMessageDialog(null,"Password should contain 1-uppercase, 1-lowercase, 1-number and should be minimum of 8 characters");
                          else {
                              String password = change_password_field.getText();

                              String hashedPassword = HashPassword.hash(password);
                              try {
                                  // update password in database
                                  PreparedStatement ps = con.prepareStatement("update students set password = ? where email = ?");
                                  ps.setString(1, hashedPassword);
                                  ps.setString(2, email_field.getText());
                                  ps.executeUpdate();
                                  
                                  JOptionPane.showMessageDialog(null,"Password has been successfully changed !", "Success", JOptionPane.INFORMATION_MESSAGE);
                                  
                                  new LoginPage(main_frame);
                                 
                                  setVisible(false);
                              
                              } catch (Exception e) {
                                  System.out.println(e);
                              }
                          }
                     }
                     else if(!change_password_field.getText().equals(confirm_password_field.getText())){
                         JOptionPane.showMessageDialog(null,"Passwords does not match");
                     }
                }
                else{
                    JOptionPane.showMessageDialog(null,"OTP Wrong");
                }
            }

        }
        @Override
        public void mouseEntered(MouseEvent evt) {
           ImageIcon user_Icon1 = new ImageIcon("src\\images\\save_white.png");
           save_button.setIcon(user_Icon1);
        }
        @Override
        public void mouseExited(MouseEvent evt) {
            ImageIcon user_Icon1 = new ImageIcon("src\\images\\save_blue.png");
            save_button.setIcon(user_Icon1);
        }
        }); 
        
        // back button
        ImageIcon back_Icon = new ImageIcon("src\\images\\back.png");
        JLabel back_label = Jframe_uses.getLabel(40, 30,50,50, "");
        back_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(back_label);
        back_label.setIcon(back_Icon);
        
        // back button mouse listener
        back_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                back_label.setForeground(Color.red);
            }
           @Override
           public void mouseExited(MouseEvent evt) {
                back_label.setForeground(Color.BLACK);
           }
           @Override
           public void mouseClicked(MouseEvent evt) {
                new LoginPage(main_frame);
                setVisible(false);
           }
        });

        // properties for forgot password panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setLayout(null); 
        main_frame.add(this);
        }
}


