package tk.andrielson.carrinhos.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.Contract;

import io.fabric.sdk.android.Fabric;

public final class CarrinhosApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Contract(pure = true)
    public static boolean ehTeste() {
        //noinspection ConstantConditions
        return (BuildConfig.DESENVOLVIMENTO || BuildConfig.TESTE);
    }

    @Contract(pure = true)
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initStetho();
        initFabric();
        DI.inicializaCoisas();
    }

    private void initStetho() {
        if (ehTeste()) Stetho.initializeWithDefaults(this);
    }

    private void initFabric() {
        if (!ehTeste()) Fabric.with(this, new Answers());
    }
}