
package college;

import dbpackage.DBConnection;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import student.ForgotPassword;
import utility.Jframe_uses;


public class EventParticipationList extends JPanel {
    
    private JFrame main_frame;
    private Connection con;
    private DefaultTableModel table_model = new DefaultTableModel(new Object[]{"Reg_no","Student_name","Department"},0);
    private ArrayList<String> participantsList;
    private String event_name;
    private JScrollPane scrollPane;
    private JTable participantsTable;
    
    public EventParticipationList(JFrame main_frame, String event_name) {
        
        this.main_frame = main_frame;
        this.event_name = event_name;
        
        // get connection
        con = new DBConnection().getConnection();
        
        // set particpants list
        this.participantsList = getParticipantsList(event_name);
        
        initComponents();
        showParticipants();
    }
    
    private void initComponents() {

        // table panel
        JPanel table_panel = Jframe_uses.getPanel(200,150,800,400);
        table_panel.setBorder(new LineBorder(Color.black));
        table_panel.setBackground(Color.white);
        table_panel.setLayout(null); 
        add(table_panel);

        //title label
        JLabel title = Jframe_uses.getLabel(485,100,250,30, "List of Registered Students"); 
        title.setForeground(Color.black);
        title.setFont(new Font("Tahoma",Font.PLAIN,20));
        add(title);

        //event name label
        JLabel event_name_label = Jframe_uses.getLabel(520,30,250,30, event_name.toUpperCase());
        event_name_label.setForeground(Color.black);
        event_name_label.setFont(new Font("Tahoma",Font.PLAIN,24));
        add(event_name_label);

        // participants table
        participantsTable = new JTable();
        participantsTable.setModel(table_model);
        participantsTable.setRowHeight(30);
        participantsTable.setBackground(Color.white);
        scrollPane = new JScrollPane(participantsTable);
        scrollPane.setBounds(0, 0, 800, 400);
        scrollPane.setViewportView(participantsTable);
        table_panel.add(scrollPane);
        
        // back button
        ImageIcon back_Icon = new ImageIcon("src\\images\\back.png");
        JLabel back_label = Jframe_uses.getLabel(40, 30,50,50, "");
        back_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(back_label);
        back_label.setIcon(back_Icon);
        
        // back button mouse listener
        back_label.addMouseListener(new MouseAdapter() {
           
           @Override
           public void mouseClicked(MouseEvent evt) {
                new CollegePage(main_frame);
                setVisible(false);
           }
        });
        
        // properties for paticipation list panel
        setBounds(0,0,1200,650);
        setBorder(BorderFactory.createMatteBorder(1, 0,0 , 0,  Color.BLACK));
        setBackground(Color.white);
        setLayout(null); 
        setVisible(true);
        main_frame.add(this);
    }
    
    // get all participants
    private ArrayList<String> getParticipantsList(String event_name) { 
        ArrayList<String> participantList;
        participantList = new ArrayList();
        
        try {
            
            // get values from database
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select student_reg_no from participants where event_name = " + "'" + event_name + "'");
            
            while (rs.next()) {
                
                // adding values to table
                String participantRegNo = rs.getString("student_reg_no");
                participantList.add(participantRegNo);
            }
        } catch(SQLException e) {
            System.out.println("Error : " + e);
        }
        return participantList;
    }
    
    // show all participants
    private void showParticipants() {
        
        try {
            Statement st = con.createStatement();
            
            // getting values from database
            for (String participantRegNo: participantsList) {
                System.out.println(participantRegNo);
                ResultSet rs = st.executeQuery("select * from students where reg_no = " + "'" + participantRegNo + "'");
                rs.next();
                table_model.addRow(new Object[]{participantRegNo, rs.getString("name"), rs.getString("dept")});
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        
    }
}
