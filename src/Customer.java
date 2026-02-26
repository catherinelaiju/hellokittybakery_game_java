import java.awt.*;
import javax.swing.*;

/**
 * Customer Class
 * 
 * Updated with Order System and Patience!
 */
public class Customer {
    
    // POSITION
    int x, y; 
    int speed = 2; 
    
    // APPEARANCE
    Image image;
    int width = 250; 
    int height = 400; 

    // ORDER SYSTEM (Feature #1)
    Item desiredItem; // What they want
    JLabel orderBubble; // Visual bubble showing the item
    
    // PATIENCE SYSTEM (Feature #2)
    int patience = 600; // 600 frames = ~10 seconds
    int maxPatience = 600;
    boolean isAngry = false;
    
    // STATES
    public static final int WALKING_IN = 0;
    public static final int SHOPPING = 1;
    public static final int WALKING_OUT = 2;
    public int state = WALKING_IN;

    // TARGET
    int targetX = 560; 
    
    // COMPONENT
    public JLabel label;

    // Constructor
    public Customer(int startX, int startY, Image img, Item order) {
        this.x = startX;
        this.y = startY;
        this.image = img;
        this.desiredItem = order;
        
        // CUSTOMER IMAGE
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label = new JLabel(new ImageIcon(scaledImg));
        label.setBounds(x, y, width, height);
        
        // ORDER BUBBLE (Thought Bubble)
        // We create a small icon of the item they want
        ImageIcon bubbleIcon = new ImageIcon(order.imagePath);
        Image scaledBubble = bubbleIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        orderBubble = new JLabel(new ImageIcon(scaledBubble));
        // Position bubble near their head (Relative to X, Y)
        orderBubble.setBounds(x + 150, y + 80, 60, 60);
        orderBubble.setOpaque(true);
        orderBubble.setBackground(Color.WHITE);
        orderBubble.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 3)); // Pink border
        orderBubble.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Initially invisible until they reach the counter?
        // Or visible immediately? Let's make it visible so player prepares.
        orderBubble.setVisible(true);
    }
    
    /**
     * update()
     * Moves customer and updates logic.
     */
    public void update() {
        // STATE MACHINE LOGIC
        if (state == WALKING_IN) {
            if (x > targetX) {
                x -= speed;
            } else {
                state = SHOPPING;
            }
        } 
        else if (state == SHOPPING) {
            // PATIENCE MECHANIC
            // Decrease patience counter
            if (patience > 0) {
                patience--;
            } else {
                // Patience ran out!
                isAngry = true;
                state = WALKING_OUT;
            }
        } 
        else if (state == WALKING_OUT) {
            x -= speed;
            // Hide bubble when leaving
            orderBubble.setVisible(false);
        }
        
        // UPDATE COMPONENT POSITIONS
        label.setLocation(x, y);
        // Bubble moves with customer
        orderBubble.setLocation(x + 150, y + 80);
    }
}
