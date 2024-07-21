package com.uaoanlao.uaoangsyplayer.View;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.listener.GSYStateUiListener;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYTextureRenderView;
import com.uaoanlao.uaoangsyplayer.R;


/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer{
    public boolean issuo=false; //锁定判断
    public float speed=3.0f; //倍速
    public float noneSpeed=1.0f; //正常倍速
    public boolean isFull=false;

    public EmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        Uaoaninit(context);
    }

    public EmptyControlVideo(Context context) {
        super(context);
        Uaoaninit(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        Uaoaninit(context);
    }

    /*@Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }*/
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

    public void Uaoaninit(final Context context){
        //final View view= LayoutInflater.from(context).inflate(getLayoutId(),this);
        setGSYStateUiListener(new GSYStateUiListener() {
            @Override
            public void onStateChanged(int i) {
                if (i==3){
                    //缓冲
                }
                if (i==2){
                    //播放
                }
                if (i==5){
                    //暂停
                }
            }
        });
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);

        if (isFull){
            //不给触摸快进，如果需要，屏蔽下方代码即可
            mChangePosition = false;
            //不给触摸音量，如果需要，屏蔽下方代码即可
            mChangeVolume = false;

            //不给触摸亮度，如果需要，屏蔽下方代码即可
            mBrightness = false;
        }/*else {
            //不给触摸快进，如果需要，屏蔽下方代码即可
            mChangePosition = true;
            //不给触摸音量，如果需要，屏蔽下方代码即可
            mChangeVolume = true;

            //不给触摸亮度，如果需要，屏蔽下方代码即可
            mBrightness = true;
        }*/
    }


    @Override
    protected void onClickUiToggle(MotionEvent e) {
        super.onClickUiToggle(e);
       //点击事件
        onClickUiToggle.onClick();
    }

    @Override
    protected void touchLongPress(MotionEvent e) {
        super.touchLongPress(e);
        //长按
        ontouchLongPress.onTouchLongPress();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //取消长按
        if (ev.getAction()==1){
            onDispatchTouchEvent.onDispatchTouchEvent();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void touchDoubleUp(MotionEvent e) {
        super.touchDoubleUp(e);
        //不需要双击暂停
    }

    //画面比例
    public void setShowType(int type){
        GSYVideoType.setShowType(type);
        this.changeTextureViewShowType();
    }

    //解码
    public void setDeCode(boolean is){
        if (is){
            //硬解码
            GSYVideoType.enableMediaCodec();
            GSYVideoType.enableMediaCodecTexture();
            this.changeTextureViewShowType();
        }else {
            //软解码
            GSYVideoType.disableMediaCodec();
            GSYVideoType.disableMediaCodecTexture();
            this.changeTextureViewShowType();
        }
    }

    //点击事件
    public interface OnClickUiToggle{
        void onClick();
    }
    public void setOnClickUiToggle(OnClickUiToggle onClickUiToggle){
        this.onClickUiToggle=onClickUiToggle;
    }
    private OnClickUiToggle onClickUiToggle;

    //长按
    public interface OntouchLongPress{
        void onTouchLongPress();
    }
    public void setOntouchLongPress(OntouchLongPress ontouchLongPress){
        this.ontouchLongPress=ontouchLongPress;
    }
    private OntouchLongPress ontouchLongPress;

    //取消长按
    public interface  OnDispatchTouchEvent{
        void onDispatchTouchEvent();
    }
    public void setOnDispatchTouchEvent(OnDispatchTouchEvent onDispatchTouchEvent){
        this.onDispatchTouchEvent=onDispatchTouchEvent;
    }
    private OnDispatchTouchEvent onDispatchTouchEvent;


}
