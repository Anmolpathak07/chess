import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {

    JButton[][] squares = new JButton[8][8];
    String[][] board = new String[8][8];

    int selectedRow = -1, selectedCol = -1;
    boolean whiteTurn = true;

    public ChessGUI() {
        setTitle("Chess Game - Piece Names");
        setSize(700, 700);
        setLayout(new GridLayout(8, 8));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeBoard();
        createBoardUI();

        setVisible(true);
    }

    void initializeBoard() {
        String[] initial = {
                "rnbqkbnr",
                "pppppppp",
                "........",
                "........",
                "........",
                "........",
                "PPPPPPPP",
                "RNBQKBNR"
        };

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = String.valueOf(initial[i].charAt(j));
    }

    void createBoardUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 12));
                btn.setMargin(new Insets(0,0,0,0));

                if ((i + j) % 2 == 0)
                    btn.setBackground(Color.WHITE);
                else
                    btn.setBackground(Color.LIGHT_GRAY);

                int r = i, c = j;
                btn.addActionListener(e -> handleClick(r, c));

                squares[i][j] = btn;
                add(btn);
            }
        }
        refreshBoard();
    }

    void handleClick(int r, int c) {

        if (selectedRow == -1) {
            if (!board[r][c].equals(".") && isCorrectTurn(board[r][c])) {
                selectedRow = r;
                selectedCol = c;
                squares[r][c].setBackground(Color.YELLOW);
            }
        } else {

            if (isValidMove(selectedRow, selectedCol, r, c)) {

                movePiece(selectedRow, selectedCol, r, c);

                boolean opponent = !whiteTurn;

                if (isKingInCheck(opponent)) {

                    if (isCheckmate(opponent)) {
                        JOptionPane.showMessageDialog(this,
                                (whiteTurn ? "White" : "Black") + " Wins by Checkmate!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Check!");
                    }

                } else if (isStalemate(opponent)) {
                    JOptionPane.showMessageDialog(this, "Draw by Stalemate!");
                }

                whiteTurn = !whiteTurn;
            }

            selectedRow = -1;
            selectedCol = -1;
            refreshBoard();
        }
    }

    boolean isCorrectTurn(String piece) {
        return whiteTurn ? piece.equals(piece.toUpperCase())
                : piece.equals(piece.toLowerCase());
    }

    boolean isSameColor(String a, String b) {
        if (b.equals(".")) return false;
        return (a.equals(a.toUpperCase()) && b.equals(b.toUpperCase())) ||
               (a.equals(a.toLowerCase()) && b.equals(b.toLowerCase()));
    }

    boolean isValidMove(int r1, int c1, int r2, int c2) {

        String piece = board[r1][c1];
        String target = board[r2][c2];

        if (piece.equals(".")) return false;
        if (isSameColor(piece, target)) return false;

        boolean valid = false;

        switch (piece.toLowerCase()) {
            case "p": valid = validPawn(r1,c1,r2,c2,piece); break;
            case "r": valid = validRook(r1,c1,r2,c2); break;
            case "n": valid = validKnight(r1,c1,r2,c2); break;
            case "b": valid = validBishop(r1,c1,r2,c2); break;
            case "q": valid = validQueen(r1,c1,r2,c2); break;
            case "k": valid = validKing(r1,c1,r2,c2); break;
        }

        if (!valid) return false;

        // simulate move
        String temp = board[r2][c2];
        board[r2][c2] = piece;
        board[r1][c1] = ".";

        boolean inCheck = isKingInCheck(piece.equals(piece.toUpperCase()));

        // undo
        board[r1][c1] = piece;
        board[r2][c2] = temp;

        return !inCheck;
    }

    // ===== PIECE RULES =====

    boolean validPawn(int r1, int c1, int r2, int c2, String p) {
        int dir = p.equals(p.toUpperCase()) ? -1 : 1;

        if (c1 == c2 && board[r2][c2].equals(".")) {
            if (r2 == r1 + dir) return true;

            if ((r1 == 6 && dir == -1 || r1 == 1 && dir == 1)
                    && r2 == r1 + 2 * dir
                    && board[r1 + dir][c1].equals(".")) return true;
        }

        if (Math.abs(c2 - c1) == 1 && r2 == r1 + dir
                && !board[r2][c2].equals(".")) return true;

        return false;
    }

    boolean validRook(int r1, int c1, int r2, int c2) {
        if (r1 != r2 && c1 != c2) return false;
        return clearPath(r1, c1, r2, c2);
    }

    boolean validKnight(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r2 - r1), dc = Math.abs(c2 - c1);
        return dr * dc == 2;
    }

    boolean validBishop(int r1, int c1, int r2, int c2) {
        if (Math.abs(r2 - r1) != Math.abs(c2 - c1)) return false;
        return clearPath(r1, c1, r2, c2);
    }

    boolean validQueen(int r1, int c1, int r2, int c2) {
        return validRook(r1,c1,r2,c2) || validBishop(r1,c1,r2,c2);
    }

    boolean validKing(int r1, int c1, int r2, int c2) {
        return Math.abs(r2 - r1) <= 1 && Math.abs(c2 - c1) <= 1;
    }

    boolean clearPath(int r1, int c1, int r2, int c2) {
        int dr = Integer.signum(r2 - r1);
        int dc = Integer.signum(c2 - c1);

        int r = r1 + dr, c = c1 + dc;
        while (r != r2 || c != c2) {
            if (!board[r][c].equals(".")) return false;
            r += dr;
            c += dc;
        }
        return true;
    }

    // ===== CHECK LOGIC =====

    int[] findKing(boolean white) {
        String king = white ? "K" : "k";

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j].equals(king))
                    return new int[]{i, j};

        return null;
    }

    boolean isUnderAttack(int r, int c, boolean byWhite) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                String piece = board[i][j];
                if (piece.equals(".")) continue;

                if (byWhite && piece.equals(piece.toUpperCase()) ||
                    !byWhite && piece.equals(piece.toLowerCase())) {

                    if (isValidMoveBasic(i, j, r, c))
                        return true;
                }
            }
        }
        return false;
    }

    boolean isValidMoveBasic(int r1, int c1, int r2, int c2) {
        String piece = board[r1][c1];
        String target = board[r2][c2];

        if (isSameColor(piece, target)) return false;

        switch (piece.toLowerCase()) {
            case "p": return validPawn(r1,c1,r2,c2,piece);
            case "r": return validRook(r1,c1,r2,c2);
            case "n": return validKnight(r1,c1,r2,c2);
            case "b": return validBishop(r1,c1,r2,c2);
            case "q": return validQueen(r1,c1,r2,c2);
            case "k": return validKing(r1,c1,r2,c2);
        }
        return false;
    }

    boolean isKingInCheck(boolean white) {
        int[] king = findKing(white);
        return isUnderAttack(king[0], king[1], !white);
    }

    boolean isCheckmate(boolean white) {

        if (!isKingInCheck(white)) return false;

        for (int r1 = 0; r1 < 8; r1++) {
            for (int c1 = 0; c1 < 8; c1++) {

                String piece = board[r1][c1];
                if (piece.equals(".")) continue;

                if (white && piece.equals(piece.toLowerCase())) continue;
                if (!white && piece.equals(piece.toUpperCase())) continue;

                for (int r2 = 0; r2 < 8; r2++) {
                    for (int c2 = 0; c2 < 8; c2++) {

                        if (isValidMove(r1, c1, r2, c2))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    boolean isStalemate(boolean white) {

        if (isKingInCheck(white)) return false;

        for (int r1 = 0; r1 < 8; r1++) {
            for (int c1 = 0; c1 < 8; c1++) {

                String piece = board[r1][c1];
                if (piece.equals(".")) continue;

                if (white && piece.equals(piece.toLowerCase())) continue;
                if (!white && piece.equals(piece.toUpperCase())) continue;

                for (int r2 = 0; r2 < 8; r2++) {
                    for (int c2 = 0; c2 < 8; c2++) {

                        if (isValidMove(r1, c1, r2, c2))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    // ===== UI =====

    void movePiece(int r1, int c1, int r2, int c2) {
        board[r2][c2] = board[r1][c1];
        board[r1][c1] = ".";
    }

    void refreshBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                String piece = board[i][j];
                squares[i][j].setText(piece.equals(".") ? "" : getPieceName(piece));

                if ((i + j) % 2 == 0)
                    squares[i][j].setBackground(Color.WHITE);
                else
                    squares[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    String getPieceName(String p) {
        String name = "";

        switch (p.toLowerCase()) {
            case "p": name = "Pawn"; break;
            case "r": name = "Rook"; break;
            case "n": name = "Knight"; break;
            case "b": name = "Bishop"; break;
            case "q": name = "Queen"; break;
            case "k": name = "King"; break;
        }

        return p.equals(p.toUpperCase()) ? "W-" + name : "B-" + name;
    }

    public static void main(String[] args) {
        new ChessGUI();
    }
}