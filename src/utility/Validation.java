
package utility;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Validation extends Exception {
    
    private String errorType = "";
    
    public Validation() {
    }
    public Validation(String errprType) {
        this.errorType = errprType;
    }
    
    // fill all details
    public void fillAllDetailsMsg(JPanel panel) {
        if (errorType.equals("regex"))
            JOptionPane.showMessageDialog(panel, "Email or password is not valid !", "Info", JOptionPane.INFORMATION_MESSAGE);
        else if (errorType.equals("email"))
            JOptionPane.showMessageDialog(panel, "Email id already exists !", "Info", JOptionPane.INFORMATION_MESSAGE);
        else if (errorType.equals("regNo"))
            JOptionPane.showMessageDialog(panel, "Wrong register number !", "Info", JOptionPane.INFORMATION_MESSAGE);
        else if (errorType.equals("year"))
            JOptionPane.showMessageDialog(panel, "Year must be less than 5 !", "Info", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(panel, "Fill all details !", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showDeletionMsg(JPanel panel) {
        JOptionPane.showMessageDialog(panel, "Deletion unsuccesfull !", "Fail", JOptionPane.ERROR_MESSAGE);
    }
    
    public void dateErrorMsg(JPanel panel) {
        JOptionPane.showMessageDialog(panel, "The date has already passed !", "Fail", JOptionPane.ERROR_MESSAGE);
    }
}
