package model;

import java.util.List;

public class Menu {


    public static class Item extends model.Item {
        //public List<SubItem> subItems;
    }

    public static class Group {
        public String name;
        public List<Item> items;
    }


    public int id;
    public String name;
    public List<Group> groups;
}
