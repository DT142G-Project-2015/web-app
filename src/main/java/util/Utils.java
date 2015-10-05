package util;

import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Response by joed1300 on 2015-10-01.
 */
public class Utils {
    public static String toJson(Object o) {

        // convert stream to list as Gson won't handle streams directly
        if (o instanceof Stream<?>)
            o = ((Stream<Map<String, Object>>)o).collect(Collectors.toList());

        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
}