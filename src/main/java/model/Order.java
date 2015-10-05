package model;


import java.util.List;

public class Order {


    public static class Note {
        public String text;
    }

    public static class Item extends model.Item {
        public Note note;
    }

    public static class Group {
        public List<Item> items;
        public String status;
    }

    public int id;
    public List<Group> groups;


    public boolean isValidPost() {  // TODO: implement this
        return true;
    }

}