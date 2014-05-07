package com.kt.moss.qtest.speedtest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class Traceroute {
}

interface TracerouteListener {
	public abstract void onTracerouteListener(List<TracerouteContainer> list, String resultMessage);
}

class TracerouteContainer implements Serializable {

	private static final long serialVersionUID = 1034744411998219581L;

	private String hostname;
	private String ip;
	private float ms;
	private boolean isSuccessful;

	public TracerouteContainer(String hostname, String ip, float ms, boolean isSuccessful) {
		this.hostname = hostname;
		this.ip = ip;
		this.ms = ms;
		this.isSuccessful = isSuccessful;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public float getMs() {
		return ms;
	}

	public void setMs(float ms) {
		this.ms = ms;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	@Override
	public String toString() {
		return "Traceroute : \nHostname : " + hostname + "\nip : " + ip + "\nMilliseconds : " + ms;
	}

}

class TracerouteWithPing {

	private static final String STRING_FINISH = "Finish";
	private static final String STRING_SUCCESS = "Success";
	private static final String STRING_NO_CONNECTIVITY = "No connectivity";
	private static final String STRING_NO_PING = "Error Ping";
	private static final String STRING_CANCEL = "Cancel";
	private static final String STRING_ERROR = "ERROR";
	private static final int INTEGER_EXECUTE_COUNT = 2;

	private static final String PING = "PING";
	private static final String FROM_PING = "From";
	private static final String SMALL_FROM_PING = "from";
	private static final String PARENTHESE_OPEN_PING = "(";
	private static final String PARENTHESE_CLOSE_PING = ")";
	private static final String TIME_PING = "time=";
	private static final String EXCEED_PING = "exceed";
	private static final String UNREACHABLE_PING = "100%";

	private List<TracerouteContainer> traces;
	private ArrayList<TracerouteContainer> firstValue;
	private int ttl;
	private int finishedTasks;
	private String urlToPing;
	private String ipToPing;
	private float elapsedTime;
	private Context context;

	private int executeCount;
	private int maxTtl;

	// timeout handling
	private static final int TIMEOUT = 30000;
	private Handler handlerTimeout;
	private static Runnable runnableTimeout;

	private TracerouteListener tracerouteListener = null;

	public void setOnTracerouteListener(TracerouteListener listener) {
		tracerouteListener = listener;
	}

	public TracerouteWithPing(Context con, int _maxTtl) {
		this.context = con;

		maxTtl = _maxTtl;
	}

	private void init() {
		ttl = 1;
		finishedTasks = 0;
	}

	private float avg(float val1, float val2) {
		float result = (val1 + val2) / 2;

		return result;
	}

	public void executeTraceroute(String url) {
		urlToPing = url;
		executeCount = 1;

		if (traces == null) {
			traces = new ArrayList<TracerouteContainer>();
		}
		traces.clear();

		init();

		new ExecutePingAsyncTask(maxTtl).execute();
	}

	private class TimeOutAsyncTask extends AsyncTask<Void, Void, Void> {

		private ExecutePingAsyncTask task;
		private int ttlTask;

		public TimeOutAsyncTask(ExecutePingAsyncTask task, int ttlTask) {
			this.task = task;
			this.ttlTask = ttlTask;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (handlerTimeout == null) {
				handlerTimeout = new Handler();
			}

			// stop old timeout
			if (runnableTimeout != null) {
				handlerTimeout.removeCallbacks(runnableTimeout);
			}
			// define timeout
			runnableTimeout = new Runnable() {
				@Override
				public void run() {
					if (task != null) {
						Log.e("TraceRoute", ttlTask + " task.isFinished()" + finishedTasks + " "
								+ (ttlTask == finishedTasks));
						if (ttlTask == finishedTasks) {
							Log.i("TraceRoute", "Timeout");
							task.setCancelled(true);
							task.cancel(true);
							// hk stopProgress
							tracerouteListener.onTracerouteListener(traces, STRING_CANCEL);
						}
					}
				}
			};
			// launch timeout after a delay
			handlerTimeout.postDelayed(runnableTimeout, TIMEOUT);

			super.onPostExecute(result);
		}
	}

	private class ExecutePingAsyncTask extends AsyncTask<Void, Void, String> {

		private boolean isCancelled;
		private int maxTtl;

		public ExecutePingAsyncTask(int maxTtl) {
			this.maxTtl = maxTtl;
		}

		/**
		 * Launches the ping, launches InetAddress to retrieve url if there is
		 * one, store trace
		 */
		@Override
		protected String doInBackground(Void... params) {
			if (hasConnectivity()) {
				try {
					String res = launchPing(urlToPing);

					TracerouteContainer trace;

					if (res.contains(UNREACHABLE_PING) && !res.contains(EXCEED_PING)) {
						// Create the TracerouteContainer object when ping
						// failed
						trace = new TracerouteContainer("", parseIpFromPing(res), elapsedTime, false);
					} else {
						// Create the TracerouteContainer object when succeed
						trace = new TracerouteContainer("", parseIpFromPing(res),
								ttl == maxTtl ? Float.parseFloat(parseTimeFromPing(res)) : elapsedTime, true);
					}

					// Get the host name from ip (unix ping do not support
					// hostname resolving)
					InetAddress inetAddr = InetAddress.getByName(trace.getIp());
					String hostname = inetAddr.getHostName();
					String canonicalHostname = inetAddr.getCanonicalHostName();
					trace.setHostname(hostname);
					Log.d("TraceRoute", "hostname : " + hostname);
					Log.d("TraceRoute", "canonicalHostname : " + canonicalHostname);

					// Store the TracerouteContainer object
					Log.d("TraceRoute", trace.toString());
					traces.add(trace);

					return res;
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else {
				return STRING_NO_CONNECTIVITY;
			}
			return "";
		}

		@SuppressLint("NewApi")
		private String launchPing(String url) throws Exception {
			// Build ping command with parameters
			Process p;
			String command = "";

			String format = "/system/bin/ping -c 1 -t %d ";
			command = String.format(format, ttl);

			Log.d("TraceRoute", "Will launch : " + command + url);

			long startTime = System.nanoTime();
			// timeout task
			new TimeOutAsyncTask(this, ttl).execute();
			// Launch command
			p = Runtime.getRuntime().exec(command + url);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			// Construct the response from ping
			String s;
			String res = "";
			while ((s = stdInput.readLine()) != null) {
				res += s + "\n";
				if (s.contains(FROM_PING) || s.contains(SMALL_FROM_PING)) {
					// We store the elapsedTime when the line from ping comes
					elapsedTime = (System.nanoTime() - startTime) / 1000000.0f;
				}
			}

			p.destroy();

			if (res.equals("")) {
				throw new IllegalArgumentException();
			}

			// Store the wanted ip adress to compare with ping result
			if (ttl == 1) {
				ipToPing = parseIpToPingFromPing(res);
			}

			return res;
		}

		@Override
		protected void onPostExecute(String result) {

			boolean isEnd = false;
			if (!isCancelled) {
				try {
					if (!"".equals(result)) {
						if (STRING_NO_CONNECTIVITY.equals(result)) {
						} else {

							if (traces.get(traces.size() - 1).getIp().equals(ipToPing)) {
								if (ttl < maxTtl) {
									ttl = maxTtl;
									traces.remove(traces.size() - 1);
									new ExecutePingAsyncTask(maxTtl).execute();
								} else {
									// hk stopProgress
									isEnd = true;
								}
							} else {
								if (ttl < maxTtl) {
									ttl++;
									new ExecutePingAsyncTask(maxTtl).execute();
								}
							}
							// context.refreshList(traces);
							if (executeCount == INTEGER_EXECUTE_COUNT) {

								int i = (traces.size() >= 0) ? traces.size() - 1 : 0;
								float resultAvg;

								if (i > firstValue.size()) {
									resultAvg = traces.get(i).getMs();
								} else {
									resultAvg = avg(traces.get(i).getMs(), firstValue.get(i).getMs());
								}

								Log.i("MSN", "traces " + i + " : " + traces.get(i).getIp() + " : "
										+ traces.get(i).getMs() + ", firstValue : " + firstValue.get(i).getMs()
										+ "Avg ; " + resultAvg);
								traces.get(i).setMs(resultAvg);
								traces.set(i, traces.get(i));

								tracerouteListener.onTracerouteListener(traces, STRING_SUCCESS);
							}
						}
					}

					finishedTasks++;

					if (isEnd) {

						if (executeCount != INTEGER_EXECUTE_COUNT) {
							executeCount++;

							if (firstValue == null) {
								firstValue = new ArrayList<TracerouteContainer>();
							}
							firstValue.clear();

							for (int i = 0; i < traces.size(); i++) {
								firstValue.add(traces.get(i));
							}

							init();
							traces.clear();

							new ExecutePingAsyncTask(maxTtl).execute();

						} else {
							tracerouteListener.onTracerouteListener(null, STRING_FINISH);

						}
					}
				} catch (final Exception e) {
					tracerouteListener.onTracerouteListener(traces, STRING_ERROR + ":" + e.toString());
				}
			}

			super.onPostExecute(result);
		}

		private void onException(Exception e) {
			Log.e("TraceRoute", e.toString());

			if (e instanceof IllegalArgumentException) {
				tracerouteListener.onTracerouteListener(null, STRING_NO_PING);
			} else {
				tracerouteListener.onTracerouteListener(null, STRING_ERROR + ":" + e.toString());
			}
			finishedTasks++;
		}

		public void setCancelled(boolean isCancelled) {
			this.isCancelled = isCancelled;
		}

	}

	private String parseIpFromPing(String ping) {
		String ip = "";
		if (ping.contains(FROM_PING)) {
			// Get ip when ttl exceeded
			int index = ping.indexOf(FROM_PING);

			ip = ping.substring(index + 5);
			if (ip.contains(PARENTHESE_OPEN_PING)) {
				// Get ip when in parenthese
				int indexOpen = ip.indexOf(PARENTHESE_OPEN_PING);
				int indexClose = ip.indexOf(PARENTHESE_CLOSE_PING);

				ip = ip.substring(indexOpen + 1, indexClose);
			} else {
				// Get ip when after from
				ip = ip.substring(0, ip.indexOf("\n"));
				if (ip.contains(":")) {
					index = ip.indexOf(":");
				} else {
					index = ip.indexOf(" ");
				}

				ip = ip.substring(0, index);
			}
		} else {
			// Get ip when ping succeeded
			int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
			int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

			ip = ping.substring(indexOpen + 1, indexClose);
		}

		return ip;
	}

	private String parseIpToPingFromPing(String ping) {
		String ip = "";
		if (ping.contains(PING)) {
			// Get ip when ping succeeded
			int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
			int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

			ip = ping.substring(indexOpen + 1, indexClose);
		}

		return ip;
	}

	private String parseTimeFromPing(String ping) {
		String time = "";
		if (ping.contains(TIME_PING)) {
			int index = ping.indexOf(TIME_PING);

			time = ping.substring(index + 5);
			index = time.indexOf(" ");
			time = time.substring(0, index);
		}

		return time;
	}

	/**
	 * Check for connectivity (wifi and mobile)
	 * 
	 * @return true if there is a connectivity, false otherwise
	 */
	public boolean hasConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
