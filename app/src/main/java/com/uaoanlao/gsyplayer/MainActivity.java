package com.uaoanlao.gsyplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shuyu.aliplay.AliPlayerManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.uaoanlao.uaoangsyplayer.View.EmptyControlVideo;
import com.uaoanlao.uaoangsyplayer.View.UaoanGSYPlayerView;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class MainActivity extends AppCompatActivity {
    private UaoanGSYPlayerView player;
    private OrientationUtils orientationUtils;
    private Button exo,ijk,ali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player=findViewById(R.id.detail_player);

        player.setVideoLayout(player);
        exo=findViewById(R.id.exo);
        ijk=findViewById(R.id.ijk);
        ali=findViewById(R.id.ali);
       // player.inits(this);


        String source1 = "http://43.248.129.14:15223/m3u8_cache/m3u8/62d22999ba487d228bc965fc135c7014.m3u8";
        player.setUp(source1, false, "唐朝诡事录第二季-第一集");
        PlayerFactory.setPlayManager(AliPlayerManager.class);
        player.startPlayLogic();

        exo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXOPlayer内核，支持格式更多
                PlayerFactory.setPlayManager(Exo2PlayerManager.class);
                player.startPlayLogic();
            }
        });
        ijk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXOPlayer内核，支持格式更多
                PlayerFactory.setPlayManager(IjkPlayerManager.class);
                player.startPlayLogic();
            }
        });
        ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXOPlayer内核，支持格式更多
                PlayerFactory.setPlayManager(AliPlayerManager.class);
                player.startPlayLogic();
            }
        });


        //上一集按钮点击事件
        player.setOnUpVideoButton(new UaoanGSYPlayerView.OnUpVideoButton() {
            @Override
            public void onClick(View vw) {

            }
        });


        //下一集按钮点击事件
        player.setOnDownVideoButton(new UaoanGSYPlayerView.OnDownVideoButton() {
            @Override
            public void onClick(View vw) {

            }
        });

        //小窗按钮点击事件
        player.setOnWindowVideoButton(new UaoanGSYPlayerView.OnWindowVideoButton() {
            @Override
            public void onClick(View vw) {

            }
        });

        //投屏按钮点击事件
        player.setOnScreenVideoButton(new UaoanGSYPlayerView.OnScreenVideoButton() {
            @Override
            public void onClick(View vw) {

            }
        });

        //选集按钮点击事件
        player.setOnSeleVideoButton(new UaoanGSYPlayerView.OnSeleVideoButton() {
            @Override
            public void onClick(View vw) {

            }
        });

        //播放完毕执行代码
        player.setOnVideoPlayComplete(new UaoanGSYPlayerView.OnVideoPlayComplete() {
            @Override
            public void onComplete() {

            }
        });

    }

    @Override

    protected void onPause() {

        super.onPause();

        player.onVideoPause();

    }


    @Override

    protected void onResume() {

        super.onResume();

        player.onVideoResume(false);

    }


    @Override

    protected void onDestroy() {

        super.onDestroy();

        player.onVideoReleaseAllVideos();



    }


    @Override
    public void onBackPressed() {
        if (!player.onBackPressed()) {
            super.onBackPressed();
        }
    }


}