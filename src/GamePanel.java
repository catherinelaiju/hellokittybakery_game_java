import javax.swing.*; // Import Swing components like JPanel, JButton
import java.awt.*;    // Import Graphics tools like Image, Color, Font
import java.awt.event.*; // Import Event listeners (for clicks)
import java.util.ArrayList; // Import ArrayList for storing lists of items

/**
 * GamePanel is the "canvas" or "screen" of our game.
 * It sits inside the JFrame (window) and handles everything we see.
 * 
 * WHY USE A PANEL?
 * - A JFrame is just a window frame (title bar, minimize/close buttons).
 * - A JPanel is a container where we can draw images, add buttons, and run the game loop.
 */
public class GamePanel extends JPanel {

    // Constant variables for Screen Dimensions
    // "final" means these values cannot change while the game is running.
    final int SCREEN_WIDTH = 800;
    final int SCREEN_HEIGHT = 600;

    // GAME STATES
    // We use numbers to represent different "screens".
    // 0 = Start Screen, 1 = Shop Screen
    int gameState = 0; 
    final int START_SCREEN = 0;
    final int SHOP_SCREEN = 1;

    // IMAGES
    // Image objects to store our background pictures.
    Image bgImage;
    Image shopBgImage;

    ArrayList<Image> customerImages = new ArrayList<>(); // Store multiple customer skins

    // BUTTONS
    JButton startButton;
    JButton checkoutButton; // New button to finish transaction
    
    // SHOP VARIABLES
    int money = 0;
    int cartCount = 0;
    int day = 1;
    
    // SHOP UI
    JLabel moneyLabel;
    JLabel cartLabel;
    JLabel dayLabel;
    
    // ITEMS IN THE BAKERY
    // ArrayList is like a "Smart Array" that can grow and change size.
    ArrayList<Item> shopItems = new ArrayList<>();
    ArrayList<JButton> itemButtons = new ArrayList<>(); // To store buttons so we can hide/show them
    
    // CUSTOMER SYSTEM
    ArrayList<Customer> customers = new ArrayList<>();
    Timer gameTimer; // The "Heartbeat" of our game
    int spawnTimer = 0; // Counts frames until next customer spawns
    
    // DAY SYSTEM
    int customersSpawned = 0;
    int customersPerDay = 3; // Start small for Day 1: 3 customers
    int spawnRate = 300; // Frames between spawns (300 = approx 5 seconds)
    boolean dayInProgress = true;

    /**
     * CONSTRUCTOR
     * This method runs ONCE when GamePanel is created.
     * Put setup code here (loading images, setting size).
     */
    public GamePanel() {
        // 1. Set the size of this panel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        
        // 2. Set the layout manager to null
        // "null" layout allows us to use Absolute Positioning (x, y coordinates).
        // By default, Swing puts things in a row (FlowLayout). We want total control!
        this.setLayout(null);

        // 3. Load Images
        // "Toolkit" is a utility to read image files from your computer.
        bgImage = Toolkit.getDefaultToolkit().getImage("src/images/bg.png");
        shopBgImage = Toolkit.getDefaultToolkit().getImage("src/images/bg2.png");
        bgImage = Toolkit.getDefaultToolkit().getImage("src/images/bg.png");
        shopBgImage = Toolkit.getDefaultToolkit().getImage("src/images/bg2.png");
        
        // Load Multiple Customer Images
        // We catch errors just in case one file is missing
        try {
            customerImages.add(Toolkit.getDefaultToolkit().getImage("src/images/customer1.png"));
            customerImages.add(Toolkit.getDefaultToolkit().getImage("src/images/customer2.png"));
            customerImages.add(Toolkit.getDefaultToolkit().getImage("src/images/customer3.png"));
            customerImages.add(Toolkit.getDefaultToolkit().getImage("src/images/customer4.png"));
            
            // Fallback: If for some reason we have 0 images, add the default one
            if (customerImages.isEmpty()) {
                 customerImages.add(Toolkit.getDefaultToolkit().getImage("src/images/hellokitty.png"));
            }
        } catch (Exception e) {
            System.out.println("Error loading customer images: " + e.getMessage());
        }

        // 4. Create UI Components
        setupStartScreen();
        setupShopItems(); // Prepare items but don't show them yet
        setupShopUI();    // Prepare labels
        
        // 5. Start Game Loop
        // Timer(16, ...) means "run this every 16 milliseconds" (~60 FPS)
        gameTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == SHOP_SCREEN) {
                    gameLoop(); // Run game logic only when in shop
                }
                repaint(); // Always redraw
            }
        });
        gameTimer.start(); // START THE ENGINE!
    }

    /**
     * Creates and adds the Start Button.
     * We create a separate method for this to keep code organized.
     */
    public void setupStartScreen() {
        // Create the button with text
        startButton = new JButton(""); // No text, just a clickable area
        
        // Style: Transparent
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        
        // Location and Size: Bottom Center (Adjust based on your BG image)
        // Assuming the button in the image is centered at bottom
        int btnWidth = 300;
        int btnHeight = 100;
        startButton.setBounds((SCREEN_WIDTH - btnWidth) / 2, 450, btnWidth, btnHeight);
        
        // ADD ACTION LISTENER
        startButton.addActionListener(e -> {
            startGame(); // Call method to switch screens
        });

        // Add the button to the panel so it appears
        this.add(startButton);
    }
    
