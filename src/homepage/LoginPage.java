package homepage;
import college.CollegePage;
import dbpackage.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import student.ForgotPassword;
import student.StudentPage;
import utility.HashPassword;
import utility.Jframe_uses;
import utility.Validation;


public class LoginPage extends JPanel implements ActionListener {
    
    private Connection con;
    
    // lables
    private JLabel email_label;
    private JLabel password_label;
    
    // text fields
    private JTextField email_txtField;
    private JPasswordField password_passField;
    
    // checkbox
    private JCheckBox showPassword_checkBox;
    
    // main frame
    private JFrame main_frame;
    
    public LoginPage(JFrame main_frame){
        this.main_frame = main_frame;
        
        // get connection
        con = new DBConnection().getConnection();
        initComponents();
    }
    
    private void initComponents() {
        
        // font
        Font tahoma = new Font("Tahoma",Font.PLAIN,15);
    
        // login panel
        JPanel login_panel = Jframe_uses.getPanel(650,100,400,400);
        login_panel.setBackground(Color.white);
        login_panel.setLayout(null); 
        add(login_panel);   

        // login name label
        JLabel login_name_lbl = Jframe_uses.getLabel(30,0,120,40, "Login");
        login_name_lbl.setFont(new Font("Segoe UI",Font.PLAIN,26));
        login_name_lbl.setForeground(Color.black);
        login_panel.add(login_name_lbl);

        // title label
        JLabel title_label = Jframe_uses.getLabel(80,70,350,60, "Event Manager");
        title_label.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,46));
        add(title_label);
    
        // description label
        JLabel description_label = Jframe_uses.getLabel(210,125,250,40, "Make work Simple");
        description_label.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,18));
        add(description_label);
    
        // calender image
        ImageIcon image =new ImageIcon(new ImageIcon("src\\images\\img3.png").getImage().getScaledInstance(450,300, Image.SCALE_DEFAULT));
        JLabel image_label = Jframe_uses.getLabel(90,165,450,400, "");
        image_label.setIcon(image);
        add(image_label);
     
        // email label
        email_label = Jframe_uses.getLabel(70,90,97,40, "Email :");
        email_label.setFont(new Font("Segoe UI",Font.PLAIN,24));
        login_panel.add(email_label);
    
        // password label
        password_label = Jframe_uses.getLabel(70,167,110,30, "Password :");
        password_label.setFont(new Font("Segoe UI",Font.PLAIN,24));
        login_panel.add(password_label);
     
        // email textfield
        email_txtField = Jframe_uses.getTextField(70,125,250,32, "");
        email_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // password textfield
        password_passField = new JPasswordField("");
        password_passField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        password_passField.setBounds(70,197,250,32);
        
        // show password checkbox
        showPassword_checkBox = new JCheckBox("Show");
        showPassword_checkBox.setBounds(252,230,60, 20);
        showPassword_checkBox.setBackground(Color.white);
        showPassword_checkBox.addActionListener(this);
        
        // adding components to login panel
        login_panel.add(showPassword_checkBox);
        login_panel.add(email_txtField);
        login_panel.add(password_passField);
    
        // forgot password label
        JLabel forgot_password = Jframe_uses.getLabel(70,260,97,40, "Forgot Password ?");
        forgot_password.setFont(new Font("Segoe",Font.PLAIN,11));
        forgot_password.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login_panel.add(forgot_password);

        // new user label
        JLabel new_user = Jframe_uses.getLabel(250,260,97,40, "New  User ?");
        new_user.setFont(new Font("Segoe",Font.PLAIN,11));
        new_user.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login_panel.add(new_user);
    
        // login button
        ImageIcon login_button_icon = new ImageIcon(("src\\images\\login_blue.png"));
        JLabel login_button = Jframe_uses.getLabel(245,310,70,40, "");
        login_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login_button.setIcon(login_button_icon);
        login_panel.add(login_button);
        
        // forgot password mouse listener
        forgot_password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                forgot_password.setForeground(Color.red);
            }
           @Override
           public void mouseExited(MouseEvent evt) {
                forgot_password.setForeground(Color.BLACK);
           }
           @Override
           public void mouseClicked(MouseEvent evt) {
                new ForgotPassword(main_frame);
                setVisible(false);
           }
        });
      
        // new user mouse listener
        new_user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                new_user.setForeground(Color.red);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                new_user.setForeground(Color.BLACK);
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                new SignupPage(main_frame);
                setVisible(false);
            }
     });
        
    // login button mouse listener
    login_button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            ImageIcon login_button_icon = new ImageIcon("src\\images\\login_white.png");
            login_button.setIcon(login_button_icon);
        }
        @Override
        public void mouseExited(MouseEvent evt) {
            ImageIcon login_button_icon = new ImageIcon("src\\images\\login_blue.png");
            login_button.setIcon(login_button_icon);
        }
        @Override
        public void mouseClicked(MouseEvent evt) {
            try {
                String email = email_txtField.getText();
                String password = password_passField.getText();

                if (email.length()==0 || password.length()==0)
                    throw new Validation();

                // check email and password valid or not
                if (isAValidCred(email, password)) {
                    setVisible(false);
                    String regNo = isAdmin(email);
                    if (regNo.length()==0)
                        new CollegePage(main_frame);
                    else
                        new StudentPage(main_frame, email);
                } else  {
                    throw new Validation("regex");
                }
            } catch(Validation e) {
                e.fillAllDetailsMsg(null);
            }
        }
    });
    
        // properties of login panel
        setBounds(0,0,1200,650);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setBackground(Color.white);
        setLayout(null);
        setVisible(true);
        main_frame.add(this);
        
        
        
    }
 @Override
    public void actionPerformed(ActionEvent evt) {
        if (showPassword_checkBox.isSelected()) {
            password_passField.setEchoChar((char) 0);
        } else {
            password_passField.setEchoChar('\u25CF');
        }
    }
    
    // check credentials are valid or not
    private boolean isAValidCred(String email, String password) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select email, password from students where email = " + "'" + email + "'");
            rs.next();
            if (rs.getString("password").equals(HashPassword.hash(password))) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    // check user is admin or student
    private String isAdmin(String email) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select reg_no, is_admin from students where email = " + "'" + email + "'");
            rs.next();
            if (!rs.getBoolean("is_admin"))
                return rs.getString("reg_no");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return "";
    }
    
}