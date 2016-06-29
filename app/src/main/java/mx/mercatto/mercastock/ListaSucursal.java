package mx.mercatto.mercastock;


public class ListaSucursal {
    private String id;
    private String name;

    public ListaSucursal(String i, String n) {
        id = i;
        name = n;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
