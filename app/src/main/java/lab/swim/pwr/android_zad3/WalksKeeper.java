package lab.swim.pwr.android_zad3;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Rafał on 2018-03-16.
 */

public class WalksKeeper {
    private static WalksKeeper instance = null;
    private List<Walk> mWalksList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WalksKeeper() {
        mWalksList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            mWalksList.add(new Walk("Walk no. " + i, (i+5)%60+":" + i%60, String.valueOf(ThreadLocalRandom.current().nextInt(1000))));
        }
    }

    public List<Walk> getWalksList() {
        return mWalksList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static WalksKeeper getInstance() {
        if (instance == null) {
            instance = new WalksKeeper();
        }
        return instance;
    }
}
