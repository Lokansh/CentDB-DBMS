package authentication.model;

public final class Session {
    private static User user;
    private static Session session = null;

    private Session() {

    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        Session.user = user;
    }
}
