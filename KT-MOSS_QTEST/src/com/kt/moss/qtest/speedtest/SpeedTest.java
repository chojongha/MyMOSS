package com.kt.moss.qtest.speedtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewDebug.FlagToString;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.kt.moss.qtest.R;
import com.kt.moss.qtest.util.CommonUtil;

public class SpeedTest extends Activity implements TracerouteListener {

	private String TESTDATE = "TESTDATE";
	private String WORKODRNUM = "WORKODRNUM";
	private String WORKODRKIND = "WORKODRKIND";
	private String SCN = "SCN";//
	private String WORKERID = "WORKERID";
	private String KORNETID = "KORNETID";
	private String TEL_NO = "TEL_NO";
	private String OFFICESCODE = "OFFICESCODE";
	private String TESTQUALITY = "TESTQUALITY";
	private String SERVERINFO = "SERVERINFO";
	private String UPLOADSPEEDMIN = "UPLOADSPEEDMIN";
	private String UPLOADSPEEDMAX = "UPLOADSPEEDMAX";
	private String UPLOADSPEEDAVG = "UPLOADSPEEDAVG";
	private String DOWNLOADSPEEDMIN = "DOWNLOADSPEEDMIN";
	private String DOWNLOADSPEEDMAX = "DOWNLOADSPEEDMAX";
	private String DOWNLOADSPEEDAVG = "DOWNLOADSPEEDAVG";
	private String PACKDELAYTIMEMIN = "PACKDELAYTIMEMIN";
	private String PACKDELAYTIMEMAX = "PACKDELAYTIMEMAX";
	private String PACKDELAYTIMEAVG = "PACKDELAYTIMEAVG";
	private String PACKDELAYTIMESTD = "PACKDELAYTIMESTD";
	private String PACKLOSSRATE = "PACKLOSSRATE";
	private String UDPDELAYTIMEMIN = "UDPDELAYTIMEMIN";
	private String UDPDELAYTIMEMAX = "UDPDELAYTIMEMAX";
	private String UDPDELAYTIMEAVG = "UDPDELAYTIMEAVG";
	private String UDPDELAYTIMESTD = "UDPDELAYTIMESTD";
	private String UDPLOSSRATE = "UDPLOSSRATE";
	private String JITTERMIN = "JITTERMIN";
	private String JITTERMAX = "JITTERMAX";
	private String JITTERAVG = "JITTERAVG";
	private String JITTERSTD = "JITTERSTD";
	private String HOPCOUNT = "HOPCOUNT";
	private String CPURATE = "CPURATE";
	private String MEMORYRATE = "MEMORYRATE";
	private String IP = "IP";
	private String GATEWAY = "GATEWAY";
	private String MAC = "MAC";
	private String CPUTYPE = "CPUTYPE";
	private String MEMORYSIZE = "MEMORYSIZE";
	private String NIC = "NIC";
	private String OS = "OS";
	private String BROWSER = "MOSS APP";
	//
	private TracerouteWithPing tracerouteWithPing = null;
	private NetSpeed speed = null;

	// intent
	private int workOdrKind = 1;

	private int sla_down = 2000;
	private int sla_up = 2000;
	private int sla_udp = 0;
	private int sla_ping = 0;

	// con
	private Context context = null;
	private int tCount = 0;
	private int testQuality = 0;

	// Resources
	private float mu = 0; // memory resource
	private float cu = 0; // cpu resource
	private float totalSize = 0;

	private int downAVG = 0;
	private int downMin = 0;
	private int downMax = 0;

	private int upAVG = 0;
	private int upMin = 0;
	private int upMax = 0;

	private float pingMin = 1000;
	private float pingMax = 0;
	private float pingAVG = 0;
	private int pingLost = 0;

	private int udpMin = 1000;
	private int udpMax = 0;
	private float udpAVG = 0;
	private int lost = 0;

	private float pingTime = 0;

	// control
	private Socket socket = null;
	private String vip;

	private int port1 = 0;
	private int port2 = 0;
	private int port3 = 0;
	private int port4 = 0;

	private int chunkSize = 0;

	private String RTRV = "";
	private String MYIP = "";

	private int sm = 0;
	private int nm = 0;
	private int count = 0;
	private int total = 0;

	private OutputStream out10013 = null;
	private BufferedReader in10013 = null;

	private int twenty = 0;
	private int overhead = 0;

	// Design

	private Button btn = null;
	private Button btn1 = null;
	private Button btn2 = null;

	private LinearLayout bo1 = null;
	private LinearLayout bo2 = null;

	private Pin pin = null;

	private BarChart download = null;
	private BarChart upload = null;

	private LineChart pingChart = null;

	private Capsule cpu = null;
	private Capsule memory = null;

	// text View
	private TextView cpuText = null;
	private TextView memoryText = null;

	private TextView downValueText = null;
	private TextView downMaxText = null;
	private TextView downMinText = null;

	private TextView upValueText = null;
	private TextView upMaxText = null;
	private TextView upMinText = null;

	private TextView downCurr = null;
	private TextView downMinCurr = null;
	private TextView downMaxCurr = null;

	private TextView upCurr = null;
	private TextView upMinCurr = null;
	private TextView upMaxCurr = null;

	private TextView pingText = null;
	private TextView pingMaxText = null;
	private TextView pingMinText = null;

	private TextView udpText = null;
	private TextView udpMaxText = null;
	private TextView udpMinText = null;

	private LinearLayout upDown = null;
	private LinearLayout udpPingLayout = null;
	private LinearLayout tracerouteLayout = null;

	private Button downloadTab = null;
	private Button pingTab = null;
	private Button tracerouteTab = null;

	private LinearLayout ll_row;

	private TextView tv_leftIP;
	private TextView tv_leftHop;
	private TextView tv_horizontalHop;
	private VerticalTextView tv_verticalHop;
	private TextView tv_rightHop;
	private TextView tv_rightIP;

	// hk start
	private ViewFlipper vf_tab;

	float xAtDown;
	float xAtUp;
	// hk end

	private LinearLayout ll_progressBar;
	private LinearLayout ll_traceRT;
	private ScrollView sv_traceRT;
	private LinearLayout ll_traceRouteError;

	private LineChart pingLinechart = null;
	private LineChart udpLinechart = null;

	List<TracerouteContainer> traces = null;
	String[] result = null;
	// Timer
	private Timer resource = null;

