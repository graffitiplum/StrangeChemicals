package org.hacktivity.strangechemicals;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;


import java.security.SecureRandom;

import org.hacktivity.strangechemicals.Chords.chords_B_Minor;
import org.hacktivity.strangechemicals.Chords.Chords;
import org.hacktivity.BlumBlumShub;

public class StrangeChemicals extends Service {

    public int CHORDS_MAX_LEN = 120120;
    public static int MAX_CURVE_SPLIT = 24;

    Synthesis syn;
    SecureRandom sr;
    Chords chords;
    chords_B_Minor bm;

    BlumBlumShub bbs;

    final Handler handler = new Handler();
    Runnable task;

    public StrangeChemicals() {
        super();

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        syn = new Synthesis(44100);
        syn.createPlayer();

        bbs = new BlumBlumShub(2310);
        sr = new SecureRandom();

        chords = new Chords();

        // Loop

        task = new Runnable() {
            @Override
            public void run() {

                int sleepms = playRandomHz();
                //handler.postDelayed(this, round(sleepms * 0.9));
                handler.postDelayed(this, 0);
            }
        };
        handler.removeCallbacks(task);
        handler.post(task);

        Toast.makeText(this, "Strange Chemicals Started", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {

        // TODO: kill timer function.
        handler.removeCallbacks(task);

        Toast.makeText(this, "StrangeChemicals Stopped", Toast.LENGTH_LONG).show();
    }

    private int playRandomHz () {

        int rnp = bbs.randInt(MAX_CURVE_SPLIT) + 1;
        int playlen = CHORDS_MAX_LEN / rnp;
        int i, ret = 0;

        for (i=0;i<rnp;i++) {

            int chord = bbs.randInt(chords.NUM_CHORDS_B_Minor);

            double[] samples = new double[playlen];
            double samples0[] = syn.getSineWave(playlen, 44100, bm.bank[chord][0]);
            double samples1[] = syn.getSineWave(playlen, 44100, bm.bank[chord][1]);
            double samples2[] = syn.getSineWave(playlen, 44100, bm.bank[chord][2]);

            int j;
            for (j = 0; j < playlen; j++) {
                samples[j] = (samples0[j] + samples1[j] + samples2[j]) / 3.0;
            }

            // TODO: randomize curve
            if ((bbs.randInt(2)) == 0) {
                samples = syn.curve0(samples);
            }

            if ((bbs.randInt(2)) == 0) {
                samples = syn.curve1(samples);
            }

            if ((bbs.randInt(2)) == 0) {
                samples = syn.curve2(samples);
            }

        /*
        if ((bbs.randInt(2)) == 0) {
            samples = syn.reverse(samples);
        }
        */

            ret += syn.writeSound(samples);

            //return playlen / 44100 * 1000;
        }
        return(ret);
    }

}
