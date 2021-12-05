package authentication.model;

import java.util.List;

public class User {


    private final String name;
    private final String username;
    private final String password;
    private final List<String> securityAnswers;

    public User(String name, String username, String password, List<String> securityAnswers) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.securityAnswers = securityAnswers;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getSecurityAnswers() {
        return securityAnswers;
    }
}
