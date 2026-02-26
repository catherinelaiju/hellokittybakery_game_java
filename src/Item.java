/**
 * Item class
 * Represents a single product in our bakery (e.g., Cupcake, Toast).
 * 
 * WHY A CLASS?
 * Instead of having separate variables like "cupcakePrice", "toastPrice", etc.,
 * we create a "Blueprint" called Item. Objects made from this blueprint
 * will have their own name, price, and stock.
 */
public class Item {
    
    // VARIABLES (Properties of an Item)
    String name;
    int price;
    int stock;
    String imagePath;

    // CONSTRUCTOR
    // This runs when we say "new Item(...)"
    public Item(String name, int price, int stock, String imagePath) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }
    
    // We can add methods here later (like buying, restocking)
}
