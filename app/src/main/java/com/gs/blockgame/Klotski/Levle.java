package com.gs.blockgame.Klotski;

import android.support.v4.util.ArrayMap;
import android.widget.RelativeLayout;
import com.gs.blockgame.Klotski.utils.Utils;

/**
 * Created by mi on 16-12-28.
 */

public class Levle {

    private int mlevle;

    static final private String[][] leave1 = new String[][]
    {
        {Utils.HZ, Utils.CC, Utils.CC, Utils.ZY},
        {Utils.HZ, Utils.CC, Utils.CC, Utils.ZY},
        {Utils.MC, Utils.GY, Utils.GY, Utils.ZF},
        {Utils.MC, Utils.SB2, Utils.SB3, Utils.ZF},
        {Utils.SB1, null    , null    , Utils.SB4},
    };

    public Levle(int levle)
    {
        mlevle = levle;
    }

    private void setLevle(int childwidth, int childheight, ArrayMap<String, KlotskiRole> roles)
    {
        switch(mlevle)
        {
            case 1:
                break;
            default :
                break;
        }
    }

    public String[][] getLeaveTable() {
        switch (mlevle) {
            case 1:
                return leave1;
            default:
                return null;
        }
    }

    public int getlevle() {
        return mlevle;
    }
}
