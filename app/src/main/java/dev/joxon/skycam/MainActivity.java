package dev.joxon.skycam;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        VideoView videoView = (VideoView) findViewById(R.id.VideoView);
        try {
            videoView.setVideoURI(Uri.parse("http://10.206.12.207:8080"));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

//        WebView webView = (WebView) findViewById(R.id.WebView);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://10.206.12.194:8080");


        FloatingActionButton settingButton = (FloatingActionButton) findViewById(R.id.FabMore);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开设置菜单
            }
        });

        FloatingActionButton screenshotButton = (FloatingActionButton) findViewById(R.id.FabShoot);
        screenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 截图
            }
        });
    }


}
