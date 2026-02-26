# Hello Kitty Bakery Game - Implementation Plan

## Project Goal
Build a complete, playable bakery game using Java Swing locally.
Focus on clear, beginner-friendly code with detailed explanations for learning purposes.

## Phase 1: Basic Setup & Start Screen (Completed)
- [x] Create project structure via `HelloKittyBakery` (Main Class)
- [x] detailed comments explaining `JFrame`, `JPanel`, `main` method
- [x] Create `GamePanel` class for handling game states
- [x] Implement Start Screen with Background (`bg.png`)
- [x] Add "Start Game" button
- [x] Explain LayoutManager vs Absolute Positioning

## Phase 2: Shop Screen Layout (Completed)
- [x] Create Shop Screen background rendering (`bg2.png`)
- [x] Add Top UI Labels (Money, Day, Cart)
- [x] Create `Item` class for bakery products
- [x] Display items on shelves using Buttons (`ItemButton`)
- [x] Load item images (`cupcake.png`, `toast.png`, `pastry.png`)

## Phase 3: Game Logic & Interactions (Next Steps)
- [x] Refine Shopping Logic (Cart management)
- [ ] Implement "Money Preview" (if applicable) or Total Bill calculation
- [x] **Explain**: Variables, Label updates, Formatting numbers
- [ ] **Explain**: Object Interactions (Player -> Item -> Cart)

## Phase 4: Customer System (Completed)
- [x] Create `Customer` class
- [x] **Explain**: Classes vs Objects (Customer blueprint)
- [x] Implement Customer Spawning Logic (entering from right)
- [x] Implement Walking Animation (Swing Timer)
- [x] **Explain**: Swing Timer, Repaint, Game Loop concepts
- [x] Implement Purchasing Logic (Simple Wait & Buy)
- [x] Customer Exit Logic

## Phase 5: Polish & Extras (In Progress)
- [x] Implement Day System (Day Counter, Difficulty Increase)
- [x] Add Sound Effects Support (`SoundManager`)
- [ ] Final Code Cleanup
- [ ] Create Walkthrough/Documentation
