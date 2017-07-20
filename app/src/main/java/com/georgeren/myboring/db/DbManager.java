package com.georgeren.myboring.db;

import com.georgeren.myboring.base.SAppApplication;
import com.litesuits.orm.BuildConfig;
import com.litesuits.orm.LiteOrm;

/**
 * Created by georgeRen on 2017/7/14.
 */

public class DbManager {
    private static volatile LiteOrm sLiteOrm;
    private static final String DB_NAME = "boring.db";

    private DbManager() {
    }
    public static LiteOrm getLiteOrm() {
        if (sLiteOrm == null) {
            synchronized (DbManager.class) {
                if (sLiteOrm == null) {
                    sLiteOrm = LiteOrm.newCascadeInstance(SAppApplication.getAppContext(), DB_NAME);
                    sLiteOrm.setDebugged(BuildConfig.DEBUG);
                }
            }
        }
        return sLiteOrm;
    }
}
