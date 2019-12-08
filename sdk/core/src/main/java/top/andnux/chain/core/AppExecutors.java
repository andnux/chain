package top.andnux.chain.core;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private final ExecutorService mDiskIO;
    private final ExecutorService mNetworkIO;
    private final ExecutorService mWorkIO;
    private final Executor mMainThread;

    private static volatile AppExecutors sAppExecutors = null;

    public static AppExecutors getInstance() {
        if (sAppExecutors == null) {
            synchronized (AppExecutors.class) {
                if (sAppExecutors == null) {
                    sAppExecutors = new AppExecutors();
                }
            }
        }
        return sAppExecutors;
    }

    public AppExecutors(ExecutorService diskIO, ExecutorService networkIO, ExecutorService workIO, Executor mainThread) {
        mDiskIO = diskIO;
        mNetworkIO = networkIO;
        mWorkIO = workIO;
        mMainThread = mainThread;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                Executors.newFixedThreadPool(1),
                new MainThreadExecutor());
    }

    public ExecutorService diskIO() {
        return mDiskIO;
    }

    public ExecutorService workIO() {
        return mWorkIO;
    }

    public ExecutorService networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
