package com.knealq.user.nfccardread.cards;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;

import com.knealq.user.nfccardread.cards.pboc.PbocCard;
import com.knealq.user.nfccardread.utils.LogUtils;

/**
 * Created by user on 7/5/17.
 */

public final class CardManager {

    private static final String TAG_CARDMANAGER_NFCCARD = "CardManager";
    public static String[][] TECH_LISTS;
    public static IntentFilter[] INTENT_FILTERS;

    static {
        try {
            TECH_LISTS = new String[][]{
                    {IsoDep.class.getName()},
                    {NfcV.class.getName()},
                    {NfcF.class.getName()},
            };

            INTENT_FILTERS = new IntentFilter[]{
                    new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")
            };
        } catch (IntentFilter.MalformedMimeTypeException e) {
            //do nothing
        }
    }


    public static String loadingTag(Parcelable parcelable, Resources res) {
        LogUtils.d(TAG_CARDMANAGER_NFCCARD, "loading, tag type-------------------");

        final Tag tag = (Tag) parcelable;
        final IsoDep isodep = IsoDep.get(tag);
        if (isodep != null) {
            LogUtils.d(TAG_CARDMANAGER_NFCCARD, "loading, IsoDep");
            return PbocCard.load(isodep, res);
        }

        final NfcV nfcv = NfcV.get(tag);
        if (nfcv != null) {
            LogUtils.d(TAG_CARDMANAGER_NFCCARD, "loading, NfcV");
            return VicinityCard.load(nfcv, res);
        }

        final NfcF nfcf = NfcF.get(tag);
        if (nfcf != null) {
            LogUtils.d(TAG_CARDMANAGER_NFCCARD, "loading, NfcF");
            return OctopusCard.load(nfcf, res);
        }

        LogUtils.d(TAG_CARDMANAGER_NFCCARD, "load nothing, tag type is not in NfcV NfcF and IsoDep--------------");
        return null;
    }

    private static final String SP = "<br/><img src=\"spliter\"/><br/>";

    public static String buildResult(String n, String i, String d, String x) {
        if (n == null)
            return null;

        final StringBuilder s = new StringBuilder();

        s.append("<br/><b>").append(n).append("</b>"); //n ´ÖÌå

        if (i != null)
            s.append(SP).append(i);

        if (d != null)
            s.append(SP).append(d);

        if (x != null)
            s.append(SP).append(x);

        return s.append("<br/><br/>").toString();
    }

}
