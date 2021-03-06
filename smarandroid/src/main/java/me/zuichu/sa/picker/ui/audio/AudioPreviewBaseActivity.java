package me.zuichu.sa.picker.ui.audio;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.zuichu.sa.picker.AudioPicker;
import me.zuichu.sa.picker.Utils;
import me.zuichu.sa.picker.adapter.audio.AudioPageAdapter;
import me.zuichu.sa.picker.bean.AudioItem;
import me.zuichu.sa.picker.ui.image.ImageBaseActivity;
import me.zuichu.sa.picker.view.ViewPagerFixed;
import me.zuichu.smarandroid.R;

/**
 * 谭东增加扩充
 * QQ 852041173
 */
public abstract class AudioPreviewBaseActivity extends ImageBaseActivity {

    protected AudioPicker audioPicker;
    protected ArrayList<AudioItem> mAudioItems;      //跳转进AudioPreviewFragment的音频文件夹
    protected int mCurrentPosition = 0;              //跳转进AudioPreviewFragment时的序号，第几个音频
    protected TextView mTitleCount;                  //显示当前yinpin1的位置  例如  5/31
    protected ArrayList<AudioItem> selectedAudios;   //所有已经选中的音频音频
    protected View content;
    protected View topBar;
    protected ViewPagerFixed mViewPager;
    protected AudioPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_preview);

        mCurrentPosition = getIntent().getIntExtra(AudioPicker.EXTRA_SELECTED_AUDIO_POSITION, 0);
        mAudioItems = (ArrayList<AudioItem>) getIntent().getSerializableExtra(AudioPicker.EXTRA_AUDIO_ITEMS);
        audioPicker = AudioPicker.getInstance();
        selectedAudios = audioPicker.getSelectedAudios();

        //初始化控件
        content = findViewById(R.id.content);

        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
        topBar = findViewById(R.id.top_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
            params.topMargin = Utils.getStatusHeight(this);
            topBar.setLayoutParams(params);
        }
        topBar.findViewById(R.id.btn_ok).setVisibility(View.GONE);
        topBar.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleCount = (TextView) findViewById(R.id.tv_des);

        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        mAdapter = new AudioPageAdapter(this, mAudioItems);
        mAdapter.setPhotoViewClickListener(new AudioPageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view) {
                onAudioSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);

        //初始化当前页面的状态
        mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mAudioItems.size()));
    }

    /**
     * 单击时，隐藏头和尾
     */
    public abstract void onAudioSingleTap();
}