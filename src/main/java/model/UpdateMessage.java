package model;


import util.Utils;

public class UpdateMessage {
    public final String response;
    public final int id;

    public UpdateMessage(String response, int id) {
        this.response = response;
        this.id = id;
    }

    public String toJson() {
        return Utils.toJson(this);
    }
}
