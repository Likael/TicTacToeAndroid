package kamil.gsk.tictactoeandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private boolean isPvP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void playerVsPlayer(View view) {
        isPvP = true;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PVP",isPvP);
        startActivity(intent);
    }
    public void playerVsComputer(View view) {
        isPvP = false;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PVP", isPvP);
        startActivity(intent);
    }
}