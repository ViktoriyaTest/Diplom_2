package org.example;

public class UserLogIn {
    private String email;
    private String password;

    public UserLogIn() {
    }

    public UserLogIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserLogIn getUserLogin(User user) {
        return new UserLogIn(user.getEmail(), user.getPassword());
    }
}