package student;
import dbpackage.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utility.Jframe_uses;

public class Profile extends JPanel{
    
    private JFrame main_frame;
    private Connection con;
    private String name, regNo, email, department;
    private int year, noOfEventsReg;
    
    // labels
    private JLabel name_label;
    private JLabel roll_no_label;
    private JLabel dept_label;
    private JLabel year_label;
    private JLabel no_of_events_registered_label;
    private JLabel update_profile_button;
    private JLabel name_value;
    private JLabel roll_no_value;
    private JLabel dept_value;
    private JLabel year_value;
    private JLabel no_of_events_registered_value;
    private JLabel email_label;
    private JLabel title_label;
    private JLabel email_value;
    
    public Profile(JFrame main_frame, String email){
        
        this.main_frame = main_frame;
        try{
            // get conection
            con = new DBConnection().getConnection();
            
            getdata(email);
            setFrame();
        }
        catch(Exception ee){
            System.out.println("Error");
        }
        
    }
    // get data
    void getdata(String email){
        try{
            
            // get values from database
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from students where email = " + "'" + email + "'");
            
            String name ="";
            String regNo ="";
            String department="";
            int year = 0;
            int noOfEventsReg = 0;
            
            // get next values
            while(rs.next()){
                name = rs.getString("name");
                regNo = rs.getString("reg_no");
                department = rs.getString("dept");
                year = rs.getInt("year");
                noOfEventsReg = rs.getInt("no_of_events_reg");
            }
            this.name = name;
            this.department = department;
            this.email = email;
            this.year = year;
            this.regNo = regNo;
            this.noOfEventsReg = noOfEventsReg;
        }
        catch(Exception ee){
            System.out.println(ee);
        } 
    }
    // set components in frame
    private void setFrame(){
        
        String n_o_e = Integer.toString(noOfEventsReg);
        
        // title label
        title_label = Jframe_uses.getLabel(550,10,200,30,"User Profile");
        title_label.setFont(new Font("Tahoma",Font.PLAIN,24));
        
        // name label
        name_label = Jframe_uses.getLabel(330,120,270,30,"NAME                                  :");
        name_label.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // roll number label
        roll_no_label = Jframe_uses.getLabel(330,170,270,30,"ROLL NO                             :");
        roll_no_label.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // email label
        email_label = Jframe_uses.getLabel(330,220,270,30,"Email                                  :");
        email_label.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // department label
        dept_label = Jframe_uses.getLabel(330,270,270,30,"DEPARTMENT                      :");
        dept_label.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // year label
        year_label = Jframe_uses.getLabel(330,320,270,30,"YEAR OF STUDYING             :");
        year_label.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // number of events registered label
        no_of_events_registered_label = Jframe_uses.getLabel(330,370,270,30,"NO OF EVENTS REGISTERED :");
        no_of_events_registered_label.setFont(new Font("Tahoma",Font.PLAIN,20));

        // name value
        name_value = Jframe_uses.getLabel(630,120,270,30,name);
        name_value.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // roll number value
        roll_no_value = Jframe_uses.getLabel(630,170,270,30,regNo);
        roll_no_value.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // email value
        email_value = Jframe_uses.getLabel(630,220,270,30,email);
        email_value.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // department value
        dept_value = Jframe_uses.getLabel(630,270,270,30,department);
        dept_value.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        //year value
        year_value = Jframe_uses.getLabel(630,320,50,30,""+year);
        year_value.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        // number of events registered value
        no_of_events_registered_value = Jframe_uses.getLabel(630,370,50,30,n_o_e);
        no_of_events_registered_value.setFont(new Font("Tahoma",Font.PLAIN,20));
       
        // update profile button
        update_profile_button = Jframe_uses.getLabel(550,500,120,40,"");
        ImageIcon button_Icon = new ImageIcon("src\\images\\update_blue.png");
        update_profile_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        update_profile_button.setIcon(button_Icon);

        // update profile mouse listener
        update_profile_button.addMouseListener(new MouseAdapter()  {
            @Override
            public void mouseEntered(MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\update_white.png");
                update_profile_button.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\update_blue.png");
                update_profile_button.setIcon(user_Icon1);
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                setVisible(false);
                new UpdateProfile(main_frame, email);
            }
           });
        //back button
        ImageIcon back_Icon = new ImageIcon("src\\images\\back.png");
        JLabel back_label = Jframe_uses.getLabel(40, 30,50,50, "");
        back_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back_label.setFont(new Font("Segoe",Font.PLAIN,11));
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
                new StudentPage(main_frame, email);
                setVisible(false);
           }
        });
        
        // adding components to panel
        add(name_label);
        add(title_label);
        add(roll_no_label);
        add(email_label);
        add(dept_label);
        add(year_label);
        add(no_of_events_registered_label);

        add(name_value);
        add(roll_no_value);
        add(email_value);
        add(dept_value);
        add(year_value);
        add(no_of_events_registered_value);

        add(update_profile_button);
        
        // properties for profile panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setLayout(null);
        setVisible(true);
        main_frame.add(this);
   }
}