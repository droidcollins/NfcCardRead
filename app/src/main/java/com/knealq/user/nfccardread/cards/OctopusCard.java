package com.knealq.user.nfccardread.cards;

import android.content.res.Resources;
import android.nfc.tech.NfcF;

import com.knealq.user.nfccardread.R;
import com.knealq.user.nfccardread.tech.FeliCa;
import com.knealq.user.nfccardread.utils.Utils;

/**
 * Created by user on 7/5/17.
 */

public final class OctopusCard {
    private static final int SYS_SZT = 0x8005;
    private static final int SRV_SZT = 0x0118;
    private static final int SYS_OCTOPUS = 0x8008;
    private static final int SRV_OCTOPUS = 0x0117;

    static String load(NfcF tech, Resources res) {
        final FeliCa.Tag tag = new FeliCa.Tag(tech);

        /*--------------------------------------------------------------*/
        // check card system
        /*--------------------------------------------------------------*/

        final int system = tag.getSystemCode();
        final FeliCa.ServiceCode service;
        if (system == SYS_OCTOPUS)
            service = new FeliCa.ServiceCode(SRV_OCTOPUS);
        else if (system == SYS_SZT)
            service = new FeliCa.ServiceCode(SRV_SZT);
        else
            return null;

        tag.connect();

        /*--------------------------------------------------------------*/
        // read service data without encryption
        /*--------------------------------------------------------------*/

        final float[] data = new float[]{0, 0, 0};
        final int N = data.length;

        int p = 0;
        for (byte i = 0; p < N; ++i) {
            final FeliCa.ReadResponse r = tag.readWithoutEncryption(service, i);
            if (!r.isOkey())
                break;

            data[p++] = (Utils.toInt(r.getBlockData(), 0, 4) - 350) / 10.0f;
        }

        tag.close();

        /*--------------------------------------------------------------*/
        // build result string
        /*--------------------------------------------------------------*/

        final String name = parseName(system, res);
        final String info = parseInfo(tag, res);
        final String hist = parseLog(null, res);
        final String cash = parseBalance(data, p, res);

        return CardManager.buildResult(name, info, cash, hist);
    }

    private static String parseName(int system, Resources res) {
        if (system == SYS_OCTOPUS)
            return res.getString(R.string.name_octopuscard);

        if (system == SYS_SZT)
            return res.getString(R.string.name_szt_f);

        return null;
    }

    private static String parseInfo(FeliCa.Tag tag, Resources res) {
        final StringBuilder r = new StringBuilder();
        final String i = res.getString(R.string.lab_id);
        final String p = res.getString(R.string.lab_pmm);
        r.append("<b>").append(i).append("</b> ")
                .append(tag.getIDm().toString());
        r.append("<br />").append(p).append(' ')
                .append(tag.getPMm().toString());

        return r.toString();
    }

    private static String parseBalance(float[] value, int count, Resources res) {
        if (count < 1)
            return null;

        final StringBuilder r = new StringBuilder();
        final String s = res.getString(R.string.lab_balance);
        final String c = res.getString(R.string.lab_cur_hkd);

        r.append("<b>").append(s).append(" <font color=\"teal\">");

        for (int i = 0; i < count; ++i)
            r.append(Utils.toAmountString(value[i])).append(' ');

        return r.append(c).append("</font></b>").toString();
    }

    private static String parseLog(byte[] data, Resources res) {
        return null;
    }
}
