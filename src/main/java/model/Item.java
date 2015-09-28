package model;

import java.math.BigDecimal;

public class Item {
    public int id;
    public String name;
    public String description;
    public BigDecimal price;
    public int foodtype;

    public boolean isValid() {
        return price.compareTo(new BigDecimal(0)) >= 0 &&   // TODO: Add more checks?
               name.length() > 0;
    }
}
