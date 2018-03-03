package tk.andrielson.carrinhos.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by Andrielson on 28/02/2018.
 */

public final class CarrinhosApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static boolean ehTeste() {
        //noinspection ConstantConditions
        return (BuildConfig.DESENVOLVIMENTO || BuildConfig.TESTE);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initStetho();
    }

    private void initStetho() {
        if (ehTeste()) Stetho.initializeWithDefaults(this);
    }
}
