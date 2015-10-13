package model;

import java.util.Date;

public class Article {
    public int id;
    public String name;
    public String category;
    public double amount;
    public String unit;
    public String exp_date;

    public boolean isValid() {

        return ((name != null) && (category != null)
                && (unit != null)
                && (exp_date != null));
    }
}
