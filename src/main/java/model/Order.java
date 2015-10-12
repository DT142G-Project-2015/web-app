package model;


import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Order {

    public static class Note {
        public int id;
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
                return Arrays.stream(this.getClass().getFields())
                        .filter(f -> f.getName().equals(name()))
                        .findAny().get()
                        .getAnnotation(SerializedName.class).value();
            }

            public static String getText(Status status) {
                return (status == null ? Initial : status).getText();
            }

            public static Status fromText(String text) {
                return Arrays.stream(values())
                        .filter(st -> st.getText().equals(text))
                        .findAny().orElse(Initial);
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
    public List<Group> groups;


    public boolean isValidPost() {  // TODO: implement this
        return true;
    }

}
