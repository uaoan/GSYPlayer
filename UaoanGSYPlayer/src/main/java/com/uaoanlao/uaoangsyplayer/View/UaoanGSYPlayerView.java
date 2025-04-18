package com.uaoanlao.uaoangsyplayer.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYStateUiListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.uaoanlao.uaoangsyplayer.Loading.AVLoadingIndicatorView;
import com.uaoanlao.uaoangsyplayer.NetSpeed.NetSpeedText;
import com.uaoanlao.uaoangsyplayer.R;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanDeCodeRecyclerViewAdapter;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanLongSpeedRecyclerViewAdapter;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanRecyclerView;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanRecyclerViewAdapter;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanTimeFinishRecyclerViewAdapter;
import com.uaoanlao.uaoangsyplayer.RecyclerView.UaoanVideoTypeRecyclerViewAdapter;
import com.uaoanlao.uaoangsyplayer.TimeUtils;
import com.uaoanlao.uaoangsyplayer.mmkv.UaoanMMKV;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * TODO: document your custom view class.
 */
public class UaoanGSYPlayerView extends LinearLayout {
    private EmptyControlVideo player;
    private LinearLayout loading; //加载框
    private TextView tv_netSpeed; //网速文本
    private NetSpeedText netSpeedText; //网速
    private ImageView /*全屏图像*/ping ,/*暂停/播放*/play ;
    private Activity activity;
    private RelativeLayout layout;
    private View vw;
    private int height=0;
    private TextView title; //标题
    private TextView time,times;
    private SeekBar seekBar;
    private int getProgress=0;
    private LinearLayout top_layoutview;
    private ImageView shangyiji,xiayiji; //上一集，下一集
    private TextView xuanji; //选集
    private ImageView suo; //锁定
    private TextView speed_long; //长按倍速显示文本

    private UaoanMMKV mmkv=new UaoanMMKV();
    private LinearLayout topLayout,bottomLayout; //顶部布局 底部布局
    private boolean isClick=false;
    private ImageView xiaochuang,touping,shezhi; //小窗 投屏 设置
    private TextView speed_text; //倍速
    private String getVideoType="默认显示"; //画面比例
    private String decode="软解码"; //解码
    private LinearLayout err_layout; //播放失败
    private TextView err_button;
    private LinearLayout finish_layout; //播放完成
    private CardView finish_card2;
    private TextView finish_button1,finish_button2; //重播  播放下一集
    private String setupURL="",setupTITLE="";
    private boolean setupCACHEWITHPLAY=false;
    public int COLOR=Color.RED;
    private AVLoadingIndicatorView avLoadingIndicatorView; //加载动画
    public String LOADING="LineSpinFadeLoaderIndicator";
    private ImageView elec100_0; //电量
    private TextView elecText;
    private TextView getTime; //时间

    private int timefinish=0; //定时关闭

    private AlertDialog.Builder dialog;
    private AlertDialog tc;

    // 保存底部控件的可见性设置
    private int[] savedBottomVisibility = new int[]{VISIBLE, VISIBLE, VISIBLE};

