# 🎀 Hello Kitty Bakery Game - Code Walkthrough

Congratulations! You have built a fully functioning Java Swing game. 
This document explains **exactly** how your code works, so you can understand every part of it.

---

## 🏗️ 1. The Structure (Big Picture)

Your game is built like a house:
*   **`HelloKittyBakery.java`** is the **Frame** of the house (The Window).
*   **`GamePanel.java`** is the **Room** where everything happens (Drawings, Buttons, Logic).
*   **`Item.java`** and **`Customer.java`** are the **Furniture & People** (Objects inside the room).
*   **`SoundManager.java`** is the **Speaker System**.

---

## 📂 2. Class-by-Class Explanation

### 🟢 A. `HelloKittyBakery.java` (The Entrance)
This is the simplest class. Its only job is to create a windows window (`JFrame`) to hold your game.

**Key Code:**
```java
JFrame frame = new JFrame("Hello Kitty Bakery");
frame.add(new GamePanel()); // Put the game inside the window
frame.setVisible(true); // Show it!
```
*   **Why split this?** It's good practice to keep the "Window setup" separate from the "Game Logic".

### 🧠 B. `GamePanel.java` (The Brains)
This is the most important file. It extends `JPanel`, which gives us a blank canvas to draw on.

**Key Concepts:**
1.  **State Machine (`gameState`):**
    We use a simple integer (`0` or `1`) to decide which screen to show.
    *   `0`: Draw the Start Screen background and button.
    *   `1`: Draw the Shop background and run the shop logic.

2.  **The Game Loop (`Timer`):**
    Games aren't static; they move. We created a `javax.swing.Timer` that runs every **16 milliseconds** (about 60 times a second).
    ```java
    gameTimer = new Timer(16, e -> {
        gameLoop(); // Move characters
        repaint();  // Draw the screen again
    });
    ```
    *   **Update**: Math happens (Customer walks `x -= 2`).
    *   **Repaint**: Graphics happen (Draw the image at the NEW x).

3.  **ArrayLists:**
    We used `ArrayList<Customer>` so we can have 0, 1, or 50 customers. Arrays like `Customer[]` have a fixed size, which is bad for games where things spawn and die.

### 🧁 C. `Item.java` (The Blueprint)
This is a "Plain Old Java Object" (POJO). It doesn't draw itself or have logic. It just holds data:
*   Name ("Cupcake")
*   Price (5)
*   Image

**Why?**
Instead of `String[] names` and `int[] prices`, we group them into one **Object**. This keeps code clean!
`shopItems.get(0).price` is much easier to read than `prices[0]`.

### 🐱 D. `Customer.java` (The AI)
This class represents a *single* customer.

**State Machine Logic:**
The customer has a "mini-brain" with 3 states:
1.  **WALKING_IN**: Move Left until `x == 350`.
2.  **SHOPPING**: Wait for 100 frames (thinking time).
3.  **WALKING_OUT**: Move Left until off-screen.

**Why different classes?**
If we put customer logic in `GamePanel`, the `GamePanel` file would be 1000 lines long! By moving it here, we check `customer.update()` inside the main loop, keeping the main file clean.

---

## 🎮 3. Game Logic Explained

### 🛒 The Shopping Logic
1.  **Spawning**: Every 300 frames (5 seconds), the GamePanel creates a `new Customer()`.
2.  **Buying**:
    *   We simplified it: When a customer finishes "waiting", we assume they bought something.
    *   `money += 15`
    *   The `GamePanel` checks this flag (`c.hasBoughtItems`) and plays a sound!

### 📅 The Day System
We added progression to make it a "Game" and not just a simulation.
*   **Day 1**: 3 Customers. Slow spawn.
*   **Day 2**: 5 Customers. Faster spawn.
*   **End of Day**: When all customers are gone, we pause (`timer.stop()`), show a popup, and then reset variables for the next day.

---

## 🛠️ 4. What To Try Next?

Now that you understand the code, try these challenges:
1.  **True Shopping**: Make customers reduce the stock of specific items (Cupcakes/Toast) instead of just giving generic money.
2.  **Game Over**: If stock runs out, maybe the game ends?
3.  **Click to Serve**: Instead of self-service, make the player click the customer to give them an item!

Enjoy your coding journey! 🚀
