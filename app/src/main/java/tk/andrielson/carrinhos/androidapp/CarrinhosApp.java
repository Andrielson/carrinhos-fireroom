package tk.andrielson.carrinhos.androidapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Andrielson on 28/02/2018.
 */

public final class CarrinhosApp extends Application {
    public static boolean ehTeste() {
        //noinspection ConstantConditions
        return (BuildConfig.DESENVOLVIMENTO || BuildConfig.TESTE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
    }

    private void initStetho() {
        if (ehTeste()) Stetho.initializeWithDefaults(this);
    }
}
