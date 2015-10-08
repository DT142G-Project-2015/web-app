package model;


import java.util.List;

public class Order {


    public static class Note {
        public String text;
    }

    public static class Item extends model.Item {
        public Note note;
        public List<SubItem> subItems;
    }

    public static class SubItem extends model.Item {
        public Note note;
    }

    public static class Group {
        public int id;
        public String status;
        public List<Item> items;
    }

    public int id;
    public int booth;
    public List<Group> groups;


    public boolean isValidPost() {  // TODO: implement this
        return true;
    }

}
