# ♟️ Java Chess Game (GUI-Based)

## 📌 Project Overview

This project is a **desktop-based Chess Game** developed using **Java Swing**. It provides a graphical user interface where two players can play chess with proper rules such as legal moves, check, checkmate, and stalemate detection.

---

## 🎯 Features

* ♟️ Interactive 8×8 chessboard GUI
* 🔄 Turn-based gameplay (White vs Black)
* ✅ Legal move validation for all pieces
* ⚔️ Check detection
* 👑 Checkmate detection (winner declared)
* 🤝 Stalemate detection (draw)
* 🎨 Unicode-based chess pieces (no images required)

---

## 🛠️ Technologies Used

* Java (Core Java)
* Java Swing (GUI)
* AWT (for layout and events)

---

## 🧠 Project Logic

* The chessboard is represented using a **2D array (8×8)**.
* Each piece has its own movement validation function:

  * `validPawn()`
  * `validRook()`
  * `validKnight()`
  * `validBishop()`
  * `validQueen()`
  * `validKing()`
* The `isValidMove()` method ensures:

  * Move follows chess rules
  * Move does not leave king in check

---

## ⚙️ How It Works

1. User clicks a piece → selects it
2. User clicks destination → attempts move
3. Program validates move
4. Board updates if move is legal
5. Game checks for:

   * Check
   * Checkmate
   * Stalemate

---

## ▶️ How to Run

### Step 1: Compile

```bash
javac ChessGUI.java
```

### Step 2: Run

```bash
java ChessGUI
```

---

## ⚠️ Requirements

* Java JDK installed
* GUI-supported environment (not headless)

---

## ❌ Limitations

* No castling
* No pawn promotion
* No en passant
* No AI opponent
* No drag-and-drop support

---

## 🚀 Future Enhancements

* Add AI opponent (Minimax Algorithm)
* Implement castling and promotion
* Improve UI (drag & drop, animations)
* Add multiplayer 


---

## 🎓 Learning Outcomes

* Understanding of Object-Oriented Programming (OOP)
* Event-driven programming in Java
* GUI development using Swing
* Implementation of real-world game logic


---

## 📄 License

This project is for **educational purposes only**.
