package com.stedi.poweroffclick;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState != null && savedInstanceState.getBoolean("firstCall"))
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
                    if (process.waitFor() != 0)
                        showToastAndFinish(R.string.power_off_failed);
                } catch (IOException ex) {
                    showToastAndFinish(R.string.device_does_not_have_root);
                } catch (InterruptedException ex) {
                    showToastAndFinish(R.string.power_off_failed);
                }
            }

            private void showToastAndFinish(final int resString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, resString, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("firstCall", true);
    }
}
