package Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cryp {
    private final long id;
    private final StringProperty name;
    private final StringProperty price;

    public Cryp(long id, String name, String price) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleStringProperty(price);
    }

    public StringProperty getIdProperty() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    public String getName() {
        return name.get();
    }

    public String getPrice() {
        return price.get();
    }

    // Getter methods for the StringProperty version of name and price
    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getPriceProperty() {
        return price;
    }
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return (id+" "+name+" "+price).toString();
    }
}
