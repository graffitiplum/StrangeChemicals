package org.hacktivity.strangechemicals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    boolean serviceRunning = false;

    Switch soundSwitch;

    private static int minValue = 30030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundSwitch = (Switch) findViewById(R.id.soundSwitch);

    }

    public void ssToggle(View view) {

        // TODO: Play file in the background.

        if (! serviceRunning) {
            startService(new Intent(this, StrangeChemicals.class));
            serviceRunning = true;
            soundSwitch.setText("On");
        } else {
            stopService(new Intent(this, StrangeChemicals.class));
            serviceRunning = false;
            soundSwitch.setText("Off");
        }
    }

    public void seekMaxLen (View view) {

    }

}
