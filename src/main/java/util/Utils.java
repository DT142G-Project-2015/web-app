package util;

import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Utils {

    public static String toJson(Stream<Map<String, Object>> s) {
        return toJson(s.collect(Collectors.toList()));
    }

    public static String toJson(Object o) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create().toJson(o);
    }
}
