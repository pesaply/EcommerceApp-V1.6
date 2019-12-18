package com.app.templateasdemo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
public class ActivityPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        WebView webView = (WebView) findViewById(R.id.pdfView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });


/*            if  (getIntent().hasExtra("pdfD")) {
            String pdf = getIntent().getStringExtra("pdfD");
            Toast.makeText(getApplicationContext(), pdf, Toast.LENGTH_LONG).show();
            //pdfa = pdf;
        }

*/

        //Url Ejemplo:
        String pdf = "http://162.214.67.53:3000/api/obtenerPdfProducto/" +getIntent().getStringExtra("pdfD");

        //Carga url de .PDF en WebView  mediante Google Drive Viewer.
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

    }

}
