package com.uaoanlao.uaoangsyplayer.NetSpeed;

import android.os.Handler;
import android.widget.TextView;

import com.uaoanlao.uaoangsyplayer.View.EmptyControlVideo;

public class NetSpeedText{
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning = false; // 用于标记循环是否正在运行

    public NetSpeedText(final EmptyControlVideo player, final TextView netspeed) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里编写每秒要执行的代码
                netspeed.setText(player.getNetSpeedText());
                if (isRunning) {
                    handler.postDelayed(this, 1000); // 延迟 1 秒后再次执行
                }
            }
        };
    }

    public void start() {
        isRunning = true;
        handler.post(runnable);
    }

    public void stop() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }
}