package com.knealq.user.nfccardread.cards.pboc;

import android.content.res.Resources;

import com.knealq.user.nfccardread.R;
import com.knealq.user.nfccardread.tech.Iso7816;

import java.util.ArrayList;

/**
 * Created by user on 7/5/17.
 */

public class ShenZhenTong extends PbocCard {
    private final static byte[] DFN_SRV =
            { (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Z', (byte) 'T' };

    private ShenZhenTong(Iso7816.Tag tag, Resources res){
        super(tag);
        name = res.getString(R.string.name_szt);
    }

    @SuppressWarnings("unchecked")
    final static ShenZhenTong load(Iso7816.Tag tag, Resources res){

        /*--------------------------------------------------------------*/
        // select PSF (1PAY.SYS.DDF01)
        /*--------------------------------------------------------------*/
        if (tag.selectByName(DFN_PSE).isOkey())
        {

            Iso7816.Response INFO, CASH;

            /*--------------------------------------------------------------*/
            // select Main Application
            /*--------------------------------------------------------------*/
            if (tag.selectByName(DFN_SRV).isOkey()){

                /*--------------------------------------------------------------*/
                // read card info file, binary (21)
                //
                /*--------------------------------------------------------------*/
                INFO = tag.readBinary(SFI_EXTRA);

                /*--------------------------------------------------------------*/
                // read balance
                /*--------------------------------------------------------------*/
                CASH = tag.getBalance(true);

                /*--------------------------------------------------------------*/
                // read log file, record (24)
                /*--------------------------------------------------------------*/
                ArrayList<byte[]> LOG = readLog(tag, SFI_LOG);

                /*--------------------------------------------------------------*/
                // build result string
                /*--------------------------------------------------------------*/
                final ShenZhenTong ret = new ShenZhenTong(tag, res);
                ret.parseBalance(CASH);
                ret.parseInfo(INFO, 4, true);
                ret.parseLog(LOG);

                return ret;
            }
        }

        return null;
    }
}
