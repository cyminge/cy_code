package com.cy.main2;
public class ChatItem implements Comparable<ChatItem> {

	public boolean sendPreproc = false;
	public Long mId = 0L;
	public Long mContentId;

	public long mTargetId;
	public long mCrowdId;

	public long mMsgId;

	@Override
	public boolean equals(Object o) {
		if (null != o && o instanceof ChatItem) {
			ChatItem bean = (ChatItem) o;
			return mMsgId == bean.mMsgId;
		}
		return false;
	}

	public int compareTo(ChatItem o) {
		if (mMsgId > o.mMsgId) {
			return (int) (mMsgId - o.mMsgId);
		}
		if (mMsgId < o.mMsgId) {
			return (int) (mMsgId - o.mMsgId);
		}
		return 0;
	}

}
