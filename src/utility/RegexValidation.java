
package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidation {

    // setting regex for skcet.edu.in or skct.edu.in and password
    private final String email_regex = "[a-zA-z0-9_.-]+@(skct|skcet)\\.edu\\.in+";
    private final String password_regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=\\S+$).{8,20}$";
    
    // check email valid or not
    public boolean isEmailValid(String email) {
        
        Pattern emailPattern = Pattern.compile(email_regex);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }
    
    // check password valid or not
    public boolean isPassValid(String password) {

        Pattern passwordPattern = Pattern.compile(password_regex);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }
}