    public UaoanGSYPlayerView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public UaoanGSYPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public UaoanGSYPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    //传入播放器或者播放器上层控件
    public void setVideoLayout(View view){
        this.vw=view;
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        this.activity= (Activity) context;
        final View view= LayoutInflater.from(context).inflate(R.layout.sample_uaoan_g_s_y_player_view,null);
        addView(view);
        layout=view.findViewById(R.id.layout);
        player=view.findViewById(R.id.player);
        loading=view.findViewById(R.id.loading);
        tv_netSpeed=view.findViewById(R.id.tv_netSpeed);
        netSpeedText=new NetSpeedText(player,tv_netSpeed);
        time=view.findViewById(R.id.starttime);
        times=view.findViewById(R.id.endtime);
        topLayout=view.findViewById(R.id.top_layout);
        bottomLayout=view.findViewById(R.id.bottom_layout);
        suo=view.findViewById(R.id.suo);
        err_layout=view.findViewById(R.id.err_layout);
        err_button=view.findViewById(R.id.err_button);
        finish_layout=view.findViewById(R.id.finish_layout);
        finish_button1=view.findViewById(R.id.finish_button1);
        finish_button2=view.findViewById(R.id.finish_button2);
        finish_card2=view.findViewById(R.id.finish_2);
        xiaochuang=view.findViewById(R.id.xiaochuang);
        touping=view.findViewById(R.id.touping);
        shezhi=view.findViewById(R.id.shezhi);
        speed_text=view.findViewById(R.id.speed_text);
        speed_long=view.findViewById(R.id.speed_long);
        shangyiji=view.findViewById(R.id.shangyiji);
        xiayiji=view.findViewById(R.id.xiayiji);
        xuanji=view.findViewById(R.id.xuanji);
        getTime=view.findViewById(R.id.time);
        elec100_0=view.findViewById(R.id.elec100_0);
        elecText=view.findViewById(R.id.elec);
        avLoadingIndicatorView=view.findViewById(R.id.loading_indicator);
        top_layoutview=view.findViewById(R.id.top_layout_view); //顶部功能按钮
        setFullEndVisibility(); //在竖屏状态下隐藏横屏显示的按钮
        setTopBottonVisibility(GONE);
        mmkv.init(activity); //初始化mmkv

        //长按倍速
        if (mmkv.isContainsMMKVKey("longspeed")){
            float num1 = Float.parseFloat(mmkv.getMMKVString("longspeed").replace("x",""));
            player.speed=num1;
        }else {
            mmkv.setMMKV("longspeed",player.speed+"x");
        }

        //解码
        if (mmkv.isContainsMMKVKey("decode")){
            if (mmkv.getMMKVString("decode").equals("硬解码")){
                //硬解码
                player.setDeCode(true);
                mmkv.setMMKV("decode","硬解码");
            }else {
                //软解码
                player.setDeCode(false);
                mmkv.setMMKV("decode","软解码");
            }
        }else {
            //软解码
            player.setDeCode(false);
            mmkv.setMMKV("decode","软解码");
        }

        //弹窗初始化
        dialog=new AlertDialog.Builder(activity);
        tc=dialog.create();

        //小窗
        xiaochuang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onWindowVideoButton.onClick(v);
            }
        });

        //投屏
        touping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onScreenVideoButton.onClick(v);
            }
        });
        //上一集
        shangyiji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpVideoButton.onClick(v);
            }
        });
        //下一集
        xiayiji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownVideoButton.onClick(v);
            }
        });
        //选集
        xuanji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeleVideoButton.onClick(v);
            }
        });

        //设置
        shezhi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopBottonVisibility(GONE);
                suo.setVisibility(GONE);
                isClick = false;
                View vw=inflate(activity,R.layout.dialog_menus,null);
                tc.setView(vw);
                tc.show();
                tc.getWindow().setGravity(Gravity.RIGHT);
                tc.getWindow().setBackgroundDrawable(new ColorDrawable());
                LinearLayout lin=vw.findViewById(R.id.layout);
                lin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tc.dismiss();
                    }
                });

                //倍速
                setSpeedList(tc,vw);

                //画面比例
                setVideoType(tc,vw);

                //长按倍速
                setLongSpeedList(tc,vw);

                //跳过片头
                setSkipTitle(vw);

                //跳过片尾
                setSkipEnd(vw);

                //解码
                setDecodeList(tc,vw);

                //定时关闭
                //setTimeFinishList(tc,vw);


            }
        });



        //倍速
        speed_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopBottonVisibility(GONE);
                suo.setVisibility(GONE);
                isClick = false;
                View vw=inflate(activity,R.layout.speed_dialog,null);
                final AlertDialog tc=new AlertDialog.Builder(activity)
                        .setView(vw)
                        .show();
                tc.getWindow().setGravity(Gravity.RIGHT);
                tc.getWindow().setBackgroundDrawable(new ColorDrawable());
                LinearLayout lin=vw.findViewById(R.id.layout);
                lin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tc.dismiss();
                    }
                });
                final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
                RecyclerView recyclerView=vw.findViewById(R.id.recycler);
                final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
                HashMap<String,Object> hashMap=new HashMap<>();
                ArrayList<String> strings=new ArrayList<>();
                strings.add("0.75x");
                strings.add("1.0x");
                strings.add("1.25x");
                strings.add("1.5x");
                strings.add("1.75x");
                strings.add("2.0x");
                strings.add("2.5x");
                strings.add("3.0x");
                for (int po=0;po<strings.size();po++){
                    hashMap=new HashMap<>();
                    hashMap.put("name",strings.get(po));
                    arrayList.add(hashMap);
                }
                uaoanRecyclerView.setAdapter(recyclerView, R.layout.speed_dialog_item, arrayList, new UaoanRecyclerView.OnRecyclerViewAdapter() {
                    @Override
                    public void bindView(UaoanRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                        TextView speed_name=holder.itemView.findViewById(R.id.title);
                        speed_name.setText(arrayList.get(position).get("name").toString());
                        if (data.get(position).get("name").toString().replace("x","").equals(""+player.noneSpeed)){
                            speed_name.setTextColor(Color.WHITE);
                        }else {
                            speed_name.setTextColor(Color.parseColor("#66ffffff"));
                        }
                        speed_name.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                float num1 = Float.parseFloat(data.get(position).get("name").toString().replace("x",""));
                                player.setSpeed(num1);
                                player.noneSpeed=num1;
                                tc.dismiss();
                            }
                        });
                    }
                }).setLinearLayoutManager(recyclerView,activity);


            }
        });


        //锁定
        suo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.issuo){
                    suo.setImageResource(R.mipmap.suos);
                    topLayout.setVisibility(VISIBLE);
                    bottomLayout.setVisibility(VISIBLE);
                    player.issuo=false;
                    player.isFull=false;
                }else {
                    suo.setImageResource(R.mipmap.suo);
                    topLayout.setVisibility(GONE);
                    bottomLayout.setVisibility(GONE);
                    player.issuo=true;
                    player.isFull=true;
                }
            }
        });

        seekBar=view.findViewById(R.id.seek_bar);
        ping=view.findViewById(R.id.ping);
        play=view.findViewById(R.id.play); //播放
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getCurrentState()==5){
                    //暂停
                    player.onVideoResume();
                }
                if (player.getCurrentState()==2){
                    //播放
                    player.onVideoPause();
                }
            }
        });
        title=view.findViewById(R.id.title);
        final ImageView finish=view.findViewById(R.id.finish); //返回按钮
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onBackPressed()){

                }
            }
        });
        //全屏切换按钮
        ping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    //全屏
                    height=vw.getHeight();
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    setParamsLayout(vw, ViewGroup.LayoutParams.MATCH_PARENT);
                    setFullStartVisibility();
                    // 隐藏状态栏
                    Window window = activity.getWindow();
                    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);  //横屏后自动旋转
                } else {
                    //退出全屏
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    setParamsLayout(vw, ViewGroup.LayoutParams.MATCH_PARENT,height);
                    // 恢复状态栏显示
                    Window window = activity.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setFullEndVisibility();
                    
                    // 恢复保存的底部控件可见性设置
                    shangyiji.setVisibility(savedBottomVisibility[0]);
                    xiayiji.setVisibility(savedBottomVisibility[1]);
                    xuanji.setVisibility(savedBottomVisibility[2]);
                    finish_card2.setVisibility(savedBottomVisibility[1]);
                }
                player.setRotateWithSystem(true);

            }
        });

        //进度回调
        player.setGSYVideoProgressListener(new GSYVideoProgressListener() {
            @Override
            public void onProgress(long l, long l1, long l2, long l3) {
                time.setText(TimeUtils.stringForTime(player.getCurrentPositionWhenPlaying()));
                times.setText(TimeUtils.stringForTime(player.getDuration()));
                seekBar.setMax((int)player.getDuration());
                seekBar.setProgress((int)player.getCurrentPositionWhenPlaying());
                //跳过片尾
                if (mmkv.isContainsMMKVKey("skipend")){
                    int to= (int) (player.getDuration()-player.getCurrentPositionWhenPlaying());
                    if (to<=mmkv.getMMKVInt("skipend")){
                        //player.setPlayPosition((int) player.getDuration());
                        player.seekTo(player.getDuration());
                    }
                }

            }
        });

        //状态回调
        player.setGSYStateUiListener(new GSYStateUiListener() {
            @Override
            public void onStateChanged(int i) {

                if (i==0){
                    loading.setVisibility(View.VISIBLE);
                    err_layout.setVisibility(GONE);
                    netSpeedText.start();
                    //跳过片头
                    if (mmkv.isContainsMMKVKey("skiptitle")) {
                        player.setSeekOnStart(mmkv.getMMKVInt("skiptitle"));
                    }



                }
                if (i==1){
                    //开始加载播放
                    err_layout.setVisibility(GONE);
                    loading.setVisibility(View.VISIBLE);
                    netSpeedText.start();
                }
                if (i==3){
                    //缓冲
                    err_layout.setVisibility(GONE);
                    loading.setVisibility(View.VISIBLE);
                    netSpeedText.start();

                }
                if (i==2){
                    //播放
                    //skiptitle
                    loading.setVisibility(View.GONE);
                    finish_layout.setVisibility(GONE);
                    netSpeedText.stop();
                    play.setImageResource(R.mipmap.stop);
                    err_layout.setVisibility(GONE);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            getProgress=progress;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            player.seekTo(getProgress);

                        }
                    });

                }
                if (i==5){
                    //暂停
                    play.setImageResource(R.mipmap.start);
                }
                if (i==7){
                    //播放失败
                    err_layout.setVisibility(VISIBLE);
                    err_button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setUp(setupURL,setupCACHEWITHPLAY,setupTITLE);
                            player.startPlayLogic();
                            err_layout.setVisibility(GONE);
                            //onErrorButtonClick.onClick(v);
                        }
                    });

                }
                if (i==6){
                    //播放完毕
                    finish_layout.setVisibility(VISIBLE);
                    if (timefinish==1){
                        activity.finish(); //如果定时是播放本集则就关闭页面
                    }
                        finish_button1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //重播
                                setUp(setupURL, setupCACHEWITHPLAY, setupTITLE);
                                player.startPlayLogic();
                                finish_layout.setVisibility(GONE);
                            }
                        });
                        finish_button2.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //播放下一集
                                finish_layout.setVisibility(VISIBLE);
                                onDownVideoButton.onClick(v);
                            }
                        });
                    onVideoPlayComplete.onComplete();
                }
                //Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 调整触摸滑动快进的比例
         *
         * @param seekRatio 滑动快进的比例，默认1。数值越大，滑动的产生的seek越小
         */
        player.setSeekRatio(2);


        //修改拖动条 进度条背景
        player.setDialogVolumeProgressBar(activity.getDrawable(R.drawable.video_volume_progress_bg));
        player.setDialogProgressBar(activity.getDrawable(R.drawable.video_dialog_progress));
        setDialogProgressTextColor(Color.WHITE,Color.LTGRAY);

        /**

        播放器点击事件

        */
        player.setOnClickUiToggle(new EmptyControlVideo.OnClickUiToggle() {
            @Override
            public void onClick() {
                if (isClick){
                    setTopBottonVisibility(GONE);
                    suo.setVisibility(GONE);
                    isClick=false;
                }else {
                    if (player.issuo==false) {
                        setTopBottonVisibility(VISIBLE);
                        //获取电量
                        int elec=getElecBatteryLevel(activity);
                        elecText.setText(elec+"%");
                        if (elec>=90){
                            elec100_0.setImageResource(R.mipmap.elec100);
                        }
                        if (elec<90){
                            elec100_0.setImageResource(R.mipmap.elec80);
                        }
                        if (elec<60){
                            elec100_0.setImageResource(R.mipmap.elec50);
                        }
                        if (elec<30){
                            elec100_0.setImageResource(R.mipmap.elec20);
                        }
                        if (elec<10){
                            elec100_0.setImageResource(R.mipmap.elec0);
                        }

                        //获取时间
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String currentTime = sdf.format(new Date());
                        getTime.setText(currentTime);
                    }
                    suo.setVisibility(VISIBLE);

                    new CountDownTimer(4000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (isClick==false){
                                cancel();
                            }
                        }

                        @Override
                        public void onFinish() {
                            setTopBottonVisibility(GONE);
                            suo.setVisibility(GONE);
                            isClick = false;
                        }
                    }.start();

                        isClick = true;

                }

            }
        });

        /**
        播放器长按事件
        */
        player.setOntouchLongPress(new EmptyControlVideo.OntouchLongPress() {
            @Override
            public void onTouchLongPress() {
                player.isFull=true;
                player.setSpeed(player.speed);
                speed_long.setVisibility(VISIBLE);
                speed_long.setText(player.getSpeed()+"倍速中...");
            }
        });


       /**
        播放器取消长按事件
         */
       player.setOnDispatchTouchEvent(new EmptyControlVideo.OnDispatchTouchEvent() {
           @Override
           public void onDispatchTouchEvent() {
               if (player.issuo==false) {
                   player.isFull = false;
               }
               player.setSpeed(player.noneSpeed);
               speed_long.setVisibility(GONE);
           }
       });



    }

    //字体颜色
    public void setDialogProgressTextColor(int color,int colors){
        player.setDialogProgressColor(color,colors);
    }

    //隐藏顶部控件
    //小窗 投屏 设置
    public void setTopLayoutVisibility(int ks1,int ks2,int ks3){
        xiaochuang.setVisibility(ks1);
        touping.setVisibility(ks2);
        shezhi.setVisibility(ks3);
    }

    //隐藏底部控件
    //上一集 下一集 选集
    public void setBottomLayoutVisibility(int ks1,int ks2,int ks3){
        shangyiji.setVisibility(ks1);
        xiayiji.setVisibility(ks2);
        xuanji.setVisibility(ks3);
        finish_card2.setVisibility(ks2);
        
        // 保存设置以便全屏时应用
        savedBottomVisibility = new int[]{ks1, ks2, ks3};
    }


    //加载动画
    public void setLoading(int id){
        if (id==1) {
            avLoadingIndicatorView.setIndicator("BallBeatIndicator");
        }
        if (id==2) {
            avLoadingIndicatorView.setIndicator("BallClipRotateIndicator");
        }
        if (id==3) {
            avLoadingIndicatorView.setIndicator("BallClipRotateMultipleIndicator");
        }
        if (id==4) {
            avLoadingIndicatorView.setIndicator("BallClipRotatePulseIndicator");
        }
        if (id==5) {
            avLoadingIndicatorView.setIndicator("BallGridBeatIndicator");
        }
        if (id==6) {
            avLoadingIndicatorView.setIndicator("BallGridPulseIndicator");
        }
        if (id==7) {
            avLoadingIndicatorView.setIndicator("BallPulseIndicator");
        }
        if (id==8) {
            avLoadingIndicatorView.setIndicator("BallPulseRiseIndicator");
        }
        if (id==9) {
            avLoadingIndicatorView.setIndicator("BallPulseSyncIndicator");
        }
        if (id==10) {
            avLoadingIndicatorView.setIndicator("BallRotateIndicator");
        }
        if (id==11) {
            avLoadingIndicatorView.setIndicator("BallScaleIndicator");
        }
        if (id==12) {
            avLoadingIndicatorView.setIndicator("BallScaleMultipleIndicator");
        }
        if (id==13) {
            avLoadingIndicatorView.setIndicator("BallScaleRippleIndicator");
        }
        if (id==14) {
            avLoadingIndicatorView.setIndicator("BallScaleRippleMultipleIndicator");
        }
        if (id==15) {
            avLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");
        }
        if (id==16) {
            avLoadingIndicatorView.setIndicator("BallTrianglePathIndicator");
        }
        if (id==17) {
            avLoadingIndicatorView.setIndicator("BallZigZagDeflectIndicator");
        }
        if (id==18) {
            avLoadingIndicatorView.setIndicator("BallZigZagIndicator");
        }
        if (id==19) {
            avLoadingIndicatorView.setIndicator("CubeTransitionIndicator");
        }
        if (id==20) {
            avLoadingIndicatorView.setIndicator("LineScaleIndicator");
        }
        if (id==21) {
            avLoadingIndicatorView.setIndicator("LineScalePartyIndicator");
        }
        if (id==22) {
            avLoadingIndicatorView.setIndicator("LineScalePulseOutIndicator");
        }
        if (id==23) {
            avLoadingIndicatorView.setIndicator("LineScalePulseOutRapidIndicator");
        }
        if (id==24) {
            avLoadingIndicatorView.setIndicator("LineSpinFadeLoaderIndicator");
        }
        if (id==25) {
            avLoadingIndicatorView.setIndicator("PacmanIndicator");
        }
        if (id==26) {
            avLoadingIndicatorView.setIndicator("SemiCircleSpinIndicator");
        }
        if (id==27) {
            avLoadingIndicatorView.setIndicator("SquareSpinIndicator");
        }
        if (id==28) {
            avLoadingIndicatorView.setIndicator("TriangleSkewSpinIndicator");
        }

    }

    //播放失败
    public void setOnErrorButtonClick(OnErrorButtonClick onErrorButtonClick){
        this.onErrorButtonClick=onErrorButtonClick;
    }
    public interface OnErrorButtonClick{
        void onClick(View vw);
    }
    private OnErrorButtonClick onErrorButtonClick;
    public void setOnErrorVisibility(int ks){
        err_layout.setVisibility(ks);
    }

   //播放完成
   public interface OnVideoPlayComplete{
        void onComplete();
   }
   public void setOnVideoPlayComplete(OnVideoPlayComplete onVideoPlayComplete){
        this.onVideoPlayComplete=onVideoPlayComplete;
   }
   private OnVideoPlayComplete onVideoPlayComplete;


    //上一集
    public interface OnUpVideoButton{
        void onClick(View vw);
    }
    public void setOnUpVideoButton(OnUpVideoButton onUpVideoButtonn){
        this.onUpVideoButton=onUpVideoButtonn;
    }
    private OnUpVideoButton onUpVideoButton;


    //下一集
    public interface OnDownVideoButton{
        void onClick(View vw);
    }
    public void setOnDownVideoButton(OnDownVideoButton onDownVideoButtonn){
        this.onDownVideoButton=onDownVideoButtonn;
    }
    private OnDownVideoButton onDownVideoButton;

    //选集
    public interface OnSeleVideoButton{
        void onClick(View vw);
    }
    public void setOnSeleVideoButton(OnSeleVideoButton onSeleVideoButtonn){
        this.onSeleVideoButton=onSeleVideoButtonn;
    }
    private OnSeleVideoButton onSeleVideoButton;

    //投屏
    public interface OnScreenVideoButton{
        void onClick(View vw);
    }
    public void setOnScreenVideoButton(OnScreenVideoButton onScreenVideoButtonn){
        this.onScreenVideoButton=onScreenVideoButtonn;
    }
    private OnScreenVideoButton onScreenVideoButton;

    //小窗
    public interface OnWindowVideoButton{
        void onClick(View vw);
    }
    public void setOnWindowVideoButton(OnWindowVideoButton onWindowVideoButtonn){
        this.onWindowVideoButton=onWindowVideoButtonn;
    }
    private OnWindowVideoButton onWindowVideoButton;


    //全屏显示控件
    private void setFullStartVisibility(){
        top_layoutview.setVisibility(VISIBLE);
        // 使用保存的设置而不是直接设置为VISIBLE
        shangyiji.setVisibility(savedBottomVisibility[0]);
        xiayiji.setVisibility(savedBottomVisibility[1]);
        xuanji.setVisibility(savedBottomVisibility[2]);
        finish_card2.setVisibility(savedBottomVisibility[1]);
        speed_text.setVisibility(VISIBLE);
    }

    //竖屏隐藏控件
    private void setFullEndVisibility(){
        top_layoutview.setVisibility(GONE);
        // 不在这里直接修改底部控件的可见性，只隐藏顶部控件
        // 底部控件的可见性将由onBackPressed或退出全屏方法单独处理
        speed_text.setVisibility(GONE);
    }

    //点击显示隐藏按钮
    private void setTopBottonVisibility(int i){
            topLayout.setVisibility(i);
            bottomLayout.setVisibility(i);

    }

    //设置播放
    public void setUp(String url, boolean cacheWithPlay, String title){
        player.setUp(url,cacheWithPlay,title);
        setupURL=url;
        setupTITLE=title;
        setupCACHEWITHPLAY=cacheWithPlay;
        this.title.setText(title);
    }
    public void setIsTouchWiget(boolean isTouchWiget) {
        player.setIsTouchWiget(isTouchWiget);
    }

    //开始播放
    public void startPlayLogic() {
        player.startPlayLogic();
    }

    //暂停
    public void onVideoPause(){
        player.onVideoPause();
    }

    //继续播放
    public void onVideoResume(){
        player.onVideoResume();
    }
    //继续播放
    public void onVideoResume(boolean is){
        player.onVideoResume(is);
    }
    //销毁
    public void onVideoReleaseAllVideos(){
        GSYVideoManager.releaseAllVideos();
    }

    public EmptyControlVideo getPlayerView(){
        return player;
    }

    public boolean onBackPressed(){
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.finish();
            return false;
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setParamsLayout(vw, ViewGroup.LayoutParams.MATCH_PARENT,height);
            // 恢复状态栏显示
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setFullEndVisibility();
            
            // 恢复保存的底部控件可见性设置
            shangyiji.setVisibility(savedBottomVisibility[0]);
            xiayiji.setVisibility(savedBottomVisibility[1]);
            xuanji.setVisibility(savedBottomVisibility[2]);
            finish_card2.setVisibility(savedBottomVisibility[1]);
            
            return true;
        }
    }


    //获取电量
    public static int getElecBatteryLevel(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }



    private void setParamsLayout(View linearLayout,int point){
        ViewGroup.LayoutParams layoutParams1 = linearLayout.getLayoutParams();
        layoutParams1.width = point;
        layoutParams1.height=point;
        linearLayout.setLayoutParams(layoutParams1);
    }
    private void setParamsLayout(View linearLayout,int point,int point1){
        ViewGroup.LayoutParams layoutParams1 = linearLayout.getLayoutParams();
        layoutParams1.width = point;
        layoutParams1.height=point1;
        linearLayout.setLayoutParams(layoutParams1);
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, activity.getResources().getDisplayMetrics());
    }

    //倍速列表
    private void setSpeedList(final AlertDialog tc,final View vw){
        final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
        final RecyclerView recyclerView=vw.findViewById(R.id.recycler1);
        recyclerView.setNestedScrollingEnabled(false);
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        strings.add("0.75x");
        strings.add("1.0x");
        strings.add("1.25x");
        strings.add("1.5x");
        strings.add("1.75x");
        strings.add("2.0x");
        strings.add("2.5x");
        strings.add("3.0x");
        for (int po=0;po<strings.size();po++){
            hashMap=new HashMap<>();
            hashMap.put("name",strings.get(po));
            arrayList.add(hashMap);
        }
        uaoanRecyclerView.setAdapter(recyclerView, R.layout.dialog_menu_items, arrayList, new UaoanRecyclerView.OnRecyclerViewAdapter() {
            @Override
            public void bindView(UaoanRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                TextView speed_name=holder.itemView.findViewById(R.id.title);
                speed_name.setText(arrayList.get(position).get("name").toString());
                if (arrayList.get(position).get("name").toString().replace("x","").equals(""+player.noneSpeed)){
                    speed_name.setTextColor(Color.WHITE);
                }else {
                    speed_name.setTextColor(Color.parseColor("#66ffffff"));
                }
                speed_name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float num1 = Float.parseFloat(data.get(position).get("name").toString().replace("x",""));
                        player.setSpeed(num1);
                        player.noneSpeed=num1;
                        recyclerView.getAdapter().notifyDataSetChanged();
                        //tc.dismiss();
                    }
                });
            }
        }).setHorizontalLinearLayoutManager(recyclerView,activity);

    }

    //画面比例
    private void setVideoType(final AlertDialog tc,final View vw){
        final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
        final RecyclerView recyclerView=vw.findViewById(R.id.recycler2);
        recyclerView.setNestedScrollingEnabled(false);
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        strings.add("默认显示");
        strings.add("16:9");
        strings.add("全屏裁剪");
        strings.add("全屏拉伸");
        strings.add("4:3");

        for (int po=0;po<strings.size();po++){
            hashMap=new HashMap<>();
            hashMap.put("name",strings.get(po));
            arrayList.add(hashMap);
        }
        uaoanRecyclerView.setVideoTypeAdapter(recyclerView, R.layout.dialog_menu_items, arrayList, new UaoanRecyclerView.OnVideoTypeRecyclerViewAdapter() {
            @Override
            public void bindView(UaoanVideoTypeRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                TextView speed_name=holder.itemView.findViewById(R.id.title);
                speed_name.setText(data.get(position).get("name").toString());
                if (arrayList.get(position).get("name").toString().equals(getVideoType)){
                    speed_name.setTextColor(Color.WHITE);
                }else {
                    speed_name.setTextColor(Color.parseColor("#66ffffff"));
                }
                speed_name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==0){
                            //默认显示比例
                            player.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
                            getVideoType="默认显示";
                        }
                        if (position==1){
                            //16:9
                            player.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
                            getVideoType="16:9";
                        }
                        if (position==2){
                            //全屏裁减显示，为了显示正常 CoverImageView 建议使用FrameLayout作为父布局
                            player.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
                            getVideoType="全屏裁剪";
                        }
                        if (position==3){
                            //全屏拉伸显示，使用这个属性时，surface_container建议使用FrameLayout
                            player.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
                            getVideoType="全屏拉伸";
                        }
                        if (position==4){
                            //4:3
                            player.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
                            getVideoType="4:3";
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        //tc.dismiss();
                    }
                });
            }
        }).setHorizontalLinearLayoutManager(recyclerView,activity);
    }

    //倍速列表
    private void setLongSpeedList(final AlertDialog tc,final View vw){
        final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
        final RecyclerView recyclerView=vw.findViewById(R.id.recycler3);
        recyclerView.setNestedScrollingEnabled(false);
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        strings.add("2.0x");
        strings.add("3.0x");
        strings.add("3.25x");
        strings.add("3.5x");
        strings.add("4.0x");
        strings.add("4.25x");
        strings.add("4.75x");
        strings.add("5.0x");
        for (int po=0;po<strings.size();po++){
            hashMap=new HashMap<>();
            hashMap.put("name",strings.get(po));
            arrayList.add(hashMap);
        }
        uaoanRecyclerView.setLongSpeedAdapter(recyclerView, R.layout.dialog_menu_items, arrayList, new UaoanRecyclerView.OnLongSpeedRecyclerViewAdapter() {
            @Override
            public void bindView(UaoanLongSpeedRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                TextView speed_name=holder.itemView.findViewById(R.id.title);
                speed_name.setText(arrayList.get(position).get("name").toString());
                if (arrayList.get(position).get("name").toString().replace("x","").equals(""+player.speed)){
                    speed_name.setTextColor(Color.WHITE);
                }else {
                    speed_name.setTextColor(Color.parseColor("#66ffffff"));
                }
                speed_name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float num1 = Float.parseFloat(data.get(position).get("name").toString().replace("x",""));
                        mmkv.setMMKV("longspeed",num1+"x");
                        player.speed=num1;
                        recyclerView.getAdapter().notifyDataSetChanged();
                        //tc.dismiss();
                    }
                });
            }
        }).setHorizontalLinearLayoutManager(recyclerView,activity);

    }

    //跳过片头
    private void setSkipTitle(final View vw){

        final SeekBar seekBar1=vw.findViewById(R.id.seek_bar1);
        final TextView time=vw.findViewById(R.id.time1);

        if (mmkv.isContainsMMKVKey("skiptitle")){
            seekBar1.setProgress(mmkv.getMMKVInt("skiptitle"));
            time.setText(TimeUtils.stringForTime(mmkv.getMMKVInt("skiptitle")));
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time.setText(TimeUtils.stringForTime(progress));
                mmkv.setMMKV("skiptitle",progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //跳过片尾
    private void setSkipEnd(final View vw){

        final SeekBar seekBar1=vw.findViewById(R.id.seek_bar2);
        final TextView time=vw.findViewById(R.id.time2);

        if (mmkv.isContainsMMKVKey("skipend")){
            seekBar1.setProgress(mmkv.getMMKVInt("skipend"));
            time.setText(TimeUtils.stringForTime(mmkv.getMMKVInt("skipend")));
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time.setText(TimeUtils.stringForTime(progress));
                mmkv.setMMKV("skipend",progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //解码列表
    private void setDecodeList(final AlertDialog tc,final View vw){
        final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
        final RecyclerView recyclerView=vw.findViewById(R.id.recycler4);
        recyclerView.setNestedScrollingEnabled(false);
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        strings.add("软解码");
        strings.add("硬解码");
        for (int po=0;po<strings.size();po++){
            hashMap=new HashMap<>();
            hashMap.put("name",strings.get(po));
            arrayList.add(hashMap);
        }
        uaoanRecyclerView.setDeCodeAdapter(recyclerView, R.layout.dialog_menu_items, arrayList, new UaoanRecyclerView.OnDeCodeRecyclerViewAdapters() {
            @Override
            public void bindView(UaoanDeCodeRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                TextView speed_name=holder.itemView.findViewById(R.id.title);
                speed_name.setText(arrayList.get(position).get("name").toString());
                if (arrayList.get(position).get("name").toString().equals(mmkv.getMMKVString("decode"))){
                    speed_name.setTextColor(Color.WHITE);
                }else {
                    speed_name.setTextColor(Color.parseColor("#66ffffff"));
                }
                speed_name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mmkv.setMMKV("decode",data.get(position).get("name").toString());
                        decode=data.get(position).get("name").toString();
                        if (position==0){
                            //软解码
                            player.setDeCode(false);
                        }
                        if (position==1){
                            //硬解码
                            player.setDeCode(true);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        //tc.dismiss();
                    }
                });
            }
        }).setHorizontalLinearLayoutManager(recyclerView,activity);

    }

/*

    //定时关闭
    private void setTimeFinishList(final AlertDialog tc,final View vw){
        final UaoanRecyclerView uaoanRecyclerView=new UaoanRecyclerView();
        final RecyclerView recyclerView=vw.findViewById(R.id.recycler5);
        final TextView textView=vw.findViewById(R.id.timefinish_text);
        textView.setText("定时关闭 (不开启)");
        recyclerView.setNestedScrollingEnabled(false);
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        strings.add("不开启");
        strings.add("播完本集");
        strings.add("30分钟");
        strings.add("60分钟");
        strings.add("90分钟");
        for (int po=0;po<strings.size();po++){
            hashMap=new HashMap<>();
            hashMap.put("name",strings.get(po));
            arrayList.add(hashMap);
        }
        uaoanRecyclerView.setTimeFinishAdapter(recyclerView, R.layout.dialog_menu_items, arrayList, new UaoanRecyclerView.OnTimeFinishRecyclerViewAdapter() {
            @Override
            public void bindView(UaoanTimeFinishRecyclerViewAdapter.ViewHolder holder, final ArrayList<HashMap<String, Object>> data, final int position) {
                final TextView names=holder.itemView.findViewById(R.id.title);
                names.setText(arrayList.get(position).get("name").toString());
                if (position==timefinish){
                    names.setTextColor(Color.WHITE);
                }else {
                    names.setTextColor(Color.parseColor("#66ffffff"));
                }

                names.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timefinish=position;
                        recyclerView.getAdapter().notifyDataSetChanged();

                        if (position==0){
                            textView.setText("定时关闭 (不开启)");
                        }
                        if (position==1){
                            textView.setText("定时关闭 (播完本集)");
                        }
                        if (position==2){
                            //30分钟（1800000）
                            startCountDown(textView,1800000);

                        }
                        if (position==3){
                            //60分钟（3600000）
                            startCountDown(textView,3600000);

                        }
                        if (position==4){
                            //90分钟（5400000）
                            startCountDown(textView,5400000);

                        }




                    }
                });
            }
        }).setHorizontalLinearLayoutManager(recyclerView,activity);

    }
*/



    private void startCountDown(final TextView textView, int totalMillis) {
        
    }

    public void setProgressColor(){
        //修改拖动条 进度条背景
        player.setDialogVolumeProgressBar(activity.getDrawable(R.drawable.video_volume_progress_bg));
        player.setDialogProgressBar(activity.getDrawable(R.drawable.video_dialog_progress));
        setDialogProgressTextColor(Color.WHITE,Color.LTGRAY);
    }

}