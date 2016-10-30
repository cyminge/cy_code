package com.cy.frame.downloader.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cy.R;
import com.cy.global.BaseApplication;
import com.cy.global.InitialWatchDog;
import com.cy.utils.Utils;

public class GameDialog extends Dialog {

    private Resources mResources;
    protected Activity mActivity;

    protected Button mPositiveButton;
    private Button mNegativeButton;

    private ListView mListView;

    private final GameDialogParams mDialogParams;

    public GameDialog(Context context) {
        super(context, R.style.Dialog);
        mActivity = (Activity) context;
        mDialogParams = new GameDialogParams(this);
        mResources = context.getResources();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.dialog);
        setGravity(Gravity.BOTTOM);
        mDialogParams.apply();
    }

    public void setGravity(int gravity) {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        window.setAttributes(wlp);
    }

    public void updateParams() {
        mDialogParams.apply();
    }

    @Override
    public void show() {
        if (isContextInvalid() || isShowing()) {
            return;
        }

        super.show();
    }

    @Override
    public void dismiss() {
        if (isContextInvalid() || !isShowing()) {
            return;
        }

        super.dismiss();
    }

    private boolean isContextInvalid() {
        return mActivity == null || mActivity.isFinishing();
    }

    public void setTitle(CharSequence title) {
        mDialogParams.mTitleText = title;
    }

    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    @Override
    public void setContentView(View contentView) {
        if (!mDialogParams.mIsListContent) {
            mDialogParams.mContentView = contentView;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(layoutResID, null);
        setContentView(contentView);
    }

    public void setContentViewAlign(int contentAlign) {
        mDialogParams.mContentAlign = contentAlign;
    }

    public void setMessage(int messageId) {
        setMessage(getContext().getString(messageId));
    }

    public void setMessage(CharSequence message) {
        mDialogParams.mMessageText = message;
    }

    public void setSingleChoiceItems(int itemsId, int checkedItem, final OnItemClickListener listener) {
        setSingleChoiceItems(mResources.getStringArray(itemsId), checkedItem, listener);
    }

    public void setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnItemClickListener listener) {
        setListContent();
        mDialogParams.mSingleListItems = new SingleChoiceListItems(items, checkedItem, listener);
    }

    public void updateSingleChoiceItem(int checkedItem) {
        if (mDialogParams.mSingleListItems != null) {
            mDialogParams.mSingleListItems.mCheckedItem = checkedItem;
        }
    }

    public void setListItems(int itemsId, final OnItemClickListener listener) {
        setListContent();
        mDialogParams.mSingleListItems = new SingleListItems(mResources.getStringArray(itemsId), listener);
    }

    private void setListContent() {
        mDialogParams.mContentView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.single_list, null);
        mListView = (ListView) mDialogParams.mContentView.findViewById(R.id.single_list);
        mDialogParams.mIsListContent = true;
    }

    public void setPositiveButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setButton(mResources.getString(textId), onClickListener, DialogInterface.BUTTON_POSITIVE);
    }

    public void setPositiveButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
        setButton(text, onClickListener, DialogInterface.BUTTON_POSITIVE);
    }

    public void setNegativeButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setButton(mResources.getString(textId), onClickListener, DialogInterface.BUTTON_NEGATIVE);
    }

    public void setNegativeButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
        setButton(text, onClickListener, DialogInterface.BUTTON_NEGATIVE);
    }

    private void setButton(CharSequence text, final OnClickListener onClickListener, final int which) {
        mDialogParams.mButtonParams.add(new SetButtonParam(text, onClickListener, which));
    }

    public class GameDialogParams {

        public ArrayList<SetButtonParam> mButtonParams = new ArrayList<SetButtonParam>();
        public CharSequence mTitleText;
        public CharSequence mMessageText;
        public View mContentView;
        public boolean mIsListContent = false;
        public SingleListItems mSingleListItems;
        public int mContentAlign = RelativeLayout.CENTER_HORIZONTAL;

        private GameDialog mDialog;

        public GameDialogParams(GameDialog dialog) {
            mDialog = dialog;
        }

        private void apply() {
            if (mTitleText != null) {
                TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
                title.setText(mTitleText);
            } else {
                TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
                title.setVisibility(View.GONE);
            }

            if (mMessageText != null) {
                TextView message = (TextView) mDialog.findViewById(R.id.dialog_message);
                message.setVisibility(View.VISIBLE);
                message.setText(mMessageText);
                message.setMovementMethod(new ScrollingMovementMethod());
            }

            FrameLayout contentLayout = (FrameLayout) mDialog.findViewById(R.id.dialog_content_layout);
            if (mContentAlign != RelativeLayout.CENTER_HORIZONTAL) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                        .getLayoutParams();
                layoutParams.addRule(mContentAlign);
                contentLayout.setLayoutParams(layoutParams);
            }
            if (mContentView != null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                contentLayout.addView(mContentView, layoutParams);
            }

            if (mIsListContent) {
                mListView.setAdapter(mDialogParams.mSingleListItems.mBaseAdapter);
                mListView.setOnItemClickListener(mDialogParams.mSingleListItems.mItemClickListener);
            }

            if (hasButton()) {
                mPositiveButton = (Button) mDialog.findViewById(R.id.positive_button);
                mNegativeButton = (Button) mDialog.findViewById(R.id.negative_button);
                LinearLayout buttonBarLayout = (LinearLayout) mDialog.findViewById(R.id.button_layout);
                if (buttonBarLayout.getVisibility() != View.VISIBLE) {
                    buttonBarLayout.setVisibility(View.VISIBLE);
                }

                setButtons();
            }
        }

        private boolean hasButton() {
            return !mButtonParams.isEmpty();
        }

        private void setButtons() {
            for (SetButtonParam param : mButtonParams) {
                setButton(param.mText, param.mOnClickListener, param.mWhich);
            }
            mButtonParams.clear();
            if (!Utils.isLollipop()) {
                setButtonOldAttribute();
            }
        }

        private boolean isMultiButton() {
            return mButtonParams.size() > 1;
        }

        private void setButton(CharSequence text, final OnClickListener onClickListener, final int which) {
            Button targetBtn = getTargetBtn(which);
            targetBtn.setVisibility(View.VISIBLE);
            targetBtn.setText(text);
            targetBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onClickListener != null) {
                        onClickListener.onClick(GameDialog.this, which);
                    }
                }
            });
            if (!isMultiButton() && !Utils.isLollipop()) {
                targetBtn.setBackgroundResource(R.drawable.dialog_single_button_background);
            }
        }

        private Button getTargetBtn(int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    return mPositiveButton;
                case DialogInterface.BUTTON_NEGATIVE:
                    return mNegativeButton;
                default:
                    return null;
            }
        }
    }

    private static class SetButtonParam {
        private CharSequence mText;
        private OnClickListener mOnClickListener;
        private int mWhich;

        public SetButtonParam(CharSequence text, OnClickListener onClickListener, int which) {
            this.mText = text;
            this.mOnClickListener = onClickListener;
            this.mWhich = which;
        }
    }

    private class SingleListItems {
        private CharSequence[] mItemText;
        private OnItemClickListener mOnClickListener;
        protected int mCheckedItem = 0;
        private BaseAdapter mBaseAdapter = new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ListViewHolder holder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.single_item, null);
                    holder = new ListViewHolder();
                    holder.mText = (TextView) convertView.findViewById(R.id.single_text);
                    holder.mChoiceButton = (ImageView) convertView.findViewById(R.id.single_choice_button);
                    convertView.setTag(holder);
                } else {
                    holder = (ListViewHolder) convertView.getTag();
                }
                initHolder(holder, position);
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return mItemText.length;
            }
        };

        private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                if (mCheckedItem != position) {
                    mCheckedItem = position;
                    InitialWatchDog.post(new Runnable() {
                        @Override
                        public void run() {
                            mBaseAdapter.notifyDataSetChanged();
                        }
                    });
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(av, view, position, id);
                }
            }
        };

        public SingleListItems(CharSequence[] items, final OnItemClickListener listener) {
            mItemText = items;
            mOnClickListener = listener;
        }

        protected void initHolder(ListViewHolder holder, int position) {
            holder.mText.setText(mItemText[position]);
        }

    }

    private class SingleChoiceListItems extends SingleListItems {

        public SingleChoiceListItems(int itemsId, int checkedItem, OnItemClickListener listener) {
            this(mResources.getStringArray(itemsId), checkedItem, listener);
        }

        public SingleChoiceListItems(CharSequence[] items, int checkedItem, OnItemClickListener listener) {
            super(items, listener);
            mCheckedItem = checkedItem;
        }

        @Override
        protected void initHolder(ListViewHolder holder, int position) {
            super.initHolder(holder, position);
            if (mCheckedItem == position) {
                holder.mChoiceButton.setBackgroundResource(R.drawable.single_choice_down);
            } else {
                holder.mChoiceButton.setBackgroundResource(R.drawable.single_choice);
            }
        }
    }

    public static class ChoiceOnClickListener implements AdapterView.OnItemClickListener {

        private int mChoice = 0;

        public ChoiceOnClickListener(int choice) {
            mChoice = choice;
        }

        public void setChoice(int choice) {
            mChoice = choice;
        }

        public int getChoice() {
            return mChoice;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mChoice = position;
        }
    }

    private static class ListViewHolder {
        private TextView mText;
        private ImageView mChoiceButton;
    }

    private void setButtonOldAttribute() {
        mPositiveButton.setBackgroundResource(R.drawable.dialog_positive_button_background_old);
        mNegativeButton.setBackgroundResource(R.drawable.dialog_negative_button_background_old);

        ColorStateList positiveColors = mResources.getColorStateList(R.color.positive_button_color_old);
        mPositiveButton.setTextColor(positiveColors);

        ColorStateList negativeColors = mResources.getColorStateList(R.color.negative_button_color_old);
        mNegativeButton.setTextColor(negativeColors);
    }
}
