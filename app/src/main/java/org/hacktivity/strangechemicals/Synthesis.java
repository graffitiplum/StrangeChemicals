package org.hacktivity.strangechemicals;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Synthesis {

    private int sampleRate;
    private AudioTrack audioTrack;

    public Synthesis(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public double[] getSineWave(int samples, int sampleRate, double frequencyOfTone) {
        double[] sample = new double[samples];
        for (int i = 0; i < samples; i++) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / frequencyOfTone));
        }
        return sample;
    }

    public double[] reverse (double[] samples) {
        double r[] = new double[samples.length]; int i;

        for (i=0;i<samples.length;i++) {
            r[i] = samples[ (samples.length - (i+1)) ];
        }
        return(r);
    }

    public double[] curve0(double[] samples) {
        double r[] = new double[samples.length];
        int i;

        for (i = 0; i < samples.length; i++) {
            r[i] = (samples[i] *
                    (pow(1.618034, 23.0 / 8.0) * samples.length) *
                    exp(-(exp(1.0) * i) / samples.length) *
                    log(((pow(1.618034, 4) * i) + samples.length) / samples.length) /
                    ((pow(1.618034, 4) * i) + samples.length));
        }
        return (r);
    }

    public double[] curve1(double[] samples) {
        double r[] = new double[samples.length];
        double n = samples.length / 2.0;

        int i;

        for (i = 0; i < samples.length; i++) {
            r[i] = (samples[i] *
                    ((1.618034 * exp(-i / n)) / n) *
                    (sqrt(pow(n, 2.0) - pow((i - n), 2.0))));
        }
        return (r);
    }

    public double[] curve2(double[] samples) {
        double r[] = new double[samples.length];
        double n = samples.length / 2.0;

        int i;

        for (i = 0; i < samples.length; i++) {
            r[i] = (samples[i] *
                    (((pow(1.618034, 4.0) + 1.0) * log((i + n) / n)) /
                            (n * pow(1.618034, 4.0))) *
                    (sqrt(pow(n, 2.0) - pow((i - n), 2.0))));
        }
        return (r);
    }


    public byte[] get16BitPcm(double[] samples) {
        byte[] generatedSound = new byte[2 * samples.length];
        int index = 0;
        for (double sample : samples) {
            // scale to maximum amplitude
            short maxSample = (short) ((sample * Short.MAX_VALUE));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSound[index++] = (byte) (maxSample & 0x00ff);
            generatedSound[index++] = (byte) ((maxSample & 0xff00) >>> 8);

        }
        return generatedSound;
    }

    public void createPlayer() {
        //FIXME sometimes audioTrack isn't initialized
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleRate,
                AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    public void writeSound(double[] samples) {
        byte[] generatedSnd = get16BitPcm(samples);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
    }

    public void destroyAudioTrack() {
        audioTrack.stop();
        audioTrack.release();
    }
}