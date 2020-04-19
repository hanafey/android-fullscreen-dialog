package io.alexanderschaefer.fullscreendialog;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.hanafey.example.OneDialogFragment;

public class MainActivity extends AppCompatActivity {

    private int dialog = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = 1;
        MaterialButton button = findViewById(R.id.button);
        button.setOnClickListener(v -> openDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    private void openDialog() {
        switch (dialog) {
            case 0:
                ExampleDialog.display(getSupportFragmentManager());
                break;
            case 1:
                new OneDialogFragment().show(getSupportFragmentManager(), "ONE");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dialog);
        }
    }
}
