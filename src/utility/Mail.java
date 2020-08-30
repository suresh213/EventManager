
package utility;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail implements Runnable{
 
    private final String adminEmail = "eventmanager1001@gmail.com";
    private final String adminPassword = "Mypass12345";
    private final Session session;
    private String subject;
    private String msg;
    private String student_email;
    
    public Mail(String subject, String msg, String student_email){
       
        this.student_email=student_email;
       this.subject = subject;
       this.msg = msg;
       System.err.println("Sending Mail.....");
      
       // setting up properties for mail
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // creating session
        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(adminEmail, adminPassword);
                    }
                });
   }
    
    // send mail
     public void sendMail() {
        try {
            //creating message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(student_email)
            );
            // subject
            message.setSubject(subject);
            // message
            message.setText(msg);
            
            // sending message
            Transport.send(message);

            System.out.println("Mail Sent");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
    
    @Override
    public void run(){
       sendMail(); 
}
}