	private Activity ac = null;
	private int fling = 0;
	private int flingSave = 0;
	private int un = 170;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_speed);

		context = this;
		ac = this;

		Intent intent = this.getIntent();

		sla_down = Integer.parseInt(intent.getStringExtra("sla_down"));
		sla_up = Integer.parseInt(intent.getStringExtra("sla_up"));
		sla_udp = Integer.parseInt(intent.getStringExtra("sla_udp"));
		sla_ping = Integer.parseInt(intent.getStringExtra("sla_ping"));
		workOdrKind = Integer.parseInt(intent.getStringExtra("workOdrKind"));
		WORKODRNUM = intent.getStringExtra("workOdrNum");
		SCN = intent.getStringExtra("scn").trim();
		WORKERID = intent.getStringExtra("workerId");
		KORNETID = intent.getStringExtra("kornetId");
		TEL_NO = intent.getStringExtra("tel_no");
		OFFICESCODE = intent.getStringExtra("officesCode");

		// textView
		cpuText = (TextView) findViewById(R.id.cputext);
		memoryText = (TextView) findViewById(R.id.memorytext);

		downValueText = (TextView) findViewById(R.id.main_tv_downloadValue);
		downMaxText = (TextView) findViewById(R.id.main_tv_downloadMax);
		downMinText = (TextView) findViewById(R.id.main_tv_downloadMin);

		upValueText = (TextView) findViewById(R.id.main_tv_uploadValue);
		upMaxText = (TextView) findViewById(R.id.main_tv_uploadMax);
		upMinText = (TextView) findViewById(R.id.main_tv_upMin);

		downMinCurr = (TextView) findViewById(R.id.minidownloadmin);
		downMaxCurr = (TextView) findViewById(R.id.mindownloadmax);
		downCurr = (TextView) findViewById(R.id.main_tv_downloadCurr);

		upMinCurr = (TextView) findViewById(R.id.miniuploadmin);
		upMaxCurr = (TextView) findViewById(R.id.miniuploadmax);
		upCurr = (TextView) findViewById(R.id.main_tv_uploadCurr);

		upDown = (LinearLayout) findViewById(R.id.main_ll_upDown);
		udpPingLayout = (LinearLayout) findViewById(R.id.main_ll_pingUdp);
		tracerouteLayout = (LinearLayout) findViewById(R.id.main_ll_traceRoute);

		pingText = (TextView) findViewById(R.id.main_tv_pingValue);
		pingMaxText = (TextView) findViewById(R.id.main_tv_pingmax);
		pingMinText = (TextView) findViewById(R.id.main_tv_pingmin);

		udpText = (TextView) findViewById(R.id.main_tv_udpValue);
		udpMaxText = (TextView) findViewById(R.id.main_tv_udpmax);
		udpMinText = (TextView) findViewById(R.id.main_tv_udpmin);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_speed_detail_title_bar);
		TextView title = (TextView) findViewById(R.id.title_name);
		title.setText("인터넷 속도 측정");

		TextView title_type = (TextView) findViewById(R.id.title_type);

		if (workOdrKind == 1) {
			title_type.setText("개통 ");
			title_type.setTextColor(Color.RED);
		} else if (workOdrKind == 2) {
			title_type.setText("수리 ");
			title_type.setTextColor(Color.RED);
		} else {
			title_type.setText("");
		}

		ll_progressBar = (LinearLayout) findViewById(R.id.main_ll_progressBar);
		ll_traceRT = (LinearLayout) findViewById(R.id.main_ll_traceRT);
		sv_traceRT = (ScrollView) findViewById(R.id.main_sv_traceRoute);
		ll_traceRouteError = (LinearLayout) findViewById(R.id.main_ll_traceRouteError);

		upload = (BarChart) findViewById(R.id.main_ll_uploadGraph);
		download = (BarChart) findViewById(R.id.main_ll_downloadGraph);

		pin = (Pin) findViewById(R.id.main_ll_speedGraph);

		cpu = (Capsule) findViewById(R.id.main_ll_cpuGraph);
		memory = (Capsule) findViewById(R.id.main_ll_memoryGraph);

		pingLinechart = (LineChart) findViewById(R.id.linechartping);
		udpLinechart = (LineChart) findViewById(R.id.linechartudp);

		TextView t1 = (TextView) findViewById(R.id.t1);
		TextView t2 = (TextView) findViewById(R.id.t2);
		TextView t3 = (TextView) findViewById(R.id.t3);
		TextView t4 = (TextView) findViewById(R.id.t4);
		TextView t5 = (TextView) findViewById(R.id.t5);
		TextView t6 = (TextView) findViewById(R.id.t6);
		TextView t7 = (TextView) findViewById(R.id.t7);
		TextView t8 = (TextView) findViewById(R.id.t8);

		t1.setPaintFlags(t1.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t2.setPaintFlags(t2.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t3.setPaintFlags(t3.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t4.setPaintFlags(t4.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t5.setPaintFlags(t5.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t6.setPaintFlags(t6.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t7.setPaintFlags(t6.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		t8.setPaintFlags(t6.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

		// props
		try {
			Properties props = new Properties();
			InputStream inputStream = this.getAssets().open("set.properties");
			props.load(inputStream);
			inputStream.close();

			vip = props.getProperty("vip");
			port1 = Integer.parseInt(props.getProperty("port1"));
			port2 = Integer.parseInt(props.getProperty("port2"));
			port3 = Integer.parseInt(props.getProperty("port3"));
			port4 = Integer.parseInt(props.getProperty("port4"));

			chunkSize = Integer.parseInt(props.getProperty("chunkSize"));

			overhead = 40 * (new byte[chunkSize].length / 1450 + 1);

		} catch (IOException e) {
			Toast.makeText(this, "프로그램을 다시 실행해주세요", Toast.LENGTH_SHORT).show();
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
		// test end

		bo1 = (LinearLayout) findViewById(R.id.bottom1);
		bo2 = (LinearLayout) findViewById(R.id.bottom2);

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);

		downloadTab = (Button) findViewById(R.id.main_bt_tabUpDown);
		downloadTab.setText("Download\nUpload");
		downloadTab.setTextColor(Color.WHITE);
		downloadTab.setEnabled(false);
		downloadTab.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				setDownUp();
			}// onClick
		});

		pingTab = (Button) findViewById(R.id.main_bt_tabPingUdp);
		pingTab.setText("Ping & UDP");
		pingTab.setTextColor(Color.WHITE);
		pingTab.setEnabled(false);
		pingTab.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				setPingUdp();
			}// onClick
		});

		tracerouteTab = (Button) findViewById(R.id.main_bt_tabTraceRoute);
		tracerouteTab.setText("Trace Route");
		tracerouteTab.setTextColor(Color.WHITE);
		tracerouteTab.setEnabled(false);
		tracerouteTab.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				setTrace();
			}// onClick
		});

		// hk start
		vf_tab = (ViewFlipper) findViewById(R.id.main_vf_tab);
		vf_tab.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					xAtDown = event.getX();

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					xAtUp = event.getX();

					if (xAtUp + un < xAtDown) {

						vf_tab.setInAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.left_in));
						vf_tab.setOutAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.left_out));

						if (flingSave != 2 || sv_traceRT.getVisibility() != View.VISIBLE) {

							int t = flingSave + 1;

							if (t > fling) {
								t = 0;
							}

							if (fling != 0) {
								vf_tab.setDisplayedChild(t);
								changeTab();
							}
						} else {
							Log.i("MSN", "33");
						}
					} else if (xAtUp - un > xAtDown) {
						vf_tab.setInAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.right_in));
						vf_tab.setOutAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.right_out));

						if (flingSave != 2 || sv_traceRT.getVisibility() != View.VISIBLE) {

							int t = flingSave - 1;

							if (t < 0) {
								t = fling;
							}

							if (fling != 0) {
								vf_tab.setDisplayedChild(t);
								changeTab();
							}

						}
					}
				}
				return true;
			}
		});

		sv_traceRT.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					xAtDown = event.getX();

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					xAtUp = event.getX();

					if (xAtDown - xAtUp > 150) {

						vf_tab.setInAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.left_in));
						vf_tab.setOutAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.left_out));

						int t = flingSave + 1;

						if (t > fling) {
							t = 0;
						}

						vf_tab.setDisplayedChild(t);
						changeTab();

					} else if (xAtUp - xAtDown > 150) {
						vf_tab.setInAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.right_in));
						vf_tab.setOutAnimation(AnimationUtils.loadAnimation(SpeedTest.this, R.anim.right_out));
						int t = flingSave - 1;

						if (t < 0) {
							t = fling;
						}

						vf_tab.setDisplayedChild(t);
						changeTab();

						return true;
					}
				}

				return false;
			}
		});
		// hk end

		tracerouteWithPing = new TracerouteWithPing(context, 30);
		tracerouteWithPing.setOnTracerouteListener(this);

		btn = (Button) findViewById(R.id.btn);
		btn.setText("측정 시작");
		btn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkWIFI()) {
					btn.setEnabled(false);
					downloadTab.setEnabled(true);

					speed = new NetSpeed();
					speed.start();

					btn.setText("측정 중입니다");

				} else {
					Toast.makeText(context, "WIFI를 연결해주세요", Toast.LENGTH_SHORT).show();
				}

			}// onClick
		});

	}// onCreate

	// hk start
	private void changeTab() {
		int page = vf_tab.getDisplayedChild();
		flingSave = page;

		Button[] tabButtons = { downloadTab, pingTab, tracerouteTab };

		for (int i = 0; i < tabButtons.length; i++) {
			if (i == page) {
				tabButtons[i].setBackgroundResource(R.drawable.btn_radio_pressed);
			} else {
				tabButtons[i].setBackgroundResource(R.drawable.btn_radio_normal);
			}
		}
	}

	// hk end

	public void clickBack(View v) {

		if (!btn.getText().equals("측정 중입니다")) {
			onBackPressed();
		}
	}

	public void clickReset(View v) {

	}

	class Usage extends TimerTask {

		float cpuUsage = 0;
		float memoryUsage = 0;

		public float memory() {

			float availableSpace = 0;

			try {

				String[] cmd = { "system/bin/cat", "/proc/meminfo" };
				Runtime operator = Runtime.getRuntime();
				java.lang.Process process = operator.exec(cmd);

				BufferedReader bufread = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String temp;
				String[] temp_split;

				while ((temp = bufread.readLine()) != null) {

					temp_split = temp.split(":");
					String[] mem_split;

					if (temp_split[0].trim().equals("MemTotal")) {
						mem_split = temp_split[1].split("kB");
						totalSize = Float.parseFloat(mem_split[0].trim());
					}

					else if (temp_split[0].trim().equals("MemFree")) {
						mem_split = temp_split[1].split("kB");
						availableSpace = Float.parseFloat(mem_split[0].trim());
					} else if (temp_split[0].trim().equals("Buffers")) {
						mem_split = temp_split[1].split("kB");
						availableSpace += Float.parseFloat(mem_split[0].trim());
					} else if (temp_split[0].trim().equals("Cached")) {
						mem_split = temp_split[1].split("kB");
						availableSpace += Float.parseFloat(mem_split[0].trim());
					} else if (temp_split[0].trim().equals("SwapCached")) {
						mem_split = temp_split[1].split("kB");
						availableSpace += Float.parseFloat(mem_split[0].trim());
						break;
					}

				}
				bufread.close();

				return (totalSize - availableSpace) / totalSize * 100;
			} catch (IOException e) {
				return 0;
			}
		}// memory

		public float cpu() {
			try {

				String[] cmd = { "/system/bin/sh", "-c", "cat /proc/stat" };
				Runtime operator = Runtime.getRuntime();
				java.lang.Process process = operator.exec(cmd);

				BufferedReader bufir = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String temp = bufir.readLine();

				String[] str_temp = temp.split("[ ]");

				long user = Long.parseLong(str_temp[2]); // user
				long nice = Long.parseLong(str_temp[3]); // nice
				long system = Long.parseLong(str_temp[4]); // kernel
				long idle = Long.parseLong(str_temp[5]); // idle

				return 100 * (user + nice + system) / (float) (user + nice + system + idle);

			} catch (Exception e) {
				return 0;
			}
		}// cpu

		@Override
		public void run() {
			cpuUsage = cpu();
			memoryUsage = memory();

			cu += cpuUsage;
			mu += memoryUsage;

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					if (cpuUsage >= 80) {
						cpu.bgRect.setColor(Color.RED);
					} else if (cpuUsage >= 60) {
						cpu.bgRect.setColor(getResources().getColor(R.color.olleh_Orange));
					} else {
						cpu.bgRect.setColor(getResources().getColor(R.color.olleh_Green));
					}

					if (memoryUsage >= 80) {
						memory.bgRect.setColor(Color.RED);
					} else if (memoryUsage >= 60) {
						memory.bgRect.setColor(getResources().getColor(R.color.olleh_Orange));
					} else {
						memory.bgRect.setColor(getResources().getColor(R.color.olleh_Green));
					}

					cpuText.setText(String.format("%.1f", cpuUsage));
					memoryText.setText(String.format("%.1f", memoryUsage));
				}
			});

			cpu.setValue((int) cpuUsage);
			memory.setValue((int) memoryUsage);

			tCount++;
		}
	}// Usage

	class NetSpeed extends Thread {

		int[] udpdata = new int[101];

		@Override
		public void run() {
			try {

				socket = new Socket(vip, port1);

				// VIP
				resource = new Timer();
				Usage re = new Usage();
				resource.schedule(re, 0, 1000);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "측정을 시작합니다", Toast.LENGTH_SHORT).show();
					}
				});

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				if (in.readLine().equals("CONNECT OK!")) {
					out.write("RTRV-REAL-IP:;\n");
					out.flush();

					if (in.readLine().equals("OK:")) {
						RTRV = in.readLine();

						out.write("RTRV-MY-IP:;\n");
						out.flush();

						in.readLine();

						if (in.readLine().equals("OK:")) {
							MYIP = in.readLine();
							in.readLine();

							in.close();
							out.close();

							socket.close();
						}// MYIP
					}// RTRV
				}// CONNECT

				// download

				setDownUp();

				byte[] chunk = new byte[chunkSize];

				socket = new Socket(RTRV, port2);

				socket.setTcpNoDelay(true);
				socket.setSoLinger(true, 30);

				String remote = socket.getRemoteSocketAddress().toString();
				pin.setStat(true);

				if (remote.startsWith("/")) {
					remote = remote.substring(1);
				}

				InputStream in10012 = socket.getInputStream();

				in10012.read();

				socket.getReceiveBufferSize();

				Timer ti = new Timer();
				TimerTask tim = new TimerTask() {
					int umc = 0;

					int[] downdata = new int[20];

					@Override
					public void run() {
						umc = (int) ((sm - nm + overhead) * 8 / 1000 / 0.5f);

						if (count == 1) {
							downMin = umc;
						}

						if (count != 0) {

							if (umc < downMin)
								downMin = umc;
							if (umc > downMax)
								downMax = umc;

							if (umc / 1000 <= 2) {
								pin.start(umc / 1000 * 60 / 2);
							} else if (umc / 1000 <= 10) {
								pin.start(60 + umc / 1000 * 30 / 10);
							} else if (umc / 1000 <= 50) {
								pin.start(90 + umc / 1000 * 30 / 50);
							} else if (umc / 1000 <= 100) {
								pin.start(120 + umc / 1000 * 30 / 100);
							} else if (umc / 1000 <= 500) {
								pin.start(150 + umc / 1000 * 30 / 500);
							} else {
								pin.start(180);
							}

							for (int t = 0; t < 19; t++) {
								downdata[t] = downdata[t + 1];
							}

							downdata[19] = (int) (umc / 1000f);
							download.setLine(downdata, downMax);

							runOnUiThread(new Runnable() {

								String downs = String.format("%.1f", umc / 1000f);
								String downMins = String.format("%.1f", downMin / 1000f);
								String downMaxs = String.format("%.1f", downMax / 1000f);

								@Override
								public void run() {

									downMinText.setText(downMins);
									downMaxText.setText(downMaxs);
									downValueText.setText(downs);

									downMinCurr.setText(downMins);
									downMaxCurr.setText(downMaxs);
									downCurr.setText(downs);

								}// run down
							});

						}// if

						nm = sm;
						count++;

						if (count == 21) {

							this.cancel();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									downAVG = (sm + overhead * count) * 8 / 1000 / 10;

									downCurr.setText(String.format("%.1f", downAVG / 1000f));
									downValueText.setText(String.format("%.1f", downAVG / 1000f));

									pin.start(0);

									if (downAVG >= sla_down || sla_down == 0) {
										findViewById(R.id.downloadimg).setBackgroundResource(R.drawable.top_info_green);
									} else {
										testQuality++;
										findViewById(R.id.downloadimg).setBackgroundResource(R.drawable.top_info_red);
									}// else
								}// run end
							});
							try {
								socket.close();
							} catch (IOException e) {

							}
						}// count

					}// run
				};

				ti.schedule(tim, 0, 500);

				try {
					while (true) {
						sm += in10012.read(chunk);
					}// while
				} catch (Exception e) {
				}

				socket.close();

				// upload

				socket = new Socket(RTRV, port3);

				socket.setTcpNoDelay(true);
				socket.setSoLinger(true, 30);

				if (remote.startsWith("/")) {
					remote = remote.substring(1);
				}

				String msg = null;
				pin.setStat(false);

				boolean first = true;

				out10013 = socket.getOutputStream();
				in10013 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				chunk = new byte[Integer.parseInt(in10013.readLine())];

				Timer tt = new Timer();
				TimerTask tha = new TimerTask() {

					int umc = 0;
					int[] updata = new int[20];

					String ms = "";
					String[] cc = null;

					@Override
					public void run() {
						try {
							while (twenty != 20) {
								ms = in10013.readLine();
								cc = ms.split(":");

								total += Integer.parseInt(cc[1]);
								umc = (int) (Integer.parseInt(cc[1]) * 8 / 1000 / 0.5f);

								if (Integer.parseInt(cc[0]) == 1) {
									upMin = umc;
								}

								if (umc < upMin)
									upMin = umc;
								if (umc > upMax)
									upMax = umc;

								if (umc / 1000 <= 2) {
									pin.start(umc / 1000 * 60 / 2);
								} else if (umc / 1000 <= 10) {
									pin.start(60 + umc / 1000 * 30 / 10);
								} else if (umc / 1000 <= 50) {
									pin.start(90 + umc / 1000 * 30 / 50);
								} else if (umc / 1000 <= 100) {
									pin.start(120 + umc / 1000 * 30 / 100);
								} else if (umc / 1000 <= 500) {
									pin.start(150 + umc / 1000 * 30 / 500);
								} else {
									pin.start(180);
								}

								runOnUiThread(new Runnable() {

									String upMins = String.format("%.1f", upMin / 1000f);
									String upMaxs = String.format("%.1f", upMax / 1000f);
									String ups = String.format("%.1f", umc / 1000f);

									@Override
									public void run() {
										upMinText.setText(upMins);
										upMaxText.setText(upMaxs);
										upValueText.setText(ups);

										upMinCurr.setText(upMins);
										upMaxCurr.setText(upMaxs);
										upCurr.setText(ups);

									}// run upload
								});

								for (int t = 0; t < 19; t++) {
									updata[t] = updata[t + 1];
								}

								updata[19] = (int) umc / 1000;
								upload.setLine(updata, upMax);

								twenty = Integer.parseInt(cc[0]);
							}
						} catch (IOException e) {
						}// catch
					}// run
				};// Task

				tt.schedule(tha, 0, 300);

				while (twenty != 20) {

					try {
						if (first) {

							msg = String.format("%s\n", new Object[] { Integer.valueOf(1) });

							out10013.write(msg.getBytes());
							out10013.flush();

							first = false;
						} else {
							out10013.write(chunk);
							out10013.flush();
						}// else

					} catch (Exception e) {
					}

				}// while

				tha.cancel();
				pin.start(0);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						upAVG = total * 8 / 1000 / 10;

						upCurr.setText(String.format("%.1f", upAVG / 1000f));
						upValueText.setText(String.format("%.1f", upAVG / 1000f));

						if (upAVG >= sla_up || sla_up != 0) {
							findViewById(R.id.uploadimg).setBackgroundResource(R.drawable.top_info_green);
						} else {
							testQuality++;
							findViewById(R.id.uploadimg).setBackgroundResource(R.drawable.top_info_red);
						}

					}
				});

				socket.close();

				in10013.close();
				out10013.close();
				// upload

				// ping
				Thread.sleep(4000);
				setPingUdp();
				fling++;

				String str = "";
				String[] amss;
				int[] pingdata = new int[101];

				for (int a = 0; a < 100; a++) {

					try {

						Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1000 " + RTRV);
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

						String line;

						while ((line = reader.readLine()) != null)
							str = line.toString();

						reader.close();

						amss = str.split("/");

						pingTime = Float.parseFloat(amss[4].substring(0, 3));

						if (pingTime >= 1000) {
							pingLost++;
							pingTime = 1000;
						} else {
							pingAVG += pingTime;
						}

						if (pingTime < pingMin)
							pingMin = pingTime;
						if (pingTime > pingMax)
							pingMax = pingTime;

						for (int t = 1; t < 100; t++) {
							pingdata[t] = pingdata[t + 1];
						}

						if (pingTime > 50) {
							pingdata[100] = (int) (50 * (pingTime / 1000f) + 50);
						} else {
							pingdata[100] = (int) pingTime;
						}
						pingLinechart.setLine(pingdata, Float.toString(pingMin), Float.toString(pingMax),
								Integer.toString(pingLost));

						if (pingTime < 100) {
							Thread.sleep(100 - (int) pingTime);
						}

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								pingText.setText(String.format("%.1f", pingTime));
								pingMaxText.setText(String.format("%.1f", pingMax));
								pingMinText.setText(String.format("%.1f", pingMin));
							}
						});

					} catch (IOException e) {
						pingLost++;
						pingTime = 1000;

						if (pingTime < pingMin)
							pingMin = pingTime;
						if (pingTime > pingMax)
							pingMax = pingTime;

						for (int t = 1; t < 100; t++) {
							pingdata[t] = pingdata[t + 1];
						}

						if (pingTime > 50) {
							pingdata[100] = (int) (50 * (pingTime / 1000f) + 50);
						} else {
							pingdata[100] = (int) pingTime;
						}

						pingLinechart.setLine(pingdata, Float.toString(pingMin), Float.toString(pingMax),
								Integer.toString(pingLost));
						e.printStackTrace();
					}
				}// for

				pingAVG = pingAVG / (100 - pingLost);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						pingText.setText(String.format("%.1f", pingAVG));
						pingMaxText.setText(String.format("%.1f", pingMax));
						pingMinText.setText(String.format("%.1f", pingMin));

						if (pingAVG < sla_ping || sla_ping == 0) {

							findViewById(R.id.pingimg).setBackgroundResource(R.drawable.top_info_green);
						} else {
							testQuality++;
							findViewById(R.id.pingimg).setBackgroundResource(R.drawable.top_info_red);
						}
					}
				});// run

				// ping end

				// udp

				Thread theUDP = new Thread() {

					byte[] strByte = null;
					byte[] data = null;

					DatagramPacket dp = null;
					DatagramSocket ds = null;

					int time100 = 0;
					long am = 0;

					@Override
					public void run() {

						for (int i = 1; i <= 100; i++) {

							am = System.currentTimeMillis();

							String str = "Seq=" + i + ", Timer=" + am;

							strByte = str.getBytes();

							try {
								// send
								dp = new DatagramPacket(strByte, strByte.length, InetAddress.getByName(RTRV), port4);
								ds = new DatagramSocket();

								ds.send(dp);

								try {
									ds.setSoTimeout(1000);
									ds.receive(dp);

									data = dp.getData();
									time100 = Integer.parseInt((Long.toString(System.currentTimeMillis() - am)));

								} catch (Exception e) {
									time100 = 1000;
								}

								ds.close();

								if (time100 >= 1000) {
									lost++;
									time100 = 1000;
								} else {
									udpAVG += (int) time100;
								}

								if (time100 < udpMin)
									udpMin = time100;
								if (time100 > udpMax)
									udpMax = time100;

								if (time100 < 100) {
									Thread.sleep(100 - time100);
								}

								for (int t = 1; t < 100; t++) {
									udpdata[t] = udpdata[t + 1];
								}

								if (time100 > 50) {
									udpdata[100] = (int) (50 * (time100 / 1000f) + 50);
								} else {
									udpdata[100] = (int) time100;
								}

								udpLinechart.setLine(udpdata, Integer.toString(udpMin), Integer.toString(udpMax),
										Integer.toString(lost));

								if (pingTime < 100) {
									Thread.sleep(100 - (int) pingTime);
								}

								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										udpText.setText(time100 + ".0");
										udpMinText.setText(udpMin + ".0");
										udpMaxText.setText(udpMax + ".0");
									}
								});

							} catch (Exception e) {
								e.printStackTrace();
							}// catch
						}// for

						udpAVG = udpAVG / ((100 - lost) * 1f);

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								udpText.setText(String.format("%.1f", udpAVG));
								udpMinText.setText(udpMin + ".0");
								udpMaxText.setText(udpMax + ".0");

								if (udpAVG < sla_udp || sla_udp == 0) {
									findViewById(R.id.udpimg).setBackgroundResource(R.drawable.top_info_green);
								} else {
									testQuality++;
									findViewById(R.id.udpimg).setBackgroundResource(R.drawable.top_info_red);
								}// else
							}// run
						});
						// udp end
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						setTrace();
						fling++;
						// traceroute
						Message msg = new Message();
						han.sendMessage(msg);
						// traceroute end

						// resource end
						resource.cancel();

						mu /= tCount;
						cu /= tCount;

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								cpuText.setText(String.format("%.1f", cu));
								memoryText.setText(String.format("%.1f", mu));

								if (cu >= 80) {
									cpu.bgRect.setColor(Color.RED);
								} else if (cu >= 60) {
									cpu.bgRect.setColor(getResources().getColor(R.color.olleh_Orange));
								} else {
									cpu.bgRect.setColor(getResources().getColor(R.color.olleh_Green));
								}

								if (mu >= 80) {
									memory.bgRect.setColor(Color.RED);
								} else if (mu >= 60) {
									memory.bgRect.setColor(getResources().getColor(R.color.olleh_Orange));
								} else {
									memory.bgRect.setColor(getResources().getColor(R.color.olleh_Green));
								}

							}
						});

						this.interrupt();
					}// run
				};

				theUDP.start();

			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btn.setText("취소");
						btn.setEnabled(true);
						btn.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								ac.finish();
							}// onClick
						});

						Toast.makeText(context, "서버에 접속할 수 없습니다", Toast.LENGTH_SHORT).show();
					}
				});
			}// catch

		}// run
	}// class Net Speed

	public boolean checkWIFI() {
		ConnectivityManager cManager;

		cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
			return true;
		} else {
			return false;
		}

	}// checkWIFI

	@Override
	public void onTracerouteListener(List<TracerouteContainer> list, String resultMessage) {

		ll_progressBar.setVisibility(View.GONE);
		sv_traceRT.setVisibility(View.VISIBLE);
		ll_traceRouteError.setVisibility(View.GONE);
		speed.interrupt();

		Log.i("TTR", resultMessage);

		if (!resultMessage.equals("Success") && !resultMessage.equals("Finish")) {

			// ll_progressBar.setVisibility(View.GONE);
			// sv_traceRT.setVisibility(View.GONE);
			// ll_traceRouteError.setVisibility(View.VISIBLE);
		}

		if (resultMessage.equals("Success")) {
			if (traces != null) {
				traces.clear();
				traces.add(new TracerouteContainer("", "", -1, true));
			} else {
				traces = new ArrayList<TracerouteContainer>();
				traces.add(new TracerouteContainer("", "", -1, true));
			}

			for (int i = 0; i < list.size(); i++) {
				traces.add(list.get(i));
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					drawTraceRoute();
					sv_traceRT.scrollTo(0, ll_traceRT.getHeight());
				}
			});

			return;

		}

		if (!resultMessage.equals("Success") && !resultMessage.equals("Finish")) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "TraceRoute 측정에 실패했습니다", Toast.LENGTH_SHORT).show();
				}
			});
		}

		if (!resultMessage.equals("Success")) {

			Thread kTimer = new Thread() {

				@Override
				public void run() {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setDownUp();
					this.interrupt();
				}
			};

			kTimer.start();

			bo1.setVisibility(View.GONE);
			bo2.setVisibility(View.VISIBLE);
			btn.setText("결과");

			btn2.setText("재측정");
			btn2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					// speed = new NetSpeed();

					// System.exit(0);

					Intent intent = new Intent(SpeedTest.this, SpeedTest.class);
					intent.putExtra("scn", SCN + "");
					intent.putExtra("workOdrNum", WORKODRNUM + "");
					intent.putExtra("workerId", WORKERID + "");
					intent.putExtra("workOdrKind", workOdrKind + "");
					intent.putExtra("kornetId", KORNETID + "");
					intent.putExtra("tel_no", TEL_NO + "");
					intent.putExtra("officesCode", OFFICESCODE + "");
					intent.putExtra("sla_down", sla_down + "");
					intent.putExtra("sla_up", sla_up + "");
					intent.putExtra("sla_udp", sla_udp + "");
					intent.putExtra("sla_ping", sla_ping + "");

					startActivity(intent);
					finish();
				}
			});

			btn.setEnabled(true);
			btn1.setText("결과 전송");
			btn1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					btn1.setEnabled(false);

					WAS was = new WAS("http://moss.kt.com/moss/SpeedTestResult/");
					// was save
					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					WifiInfo info = ((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo();

					TESTDATE = sdfNow.format(date);

					if (testQuality != 0) {
						TESTQUALITY = "1";
					} else {
						TESTQUALITY = "0";
					}

					char[] avm = RTRV.toCharArray();
					int avms = 0;

					for (int i = 0; i < avm.length; i++) {
						if (avm[i] == '.') {
							avms = i;
						}
					}

					System.out.println(RTRV);
					SERVERINFO = RTRV.substring(avms + 1);
					GATEWAY = MYIP;
					CPUTYPE = Build.CPU_ABI;
					NIC = Build.MODEL;
					OS = "Android Ver " + Build.VERSION.RELEASE;
					IP = MYIP;
					WORKODRKIND = Integer.toString(workOdrKind);
					MEMORYSIZE = Float.toString(totalSize / 1000f);
					UPLOADSPEEDAVG = Integer.toString(upAVG);
					UPLOADSPEEDMIN = Integer.toString(upMin);
					UPLOADSPEEDMAX = Integer.toString(upMax);
					DOWNLOADSPEEDAVG = Integer.toString(downAVG);
					DOWNLOADSPEEDMIN = Integer.toString(downMin);
					DOWNLOADSPEEDMAX = Integer.toString(downMax);
					PACKDELAYTIMEAVG = Float.toString(pingAVG);
					PACKDELAYTIMEMIN = Float.toString(pingMin);
					PACKDELAYTIMEMAX = Float.toString(pingMax);
					PACKLOSSRATE = Integer.toString(pingLost);
					PACKDELAYTIMESTD = Integer.toString((int) pingAVG * 1000);
					JITTERMIN = "0";
					JITTERMAX = "0";
					JITTERAVG = "0";
					JITTERSTD = "0";
					UDPDELAYTIMEAVG = Float.toString(udpAVG);
					UDPDELAYTIMEMIN = Float.toString(udpMin);
					UDPDELAYTIMEMAX = Float.toString(udpMax);
					UDPDELAYTIMESTD = Integer.toString((int) udpAVG * 1000);
					UDPLOSSRATE = Integer.toString(lost);
					HOPCOUNT = Integer.toString(traces.size() - 1); //
					CPURATE = Float.toString(cu);
					MEMORYRATE = Float.toString(mu);
					MAC = info.getMacAddress();

					was.start();

				}
			});
		}
	};

	Handler han = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// tracerouteWithPing.executeTraceroute(RTRV);
			tracerouteWithPing.executeTraceroute("173.194.126.243");
		}
	};

	public void setDownUp() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				upDown.setVisibility(View.VISIBLE);
				udpPingLayout.setVisibility(View.GONE);
				tracerouteLayout.setVisibility(View.GONE);

				downloadTab.setBackgroundResource(R.drawable.btn_radio_pressed);
				pingTab.setBackgroundResource(R.drawable.btn_radio_normal);
				tracerouteTab.setBackgroundResource(R.drawable.btn_radio_normal);

				downloadTab.setEnabled(true);
			}
		});

		flingSave = 0;
	}

	public void setPingUdp() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				upDown.setVisibility(View.GONE);
				udpPingLayout.setVisibility(View.VISIBLE);
				tracerouteLayout.setVisibility(View.GONE);

				downloadTab.setBackgroundResource(R.drawable.btn_radio_normal);
				pingTab.setBackgroundResource(R.drawable.btn_radio_pressed);
				tracerouteTab.setBackgroundResource(R.drawable.btn_radio_normal);

				pingTab.setEnabled(true);
			}
		});

		flingSave = 1;
	}

	private void settingTraceRoute() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int ipWidth = 130;

		if (metrics.densityDpi == 240) {
			ipWidth = 130;
		} else if (metrics.densityDpi == 320) {
			ipWidth = 180;
		} else if (metrics.densityDpi == 480) {
			ipWidth = 250;
		}

		LinearLayout.LayoutParams ll_ip_param = new LinearLayout.LayoutParams(ipWidth, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams ll_hop_param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams ll_node_param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 1);

		ll_row = new LinearLayout(this);
		ll_row.setOrientation(LinearLayout.HORIZONTAL);
		ll_row.setGravity(Gravity.CENTER);

		tv_leftIP = new TextView(this);
		tv_leftIP.setTextSize(10);
		tv_leftIP.setPadding(0, 0, 5, 0);
		tv_leftIP.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		tv_leftIP.setLayoutParams(ll_ip_param);
		tv_leftIP.setTextColor(Color.parseColor("#000000"));
		tv_leftHop = new TextView(this);
		tv_leftHop.setGravity(Gravity.CENTER);
		tv_leftHop.setLayoutParams(ll_hop_param);
		tv_leftHop.setTextColor(Color.parseColor("#ffffff"));
		tv_leftHop.setTextSize(15);
		tv_horizontalHop = new TextView(this);
		tv_horizontalHop.setGravity(Gravity.CENTER);
		tv_horizontalHop.setTextSize(10);
		tv_horizontalHop.setLayoutParams(ll_node_param);
		tv_horizontalHop.setTextColor(Color.parseColor("#000000"));
		tv_verticalHop = new VerticalTextView(this, null, metrics.densityDpi);
		tv_verticalHop.setGravity(Gravity.CENTER);
		tv_verticalHop.setTextSize(10);
		tv_verticalHop.setLayoutParams(ll_hop_param);
		tv_verticalHop.setTextColor(Color.parseColor("#000000"));
		tv_rightHop = new TextView(this);
		tv_rightHop.setGravity(Gravity.CENTER);
		tv_rightHop.setTextSize(15);
		tv_rightHop.setLayoutParams(ll_hop_param);
		tv_rightHop.setTextColor(Color.parseColor("#ffffff"));
		tv_rightIP = new TextView(this);
		tv_rightIP.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_rightIP.setTextSize(10);
		tv_rightIP.setPadding(5, 0, 0, 0);
		tv_rightIP.setLayoutParams(ll_ip_param);
		tv_rightIP.setTextColor(Color.parseColor("#000000"));
	}

	private void drawTraceRoute() {

		// LinearLayout ll_row = new LinearLayout(this);
		// ll_row.setOrientation(LinearLayout.HORIZONTAL);
		//
		//
		// ImageView img_start = new ImageView(this);
		// img_start.setId(0);
		// img_start.setBackgroundResource(R.drawable.ico_trace_route_start);
		ll_traceRT.removeAllViews();
		ll_traceRT.setOrientation(LinearLayout.VERTICAL);

		int rowCount = 1;
		boolean isEvenRow = false;

		settingTraceRoute();

		for (int i = 0; i < traces.size(); i++) {

			tv_horizontalHop.setBackgroundResource(R.drawable.ico_trace_route_node);
			tv_verticalHop.setBackgroundResource(R.drawable.ico_trace_route_node_ver);

			if (i == 0) {
				settingTraceRoute();

				TextView tv_start = new TextView(this);

				tv_start.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tv_start.setBackgroundResource(R.drawable.ico_trace_route_start);
				tv_leftIP.setText("");
				ll_row.addView(tv_leftIP);
				ll_row.addView(tv_start);
			} else {
				tv_leftHop.setBackgroundResource(R.drawable.ico_trace_route);
				tv_rightHop.setBackgroundResource(R.drawable.ico_trace_route);

				if (rowCount % 2 != 0) {
					if (!isEvenRow) {
						if (rowCount == i) { // right

							String um = String.format("%.2f", traces.get(i).getMs());
							tv_horizontalHop.setText(Float.parseFloat(um) + "");
							tv_rightHop.setText(i + "");
							tv_rightIP.setText(traces.get(i).getIp());

							ll_row.addView(tv_horizontalHop);
							ll_row.addView(tv_rightHop);
							ll_row.addView(tv_rightIP);

							ll_traceRT.addView(ll_row);
							rowCount++;
							isEvenRow = !isEvenRow;

							settingTraceRoute();
						} else { // left
							tv_leftIP.setText(traces.get(i).getIp());
							tv_leftHop.setText(i + "");

							tv_rightIP.setText("");
							tv_rightHop.setText("");
							tv_horizontalHop.setText("");
							tv_rightHop.setBackgroundResource(R.drawable.ico_trace_route_opacity);
							tv_horizontalHop.setBackgroundResource(R.drawable.ico_trace_route_node_opacity);

							ll_row.addView(tv_leftIP);
							ll_row.addView(tv_leftHop);

							if ((i + 1) == traces.size()) {

								ll_row.addView(tv_horizontalHop);
								ll_row.addView(tv_rightHop);
								ll_row.addView(tv_rightIP);

								ll_traceRT.addView(ll_row);
								rowCount++;
								isEvenRow = !isEvenRow;

								settingTraceRoute();
							}
						}
					} else {
						if (rowCount == i) {// left
							tv_leftIP.setText(traces.get(i).getIp());
							tv_leftHop.setText(i + "");

							String um = String.format("%.2f", traces.get(i).getMs());
							tv_horizontalHop.setText(Float.parseFloat(um) + "");

							ll_row.addView(tv_leftIP);
							ll_row.addView(tv_leftHop);
							ll_row.addView(tv_horizontalHop);
							ll_row.addView(tv_rightHop);
							ll_row.addView(tv_rightIP);

							ll_traceRT.addView(ll_row);
							rowCount++;
							isEvenRow = !isEvenRow;

							settingTraceRoute();
						} else {// right

							tv_leftIP.setText("");
							tv_leftHop.setText("");
							tv_horizontalHop.setText("");
							tv_leftHop.setBackgroundResource(R.drawable.ico_trace_route_opacity);
							tv_horizontalHop.setBackgroundResource(R.drawable.ico_trace_route_node_opacity);

							tv_rightHop.setText(i + "");
							tv_rightIP.setText(traces.get(i).getIp());

							if ((i + 1) == traces.size()) {

								ll_row.addView(tv_leftIP);
								ll_row.addView(tv_leftHop);
								ll_row.addView(tv_horizontalHop);
								ll_row.addView(tv_rightHop);
								ll_row.addView(tv_rightIP);

								ll_traceRT.addView(ll_row);
								rowCount++;
								isEvenRow = !isEvenRow;

								settingTraceRoute();
							}
						}
					}
				} else {

					tv_leftIP.setText("");
					tv_leftHop.setText("");
					tv_leftHop.setBackgroundResource(R.drawable.ico_trace_route_opacity);
					tv_horizontalHop.setText("");
					tv_horizontalHop.setBackgroundResource(R.drawable.ico_trace_route_node_opacity);
					tv_rightHop.setText("");
					tv_rightHop.setBackgroundResource(R.drawable.ico_trace_route_opacity);

					String um = String.format("%.2f", traces.get(i).getMs());
					tv_verticalHop.setText(Float.parseFloat(um) + "");

					if (isEvenRow) {
						ll_row.addView(tv_leftIP);
						ll_row.addView(tv_leftHop);
						ll_row.addView(tv_horizontalHop);
						ll_row.addView(tv_verticalHop);
						ll_row.addView(tv_rightIP);
					} else {
						ll_row.addView(tv_leftIP);
						ll_row.addView(tv_verticalHop);
						ll_row.addView(tv_horizontalHop);
						ll_row.addView(tv_rightHop);
						ll_row.addView(tv_rightIP);
					}

					ll_traceRT.addView(ll_row);

					rowCount++;

					settingTraceRoute();
					i--;

				}

			}
		}
	}

	public void setTrace() {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				upDown.setVisibility(View.GONE);
				udpPingLayout.setVisibility(View.GONE);
				tracerouteLayout.setVisibility(View.VISIBLE);

				downloadTab.setBackgroundResource(R.drawable.btn_radio_normal);
				pingTab.setBackgroundResource(R.drawable.btn_radio_normal);
				tracerouteTab.setBackgroundResource(R.drawable.btn_radio_pressed);

				tracerouteTab.setEnabled(true);
			}
		});

		flingSave = 2;
	}

	public class WAS extends Thread {

		private String url = "";
		private HttpClient httpClient;
		private HttpPost httpPost;

		public WAS(String url) {
			this.url = url;
		}

		public void run() {
			try {
				httpClient = new DefaultHttpClient();
				HttpParams params = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 5000);
				HttpConnectionParams.setSoTimeout(params, 5000);

				httpPost = new HttpPost(url);
				HashMap<String, Object> jsonValues = new HashMap<String, Object>();

				jsonValues.put("TESTDATE", TESTDATE);
				jsonValues.put("WORKODRNUM", WORKODRNUM);
				jsonValues.put("WORKODRKIND", WORKODRKIND);
				jsonValues.put("SCN", SCN);
				jsonValues.put("WORKERID", WORKERID);
				jsonValues.put("KORNETID", KORNETID);
				jsonValues.put("TEL_NO", TEL_NO);
				jsonValues.put("OFFICESCODE", OFFICESCODE);
				jsonValues.put("TESTQUALITY", TESTQUALITY);
				jsonValues.put("SERVERINFO", SERVERINFO);

				jsonValues.put("UPLOADSPEEDMIN", UPLOADSPEEDMIN);
				jsonValues.put("UPLOADSPEEDMAX", UPLOADSPEEDMAX);
				jsonValues.put("UPLOADSPEEDAVG", UPLOADSPEEDAVG);
				jsonValues.put("DOWNLOADSPEEDMIN", DOWNLOADSPEEDMIN);
				jsonValues.put("DOWNLOADSPEEDMAX", DOWNLOADSPEEDMAX);
				jsonValues.put("DOWNLOADSPEEDAVG", DOWNLOADSPEEDAVG);
				jsonValues.put("PACKDELAYTIMEMIN", PACKDELAYTIMEMIN);
				jsonValues.put("PACKDELAYTIMEMAX", PACKDELAYTIMEMAX);
				jsonValues.put("PACKDELAYTIMEAVG", PACKDELAYTIMEAVG);
				jsonValues.put("PACKDELAYTIMESTD", PACKDELAYTIMESTD);
				jsonValues.put("PACKLOSSRATE", PACKLOSSRATE);
				jsonValues.put("UDPDELAYTIMEMIN", UDPDELAYTIMEMIN);
				jsonValues.put("UDPDELAYTIMEMAX", UDPDELAYTIMEMAX);
				jsonValues.put("UDPDELAYTIMEAVG", UDPDELAYTIMEAVG);
				jsonValues.put("UDPDELAYTIMESTD", UDPDELAYTIMESTD);
				jsonValues.put("UDPLOSSRATE", UDPLOSSRATE);
				jsonValues.put("JITTERMIN", JITTERMIN);
				jsonValues.put("JITTERMAX", JITTERMAX);
				jsonValues.put("JITTERAVG", JITTERAVG);
				jsonValues.put("JITTERSTD", JITTERSTD);
				jsonValues.put("HOPCOUNT", HOPCOUNT);
				jsonValues.put("CPURATE", CPURATE);
				jsonValues.put("MEMORYRATE", MEMORYRATE);
				jsonValues.put("IP", IP);
				jsonValues.put("GATEWAY", GATEWAY);
				jsonValues.put("MAC", MAC);
				jsonValues.put("CPUTYPE", CPUTYPE);
				jsonValues.put("MEMORYSIZE", MEMORYSIZE);
				jsonValues.put("NIC", NIC);
				jsonValues.put("OS", OS);
				jsonValues.put("BROWSER", BROWSER);

				JSONObject json = new JSONObject(jsonValues);

				JSONArray jsonArray = new JSONArray();
				jsonArray.put(json);
				System.out.println(jsonValues.toString());
				HashMap<String, Object> jsonValues2 = new HashMap<String, Object>();
				jsonValues2.put("request", jsonArray);

				JSONObject jsonFinal = new JSONObject(jsonValues2);

				AbstractHttpEntity entity = new ByteArrayEntity(jsonFinal.toString().getBytes("UTF8"));
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

				httpPost.setEntity(entity);

				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity resultEntity = response.getEntity();

				String responseString = EntityUtils.toString(resultEntity);

				httpClient.getConnectionManager().shutdown();

				JSONObject jsonObject = new JSONObject(responseString).getJSONObject("header");
				result = jsonObject.getString("resultCode").toString().split(":");

			} catch (Exception e) {
				result[0] = "1";
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					btn1.setEnabled(true);

					if (result[0].equals("0")) {
						Toast.makeText(context, "결과를 전송했습니다", Toast.LENGTH_SHORT).show();

						btn1.setText("확인");
						btn1.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								ac.finish();
							}
						});
					} else {
						Toast.makeText(context, "전송 실패했습니다", Toast.LENGTH_SHORT).show();
					}

				}
			});

			this.interrupt();
		}

	}// class

}// public class

