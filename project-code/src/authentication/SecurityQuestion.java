package authentication;

import java.util.*;

public class SecurityQuestion {

    private final Map<Integer, String> securityQuestion = new LinkedHashMap<>();
    private static SecurityQuestion securityInstance;

    private SecurityQuestion() {
        // Required private constructor.
        securityQuestion();
    }

    //List of security questions
    private void securityQuestion() {
        securityQuestion.clear();
        securityQuestion.put(1, "What is your mother's maiden name?");
        securityQuestion.put(2, "What is your first school name?");
        securityQuestion.put(3, "What is your childhood friend's name?");
    }

    public static SecurityQuestion getInstance() {
        if (securityInstance == null) {
            securityInstance = new SecurityQuestion();
        }
        return securityInstance;
    }

    public Map<Integer, String> getSecurityQuestion() {
        return securityQuestion;
    }

    //fetching index of the security questions
    public int questionIndex(final String question) {
        for (Map.Entry<Integer, String> entry : securityQuestion.entrySet()) {
            if (entry.getValue().equals(question)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    // Generating Random Questions
    //Referred URL : https://www.educative.io/edpresso/how-to-generate-random-numbers-in-java
    public String generateRandomQuestion() {
        final int min = 1;
        final int max = 3;
        final int randomQuestionIndex = new Random().nextInt(max - min + 1) + min;
        return securityQuestion.get(randomQuestionIndex);
    }

}

