package model;


import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Order {

    public static class Note {
        public Integer id;
        public String text;
    }

    public static class Item extends model.Item {
        public List<Note> notes;
        public List<SubItem> subItems;
    }

    public static class SubItem extends model.Item {
        public List<Note> notes;
    }

    public static class Group {

        public enum Status {
            @SerializedName("initial") Initial,
            @SerializedName("readyForKitchen") ReadyForKitchen,
            @SerializedName("readyToServe") ReadyToServe,
            @SerializedName("done") Done;

            private String getText() {
                try {
                    return getClass().getField(name()).getAnnotation(SerializedName.class).value();
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);  // IMPOSSIBLE
                }
            }

            public static String getText(Status status) {
                return (status == null ? Initial : status).getText();
            }

            public static Status fromText(String text) {
                for (Status st : values()) {
                    if (st.getText().equals(text))
                        return st;
                }
                return Initial;
            }

            public static String sanitize(String s) {
                return fromText(s).getText();
            }
        }

        @SerializedName("status")
        public Status status;
        public int id;
        public List<Item> items;
    }

    public int id;
    public int booth;
    public Boolean payed;
    public List<Group> groups;


    public boolean isValidPost() {  // TODO: implement this
        return true;
    }

}
