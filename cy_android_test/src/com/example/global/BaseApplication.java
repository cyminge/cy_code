package com.example.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.example.HookHelper;

@SuppressLint("NewApi") public class BaseApplication extends Application {

    private static BaseApplication mBaseApplication = null;
    private static Context mBaseAppContext = null;

    public static Context getAppContext() {
        return mBaseAppContext;
    }

    public static BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();

        mBaseApplication = this;
        mBaseAppContext = mBaseApplication.getApplicationContext();
        
        HookHelper.init(getApplicationContext());

        Log.e("cyTest", "启动应用进程 !!! start");
        
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
//        try {
//            String proName = getCurProcessName(this);
//            if (null != proName) {
//                if ((proName.contains(":remote"))) { // 百度地图
//                    return;
//                } else if (proName.contains("daemon")) { // 守护进程
//                    Log.i("cyTest", "进入守护进程中？？");
//                    return;
//                }
//            } // 非主进程(百度和守护进程)不需要后面的初始化
//
//        } catch (SecurityException secExp) {
//            secExp.printStackTrace();
//
//            String msg = secExp.getMessage();
//            if (null == msg || msg.contains("Isolated")) {
//                return;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // mBaseApplication = this;
        // mBaseAppContext = mBaseApplication.getApplicationContext();

//        Log.e("cyTest", "启动应用进程 !!! end");
        
//        flag = true;
//		mThread.start();
    }
    
    boolean flag = false;
	
	Thread mThread = new Thread() {
		public void run() {
			while (flag) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
//					getCPUUsageRate();
//					Log.e("cyTest", "-->"+getCPUUsageRate());
					Log.e("cyTest", "-->isCPUBusy:"+isCPUBusy());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		};
	};
	
	public boolean isCPUBusy() {
		String cpuUsageRate = getCPUUsageRate();
		Log.e("cyTest", "-->cpuUsageRate:"+cpuUsageRate);
		if(cpuUsageRate.isEmpty()) {
			return false;
		}
		
		int rate;
		try {
			rate = Integer.parseInt(cpuUsageRate);
			if(rate >= 50) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
    
    private String getCPUUsageRate() {
        String result = "";
        try {
			Process p = Runtime.getRuntime().exec("top -n 1");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((result = br.readLine()) != null) {
			    if (result.trim().length() < 1) {
			        continue;
			    } else {
			        String[] cpuusr = result.split(",");
			        String user = ((cpuusr[0].trim().split("\\s"))[1].toString());
			        result = user.substring(0, user.length()-1);
			        return result;
			    }
			}
		} catch (Exception e) {
			Log.w("cyTest", "get cpu usage rate error");
		}
        return result;
    }
	
//	/**
//     * 获取cpu使用率
//     */
//    public static void getCPUUsageRate() throws Exception {
//        String result;
//        // top -m 10:取前十条，间隔刷新为1s. Top –d 1:间隔刷新为1s. Top -n 1:刷新次数为1
//        Process p = Runtime.getRuntime().exec("top -m 5");
////        Process p = Runtime.getRuntime().exec("top -n 1");
//        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        while ((result = br.readLine()) != null) {
//            if (result.trim().length() < 1) {
//                continue;
//            } else {
////                String[] CPUusr = result.split(",");
////                for(int i=0; i< CPUusr.length; i++) {
////                	System.out.println("-->"+CPUusr[i]);
////                }
////                System.out.println();
////                String user = ((CPUusr[0].trim().split("\\s"))[1].toString());
////                String system = ((CPUusr[1].trim().split("\\s"))[1].toString());
////                return "1.用户进程：" + user + ", " + "2.系统进程：" + system + ".";
//            	Log.e("cyTest", "-->result:"+result);
//            	return;
//            }
//        }
////        return "NO CPU Message !!";
//    }
	
//	/**
//     * 获取cpu使用率
//     */
//    public static String getCPUUsageRate() throws Exception {
//        String result;
//        // top -m 10:取前十条，间隔刷新为1s. Top –d 1:间隔刷新为1s. Top -n 1:刷新次数为1
////        Process p = Runtime.getRuntime().exec("top -m 5");
//        Process p = Runtime.getRuntime().exec("top -n 1");
//        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        while ((result = br.readLine()) != null) {
//            if (result.trim().length() < 1) {
//                continue;
//            } else {
//                String[] CPUusr = result.split(",");
//                for(int i=0; i< CPUusr.length; i++) {
//                	System.out.println("-->"+CPUusr[i]);
//                }
//                System.out.println();
//                String user = ((CPUusr[0].trim().split("\\s"))[1].toString());
//                String system = ((CPUusr[1].trim().split("\\s"))[1].toString());
//                return "1.用户进程：" + user + ", " + "2.系统进程：" + system + ".";
//            }
//        }
//        return "NO CPU Message !!";
//    }
    
    /**
     * 获取cpu信息
     * 
     * @return
     */
    public static String fetchCPUInfo() {
        String result = null;
        try {
            String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
            result = run(args, "/system/bin/");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 获取cpu信息
     * 
     * @param cmd
     * @param workdirectory
     * @return
     * @throws IOException
     */
    private static String run(String[] cmd, String workdirectory) throws IOException {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            InputStream in = null;
            // 设置一个路径
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1)
                    result = result + new String(re);
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }

    private List<BaseActivity> mActivities = Collections.synchronizedList(new LinkedList<BaseActivity>());

    public void addActivity(BaseActivity activity, boolean canAsBootActivity) {
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public BaseActivity getActivity(int location) {
        if (mActivities.isEmpty() || location > (mActivities.size() - 1) || location < 0) {
            return null;
        }
        return mActivities.get(location);
    }

    public BaseActivity getTopActivity() {
        return getActivity(mActivities.size() - 1);
    }

    public int getSize() {
        return mActivities.size();
    }

    public void exit() {
        List<Activity> activities = new LinkedList<Activity>();
        activities.addAll(mActivities);
        try {
            for (Activity activity : activities) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
        }
    }
}
