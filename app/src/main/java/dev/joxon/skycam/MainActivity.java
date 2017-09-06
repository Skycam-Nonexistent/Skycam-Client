package dev.joxon.skycam;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// git config --global http.proxy socks5:127.0.0.1:1080
// git config --global https.proxy socks5:127.0.0.1:1080
// git config --global --unset http.proxy
// git config --global --unset https.proxy

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private FloatingActionsMenu floatingActionsMenu;

    private static final String DEFAULT_URL = "http://10.206.12.207:8080";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && floatingActionsMenu.isExpanded()) {
            floatingActionsMenu.collapse();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        // Display
        webView = findViewById(R.id.webView);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                webView.reload();
                if (floatingActionsMenu.isExpanded()) {
                    floatingActionsMenu.collapse();
                }
                return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setDrawingCacheEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        // Add timeout actions?
        webView.loadUrl(DEFAULT_URL);

        // Input URL
        floatingActionsMenu = findViewById(R.id.fabMore);
        findViewById(R.id.buttonInputUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapse();

                final EditText editText = new EditText(MainActivity.this);
                final String http = "http://";
                editText.setSingleLine();
                editText.setText(http);
                editText.setSelection(http.length());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.input_url)
                        .setView(editText)
                        .setIcon(android.R.drawable.ic_menu_edit)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                webView.loadUrl(editText.getText().toString());
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // cancel
                            }
                        })
                        .setNeutralButton(R.string.refresh, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                webView.reload();
                            }
                        })
                        .show();
            }
        });

        // Auto Trace
        findViewById(R.id.buttonAutoTrace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapse();
            }
        });

        // Upload pics
        findViewById(R.id.buttonUploadPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapse();
            }
        });

        // Take screenshots
        findViewById(R.id.fabShoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/Skycam/";
                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdir())
                        Toast.makeText(MainActivity.this, R.string.mkdir_failed, Toast.LENGTH_SHORT).show();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
                String fname = path + sdf.format(new Date()) + ".png";

                Bitmap bitmap = webView.getDrawingCache();
                if (bitmap != null) {
                    Log.d("screenshot:", "bitmap got!");
                    try {
                        FileOutputStream out = new FileOutputStream(fname);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.d("screenshot", "file " + fname + " output done.");
                    } catch (Exception e) {
                        Log.i("Show", e.toString());
                    }
                } else {
                    Log.d("screenshot", "bitmap is NULL!");
                }
            }
        });
    }
}
