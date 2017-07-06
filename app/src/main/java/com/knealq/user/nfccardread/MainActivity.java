package com.knealq.user.nfccardread;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.knealq.user.nfccardread.cards.CardManager;
import com.knealq.user.nfccardread.utils.LogUtils;
import com.knealq.user.nfccardread.utils.Utils;

import org.xml.sax.XMLReader;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements Html.ImageGetter, Html.TagHandler {

    private static final String TAG_MAINACTIVITY_NFCCARD = "MainActivity";

    private Drawable spliter;

    @Override
    public Drawable getDrawable(String source) {
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity getDrawable().");

        final Drawable ret;
        if (source.startsWith("spliter")) {
            LogUtils.e(TAG_MAINACTIVITY_NFCCARD, " getDrawable,spliter = " + spliter);
            if (null == spliter) {
                final int w = mResource.getDisplayMetrics().widthPixels;
                final int h = (int) (mResource.getDisplayMetrics().densityDpi / 72f + 0.5f);

                final int[] pix = new int[w * h];
                Arrays.fill(pix, mResource.getColor(R.color.bg_default_color));
                spliter = new BitmapDrawable(Bitmap.createBitmap(pix, w, h, Bitmap.Config.ARGB_8888));
                spliter.setBounds(0, 3 * h, w, 4 * h);
            }
            ret = spliter;

        } else if (source.startsWith("icon_main")) {
            LogUtils.e(TAG_MAINACTIVITY_NFCCARD, " getDrawable, icon_main ");
            ret = mResource.getDrawable(R.mipmap.ic_app_main);

            final String[] params = source.split(",");
            final float f = mResource.getDisplayMetrics().densityDpi / 72f;
            final float w = Utils.parseInt(params[1], 10, 16) * f + 0.5f;
            final float h = Utils.parseInt(params[2], 10, 16) * f + 0.5f;
            ret.setBounds(0, 0, (int) w, (int) h);

        } else {
            ret = null;
        }

        return ret;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity handleTag().");

        if (!opening && "version".equals(tag)) {
            try {
                output.append(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException nnfe) {
                nnfe.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private enum ContentType {
        HINT, DATA, MSG
    }

    private TextView mTextMessage;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    private Resources mResource;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onCreate().");
        setContentView(R.layout.activity_main);

        mResource = this.getResources();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onPause(). disableForegroundDispatch");

        if (null != mNfcAdapter) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onStart().");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onStop().");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onDestroy().");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onRestart().");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onResume(). enableForegroundDispatch");

        if (null != mNfcAdapter) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, CardManager.INTENT_FILTERS, CardManager.TECH_LISTS);
        }

        final CharSequence textShown = mTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(textShown) || mTextMessage.getTag() == ContentType.HINT) {
            showHintText();
        }
    }

    private void showHintText() {
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity showHintText().");

        final String tips_home;
        if (null == mNfcAdapter) {
            tips_home = mResource.getString(R.string.tips_not_support_nfc_feature);
        } else if (mNfcAdapter.isEnabled()) {
            tips_home = mResource.getString(R.string.tips_nfc_enabled);
        } else {
            tips_home = mResource.getString(R.string.tips_nfc_disabled);
        }


        final int hintPadding = mResource.getDimensionPixelSize(R.dimen.show_data_text_margin);
        mTextMessage.setPadding(hintPadding, hintPadding, hintPadding, hintPadding);
        mTextMessage.setTextSize(mResource.getDimensionPixelSize(R.dimen.text_size_middle));
        mTextMessage.setTextColor(mResource.getColor(R.color.text_tip_color));
        mTextMessage.setGravity(Gravity.CENTER_VERTICAL);
        mTextMessage.setTag(ContentType.HINT);
        mTextMessage.setText(Html.fromHtml(tips_home));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity onNewIntent().");

        final Parcelable parcelableData = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (null != parcelableData) {
            showDataText(CardManager.loadingTag(parcelableData, mResource));
        } else {
            showHintText();
        }
    }

    private void showDataText(String dataText) {
        LogUtils.e(TAG_MAINACTIVITY_NFCCARD, "NfcCardRead MainActivity showDataText() : " + dataText);

        if(TextUtils.isEmpty(dataText)){
            showHintText();
            return;
        }

        final int hintPadding = mResource.getDimensionPixelSize(R.dimen.show_data_text_margin);
        mTextMessage.setPadding(hintPadding, hintPadding, hintPadding, hintPadding);
        mTextMessage.setTextSize(mResource.getDimensionPixelSize(R.dimen.text_size_middle));
        mTextMessage.setTextColor(mResource.getColor(R.color.text_tip_color));
        mTextMessage.setGravity(Gravity.NO_GRAVITY);
        mTextMessage.setTag(ContentType.DATA);
        mTextMessage.setText(Html.fromHtml(dataText));
    }
}
