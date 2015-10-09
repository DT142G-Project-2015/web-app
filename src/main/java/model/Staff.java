package model;

import java.math.BigDecimal;

public class Staff {
    public int id;
    public String username;
    public String password;
    public int role;
    public String first_name;
    public String last_name;


    public boolean isValid() {
        return username.length() > 0 && password.length() > 5 && (role == 1 || role == 2) && first_name.length() > 0 && last_name.length() > 0;
    }
}