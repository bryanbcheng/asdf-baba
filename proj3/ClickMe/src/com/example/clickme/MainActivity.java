package com.example.clickme;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
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

			trustAllHosts();
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
	private void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains.
		TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager.
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
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