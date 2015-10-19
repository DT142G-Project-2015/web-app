package model;


import java.util.List;

public class Shift {
    public int id;
    public int max_staff;
    public java.sql.Date start;
    public java.sql.Date stop;
    public String description;
    public boolean repeat;
    public int count_staff;
    public List<Integer> scheduled;
}
