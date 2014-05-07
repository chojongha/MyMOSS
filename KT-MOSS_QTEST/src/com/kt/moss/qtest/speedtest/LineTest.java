package com.kt.moss.qtest.speedtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.moss.qtest.R;

public class LineTest extends Activity {

	private String scn = "";
	private String workerId = "";
	private String workOdrNum = "";

	private String deviceIp = "";
	private String deviceMac = "";
	private String publicIp = "";

	private String vipAddr = "";

	private String cookie = "";
	private String ftthFlag = "";

	private String orderNescode = "";
	private String orderOntmac = "";
	private String orderPort = "";

	private String networkNescode = "";
	private String networkOntmac = "";
	private String networkPort = "";

	Button inBtn = null;

	DefaultHttpClient httpClient = null;
	HttpParams params = null;
	HttpPost httpPost = null;

	Activity ac = null;

	private String url1 = "";
	private String url2 = "";
	private String url3 = "";

	private Context context = null;

	public void clickBack(View v) {
		onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_line_certification);

		ac = this;
		context = this;

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_speed_detail_title_bar);

		TextView title_type = (TextView) findViewById(R.id.title_type);
		TextView title = (TextView) findViewById(R.id.title_name);

		title.setText("회선 인증");
		title_type.setText("");

		try {
			Properties props = new Properties();
			InputStream inputStream = this.getAssets().open("set.properties");
			props.load(inputStream);
			inputStream.close();

			url1 = props.getProperty("url1");
			url2 = props.getProperty("url2");
			url3 = props.getProperty("url3");
			vipAddr = props.getProperty("vip");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// try{

		// Properties props = new Properties();
		// InputStream inputStream = new FileInputStream("set.properties");
		// props.load(inputStream);
		// inputStream.close();
		//
		// url1 = props.getProperty("url1");
		// url2 = props.getProperty("url2");
		// url3 = props.getProperty("url3");
		//
		// }catch(Exception e){
		// try {
		//
		// FileWriter fw = new FileWriter("set.properties");
		//
		// fw.write("url1 = http://moss.kt.com/moss/ConfirmSubscriberPort/\r\n");
		// fw.write("url2 = http://moss.kt.com/moss/ChangeSubscriberPort/\r\n");
		// fw.write("url3 = http://moss.kt.com/moss/PortChangeYN/\r\n");
		//
		// fw.write("vip = 211.216.50.49\r\n");
		// fw.write("port1 = 10011\r\n");
		// fw.write("port2 = 10012\r\n");
		// fw.write("port3 = 10013\r\n");
		// fw.write("port4 = 10014\r\n");
		// fw.write("chunkSize = 8192");
		//
		// fw.close(); // 스트림 닫기
		//
		// Properties props = new Properties();
		// InputStream inputStream = new FileInputStream("set.properties");
		// props.load(inputStream);
		// inputStream.close();
		//
		// url1 = props.getProperty("url1");
		// url2 = props.getProperty("url2");
		// url3 = props.getProperty("url3");
		//
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// }

		inBtn = (Button) findViewById(R.id.inBtn);
		inBtn.setText("확인");

		inBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				ac.finish();
			}// onClick
		});

		Intent intent = this.getIntent();
		scn = intent.getStringExtra("scn");
		workerId = intent.getStringExtra("workerId");
		workOdrNum = intent.getStringExtra("workOdrNum");

		boolean wi = false;

		try {
			wi = wifi();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		if (wi) {
			VirtualIP virtualIP = new VirtualIP();
			vip = vipAddr;
			port = 10011;

			virtualIP.start();

		} else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
		}//

	}// onCreate

	public boolean wifi() throws Exception {

		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (!cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
			Toast.makeText(getBaseContext(), "WI-FI를 연결해주세요", Toast.LENGTH_SHORT).show();
			return false;
		}// wifi

		WifiManager mng = (WifiManager) getSystemService(WIFI_SERVICE);
		deviceMac = mng.getConnectionInfo().getMacAddress(); // wifi MAC Address

		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();

			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

				InetAddress inetAddress = enumIpAddr.nextElement();

				if (!inetAddress.isLoopbackAddress()) {
					deviceIp = inetAddress.getHostAddress().toString(); // IP
				}// if
			}// for
		}// for

		return true;

	}// wifi

	String vip = "";
	int port = 0;

	class VirtualIP extends Thread {

		public String startWeb(String url, HashMap map) throws Exception {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();

			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);

			HttpPost httpPost = new HttpPost(url);

			JSONObject json = new JSONObject(map);

			JSONArray jsonArray = new JSONArray();
			jsonArray.put(json);

			HashMap<String, Object> jsonValues2 = new HashMap<String, Object>();
			jsonValues2.put("request", jsonArray);

			JSONObject jsonFinal = new JSONObject(jsonValues2);

			System.out.println(jsonFinal.toString());
			AbstractHttpEntity entity = new ByteArrayEntity(jsonFinal.toString().getBytes("UTF8"));
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resultEntity = response.getEntity();

			String responseString = EntityUtils.toString(resultEntity);

			httpClient.getConnectionManager().shutdown();

			return responseString;
		}

		@Override
		public void run() {
			try {
				Socket socket = new Socket(vip, port);

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				if (in.readLine().equals("CONNECT OK!")) {
					out.write("RTRV-REAL-IP:;\n");
					out.flush();

					if (in.readLine().equals("OK:")) {
						in.readLine(); // RTRV

						out.write("RTRV-MY-IP:;\n");
						out.flush();

						in.readLine();

						if (in.readLine().equals("OK:")) {
							publicIp = in.readLine();

							in.readLine();

							in.close();
							out.close();

							socket.close();
						}// MYIP
					}// RTRV
				}// if

				HashMap<String, Object> jsonValues = new HashMap<String, Object>();
				System.out.println(publicIp);
				jsonValues.put("deviceIp", deviceIp);
				jsonValues.put("deviceMac", deviceMac);
				jsonValues.put("workOdrNum", workOdrNum);
				jsonValues.put("publicIp", publicIp);
				jsonValues.put("scn", scn);
				jsonValues.put("workerId", workerId);

				String str = startWeb(url1, jsonValues);
				System.out.println("url1" + str);
				JSONObject jsonObject = new JSONObject(str).getJSONObject("header");
				JSONArray array = new JSONObject(str).getJSONArray("response");
				JSONObject am = array.getJSONObject(0);

				cookie = am.getString("cookie");
				ftthFlag = am.getString("ftthFlag");
				networkNescode = am.getString("networkNescode");
				networkOntmac = am.getString("networkOntmac");
				networkPort = am.getString("networkPort");
				orderNescode = am.getString("orderNescode");
				orderOntmac = am.getString("orderOntmac");
				orderPort = am.getString("orderPort");

				int num = Integer.parseInt(jsonObject.getString("resultCode"));

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((TextView) findViewById(R.id.deviceIp)).setText(deviceIp);
						((TextView) findViewById(R.id.deviceMac)).setText(deviceMac);
						((TextView) findViewById(R.id.publicIp)).setText(publicIp);

						if (ftthFlag.equals("0")) {
							((TextView) findViewById(R.id.ftthFlag)).setText("일반 가입자");
						} else {
							((TextView) findViewById(R.id.ftthFlag)).setText("FTTH-REAL 가입자");
						}

						((TextView) findViewById(R.id.networkOntmacs)).setText(networkOntmac); //
						((TextView) findViewById(R.id.orderOntmacs)).setText(orderOntmac); //

						((TextView) findViewById(R.id.networkPorts)).setText(networkPort);
						((TextView) findViewById(R.id.orderPorts)).setText(orderPort);

						((TextView) findViewById(R.id.networkNescodes)).setText(networkNescode);
						((TextView) findViewById(R.id.orderNescodes)).setText(orderNescode);
						((TextView) findViewById(R.id.said)).setText(scn);
					}
				});

				// num = 600;
				if (num == 200) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							((TextView) findViewById(R.id.su)).setText("성공");
						}
					});
				} else if (num >= 600) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							((TextView) findViewById(R.id.su)).setText("실패");
							inBtn.setText("포트 변경");
						}
					});

					inBtn.setOnClickListener(new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							try {

								HashMap<String, Object> jsonValues2 = new HashMap<String, Object>();

								jsonValues2.put("said", scn);
								jsonValues2.put("ftthFlag", ftthFlag);
								jsonValues2.put("cookie", cookie);

								jsonValues2.put("workerId", workerId);
								jsonValues2.put("workOdrNum", workOdrNum);

								if (ftthFlag.equals("0")) {

									jsonValues2.put("orderNescode", orderNescode);
									jsonValues2.put("orderPort", orderPort);
									jsonValues2.put("networkNescode", networkNescode);
									jsonValues2.put("networkPort", networkPort);
									jsonValues2.put("orderOntmac", "");
									jsonValues2.put("networkOntmac", "");
								} else if (ftthFlag.equals("1")) {

									jsonValues2.put("orderNescode", "");
									jsonValues2.put("orderPort", "");
									jsonValues2.put("networkNescode", "");
									jsonValues2.put("networkPort", "");

									jsonValues2.put("orderOntmac", orderOntmac);
									jsonValues2.put("networkOntmac", networkOntmac);

								}

								String str = startWeb(url2, jsonValues2);
								System.out.println(str);
								JSONObject jsonObjects = new JSONObject(str).getJSONObject("header");
								JSONArray array = new JSONObject(str).getJSONArray("response");

								JSONObject am = array.getJSONObject(0);

								// resultCode = am.getString("resultCode");
								// resultMsg = am.getString("resultMsg");
								ftthFlag = am.getString("ftthFlag");
								orderNescode = am.getString("orderNescode");
								orderPort = am.getString("orderPort");
								networkNescode = am.getString("networkNescode");
								networkPort = am.getString("networkPort");
								orderOntmac = am.getString("orderOntmac");
								networkOntmac = am.getString("networkOntmac");

								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										((TextView) findViewById(R.id.ftthFlag)).setText(ftthFlag);
										((TextView) findViewById(R.id.networkOntmacs)).setText(networkOntmac); //
										((TextView) findViewById(R.id.orderOntmacs)).setText(orderOntmac); //
										((TextView) findViewById(R.id.networkPorts)).setText(networkPort);
										((TextView) findViewById(R.id.orderPorts)).setText(orderPort);
										((TextView) findViewById(R.id.networkNescodes)).setText(networkNescode);
										((TextView) findViewById(R.id.orderNescodes)).setText(orderNescode);

										((TextView) findViewById(R.id.su)).setText("포트 변경");
									}
								});
								int num = Integer.parseInt(jsonObjects.getString("resultCode"));
								if (num == 200) {

									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											inBtn.setText("확인");
											Toast.makeText(context, "포트를 변경했습니다", Toast.LENGTH_SHORT).show();
										}
									});

									inBtn.setOnClickListener(new Button.OnClickListener() {

										@Override
										public void onClick(View v) {
											ac.finish();
										}
									});
									/*
									 * jsonValues = new HashMap<String,
									 * Object>();
									 * 
									 * jsonValues.put("said", scn);
									 * jsonValues.put("workerId", workerId);
									 * jsonValues.put("workOdrNum", workOdrNum);
									 * 
									 * String st = startWeb(url3,jsonValues);
									 * 
									 * JSONObject jsonObjectss = new
									 * JSONObject(st).getJSONObject("header");
									 * JSONArray arrays = new
									 * JSONObject(st).getJSONArray("response");
									 * 
									 * switch
									 * (Integer.parseInt(jsonObjectss.getString
									 * ("resultCode"))) {
									 * 
									 * case 200: {
									 * 
									 * jsonObjectss.getString("resultMsg");
									 * jsonObjectss.getString("changeFlag");
									 * 
									 * runOnUiThread(new Runnable() {
									 * 
									 * @Override public void run() {
									 * inBo1.setVisibility(View.VISIBLE);
									 * inBo2.setVisibility(View.GONE);
									 * 
									 * Toast.makeText(context,"포트를 변경했습니다",Toast.
									 * LENGTH_SHORT).show(); } }); break; }
									 * 
									 * case 400: { // bad request break; }
									 * 
									 * case 610: { break; }
									 * 
									 * default: break; }
									 */
								} else {

								}

							} catch (Exception e) {
								e.printStackTrace();
							}// catch
						}
					});

				}
			} catch (Exception e1) {
				e1.printStackTrace();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
					}
				});
			}// catch

		}// run
	}
}// class Virtual IP

