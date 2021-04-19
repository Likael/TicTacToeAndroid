package kamil.gsk.tictactoeandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onClick(View view) {
        int mode = AppCompatDelegate.MODE_NIGHT_YES;
        mode = AppCompatDelegate.getDefaultNightMode() == mode ? AppCompatDelegate.MODE_NIGHT_NO : mode;
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}