package model;

public class Article {
    public int id;
    public String name;
    public String category;
    public double amount;
    public String unit;
    public String exp_date;

    public boolean isValid() {

        return (name != null) && (category != null)
                && (amount != 0.0) && (unit != null)
                && (exp_date != null);
    }
}
