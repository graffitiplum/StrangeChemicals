package org.hacktivity.strangechemicals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.hacktivity.strangechemicals.Synthesis;

public class MainActivity extends AppCompatActivity {

    Synthesis syn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syn = new Synthesis(44100);
        syn.createPlayer();
    }

    public void tvClick(View view) {

        double samples[] = syn.getSineWave(8000, 44100, 2310);
        samples = syn.curve0(samples);

        syn.writeSound(samples);
        syn.writeSound(samples);
        syn.writeSound(samples);

        // TODO: Play file in the background.
    }

}
