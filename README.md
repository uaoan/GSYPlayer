
```
//播放器单独布局传入播放器变量 则传入父控件布局
player.setVideoLayout(player);


//设置播放链接
String source1 = "http://43.248.129.14:15223/m3u8_cache/m3u8/62d22999ba487d228bc965fc135c7014.m3u8";
player.setUp(source1, false, "唐朝诡事录第二季-第一集");


//设置播放内核
PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式
PlayerFactory.setPlayManager(AliPlayerManager.class);//aliplay 内核模式

//开始播放
player.startPlayLogic();

//暂停播放
player.onVideoPause();

//恢复播放
player.onVideoResume();

//恢复播放（是否有进度变化）
player.onVideoResume(false);

//销毁
player.onVideoReleaseAllVideos();


//返回按下
@Override
    public void onBackPressed() {
        if (!player.onBackPressed()) {
            super.onBackPressed();
        }
    }



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
```
