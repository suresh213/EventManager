package student;

import dbpackage.DBConnection;
import com.toedter.calendar.JDateChooser;
import homepage.LoginPage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import utility.Jframe_uses;

public class StudentPage extends JPanel{
    private Connection con;
    private String email, userName;

    private DefaultListModel events_model=new DefaultListModel();;
    private String registerbuttonvalue;
    ResultSet resultset;

    // labels
    private JLabel user_label;
    private JLabel logout_label;
    private JLabel  title;
    private JLabel  all_events_label;
    private JLabel  show_all_events_label;
    private JLabel pick_by_date_label;
    private JLabel  show_label;
    private JLabel  event_name_label;
    private JLabel  department_label;
    private JLabel date_label;
    private JLabel venue_label;
    private JLabel  description_label;
    private JLabel  event_name_value;
    private JLabel  department_value;
    private JLabel date_value;
    private JLabel venue_value;
    private JLabel  description_value;
    private JLabel  time_show;
    private JLabel  date_show;
    private JLabel  register_button;
 
    private JList event_list; 
    
    //panels
    private JPanel event_panel;
    private JPanel display_panel;
    private JPanel jlist_panel;
    
    private ImageIcon user_Icon,logout_button,button_icon;
    private JDateChooser jDateChooser;
    private JScrollPane scroll;
    
    private JFrame main_frame;
   
