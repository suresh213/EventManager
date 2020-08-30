package student;

import dbpackage.DBConnection;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import utility.Jframe_uses;
import utility.Validation;

public class UpdateProfile extends JPanel {
    
    private JFrame main_frame;
    private Connection con;
    private String email, regNo, name, department;
    private int year, noOfEventsReg;
    private JLabel save_btn;
    
    public UpdateProfile(JFrame main_frame, String email) {
       
        this.main_frame = main_frame;
        try{
            // get connection
            con = new DBConnection().getConnection();
            
            updateData(email);
            initComponents();
        }
        catch(Exception ee){
            System.out.println("Error");
        }
    }
    private void updateData(String email){
        try{
            // get values from database
            System.out.println(email + " helo");
            String query = "select * from students where email = ?";

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            String name ="";
            String regNo = "";
            String department="";
            int year = 0;
            int noOfEventsReg = 0;
            
            while(rs.next()){
                regNo = rs.getString("reg_no");
                name = rs.getString("name");
                department = rs.getString("dept");
                year = rs.getInt("year");
                noOfEventsReg = rs.getInt("no_of_events_reg");
            }

            this.email = email;
            this.name = name;
            this.department = department;
            this.regNo = regNo;
            this.year = year;
            this.noOfEventsReg = this.noOfEventsReg;
        }
        catch(Exception ee){
            System.out.println(ee);
        } 
    }
    void initComponents(){
        
        //font
        Font tahoma = new Font("Tahoma",Font.PLAIN,15);
        
        // title label
        JLabel title_label = Jframe_uses.getLabel(550,10,200,30,"Update Profile");
        title_label.setFont(new Font("Tahoma",Font.PLAIN,24));
       
        // name label
        JLabel name_lbl = Jframe_uses.getLabel(400,174,240,20,"USER NAME       : ");
        name_lbl.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // name textfield
        JTextField name_txtField = Jframe_uses.getTextField(585,170,200,30,name);
        name_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // department label
        JLabel department_lbl = Jframe_uses.getLabel(400,244,240,20,"DEPARTMENT    : ");
        department_lbl.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // department textfield
        JTextField department_txtField = Jframe_uses.getTextField(585,240,200,30, department);
        department_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
        
        // year label
        JLabel year_lbl = Jframe_uses.getLabel(400,314,240,20,"YEAR OF STUDY :");
        year_lbl.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // year textfield
        JTextField year_txtField = Jframe_uses.getTextField(585,310,200,30,Integer.toString(year));
        year_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
      
        // save button
        ImageIcon save_icon = new ImageIcon("src\\images\\save_blue.png");
        save_btn = new JLabel("Save");
        save_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        save_btn.setBounds(550,430,62,40);
        save_btn.setIcon(save_icon);
        add(save_btn);
        
        // adding components to panel
        add(name_lbl);
        add(title_label);
        add(department_lbl);
        add(year_lbl);
        add(name_txtField);
        add(department_txtField);
        add(year_txtField);
        add(save_btn);
        
        // save button mouse listener
        save_btn.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent evt) {
                save_btn.setIcon(new ImageIcon("src\\images\\save_white.png"));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                save_btn.setIcon(new ImageIcon("src\\images\\save_blue.png"));
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
               
                String name = name_txtField.getText();
                String department = department_txtField.getText();
                int year = -1;
                try {
                    year = Integer.parseInt(year_txtField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Year must be a number !", "Error", JOptionPane.ERROR_MESSAGE);
                }
                // check updated or not
                if (isUpdated(name,regNo,department,year)) {
                    setVisible(false);
                    new Profile(main_frame, email);
                }
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
                new Profile(main_frame, email);
                setVisible(false);
           }
        });
        
        // properties for update profile panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setLayout(null); 
        setVisible(true);
        main_frame.add(this);
    }
    
    // check updated or not
    private boolean isUpdated(String name,String regNo,String department,int year){
       try{
          
           if (name.length()==0 || regNo.length()==0 || department.length()==0)
               throw new Validation();
           
           if (year < 1) {
               JOptionPane.showMessageDialog(null, "Year cannot be 0 or negative !", "Error", JOptionPane.ERROR_MESSAGE);
               return false;
           }
           
           //update details in database
           Statement st=con.createStatement();
           String query ="update students set name = ?,dept = ?,year = ? where reg_no =?";
           PreparedStatement pstmt = con.prepareStatement(query);
           pstmt.setString(1,name);
           pstmt.setString(2,department);
           pstmt.setInt(3, year);
           pstmt.setString(4, regNo);
           
           int result1 = pstmt.executeUpdate();
           con.setAutoCommit(true);
           
           // close connection
           con.close();
           
           if(result1 >= 1 ){
            JOptionPane.showMessageDialog(null,"Updated!");
            return true;
           }
           else{
                JOptionPane.showMessageDialog(null,"Not updated!!");
                return false;
           }
        }
      
        catch (Validation e) {
            e.fillAllDetailsMsg(null);
        }
        catch(Exception ee){
            System.out.println("error");
            JOptionPane.showMessageDialog(null,"  Exception   -->"+ee);
        }
       return false;
    }
}
