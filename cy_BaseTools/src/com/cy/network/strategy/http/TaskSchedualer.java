package com.cy.network.strategy.http;

import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cy.tracer.Tracer;
import com.cy.utils.StringUtil;
import com.loopj.android.http.AsyncHttpClient;

import de.greenrobot.event.EventBus;

public class TaskSchedualer {
	private static final String TAG_SCHEDUAL_PREF = "com.veclink.network.strategy.http.TaskSchedualer";
	private static Context mContext;
	private static EventBus mEventBus = new EventBus();
	private static BaseAdapterSession mCurrSession = null;
	private static AsyncHttpClient mAsyncClient = null;

	/* for singleton pattern */
	private static TaskSchedualer mInstance = null;

	private TaskSchedualer() {
	}

	public static TaskSchedualer getInstance() {
		if (mInstance == null) {
			mInstance = new TaskSchedualer();
		}
		return mInstance;
	}

	/**
	 * Initial http task schedualer
	 */
	public void init(Context context) {
		TaskSchedualer.mContext = context;
		mEventBus.register(this);
	}

	/**
	 * 
	 * @param obj
	 */
	public void onEvent(Object obj) {

		if (mCurrSession != null) {
			try {
				Method getError = obj.getClass().getMethod("getError");

				if (null != getError) {
					String errorString = (String) getError.invoke(obj);

					if (StringUtil.equalNoThrow(errorString, "0")) {
						Tracer.i("TaskSchedualer", "TaskSchedualer execute one task done");
						deleteSession(mCurrSession);
					} else {
						Tracer.w("TaskSchedualer", "receive a event with network error");
					}
				} else {
					Tracer.e("TaskSchedualer", "receive a event that dosen't have getError function");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				endExecute();
			}
		} else {
			Tracer.e("TaskSchedualer", "TaskSchedualer event bus receive a invalid object");
		}
	}

	/**
	 * enqueue a http session that will be executed.
	 * 
	 * @param context
	 * @param httpSession
	 */
	public static void enqueueTask(BaseAdapterSession httpSession) {
		Tracer.d("TaskSchedualer", "enqueue task" + httpSession.getTag());
		if (mCurrSession != null && StringUtil.equalNoThrow(mCurrSession.getTag(), httpSession.getTag())) {
			endExecute();
		}
		debugTaskSchedualer();
		saveSession(httpSession);
		debugTaskSchedualer();
	}

	/**
	 * Fetch a http task and scheduale. Execution must wrap and keep update mCurrSession.
	 * We must keep only one task in execute.
	 */
	public static void schedual(Context context) {
		Tracer.d("TaskSchedualer", "time to schedual ... ");
		SharedPreferences preference = context.getSharedPreferences(TAG_SCHEDUAL_PREF, Context.MODE_PRIVATE);
		@SuppressWarnings("unchecked")
		Map<String, String> leftTasks = (Map<String, String>) preference.getAll();

		/* if there has task to be execute */
		if (leftTasks != null && !leftTasks.isEmpty()) {
			String objString = leftTasks.entrySet().iterator().next().getValue();
			if (objString != null && objString.length() > 0) {
				Object obj = (BaseSession) StringUtil.stringToObject(objString);
				if (obj != null && obj instanceof BaseSession) {
					BaseAdapterSession session = (BaseAdapterSession) obj;
					
					Tracer.d("TaskSchedualer", "session's tag is : " + session.getTag());
					execute(session);
				}
			}
		}
	}

	private static void execute(BaseAdapterSession session) {
		mCurrSession = session;
		mCurrSession.setSpecifiedEventBus(mEventBus);
		mAsyncClient = SimpleHttpSchedualer.ansycSchedual(mContext, mCurrSession);
	}

	private static void endExecute() {
		if (mAsyncClient != null) {
			mAsyncClient.cancelRequests(mContext, true);
		}
		if (mCurrSession != null) {
			mCurrSession = null;
		}
	}

	/**
	 * enqueue session if session with same tag not exist in share preference
	 * if mCurrSession has same tag, cancel current quest and modify session
	 * 
	 * @param session
	 */
	private static void saveSession(BaseAdapterSession session) {
		SharedPreferences preference = mContext.getSharedPreferences(TAG_SCHEDUAL_PREF, Context.MODE_PRIVATE);
		Editor editor = preference.edit();

		String sameTask = preference.getString(session.getTag(), null);
		/* Remove item that match current tag */
		if (null != sameTask) {
			editor.remove(session.getTag());
		}
		editor.putString(session.getTag(), StringUtil.objectToString(session));
		editor.commit();
	}

	private static void deleteSession(BaseAdapterSession session) {
		Tracer.d("TaskSchedualer", "delete session");

		SharedPreferences preference = mContext.getSharedPreferences(TAG_SCHEDUAL_PREF, Context.MODE_PRIVATE);
		Editor editor = preference.edit();

		String sameTask = preference.getString(session.getTag(), null);
		/* Remove item that match current tag */
		if (null != sameTask) {
			editor.remove(session.getTag());
			editor.commit();
		} else {
			Tracer.e("TaskSchedualer", "delete a task failed");
		}

		debugTaskSchedualer();
	}

	private static void debugTaskSchedualer() {
		SharedPreferences preference = mContext.getSharedPreferences(TAG_SCHEDUAL_PREF, Context.MODE_PRIVATE);
		Tracer.d("TaskSchedualer", "current task count " + preference.getAll().size());
	}
}