// Checkout button removed in favor of direct serving interaction
    
    /**
     * Define the items available in our shop.
     */
    public void setupShopItems() {
        // --- SHELF 1 (TOP) ---
        Item cupcake = new Item("Cupcake", 5, 10, "src/images/cupcake.png");
        Item toast = new Item("Toast", 3, 20, "src/images/toast.png");
        Item pastry = new Item("Pastry", 8, 5, "src/images/pastry.png");
        
        // --- SHELF 2 (MIDDLE) ---
        Item boba = new Item("Boba Tea", 12, 10, "src/images/boba.png");
        Item pizza = new Item("Pizza Slice", 15, 8, "src/images/pizza.png");
        Item burger = new Item("Burger", 14, 8, "src/images/burger.png");
        
        // --- SHELF 3 (BOTTOM) ---
        Item momo = new Item("Momo", 8, 15, "src/images/momo.png");
        Item fries = new Item("French Fries", 6, 20, "src/images/frenchfries.png");
        Item ramen = new Item("Ramen", 18, 5, "src/images/ramen.png");
        
        // Add all to list
        shopItems.add(cupcake); shopItems.add(toast); shopItems.add(pastry);
        shopItems.add(boba); shopItems.add(pizza); shopItems.add(burger);
        shopItems.add(momo); shopItems.add(fries); shopItems.add(ramen);
        
        // POSITIONING
        
        // Shelf 1 (Top) Y = 220 (Standard size 60x60)
        createItemButton(cupcake, 260, 220, 60, 60); 
        createItemButton(toast, 370, 220, 60, 60);
        createItemButton(pastry, 480, 220, 60, 60);
        
        // Shelf 2 (Middle) Y = 320 (Larger size 80x80)
        // Adjusted X slightly to center better with larger size
        createItemButton(boba, 250, 320, 80, 80);
        createItemButton(pizza, 360, 320, 80, 80);
        createItemButton(burger, 470, 320, 80, 80);
        
        // Shelf 3 (Bottom) Y = 430 (Larger size 80x80)
        createItemButton(momo, 250, 430, 80, 80);
        createItemButton(fries, 360, 430, 80, 80);
        createItemButton(ramen, 470, 430, 80, 80);
    }
    
    /**
     * Helper method to create a clickable button for an item
     */
    public void createItemButton(Item item, int x, int y) {
        // Default size 60x60
        createItemButton(item, x, y, 60, 60);
    }

    /**
     * Helper method to create a clickable button for an item - Custom Size
     */
    public void createItemButton(Item item, int x, int y, int w, int h) {
        // Create an Icon from the image path
        ImageIcon icon = new ImageIcon(item.imagePath);
        // Scale the image 
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(img));
        
        // Style to look like just the image (no border, transparent bg)
        btn.setBounds(x, y, w, h);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        
        // ACTION: What happens when we click the item?
        btn.addActionListener(e -> {
            buyItem(item); // Call buy logic
        });
        
        // Initially invisible (because we start on Title Screen)
        btn.setVisible(false);
        
        this.add(btn);
        itemButtons.add(btn); // Save reference so we can show it later
    }
    
    /**
     * Setup the Labels for Money, Day, Cart
     */
    /**
     * Helper to make labels look cute (Pink box, white text)
     */
    public void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); // Cute font
        lbl.setOpaque(true); // Allow background color
        lbl.setBackground(new Color(255, 182, 193)); // Light Pink
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside box
        ));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setupShopUI() {
        moneyLabel = new JLabel("Money: Rs. 0");
        styleLabel(moneyLabel);
        moneyLabel.setBounds(20, 20, 200, 40); // Increased height for box
        moneyLabel.setVisible(false);
        this.add(moneyLabel);
        
        cartLabel = new JLabel("Cart: 0"); 
        styleLabel(cartLabel);
        cartLabel.setBounds(650, 20, 120, 40);
        cartLabel.setVisible(false);
        this.add(cartLabel);
        
        dayLabel = new JLabel("Day: 1");
        styleLabel(dayLabel);
        dayLabel.setBounds(350, 20, 100, 40);
        dayLabel.setVisible(false);
        this.add(dayLabel);
        
        // setupCheckoutButton(); // Removed
    }

    /**
     * Switches from Start Screen to Shop Screen.
     */
    public void startGame() {
        gameState = SHOP_SCREEN; // Change state variable
        
        // Remove start button because we don't need it in the shop
        startButton.setVisible(false);
        
        // Show Shop UI
        moneyLabel.setVisible(true);
        cartLabel.setVisible(true);
        dayLabel.setVisible(true);
        
        // Show Item Buttons
        for (JButton btn : itemButtons) {
            btn.setVisible(true);
        }
        
        // checkoutButton.setVisible(true); // Removed
        
        // REPAINT
        // This tells computer: "Something changed! Redraw the screen immediately."
        repaint(); 
    }
    
    /**
     * Logic for buying an item
     */
    /**
     * Logic for buying an item - UPDATED for Order System & Restocking
     */
    public void buyItem(Item item) {
        // STOCK CHECK / RESTOCKING
        if (item.stock <= 0) {
           int choice = JOptionPane.showConfirmDialog(this, 
               "Out of Stock! Restock 10 " + item.name + "s for Rs. 5?",
               "Restock", JOptionPane.YES_NO_OPTION);
           
           if (choice == JOptionPane.YES_OPTION) {
               if (money >= 5) {
                   money -= 5;
                   item.stock += 10;
                   playSound("kaching.wav"); // Re-use sound
                   updateGameLabels();
               } else {
                   JOptionPane.showMessageDialog(this, "Not enough money!");
               }
           }
           return; // Stop here, don't try to sell
        }

        // VALIDATION: Is there a customer?
        if (customers.isEmpty()) {
            return;
        }
        
        Customer currentCustomer = customers.get(0);
        
        // Only allow selling if customer is at the counter
        if (currentCustomer.state != Customer.SHOPPING) {
             return;
        }
        
        // MATCHING LOGIC
        if (item.name.equals(currentCustomer.desiredItem.name)) {
            // SUCCESS!
            item.stock--; 
            money += item.price;
            
            // BONUS: Fast service bonus?
            if (currentCustomer.patience > 400) {
                money += 2; // Tip
            }
            
            playSound("kaching.wav");
            updateGameLabels();
            
            // Customer leaves happy
            currentCustomer.state = Customer.WALKING_OUT;
            
        } else {
            // WRONG ITEM
            playSound("error.wav"); // Need to ensure we handle this sound or just strict silence
            // Penalty: Patience drops
            currentCustomer.patience -= 100;
            // Visual feedback could be added here (e.g. shake animation)
        }
    }
    
    public void processCheckout() {
        if (customers.isEmpty()) return;
        
        Customer c = customers.get(0);
        if (c.state == Customer.SHOPPING) {
            // Customer pays and leaves
            money += c.bill;
            
            // Show receipt
            JOptionPane.showMessageDialog(this, "Customer paid Rs." + c.bill + " and left!");
            
            playSound("kaching.wav");
            c.state = Customer.WALKING_OUT; // Trigger exit animation
            
            // Reset Cart for next customer
            cartCount = 0;
            updateGameLabels();
        }
    }
    
    public void updateGameLabels() {
        cartLabel.setText("Cart: " + cartCount);
        moneyLabel.setText("Money: Rs. " + money); 
    }

    /**
     * PAINTCOMPONENT - THE ARTIST
     * This custom method is where all graphics are drawn.
     * It is called automatically by Java whenever the screen updates.
     * 
     * g (Graphics) is like a paintbrush. We tell it what to draw.
     */
    /**
     * MAIN GAME LOOP
     * This runs every 16ms.
     */
    public void gameLoop() {
        // 1. SEQUENTIAL CUSTOMER SPAWNING
        // Logic: If NO customers are on screen, AND we have daily quota left...
        // ... Start a timer (delay) then spawn ONE.
        if (customers.isEmpty() && customersSpawned < customersPerDay) {
            spawnTimer++;
            if (spawnTimer > 100) { // Wait a bit before next one walks in
                spawnCustomer();
                spawnTimer = 0;
            }
        }
        // CHECK ID DAY IS OVER
        else if (customersSpawned >= customersPerDay && customers.isEmpty()) {
            endDay();
        }

        // 2. UPDATE CUSTOMERS
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            c.update(); // Move the customer
            
            // CHECK ANGRY DEPARTURE
            if (c.isAngry && c.state == Customer.WALKING_OUT && c.x == 350) { 
                // Just turned angry and started leaving (x position roughly where they stand)
                // We could play a "fail" sound here once
            }

            // CHECK IF OFF SCREEN
            if (c.state == Customer.WALKING_OUT && c.x < -200) {
                // CLEANUP: Remove visual label AND bubble
                this.remove(c.label);
                this.remove(c.orderBubble);
                
                customers.remove(i);
                i--; // Adjust index since list got shorter
            }
        }
    }
    
    public void spawnCustomer() {
        // Pick a random image from our list
        int randomIndex = (int) (Math.random() * customerImages.size());
        Image randomSkin = customerImages.get(randomIndex);
        
        // PICK RANDOM ORDER
        int itemIndex = (int) (Math.random() * shopItems.size());
        Item randomItem = shopItems.get(itemIndex);
        
        // Spawn at Y=240 
        Customer newCust = new Customer(SCREEN_WIDTH + 50, 240, randomSkin, randomItem);
        customers.add(newCust);
        customersSpawned++;
        
        // ADD TO SCREEN & MANAGE LAYERS
        this.add(newCust.label);
        this.add(newCust.orderBubble);
        
        // Z-Index Management (Lower index = On Top)
        try {
            // HUD Top
            this.setComponentZOrder(moneyLabel, 0);
            this.setComponentZOrder(dayLabel, 1);
            this.setComponentZOrder(cartLabel, 2);
            // newCust.orderBubble must be above newCust.label
            this.setComponentZOrder(newCust.orderBubble, 3);
            this.setComponentZOrder(newCust.label, 4);
            
            // Items are behind (usually auto-layered, but we can leave them)
        } catch (IllegalArgumentException e) {
            // Ignore
        }
    }
    
    /**
     * Called when the day ends.
     */
    public void endDay() {
        dayInProgress = false;
        gameTimer.stop(); // Pause the game
        
        // Show a popup message
        JOptionPane.showMessageDialog(this, "Day " + day + " Complete!\nMoney earned: Rs. " + money);
        
        startNextDay();
    }
    
    /**
     * Sets up the next day with increased difficulty.
     */
    public void startNextDay() {
        day++;
        dayLabel.setText("Day: " + day);
        
        // INCREASE DIFFICULTY
        // More customers logic
        customersPerDay = 3 + (day * 2); 
        
        // Faster spawns (reduce rate, but don't go below 60 frames/1 second)
        if (spawnRate > 100) {
            spawnRate -= 30; 
        }
        
        // Reset counters
        customersSpawned = 0;
        spawnTimer = 0;
        dayInProgress = true;
        
        gameTimer.start(); // Resume game
    }
    
    /**
     * Placeholder sound player
     */
    public void playSound(String soundName) {
        // Use our new SoundManager
        SoundManager.playSound(soundName);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the screen first

        // DRAW BASED ON STATE
        if (gameState == START_SCREEN) {
            // Draw background image at (0,0) with full width/height
            g.drawImage(bgImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            
            // Note: startButton draws itself because we did this.add(startButton)
        } 
            // Draw shop background
            g.drawImage(shopBgImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            
            // DRAW PATIENCE BARS
            for(Customer c : customers) {
                if (c.state == Customer.SHOPPING) {
                    // Calculate bar width based on patience
                    // Max width = 100px
                    int barWidth = (int) ((c.patience / (float)c.maxPatience) * 100);
                    
                    // Color Logic
                    if (barWidth > 60) g.setColor(Color.GREEN);
                    else if (barWidth > 30) g.setColor(Color.YELLOW);
                    else g.setColor(Color.RED);
                    
                    // Draw Bar slightly above head
                    g.fillRect(c.x + 120, c.y + 60, barWidth, 10);
                    
                    // Draw Border
                    g.setColor(Color.BLACK);
                    g.drawRect(c.x + 120, c.y + 60, 100, 10);
                }
            }
        }
    }
}