    public StudentPage(JFrame main_frame, String email) {
      
        this.main_frame = main_frame;
        
        this.email = email;
        // get connection
        con = new DBConnection().getConnection();
        
        try{
            
            // get details from database
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select name, email from students where email = " + "'" + email + "'");
            rs.next();
            userName = rs.getString("name");
           
            ResultSet resultset_allevents = st.executeQuery("Select event_name from events"); 
            showevents(resultset_allevents);
        }   
        catch(SQLException e) {
            System.err.println(e);
        }
        
        initcomponents();
       
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Calendar cal = Calendar.getInstance();
                    
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    int second = cal.get(Calendar.SECOND);
                    
                    Date date = cal.getTime();
                    String time24 = new SimpleDateFormat("HH:mm:ss").format(date);
                    
                    date_show.setText(time24);
                }
            }     
        }).start();
    } 
    
    public void initcomponents(){

        //Font
        Font segoe_ui = new Font("Segoe UI", Font.PLAIN, 24);
        Font tahoma = new Font("Tahoma",Font.PLAIN,11);

        //LineBorder
        LineBorder line_border = new LineBorder(Color.black);

        //TitledBorder
        TitledBorder titled_border = BorderFactory.createTitledBorder(line_border,"Event Details");
        titled_border.setTitleJustification(TitledBorder.CENTER);
        titled_border.setTitleFont(segoe_ui);

        // name panel
        JPanel name_panel = Jframe_uses.getPanel(0,55,1200,120);
        name_panel.setBackground(new Color(0,30,50));
        name_panel.setLayout(null); 
        add(name_panel);

        // student_page_label
        JLabel student_page_label = Jframe_uses.getLabel(15,5,200,42, "Student Page"); 
        student_page_label.setForeground(Color.black);
        student_page_label.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,32));
        add(student_page_label);
        
        // profile label
        JLabel profile_name = Jframe_uses.getLabel(1055,39,50,15, "Profile"); 
        profile_name.setForeground(Color.black);
        profile_name.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,12));
        add(profile_name);
        
        // logout label
        JLabel logout_name = Jframe_uses.getLabel(1121,39,50,15, "Logout"); 
        logout_name.setForeground(Color.black);
        logout_name.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,12));
        add(logout_name);
        
        // username label
        JLabel user_name = Jframe_uses.getLabel(45,20,200,80, "Hey , " + userName); 
        user_name.setForeground(Color.white);
        user_name.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,32));
        name_panel.add(user_name);
     
        // user icon
        user_Icon = new ImageIcon(new ImageIcon("src\\images\\user.png").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT));
        user_label = Jframe_uses.getLabel(1050,2,40,40, "");
        user_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        user_label.setIcon(user_Icon);
        add(user_label);
        user_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
               setVisible(false);
               new Profile(main_frame, email);
            }
        });
        
        // logout icon
        ImageIcon logout_icon = new ImageIcon("src\\images\\logout_red.png");
        JLabel logout_label = Jframe_uses.getLabel(1120,6,40,40, "");
        logout_label.setFont(new Font("Segoe",Font.PLAIN,11));
        logout_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout_label.setIcon(logout_icon);
        add(logout_label);
        
   
        //Event display
        event_panel = Jframe_uses.getPanel(20,204,420,360);
        event_panel.setBackground(Color.white);
        event_panel.setLayout(null); 
        add(event_panel);

        // jlist panel
        jlist_panel = Jframe_uses.getPanel(20,60,380,260);
        jlist_panel.setBackground(Color.white);
        jlist_panel.setLayout(new BorderLayout()); 
        event_panel.add(jlist_panel);

        // title label
        title = Jframe_uses.getLabel(170,10,120,20,"EVENTS");
        title.setForeground(Color.black);
        title.setFont(segoe_ui);
        event_panel.add(title);

        // all events label
        all_events_label = Jframe_uses.getLabel(30,40,90,14,"All Events");
        all_events_label.setForeground(Color.black);
        all_events_label.setFont(tahoma);
        event_panel.add(all_events_label);

        // event list
        event_list = new JList<>(events_model);
        LineBorder line_border_grey = new LineBorder(new Color(0,0,0,20));
        event_list.setBorder(line_border_grey);
        event_list.setSelectionBackground(new Color(123, 142, 173));
        event_list.setBackground(Color.white);
        event_list.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        event_list.setFixedCellHeight(38);
        event_list.setBounds(0,0,380,260);

        // scroll pane
        scroll = new JScrollPane(event_list);
        jlist_panel.add(scroll);
        scroll.setVisible(true);

        //Event details disply
        display_panel = Jframe_uses.getPanel(640,220,530,260);
        display_panel.setLayout(null); 
        display_panel.setBackground(Color.white);
        LineBorder line_border_dark_grey = new LineBorder(new Color(0,0,0,70));
        TitledBorder titled_border_grey = BorderFactory.createTitledBorder(line_border_dark_grey,"Event Details");
        titled_border_grey.setTitleFont(segoe_ui);
        display_panel.setBorder(titled_border_grey);
        add(display_panel);

        // event name label
        event_name_label = Jframe_uses.getLabel(20,50,100,20, "Event Name  : ");
        event_name_label.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // department label
        department_label = Jframe_uses.getLabel(20,90,100,20, "Department  : ");
        department_label.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // date label
        date_label = Jframe_uses.getLabel(20,130,100,20, "Date              : ");
        date_label.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // venue label
        venue_label = Jframe_uses.getLabel(20,170,100,20, "Venue            : ");
        venue_label.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // decription label
        description_label = Jframe_uses.getLabel(20,210,100,20, "Description    : ");
        description_label.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // add components to display panel
        display_panel.add(event_name_label);
        display_panel.add(department_label);
        display_panel.add(date_label);
        display_panel.add(venue_label);
        display_panel.add(description_label);

        // event name value
        event_name_value = Jframe_uses.getLabel(130,50,200,20, "---");
        event_name_value.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // department value
        department_value = Jframe_uses.getLabel(130,90,200,20, "---");
        department_value.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // date value
        date_value = Jframe_uses.getLabel(130,130,200,20, "---");
        date_value.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // venue value
        venue_value = Jframe_uses.getLabel(130,170,200,20, "---");
        venue_value.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // description value
        description_value = Jframe_uses.getLabel(130,210,200,20, "---");
        description_value.setFont(new Font("Segoe UI",Font.PLAIN,15));

        // add components to display panel
        display_panel.add(event_name_value);
        display_panel.add(department_value);
        display_panel.add(date_value);
        display_panel.add(venue_value);
        display_panel.add(description_value);

        // pick by date
        pick_by_date_label = Jframe_uses.getLabel(470,270,110,17, "Pick by date");
        pick_by_date_label.setFont(new Font("Tahoma",Font.PLAIN,16));
        add(pick_by_date_label);

        //Date picker
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jDateChooser.setDateFormatString("dd/MM/yyyy");
        jDateChooser.setBounds(470, 290, 131, 29);
        add(jDateChooser);

        //show label
        ImageIcon show_icon = new ImageIcon("src\\images\\show_blue.png");
        show_label = Jframe_uses.getLabel(500,360,78,40,"");
        show_label.setFont(tahoma);
        show_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        show_label.setIcon(show_icon);
        add(show_label);

        // show all events label
        ImageIcon showall_icon = new ImageIcon("src\\images\\showall_blue.png");
        show_all_events_label = Jframe_uses.getLabel(485,430,109,40, ""); 
        show_all_events_label.setForeground(Color.black);
        show_all_events_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        show_all_events_label.setIcon(showall_icon);
        add(show_all_events_label);

        // register button 
        button_icon = new ImageIcon("src\\images\\black_button.png");
        register_button = Jframe_uses.getLabel(850,520,95,43, "");
        register_button.setIcon(button_icon);
        register_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(register_button);

        // time show
        time_show = Jframe_uses.getLabel(1050,585,97,27, "Time");
        time_show.setFont(tahoma);
        add(time_show);

        //date show
        date_show = Jframe_uses.getLabel(1100,585,97,27, "Date");
        date_show.setFont(tahoma);
        add(date_show);

        // event list selection listener
        event_list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                registerbuttonvalue =  (String) event_list.getSelectedValue();
                try{
                    Statement st = con.createStatement();
                   ResultSet detailset = st.executeQuery("Select * from Events where event_name='"+registerbuttonvalue+"'");
                   detailset.next();
                   event_name_value.setText(detailset.getString("event_name"));
                   department_value.setText(detailset.getString("department"));
                   date_value.setText(detailset.getString("date"));
                   venue_value.setText(detailset.getString("venue"));
                   description_value.setText(detailset.getString("description"));
                }
                catch(SQLException e)
                 {
                 System.out.println(e);
                 }     
                }
            }
        });

        // show all events mouse listener                              
        show_all_events_label.addMouseListener(new java.awt.event.MouseAdapter() {

              @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\showall_white.png");
                show_all_events_label.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\showall_blue.png");
                show_all_events_label.setIcon(user_Icon1);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {                                       
                 // TODO add your handling code here:
                ResultSet resultset_allevents; 
                try {
                    Statement st = con.createStatement();
                    resultset_allevents = st.executeQuery("Select event_name from Events");
                    showevents(resultset_allevents);
                    all_events_label.setText("All Events");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }  
        });                             

        // show label mouse listener                               
        show_label.addMouseListener(new java.awt.event.MouseAdapter() {
             @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\show_white.png");
                show_label.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\show_blue.png");
                show_label.setIcon(user_Icon1);
            }


            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {   
                try{
                    Date eventdate = jDateChooser.getDate();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");  
                    String strdate = dateFormat.format(eventdate); 
                    Statement st = con.createStatement();
                    ResultSet resultset_bydate = st.executeQuery("Select * from events where date = '"+strdate+"'");
                   // events.clear();
                    showevents(resultset_bydate);
                    all_events_label.setText(strdate);
                }
                catch(SQLException e){
                    System.err.println(e);
                } catch (NullPointerException er) {
                    JOptionPane.showMessageDialog(null, "Choose a date !", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }                                      
        });
    
        // register button mouse listener
        register_button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\white_button.png");
                register_button.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\black_button.png");
                register_button.setIcon(user_Icon1);
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (event_list.getSelectedValue() != null) {
                    try {
                        con.close();
                        new EventRegisteration(main_frame, event_name_value.getText(), email);
                        setVisible(false);
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Select an event !", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    
        // logout label mouse listener
        logout_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
              logout_label.setIcon(new ImageIcon("src\\images\\logout_blue.png"));
                
            }
           @Override
           public void mouseExited(MouseEvent evt) {
               logout_label.setIcon(new ImageIcon("src\\images\\logout_red.png"));
           }
           @Override
           public void mouseClicked(MouseEvent evt) {
                new LoginPage(main_frame);
                setVisible(false);
           }
        });
    
        // properties of student panel
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setLayout(null);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setVisible(true);
    
    main_frame.add(this);
}
    // getting events from database
    public void showevents(ResultSet resultset){
        try{
            this.resultset=resultset;
            events_model.clear();

            while(resultset.next()) {
                events_model.addElement(resultset.getString("event_name"));
            }
        } catch(SQLException e) {
              System.out.print(e);
        }
    }
}
