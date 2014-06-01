package com.example.clickme;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String adUrlString = "https://localhost:4443/";
    ImageView adImageView = (ImageView) findViewById(R.id.ad_image);
    new DisplayAdTask(adImageView).execute(adUrlString);
  }

  /**
   * An asynchronous task for retrieving the ad image from the specified URL
   * and displaying it in the application.
   */
  private class DisplayAdTask extends AsyncTask<String, Void, Bitmap> {
    ImageView adImageView;

    public DisplayAdTask(ImageView imageView) {
      adImageView = imageView;
    }

    /**
     * Retrieve the ad image from the remote URL.
     */
    @Override
    protected Bitmap doInBackground(String... urlStrings) {
      String urlString = urlStrings[0];
      URL url = null;
      try {
        url = new URL(urlString);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }

      trustGoodHosts();
      HttpsURLConnection connection = null;
      Bitmap adBitmap = null;
      SocketAddress addr = new InetSocketAddress(
          System.getProperty("http.proxyHost"),
          Integer.parseInt(System.getProperty("http.proxyPort")));
      Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

      try {
        connection = (HttpsURLConnection) url.openConnection(proxy);
        InputStream inputStream = connection.getInputStream();
        adBitmap = BitmapFactory.decodeStream(new BufferedInputStream(
            inputStream));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        connection.disconnect();
      }

      return adBitmap;
    }

    /**
     * If the ad image was successfully retrieved and decoded, display it.
     * Otherwise, display an error image.
     */
    @Override
    protected void onPostExecute(Bitmap adBitmap) {
      if (adBitmap != null) {
        adImageView.setImageBitmap(adBitmap);
      } else {
        adImageView.setImageDrawable(getResources().getDrawable(
            R.drawable.error));
      }
    }
  }

  /**
   * Force HTTPS connections to trust all servers, by default.
   */
  private void trustGoodHosts() {

    String certString = "-----BEGIN CERTIFICATE-----\nMIIDbzCCAlegAwIBAgIJAI6SwD75PtCaMA0GCSqGSIb3DQEBCwUAME4xCzAJBgNV\nBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQKDA1UaGUgR29vZCBH" + 
        "dXlzMRIwEAYDVQQDDAlsb2NhbGhvc3QwHhcNMTQwNTE3MDIwMDU4WhcNMTUwNTE3\nMDIwMDU4WjBOMQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQG" + 
        "A1UECgwNVGhlIEdvb2QgR3V5czESMBAGA1UEAwwJbG9jYWxob3N0MIIBIjANBgkq\nhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu3FjlrF1wu79mimRXYzArP8iPZiFTEjB" + 
        "Et/lRqsZ58qXcPnZSoArX1XC0fMZa7RcYgAuTonvBi+CcvTIF4fSf5jD8I5Abx5t\n4q5Ae10+vzbooiTSrnCfPUzGR8zfvhSrt/DQ9BaixhUn2oDLjVB09QRYPgop65ph" +
        "oAdmVTR66siGkD0YLGjyDXntYrLDr6ZV22Ht/8st6ELEtV27hAOxKY/PrE4BlpKz\nV/vU689+hNV03gR8Vz4oZDw7wek+H9udDEd5EgasA7DtlrKX4eTy9eG4w4NRFy1i" +
        "70ugF1gTO0nRcQuiJ36LLRmao1HwPyKtIbl7AS4E4yniQVSnmOm/SQIDAQABo1Aw\nTjAdBgNVHQ4EFgQUyuvfYa2dNV0hFqnEJglGM78rPjowHwYDVR0jBBgwFoAUyuvf" +
        "Ya2dNV0hFqnEJglGM78rPjowDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOC\nAQEARX0EfwkodVzqrJVxZxy2eBwR1WIkppUVpb814dO7CSPJWRGpqPll21wGyV/s" +
        "nRQ6aUdbl2ISDErxpzoAUUJTQIIdj7hmxU7wlr3ltIGk9vw9Yz1ZJutrk245ubpq\nRL1AmrV3rn+SaGL8wc48B2mXYFoaIxsOnut42VQYFaCdYQ/I6ZzeYv+cW811UbPn" +
        "w09Gzro8z42O+No4mAJvmbpkTZ+hnabSkZ5+/OTRjjv5SoosLjZBZxbovzUpfLTu\nH+/U/WF5CEaNU1ZPeBfrvgZd31niByX9OEO05oVWrinaGFruPOFMH78ysks9HlFs" +
        "+LIBXBY+4KlSj48NRiS4KHMmnw==\n-----END CERTIFICATE-----";
    InputStream stream = new ByteArrayInputStream(certString.getBytes(StandardCharsets.UTF_8));
    Certificate ca = null;
    CertificateFactory cf;

    try {
      cf = CertificateFactory.getInstance("X.509");
      ca = cf.generateCertificate(stream);
      System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } catch (CertificateException e) {
      e.printStackTrace();
    } finally {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    SSLContext sslContext = null;
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore;
    try {
      keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(null, null);
      keyStore.setCertificateEntry("ca", ca);

      String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
      tmf.init(keyStore);

      // Create an SSLContext that uses our TrustManager
      sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, tmf.getTrustManagers(), null);

    } catch (KeyStoreException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CertificateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (KeyManagementException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
        .getSocketFactory());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}