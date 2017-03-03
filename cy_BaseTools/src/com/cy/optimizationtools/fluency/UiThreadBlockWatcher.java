package com.cy.optimizationtools.fluency;

import android.os.Looper;
import android.util.Printer;

public class UiThreadBlockWatcher {
    public static void install(final long timeBound) {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                if (x.contains(">>>>> Dispatching to ")) {
                    MessageDispatchTimeStackPrinter.getInstance(timeBound).startPrint();
                } else if (x.contains("<<<<< Finished to ")) {
                    MessageDispatchTimeStackPrinter.getInstance(timeBound).stopPrint();
                }
            }
        });
    }
}
