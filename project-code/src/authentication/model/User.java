package authentication.model;

import java.util.List;

public class User {
    private final String username;
    private final String password;
    private final List<String> securityQuestions;

    public User(String username, String password, List<String> securityQuestions) {
        this.username = username;
        this.password = password;
        this.securityQuestions = securityQuestions;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getSecurityQuestions() {
        return securityQuestions;
    }
}
