package com.pmid.printbluetooth;


import android.app.Application;
import android.os.Bundle;

import com.mazenrashed.printooth.Printooth;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
    }
}
