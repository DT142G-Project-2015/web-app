package model;


import java.util.Date;
import java.util.List;

public class Shift {
    public int id;
    public int max_staff;
    public Date start;
    public Date stop;
    public String description;
    public boolean repeated;
    public int count_staff;
    public List<Integer> scheduled;
}
