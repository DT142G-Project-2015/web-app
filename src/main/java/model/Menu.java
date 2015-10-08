package model;

import java.util.Date;
import java.util.List;

public class Menu {


    public static class Item extends model.Item {
        //public List<SubItem> subItems;
    }

    public static class Group {
        public int id;
        public String name;
        public List<Item> items;

        public boolean isValidPost() {
            return name != null && name.length() > 0;
        }
    }

    public int id;
    public String name;
    public Date start_date;
    public Date stop_date;
    public List<Group> groups;

    public boolean isValidPost() {
        return name != null && name.length() > 0;
    }
}
