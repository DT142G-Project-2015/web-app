package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Category {

    public String name;
    public List<Article> articles;
    public int id;


    public static class Article {
        public int id;
        public String name;
        public double amount;
        public String unit;
        public String exp_date;
        public int category_id;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        public boolean isValid() {
            return ((name != null) && (unit != null) && (exp_date.compareTo(date) >= 0));
        }
    }
}


