package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
    public int id;
    public String name;
    public String category;
    public double amount;
    public String unit;
    public String exp_date;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


    public boolean isValid() {

        return ((name != null) && (category != null)
                && (unit != null)
                && (exp_date.compareTo(date) >= 0));
    }
}