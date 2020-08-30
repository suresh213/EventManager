package college;

import com.toedter.calendar.JDateChooser;
import dbpackage.DBConnection;
import homepage.LoginPage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import utility.Jframe_uses;
import utility.Mail;
import utility.Validation;

public final class CollegePage extends JPanel {
    
    //panels
    private JPanel newEvent_panel;
    private JPanel newEventForm_panel;
    private JPanel eventList_panel;
    private JPanel jlist_panel;
    
    //buttons
    private JLabel updateEvent_btn;
    private JLabel addEvent_btn;
    private JLabel deleteEvent_btn;
    private JLabel showEvent_btn;
     
    //date label
    private JLabel date_show;
    
    //time label
    private JLabel time_show;
    
    //labels
    private JLabel newEvent_lbl;
    private JLabel eventName_lbl;
    private JLabel date_lbl;
    private JLabel dept_lbl;
    private JLabel venue_lbl;
    private JLabel eventList_lbl;
    private JLabel eventDescription_lbl;
    private JLabel mail_lbl;
    
    //text fields
    private JTextField eventName_txtField;
    private JTextField dept_txtField;
    private JTextField venue_txtField;
    
    //text area
    JTextArea eventDescription_txtArea;
    
    //date chooser
    JDateChooser event_dateChooser;
    
    // list box
    private JList events_listBox;
    
    // database connection
    private Connection con;
    private ArrayList<String> allEventsList;
    private DefaultListModel<String> lstModel;
    
    // main frame
    private JFrame main_frame;
    
    public CollegePage(JFrame main_frame) {
        
        this.main_frame = main_frame;
        
        // get connection
        con = new DBConnection().getConnection();
        // delete completed events
        deleteCompletedEvents();
        // get all events
        allEventsList = getAllEventsList();
        
        initComponents();
        
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
    
    private void initComponents() {
        
        //font 
        Font tahoma = new Font("Tahoma",Font.PLAIN,11);
        
        // name panel
        JPanel name_panel = Jframe_uses.getPanel(0,55,1200,120);
        name_panel.setBackground(new Color(0,30,50));
        name_panel.setLayout(null); 
        add(name_panel);
     
        // admin name label
        JLabel admin_name = Jframe_uses.getLabel(55,20,200,80, "Hey,  Admin"); 
        admin_name.setForeground(Color.white);
        admin_name.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,36));
        name_panel.add(admin_name);
    
        // event list panel
        eventList_panel = Jframe_uses.getPanel(40,200, 380, 320);
        eventList_panel.setLayout(null);
        eventList_panel.setBackground(new Color(240,240,240));
        add(eventList_panel);

        // jlist panel
        jlist_panel = Jframe_uses.getPanel(40,50, 300, 250);
        jlist_panel.setLayout(new BorderLayout());
        jlist_panel.setBackground(Color.white);
        eventList_panel.add(jlist_panel);
       
        // admin page label
        JLabel admin_page = Jframe_uses.getLabel(15,5,200,42, "Admin Page"); 
        admin_page.setForeground(Color.black);
        admin_page.setFont(new Font("Segoe UI LIGHT",Font.PLAIN,32));
        add(admin_page);
        
        // event list label
        eventList_lbl = Jframe_uses.getLabel(140, 3, 120, 60, "Events List");
        eventList_lbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        eventList_panel.add(eventList_lbl);
        
        // list model
        lstModel = new DefaultListModel<>();
        // event array
        String eventsArr[] = new String[allEventsList.size()];
        int index=0;
        for (String event_name: allEventsList) {
            eventsArr[index++] = event_name;
            lstModel.addElement(event_name);
        }
        
        // jlist for events
        events_listBox = new JList<>();
        events_listBox.setModel(lstModel);
        events_listBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        events_listBox.setSelectionBackground(new Color(123, 142, 173));
        events_listBox.setFixedCellHeight(35);
        events_listBox.setBounds(0,0, 300, 250);
        events_listBox.setBorder(new LineBorder(new Color(0,0,0,20)));
        
