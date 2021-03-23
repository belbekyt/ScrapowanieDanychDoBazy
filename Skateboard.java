public class Skateboard {
    private String name;
    private String price;

    public Skateboard(){

    }

    public Skateboard(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Skateboard[" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ']';
    }
}