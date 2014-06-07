package gxu.software_engineering.market.android.util;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by longkai on 14-6-8.
 */
public class RESTMethod {
	private static HttpURLConnection getConnection(String uri) throws IOException {
		URL url = new URL(uri);
		return (HttpURLConnection) url.openConnection();
	}

	private static String getResult(HttpURLConnection connection) throws IOException {
		int code = connection.getResponseCode();
		InputStream in;
		if (code != HttpURLConnection.HTTP_OK) {
			in = connection.getErrorStream();
		} else {
			in = connection.getInputStream();
		}
		Scanner scanner = new Scanner(in);
		String result = scanner.hasNext() ? scanner.next() : null;
		scanner.close();
		connection.disconnect();
		return result;
	}

	public static JSONObject get(String uri) throws IOException, JSONException {
		HttpURLConnection connection = getConnection(uri);
		connection.setRequestMethod("GET");
		return new JSONObject(getResult(connection));
	}

	public static JSONObject post(String uri, UrlEncodedFormEntity entity) throws IOException, JSONException {
		HttpURLConnection connection = getConnection(uri);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		OutputStream out = connection.getOutputStream();
		InputStream in = entity.getContent();
		int size = 1024;
		byte[] buffer = new byte[size];
		int len;
		while ((len = in.read(buffer, 0, size)) != -1) {
			out.write(buffer, 0, len);
		}
		return new JSONObject(getResult(connection));
	}

	public static JSONObject put(String uri, UrlEncodedFormEntity entity) throws Exception {
		HttpURLConnection connection = getConnection(uri);
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		OutputStream out = connection.getOutputStream();
		InputStream in = entity.getContent();
		int size = 1024;
		byte[] buffer = new byte[size];
		int len;
		while ((len = in.read(buffer, 0, size)) != -1) {
			out.write(buffer, 0, len);
		}
		return new JSONObject(getResult(connection));
	}
}
