package homepage;

import dbpackage.DBConnection;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.JOptionPane;
import student.StudentPage;
import utility.RegexValidation;
import utility.Validation;
import utility.HashPassword;
import utility.Jframe_uses;

public class SignupPage extends JPanel {
    
    // main frame
    private JFrame main_frame;
    
    // db connection 
    private Connection con;
    
    // labels
    private JLabel name_lbl;
    private JLabel regNo_lbl;
    private JLabel department_lbl;
    private JLabel year_lbl;
    private JLabel email_lbl;
    private JLabel password_lbl;
    
    // text fields
    private JTextField name_txtField;
    private JTextField regNo_txtField;
    private JTextField department_txtField;
    private JTextField year_txtField;
    private JTextField email_txtField;
    private JPasswordField password_txtField;
    
    public SignupPage(JFrame main_frame)
    {
        this.main_frame = main_frame;
        
        // get connection
        con = new DBConnection().getConnection();
        
        initComponents();
    }
    private void initComponents()
    {
        // font
        Font tahoma = new Font("Tahoma",Font.PLAIN,15); 

        // title label
        JLabel title_label = Jframe_uses.getLabel(80,70,350,60, "Event Manager");
        title_label.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,46));
        add(title_label);
    
        // description label
        JLabel description_label = Jframe_uses.getLabel(210,125,250,40, "Make work Simple");
        description_label.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,18));
        add(description_label);
        
        // signup label
        JPanel signup_panel = Jframe_uses.getPanel(650,60,400,480);
        signup_panel.setBackground(new Color(255,255,255));
        signup_panel.setLayout(null); 
        add(signup_panel);   
        
        // signup name label
        JLabel signup_name_lbl = Jframe_uses.getLabel(30,0,120,40, "Sign up");
        signup_name_lbl.setFont(new Font("Segoe",Font.PLAIN,26));
        signup_panel.add(signup_name_lbl);

        // calender image
        ImageIcon image = new ImageIcon(new ImageIcon("src\\images\\img2.png").getImage().getScaledInstance(450,300, Image.SCALE_DEFAULT));
        JLabel image_label = Jframe_uses.getLabel(90,165,450,400, "");
        image_label.setIcon(image);
        add(image_label);

        // back button
        JLabel back_to_login = Jframe_uses.getLabel(68,432,77,20, "Back to Login");
        back_to_login.setFont(new Font("Segoe",Font.PLAIN,12));
        back_to_login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signup_panel.add(back_to_login);

        //signup button
        ImageIcon signup_button_icon = new ImageIcon(("src\\images\\signup_blue.png"));
        JLabel signup_button = Jframe_uses.getLabel(245,420,86,40, "");
        signup_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signup_button.setIcon(signup_button_icon);
        signup_panel.add(signup_button);
        
        // name label
        name_lbl = Jframe_uses.getLabel(60,55,70,30, "Name :");
        name_lbl.setFont(tahoma);
        
        // register number label
        regNo_lbl = Jframe_uses.getLabel(60,115,80,30,"RegNo :");
        regNo_lbl.setFont(tahoma);
        
        // department label
        department_lbl = Jframe_uses.getLabel(60,175,100,30, "Department :");
        department_lbl.setFont(tahoma);
        
        // year label
        year_lbl = Jframe_uses.getLabel(60,235,200,30, "Year Of Study :");
        year_lbl.setFont(tahoma);
        
        // email label
        email_lbl = Jframe_uses.getLabel(60,295,100,30, "Email :");
        email_lbl.setFont(tahoma);
        
        // password label
        password_lbl = Jframe_uses.getLabel(60,355,100,30, "Password :");
        password_lbl.setFont(tahoma);
      
        // name textfield
        name_txtField = Jframe_uses.getTextField(60,80,250,32, "");
        name_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        
        // register number textfield
        regNo_txtField = Jframe_uses.getTextField(60,140,250,32, "");
        regNo_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // department textfield
        department_txtField = Jframe_uses.getTextField(60,200,250,32, "");
        department_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // year textfield
        year_txtField = Jframe_uses.getTextField(60,260,250,32, "");
        year_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // email textfield
        email_txtField = Jframe_uses.getTextField(60,320,250,32, "");
        email_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // password textfield
        password_txtField = Jframe_uses.getPasswordField(60,380,250,32);
        password_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // adding components to signup panel
        signup_panel.add(name_lbl);
        signup_panel.add(regNo_lbl);
        signup_panel.add(department_lbl);
        signup_panel.add(year_lbl);
        signup_panel.add(email_lbl);
        signup_panel.add(password_lbl);
        signup_panel.add(name_txtField);
        signup_panel.add(regNo_txtField);
        signup_panel.add(department_txtField);
        signup_panel.add(year_txtField);
        signup_panel.add(email_txtField);
        signup_panel.add(password_txtField);
        
        // back to login mouse listener
        back_to_login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                back_to_login.setForeground(Color.red);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                back_to_login.setForeground(Color.BLACK);
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                new LoginPage(main_frame);
                setVisible(false);
            }
        });   
        
        // signup button mouse listener
        signup_button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            ImageIcon user_Icon1 = new ImageIcon("src\\images\\signup_white.png");
            signup_button.setIcon(user_Icon1);
        }
        @Override
        public void mouseExited(MouseEvent evt) {
            ImageIcon user_Icon1 = new ImageIcon("src\\images\\signup_blue.png");
            signup_button.setIcon(user_Icon1);
        }
        public void mouseClicked(MouseEvent evt) {
          try {
                // if fields are empty
                if (!isCredsFilled())
                    throw new Validation();

                // if year not integer show error
                
                // if year > 5 show error
                if (Integer.parseInt(year_txtField.getText()) >= 5)
                    throw new Validation("year");

                // regex validation
                if (!isEmailPassValid(email_txtField.getText(), password_txtField.getText()))
                    throw new Validation("regex");

                // email already exists ?
                if (emailExists(con.createStatement(), email_txtField.getText()))
                    throw new Validation("email");

                // insert values to database
                PreparedStatement ps = con.prepareStatement("insert into students values(?,?,?,?,?,?,?,?)");
                ps.setString(1,regNo_txtField.getText());
                ps.setString(2,name_txtField.getText());
                ps.setString(3,department_txtField.getText());
                ps.setString(4,year_txtField.getText());
                ps.setString(5,email_txtField.getText());
                ps.setBoolean(7, false);
                ps.setInt(8, 0);

                // hash password
                String hashedPassword = hashPassword(password_txtField.getText());
                ps.setString(6, hashedPassword);

                ps.executeUpdate();

                // closing connection
                con.close();

                JOptionPane.showMessageDialog(null,"Signup Successful !", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.out.println();
                new StudentPage(main_frame, email_txtField.getText());
                setVisible(false);
            } catch(SQLException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Resister number already exists !", "Sorry", JOptionPane.ERROR_MESSAGE);
            } catch(Validation er) {
                er.fillAllDetailsMsg(null);
            } catch(NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Year must be a number !", "Sorry", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
        
        // properties for signup panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setLayout(null); 
        setVisible(true);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        main_frame.add(this);
    }
   
    // check all fields are filled
    private boolean isCredsFilled() {
        String regNo = regNo_txtField.getText();
        String name = name_txtField.getText();
        String department = department_txtField.getText();
        String year = year_txtField.getText();
        String email = email_txtField.getText();
        String password = password_txtField.getText();
        
        if (regNo.length()==0 || name.length()==0 || department.length()==0 || year.length()==0 || email.length()==0 || password.length()==0)
            return false;
        return true;
    }
    
    // check email and password valid or not
    private boolean isEmailPassValid(String email, String password) {
        if (!new RegexValidation().isEmailValid(email))
            return false;
        if (!new RegexValidation().isPassValid(password))
            return false;
        
        return true;
    }
    
    // hash password
    private String hashPassword(String password) {
        return HashPassword.hash(password);
    }
    
    // check emails already exists or not
    public static boolean emailExists(Statement st, String email) {
        try {
            ResultSet rs = st.executeQuery("select * from students where email = " + "'" + email +"'");
            rs.next();
            rs.getString("email");
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}

