package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Category {

    public String name;
    public List<Article> articles;
    public int id;


    public static class Article {
        public int article_id;
        public String article_name;
        public double amount;
        public String unit;
        public String exp_date;
        public int category_id;

        public boolean isValid() {
            return ((article_name != null) && (unit != null));
        }
    }
}


