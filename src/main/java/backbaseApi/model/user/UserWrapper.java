package backbaseApi.model.user;

public class UserWrapper {
    private User user;

    public UserWrapper(User user) {
        this.user = user;
    }
    // getter and setter for the field

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
