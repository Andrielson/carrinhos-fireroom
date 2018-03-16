package tk.andrielson.carrinhos.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.Contract;

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
    }

    private void initStetho() {
        if (ehTeste()) Stetho.initializeWithDefaults(this);
    }
}
/*FIXME
https://firebaseopensource.com/projects/firebase/firebaseui-android/firestore/README.md
https://github.com/firebase/FirebaseUI-Android/tree/master/app/src/main/java/com/firebase/uidemo/database/firestore

 */