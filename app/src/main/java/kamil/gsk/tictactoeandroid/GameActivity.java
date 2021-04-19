package kamil.gsk.tictactoeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity {


    private final List<Button> pieces = new ArrayList<>();
    private String[] board = {" ", " ", " ", " ", " ", " ", " ", " ", " "};
    private String winner = " ";
    private String turn = "X";
    private boolean end = false;
    private boolean isPvP;
    private static final String KEY_BOARD = "BOARD";
    private static final String KEY_WINNER = "WINNER";
    private static final String KEY_TURN = "TURN";
    private static final String KEY_END = "END";
    private static final String KEY_PVP = "PVP";
    private final HashMap<String, Integer> scores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        populateHashMap();
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            isPvP = intent.getBooleanExtra("PVP", false);
        }
    }

    public void onClick(View view) {
        if (end) {
            resetBoard();
            return;
        }

        Button button = (Button) view;
        String buttonName = view.getResources().getResourceEntryName(view.getId());
        if (!button.getText().equals(""))
            return;
        int number = Integer.parseInt(buttonName.substring(buttonName.length() - 1));
        board[number] = turn;
        pieces.add(button);
        button.setText(turn);
        changeTurn();
        if (!checkWinner().equals(" "))
            if (checkWinner().equals("T"))
                announceTie();
            else
                announceWinner();
        if (end) {
            return;
        }
        if (!isPvP)
            computerMove();
    }

    private void computerMove() {
        int bestScore = -1000;
        int score = 0;
        int choice = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i].equals(" ")) {
                board[i] = turn;
                score = minmax(board, 0, false);
                board[i] = " ";
                if (score > bestScore) {
                    bestScore = score;
                    choice = i;
                }
            }
        }
        board[choice] = turn;
        int buttonId = getResources().getIdentifier("button" + choice, "id", getPackageName());
        Button button = findViewById(buttonId);
        pieces.add(button);
        button.setText(turn);
        changeTurn();
        if (!checkWinner().equals(" "))
            if (checkWinner().equals("T"))
                announceTie();
            else
                announceWinner();
    }

    private void populateHashMap() {
        if (turn.equals("X")) {
            scores.put("X", -10);
            scores.put("O", 10);
            scores.put("T", 0);
        } else {
            scores.put("X", 10);
            scores.put("O", -10);
            scores.put("T", 0);
        }
    }

    private int minmax(String[] board, int depth, boolean isMaximizing) {

        String win = checkWinner();
        if (!win.equals(" ")) {
            return scores.get(win);
        }
        int bestScore;
        if (isMaximizing) {

            bestScore = -1000;
            int score;
            for (int i = 0; i < 9; i++) {
                if (board[i].equals(" ")) {
                    board[i] = turn;
                    score = minmax(board, depth + 1, false);
                    bestScore = Math.max(score, bestScore);
                    board[i] = " ";
                }
            }
        } else {

            bestScore = 1000;
            int score;
            for (int i = 0; i < 9; i++) {
                if (board[i].equals(" ")) {
                    changeTurn();
                    board[i] = turn;
                    changeTurn();
                    score = minmax(board, depth + 1, true);
                    bestScore = Math.min(score, bestScore);
                    board[i] = " ";
                }
            }
        }
        return bestScore;

    }


    private void changeTurn() {
        if (turn.equals("X"))
            turn = "O";
        else turn = "X";
    }

    private void resetBoard() {
        for (Button button : pieces) {
            button.setText("");
        }
        pieces.clear();
        end = false;
        for (int i = 0; i < 9; i++) {
            board[i] = " ";
        }
        populateHashMap();
    }

    private void retrieveBoard() {
        for (int i = 0; i < 9; i++) {
            int buttonId = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button button = findViewById(buttonId);
            if (!board[i].equals(" ")) {
                button.setText(board[i]);
                pieces.add(button);
            }
        }
    }

    private String checkWinner() {
        boolean flag = false;
        String localWinner;
        for (int i = 0; i < 3; i++) {
            if (!board[i].equals(" ") && board[i].equals(board[i + 3]) && board[i].equals(board[i + 6])) {
                winner = board[i];
                flag = true;
            }
        }
        for (int i = 0; i < 9; i += 3) {
            if (!board[i].equals(" ") && board[i].equals(board[i + 1]) && board[i].equals(board[i + 2])) {
                winner = board[i];
                flag = true;
            }
        }
        if (!board[2].equals(" ") && board[2].equals(board[4]) && board[2].equals(board[6])) {
            winner = board[2];
            flag = true;
        }
        if (!board[0].equals(" ") && board[0].equals(board[4]) && board[4].equals(board[8])) {
            winner = board[0];
            flag = true;
        }
        if (flag) {
            localWinner = winner;
            return localWinner;
        }
        boolean tie = true;
        for (String s : board)
            if (s.equals(" ")) {
                tie = false;
                break;
            }
        if (tie) {
            return "T";
        }
        return " ";
    }

    private void announceTie() {
        Toast.makeText(this, "Tie", Toast.LENGTH_LONG).show();
        winner = " ";
        end = true;
    }

    private void announceWinner() {
        Toast.makeText(this, winner + " Won", Toast.LENGTH_LONG).show();
        winner = " ";
        end = true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_WINNER, winner);
        outState.putString(KEY_TURN, turn);
        outState.putStringArray(KEY_BOARD, board);
        outState.putBoolean(KEY_END, end);
        outState.putBoolean(KEY_PVP, isPvP);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        end = savedInstanceState.getBoolean(KEY_END);
        isPvP = savedInstanceState.getBoolean(KEY_PVP);
        winner = savedInstanceState.getString(KEY_WINNER);
        turn = savedInstanceState.getString(KEY_TURN);
        board = savedInstanceState.getStringArray(KEY_BOARD);
        retrieveBoard();
    }
}