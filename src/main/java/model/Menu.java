package model;

import java.util.ArrayList;
import java.util.List;

public class Menu {


    public static class Item extends model.Item {
        //public List<SubItem> subItems;
        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getPrice() {
            return price.toString();
        }
    }

    public static class Group {
        public int id;
        public String name;
        public List<Item> items = new ArrayList<>();


        public Group() {}

        public Group(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<Item> getItems() {
            return items;
        }

        public boolean isValidPost() {
            return name != null && name.length() > 0;
        }
    }

    public int id;
    public Integer type;
    public java.sql.Date start_date;
    public java.sql.Date stop_date;
    public List<Group> groups;

    public boolean isValidPost() {
        return type != null && (type == 0 || type == 1 || type == 2);
    }
}
