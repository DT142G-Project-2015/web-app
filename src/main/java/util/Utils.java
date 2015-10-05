package util;

import com.google.gson.GsonBuilder;

/**
 * Created by joed1300 on 2015-10-01.
 */
public class Utils {
    public static String toJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
}
