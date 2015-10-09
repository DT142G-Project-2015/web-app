package model;

import java.math.BigDecimal;

public class Staff {
    public int id;
    public String username;
    public String password;
    public int role;
    public boolean isValid() {
        return username.length() > 0 && password.length() > 6 && (role == 1 || role == 2);

    }
}