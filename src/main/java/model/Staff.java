package model;

/**
 * Created by qwertyuiop on 2015-10-08.
 */
public class Staff {
    public String username;
    public String password;
    public int role;

    public boolean isValid() {
        return (username.length() > 0) && (password.length() > 6) && (role == 1 || role == 2);
    }
}
