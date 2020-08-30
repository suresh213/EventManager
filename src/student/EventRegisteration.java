package student;

import dbpackage.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utility.Jframe_uses;
import utility.Mail;
import utility.Validation;

public class EventRegisteration extends JPanel {
    
    JFrame main_frame;
    
    private Connection con;
    
    private final String[] gender={"Male","Female"};
    
    //labels
    private JLabel eventName_lbl;
    private JLabel regNo_lbl;
    private JLabel phoneNo_lbl;
    private JLabel gender_lbl;
    private JTextField regNo_txtField;
    private JTextField phoneNo_txtField;
    private JComboBox gender_comboBox;
    private JLabel confirm_btn;
    private JLabel cancel_btn;

    private String eventName;
    private String email;

    public EventRegisteration(JFrame main_frame, String eventName, String email) {
        
        this.main_frame = main_frame;
        
        this.eventName=eventName;
        this.email = email;
        
        // get connection
        con = new DBConnection().getConnection();
        
        initcomponents();
    }
  public void initcomponents() { 
       
        // eventname label
        eventName_lbl=new JLabel();
        eventName_lbl.setText(eventName.toUpperCase());
        eventName_lbl.setBounds(520,30,250,30);
        eventName_lbl.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        
        // register number label
        regNo_lbl=new JLabel("Register Number : ");
        regNo_lbl.setBounds(400,174,240,24);
        regNo_lbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        
        // phone number label
        phoneNo_lbl=new JLabel("Phone Number    : ");
        phoneNo_lbl.setBounds(400,244,240,20);
        phoneNo_lbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        
        // gender label
        gender_lbl=new JLabel("Gender               : ");
        gender_lbl.setBounds(400,314,240,20);
        gender_lbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        
        // register number text field
        regNo_txtField=new JTextField();
        regNo_txtField.setBounds(585,170,200,30);
        regNo_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // phone number textfield
        phoneNo_txtField=new JTextField();
        phoneNo_txtField.setBounds(585,240,200,30);
        phoneNo_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // gender combo box
        gender_comboBox=new JComboBox(gender);
        gender_comboBox.setBounds(585,310,200,30);
        
        // confirm button
        confirm_btn=new JLabel("");
        confirm_btn.setBounds(520,400,85,40);
        confirm_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ImageIcon confirm_btn_icon = new ImageIcon("src\\images\\confirm_blue.png");
        confirm_btn.setIcon(confirm_btn_icon);
        
        // cancel button
        cancel_btn=new JLabel("");
        cancel_btn.setBounds(620,400,80,40);
        cancel_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ImageIcon cancel_btn_icon = new ImageIcon("src\\images\\cancel_red.png");
        cancel_btn.setIcon(cancel_btn_icon);
   
        // back button
        ImageIcon back_Icon = new ImageIcon("src\\images\\back.png");
        JLabel back_label = Jframe_uses.getLabel(40, 30,50,50, "");
        back_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(back_label);
        back_label.setIcon(back_Icon);
        
        // adding components to panel
        add(eventName_lbl);
        add(regNo_lbl);
        add(phoneNo_lbl);
        add(gender_lbl);
        add(regNo_txtField);
        add(phoneNo_txtField);
        add(gender_comboBox);
        add(confirm_btn);
        add(cancel_btn);
        
        // back button mouse listener
         back_label.addMouseListener(new MouseAdapter() {
           
           @Override
           public void mouseClicked(MouseEvent evt) {
                new StudentPage(main_frame,email);
                setVisible(false);
           }
        });
        
        // cancel button mouse listener
        cancel_btn.addMouseListener(new MouseAdapter(){
            
         @Override
            public void mouseEntered(MouseEvent evt) {
                cancel_btn.setIcon(new ImageIcon("src\\images\\cancel_white.png"));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                cancel_btn.setIcon(new ImageIcon("src\\images\\cancel_red.png"));
            }    
            @Override
            public void mouseClicked(MouseEvent evt) {
                new StudentPage(main_frame, email);
                setVisible(false);
            }
        });   
        
        // confirm button mouse listener
        confirm_btn.addMouseListener(new MouseAdapter(){
            
         @Override
            public void mouseEntered(MouseEvent evt) {
                confirm_btn.setIcon(new ImageIcon("src\\images\\confirm_white.png"));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                confirm_btn.setIcon(new ImageIcon("src\\images\\confirm_blue.png"));
            }    
        @Override
      public void mouseClicked(MouseEvent evt) {
        
        String studRegNo = regNo_txtField.getText();
        String phoneNo = phoneNo_txtField.getText();
        String gender = gender_comboBox.getSelectedItem().toString();
        
        try {
            // check fields empty or not
            if (studRegNo.length()==0 || phoneNo.length()==0 || gender.length()==0)
                throw new Validation();
            
            // get values from database
            PreparedStatement ps = con.prepareStatement("select reg_no from students where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String regNo = rs.getString("reg_no");
            
            // check register number valid
            if (!regNo.equals(studRegNo))
                throw new Validation("regNo");
            
            // check event already registered or not
            if (!alreadyRegistered(regNo)) {
                
                // insert values to database
                String q="insert into participants values(?,?,?,?)";
                PreparedStatement Pstatement=con.prepareStatement(q);
                Pstatement.setString(1,eventName);
                Pstatement.setString(2,regNo_txtField.getText());
                Pstatement.setString(3,phoneNo_txtField.getText());
                Pstatement.setString(4,gender_comboBox.getSelectedItem().toString());
                Pstatement.executeUpdate();
                
                // show dialogs
                JOptionPane.showMessageDialog(null,"Registered Successfully !", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Thread(new Mail("Registration successfull !", "Your registration for the " + eventName + " is successful !",email)).start();
                getAndUpdateRegisteredEvents(regNo);
                
                new StudentPage(main_frame, email);
                
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null,"You have already registered for the event !", "Info", JOptionPane.INFORMATION_MESSAGE);
                new StudentPage(main_frame, email);
                setVisible(false);
            }
            
        } catch (Validation e) {
            e.fillAllDetailsMsg(null);
        } 
        catch(HeadlessException | SQLException e1){
            // System.out.print("--->"+e1);
        }
    }
  });
        // properties for event registration panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setLayout(null); 
        setVisible(true);
        
        main_frame.add(this);
    }
  
    // update events registered values 
    private void getAndUpdateRegisteredEvents(String regNo) {
        try {
            PreparedStatement ps1 = con.prepareStatement("select no_of_events_reg from students where reg_no = ?");
            ps1.setString(1, regNo);
            ResultSet noofEventsregistedList = ps1.executeQuery();
            noofEventsregistedList.next();
            int noOfEventsReg = noofEventsregistedList.getInt("no_of_events_reg");
            
            PreparedStatement ps2 = con.prepareStatement("update students set no_of_events_reg = ? where reg_no = ?");
            ps2.setInt(1, ++noOfEventsReg);
            ps2.setString(2, regNo);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
  
    // check register number exists or not
    private boolean regNotExists(String regNo) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from students where reg_no = " + "'" + regNo + "'");
            rs.next();
            rs.getString("name");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    // check events already registered or not
    private boolean alreadyRegistered(String regNo) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from participants where student_reg_no = " + "'" + regNo + "'");
            
            while (rs.next()) {
                if (rs.getString("event_name").equals(eventName))
                    return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}
