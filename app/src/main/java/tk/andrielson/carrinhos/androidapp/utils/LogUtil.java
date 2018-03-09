package tk.andrielson.carrinhos.androidapp.utils;

import android.util.Log;

import tk.andrielson.carrinhos.androidapp.BuildConfig;
import tk.andrielson.carrinhos.androidapp.CarrinhosApp;

/**
 * Created by Andrielson on 08/03/2018.
 */

public final class LogUtil {
    private LogUtil() {

    }
    public static void Log(String tag, String mensagem, int tipo) {
        switch (tipo) {
            case Log.DEBUG:
                if (CarrinhosApp.ehTeste())
                    Log.d(tag, mensagem);
                break;
            case Log.ERROR:
                Log.e(tag, mensagem);
                break;
            case Log.INFO:
                Log.i(tag, mensagem);
                break;
            case Log.VERBOSE:
                if (CarrinhosApp.ehTeste())
                    Log.v(tag, mensagem);
                break;
            case Log.WARN:
                Log.w(tag, mensagem);
                break;
        }
    }
    public static void Log(String tag, String mensagem, Throwable throwable, int tipo) {
        switch (tipo) {
            case Log.DEBUG:
                if (CarrinhosApp.ehTeste())
                    Log.d(tag, mensagem, throwable);
                break;
            case Log.ERROR:
                Log.e(tag, mensagem, throwable);
                break;
            case Log.INFO:
                Log.i(tag, mensagem, throwable);
                break;
            case Log.VERBOSE:
                if (CarrinhosApp.ehTeste())
                    Log.v(tag, mensagem, throwable);
                break;
            case Log.WARN:
                Log.w(tag, mensagem, throwable);
                break;
        }
    }
}