        // list selection listener
        events_listBox.addListSelectionListener((javax.swing.event.ListSelectionEvent evt) -> {
            events_listBoxValueChanged(evt);
        });
        
        // scroll pane for eventslist
        JScrollPane scroll = new JScrollPane(events_listBox);
        jlist_panel.add(scroll);
        scroll.setVisible(true);
        
        // new event label
        newEvent_lbl = Jframe_uses.getLabel(700,180, 230, 40, "Add a new Event");
        newEvent_lbl.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(newEvent_lbl);
        
        
        // new event panel
        newEvent_panel = Jframe_uses.getPanel(470,230, 700, 250);
        newEvent_panel.setLayout(null);
        newEvent_panel.setBackground(new Color(230, 231, 232));
        add(newEvent_panel);
        
        
        // event name label
        eventName_lbl = Jframe_uses.getLabel(45, 20, 100, 30, "Event Name:");
        eventName_lbl.setFont(new Font(null, Font.PLAIN, 15));
        newEvent_panel.add(eventName_lbl);
        
        // event name text field
        eventName_txtField = Jframe_uses.getTextField(45, 50, 250, 30, "");
        eventName_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));

        newEvent_panel.add(eventName_txtField);
        
        //focus listerner for textfield
        eventName_txtField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                eventName_txtFieldFocusGained(evt);
            }
            @Override
            public void focusLost(FocusEvent evt) {
                eventName_txtFieldFocusLost(evt);
            }
        });
        
        // department label
        dept_lbl = Jframe_uses.getLabel(45, 90, 100, 30, "Dept:");
        dept_lbl.setFont(new Font(null, Font.PLAIN, 15));
        newEvent_panel.add(dept_lbl);
        
        // department text field
        dept_txtField = Jframe_uses.getTextField(45, 120, 250, 30, "");
        dept_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));
        newEvent_panel.add(dept_txtField);
        dept_txtField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                dept_txtFieldFocusGained(evt);
            }
            @Override
            public void focusLost(FocusEvent evt) {
                dept_txtFieldFocusLost(evt);
            }
        });
        
        // event date label
        date_lbl = Jframe_uses.getLabel(370, 20, 100, 30, "Date:");
        date_lbl.setFont(new Font(null, Font.PLAIN, 15));
        newEvent_panel.add(date_lbl);
        
        // date chooser
        event_dateChooser = new JDateChooser();
        event_dateChooser.setBounds(370, 50, 120, 30);
        event_dateChooser.setDate(new java.util.Date());
        newEvent_panel.add(event_dateChooser);
        
        // event venue label
        venue_lbl = Jframe_uses.getLabel(370, 90, 100, 30, "Venue:");
        venue_lbl.setFont(new Font(null, Font.PLAIN, 15));
        newEvent_panel.add(venue_lbl);
        
        // event venue text field
        venue_txtField = Jframe_uses.getTextField(370, 120, 250, 30, "");
        venue_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));
        newEvent_panel.add(venue_txtField);
        
        // focus listener
        venue_txtField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                venue_txtFieldFocusGained(evt);
            }
            @Override
            public void focusLost(FocusEvent evt) {
                venue_txtFieldFocusLost(evt);
            }
        });
        
        
        // event description label
        eventDescription_lbl = Jframe_uses.getLabel(45, 165, 100, 30, "Description:");
        eventDescription_lbl.setFont(new Font(null, Font.PLAIN, 15));
        newEvent_panel.add(eventDescription_lbl);
        
        // event description text area
        eventDescription_txtArea = Jframe_uses.getTextArea(45, 195,600, 30, "");
        eventDescription_txtArea.setLineWrap(true);
        eventDescription_txtArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));
        newEvent_panel.add(eventDescription_txtArea);
        eventDescription_txtArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                eventDescription_txtAreaFocusGained(evt);
            }
            @Override
            public void focusLost(FocusEvent evt) {
                eventDescription_txtAreaFocusLost(evt);
            }
        });
        
        // mail sent detail
        mail_lbl = Jframe_uses.getLabel(530,570, 170, 30, "");
        mail_lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(mail_lbl);
        
        
        // add event button
        ImageIcon add_button_icon = new ImageIcon("src\\images\\add_blue.png");
        addEvent_btn = Jframe_uses.getLabel(600, 500, 90, 40, "");
        addEvent_btn.setIcon(add_button_icon);
        addEvent_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(addEvent_btn);
        
        // add event mouse listener
        addEvent_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\add_white.png");
                addEvent_btn.setIcon(user_Icon1);
            }
            @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\add_blue.png");
                addEvent_btn.setIcon(user_Icon1);
            }
            @Override
                public void mouseClicked(java.awt.event.MouseEvent evt){
                addEvent_btnMouseClicked(evt);
             }
        });
         
        
        
        // update event button
        ImageIcon update_button_icon = new ImageIcon("src\\images\\update_blue.png");
        updateEvent_btn = Jframe_uses.getLabel(750, 500, 90, 40, "");
        updateEvent_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateEvent_btn.setIcon(update_button_icon);
        add(updateEvent_btn);
        
        // update event mouse listener
        updateEvent_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\update_white.png");
                updateEvent_btn.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\update_blue.png");
                updateEvent_btn.setIcon(user_Icon1);
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                updateEvent_btnMouseClicked(evt);
            }
        });
        
        // delete event button
        ImageIcon delete_button_icon = new ImageIcon("src\\images\\delete_blue.png");
        deleteEvent_btn = Jframe_uses.getLabel(900, 500, 90, 40, "DELETE");
        deleteEvent_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteEvent_btn.setIcon(delete_button_icon);
        add(deleteEvent_btn);
        
        // delete event mouse listener
        deleteEvent_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\delete_white.png");
                deleteEvent_btn.setIcon(user_Icon1);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\delete_blue.png");
                deleteEvent_btn.setIcon(user_Icon1);
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                deleteEvent_btnMouseClicked(evt);
            }
        });
        
        
        
        // show an event detail
        ImageIcon show_button_icon = new ImageIcon("src\\images\\showpart_blue.png");
        showEvent_btn = Jframe_uses.getLabel(160, 540,160, 50, "");
        showEvent_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showEvent_btn.setIcon(show_button_icon);
        add(showEvent_btn);
        
        // show event mouse listener
        showEvent_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\showpart_white.png");
                showEvent_btn.setIcon(user_Icon1);
            }
            @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                ImageIcon user_Icon1 = new ImageIcon("src\\images\\showpart_blue.png");
                showEvent_btn.setIcon(user_Icon1);
            }
            @Override
                public void mouseClicked(java.awt.event.MouseEvent evt){
                showEvent_btnMouseClicked(evt);
             }
        });
        
        // logout button
        ImageIcon logout_icon = new ImageIcon("src\\images\\logout_red.png");
        JLabel logout_label = Jframe_uses.getLabel(1120,6,40,40, "");
        logout_label.setFont(new Font("Segoe",Font.PLAIN,11));
        logout_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout_label.setIcon(logout_icon);
        add(logout_label);
        
        // logout mouse listener
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
        
        // time show
        time_show = Jframe_uses.getLabel(1050,585,97,27, "Time");
        time_show.setFont(tahoma);
        add(time_show);

        //date show
        date_show = Jframe_uses.getLabel(1100,585,97,27, "Date");
        date_show.setFont(tahoma);
        add(date_show);
   
        // set properties of college page
        setBounds(0,0,1200,650);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setLayout(null); 
        setVisible(true);
        main_frame.add(this);
    }
    
    //Deleting completed events
    private void deleteCompletedEvents() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from events");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            while (rs.next()) {
                if (sdf.parse(sdf.format(new java.util.Date())).compareTo(sdf.parse(rs.getString("date"))) >= 1) {
                    con.createStatement().executeUpdate("delete from events where event_name = " + "'" + rs.getString("event_name") + "'");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        } catch (ParseException er) {
            System.out.println("Parse Error : " + er);
        }
    }
    
    // get events from database
    private ArrayList<String> getAllEventsList() {
        ArrayList<String> eventsList = new ArrayList<>();
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from events");
            
            // date format
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                if (sdf.parse(sdf.format(new java.util.Date())).compareTo(sdf.parse(rs.getString("date"))) >= 1) {
                    con.createStatement().executeUpdate("delete from events where event_name = " + "'" + rs.getString("event_name") + "'");
                } else {
                    eventsList.add(rs.getString("event_name"));
                }
            }
            
        
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        } catch (ParseException er) {
            System.out.println("Parse Error : " + er);
        }
        return eventsList;
    }
    
    //logging out
    private void logout_btnActionPerformed(ActionEvent evt) {   
        new LoginPage(main_frame);
        setVisible(false); //close panel
    }                     
    
    // show event details
    private void showEvent_btnMouseClicked(MouseEvent evt) {
        if (events_listBox.getSelectedValue() != null) {
            new EventParticipationList(main_frame, events_listBox.getSelectedValue().toString());
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select an event !", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // update existing event
    private void updateEvent_btnMouseClicked(MouseEvent evt) {                                                
        
        try {
            // getting datas from textfields
            Statement st = con.createStatement();
            String event_name = eventName_txtField.getText();
            String updatedDate = new SimpleDateFormat("dd-MM-yyyy").format(event_dateChooser.getDate());
            String updatedDept = dept_txtField.getText();
            String updatedVenue = venue_txtField.getText();
            String updatedDescription = eventDescription_txtArea.getText();
            
            // check textfields empty
            if (event_name.equals("") || updatedDept.equals("") || updatedVenue.equals("") || updatedDescription.equals("")) {
                throw new Validation();
            }
            
            // updating details in database
            st.executeUpdate("update events set "
                    + "date = " + "'" + updatedDate + "'" + ", "
                    + "department = " + "'" + updatedDept + "'" + ", "
                    + "venue = " + "'" + updatedVenue + "'" + ", "
                    + "description = " + "'" + updatedDescription + "'"  
                + " where event_name = " + "'" + eventName_txtField.getText() + "'");
            
            // set details in textfields
            eventName_txtField.setText("");
            event_dateChooser.setDate(new java.util.Date());
            dept_txtField.setText("");
            venue_txtField.setText("");
            eventDescription_txtArea.setText("");
            
            // show dialog
            JOptionPane.showMessageDialog(newEventForm_panel, "Event Updated !", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch(Validation e) {
            e.fillAllDetailsMsg(newEventForm_panel);
        } catch(SQLException er){
            System.out.println(er);
            JOptionPane.showMessageDialog(newEventForm_panel, "Event not available !", "Fail", JOptionPane.ERROR_MESSAGE);
        } catch(HeadlessException err) {
            JOptionPane.showMessageDialog(newEventForm_panel, "Error !", "Fail", JOptionPane.ERROR_MESSAGE);
        }
    }         
    
    // add new event
    private void addEvent_btnMouseClicked(MouseEvent evt) {  
        try {
            // get detals from textfield
            String event_name = eventName_txtField.getText();
            String department_name = dept_txtField.getText();
            String event_venue = venue_txtField.getText();
            String event_description = eventDescription_txtArea.getText();
            
            // date format
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String event_date = sdf.format(event_dateChooser.getDate());
            
            if (event_name.length()==0 || department_name.length()==0 || event_venue.length()==0 || event_description.length()==0) {
                throw new Validation();
            }
            
            try {
                // check date passes or not
                if (sdf.parse(sdf.format(new java.util.Date())).compareTo(event_dateChooser.getDate()) >= 1) {
                    throw new Validation();
                }
            } catch (Validation e) {
                e.dateErrorMsg(newEventForm_panel);
                return;
            } catch (ParseException er) {
                JOptionPane.showMessageDialog(newEventForm_panel, "Error !", "Fail", JOptionPane.ERROR_MESSAGE);
            }

            // insert values in database
            String query = "insert into events values(?, ?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(query);
            
            ps.setString(1, event_name);
            ps.setString(2, event_date);
            ps.setString(3, department_name);
            ps.setString(4, event_venue);
            ps.setString(5, event_description);
            
            // setting details in textfields
            eventName_txtField.setText("");
            event_dateChooser.setDate(new java.util.Date());
            dept_txtField.setText("");
            venue_txtField.setText("");
            eventDescription_txtArea.setText("");
            
            ps.executeUpdate();
            
            // adding events to list
            lstModel.addElement(event_name);
            
            JOptionPane.showMessageDialog(newEventForm_panel, "New  event successfully created!", "Success", JOptionPane.INFORMATION_MESSAGE); 
            sendMailToStudents(event_name);
            
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(newEventForm_panel, "Event already available !", "Fail", JOptionPane.ERROR_MESSAGE);
        } catch (Validation e) {
            e.fillAllDetailsMsg(newEventForm_panel);
        }
            
    }
    
    // delete existing event
    private void deleteEvent_btnMouseClicked(MouseEvent evt) {  
        try {
            // deleting events from database
            Statement st = con.createStatement();
            String event_name = eventName_txtField.getText();
            st.executeUpdate("delete from events where event_name = " + "'" + event_name + "'");
            
            if (event_name.length()==0)
                throw new Validation();
            
            // deleting events from list
            lstModel.removeElement(event_name);
            eventName_txtField.setText("");
            dept_txtField.setText("");
            venue_txtField.setText("");
            event_dateChooser.setDate(new java.util.Date());
            eventDescription_txtArea.setText("");
            
            JOptionPane.showMessageDialog(newEvent_panel, "Event succesfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException e) {
            System.out.println("Error : " + e);
        }  catch (Validation er) {
            er.showDeletionMsg(newEventForm_panel);
            eventName_txtField.setText("");
        }
        
    }  
    
    // send mail to students
    private void sendMailToStudents(String event_name) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from students");
            
            // setting subject to mail
            while (rs.next()) {
                if (!rs.getBoolean("is_admin")) {
                    new Thread(new Mail("New Event !", event_name + " is round the corner ! Do register for the event.",rs.getString("email"))).start();
                }
            }
            mail_lbl.setText("Mail sent successfully !");
        } catch (SQLException e) {
            System.out.println("SqL Error : " + e);
        }
    }

    // setting values in textfields
    private void events_listBoxValueChanged(ListSelectionEvent evt) {
        try {
            if (events_listBox.getSelectedValue() != null) {
                String event_name = events_listBox.getSelectedValue().toString();
            
                String query = "select * from events where event_name = " + "'" + event_name + "'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                rs.next();
                eventName_txtField.setText(event_name);
                dept_txtField.setText(rs.getString("department"));
                event_dateChooser.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("date")));
                venue_txtField.setText(rs.getString("venue"));
                eventDescription_txtArea.setText(rs.getString("description"));
            }
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(newEventForm_panel, "Event not available !", "Fail", JOptionPane.ERROR_MESSAGE);
        } catch (Exception er) {
            JOptionPane.showMessageDialog(newEventForm_panel, "Error !", "Fail", JOptionPane.ERROR_MESSAGE);
        }
    }                                           

    private void eventName_txtFieldFocusGained(FocusEvent evt) {        
        eventName_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    }                                              

    private void eventName_txtFieldFocusLost(FocusEvent evt) { 
        eventName_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));
    }                                            

    private void dept_txtFieldFocusGained(FocusEvent evt) { 
        dept_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));
    }                                         

    private void dept_txtFieldFocusLost(FocusEvent evt) {
        dept_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));

    }                                       

    private void venue_txtFieldFocusGained(FocusEvent evt) { 
        venue_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,  Color.BLACK));

    }                                          

    private void venue_txtFieldFocusLost(FocusEvent evt) {
        venue_txtField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));

    }                  
    
    private void eventDescription_txtAreaFocusGained(FocusEvent evt) { 
        eventDescription_txtArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

    }     
    
    private void eventDescription_txtAreaFocusLost(FocusEvent evt) {
        eventDescription_txtArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)));

    } 
}
