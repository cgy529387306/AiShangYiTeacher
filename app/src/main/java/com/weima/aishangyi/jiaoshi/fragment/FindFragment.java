package com.weima.aishangyi.jiaoshi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mb.android.utils.PreferencesHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.ActivityClassListActivity;
import com.weima.aishangyi.jiaoshi.activity.NearbyActivity;
import com.weima.aishangyi.jiaoshi.activity.TalentCircleActivity;
import com.weima.aishangyi.jiaoshi.activity.TalentQueryActivity;
import com.weima.aishangyi.jiaoshi.activity.UserInviteActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

/**
 * 订单
 * Created by cgy on 16/7/18.
 */
public class FindFragment extends Fragment {
    private ImageView imv_talent_new;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mUpdateUserInfoReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (imv_talent_new!=null){
                int count = PreferencesHelper.getInstance().getInt(ProjectConstants.Preferences.KEY_TALENT_COUNT,0);
                imv_talent_new.setVisibility(count==0?View.GONE:View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mLocalBroadcastManager.registerReceiver(mUpdateUserInfoReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_TALENT_NEW));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mUpdateUserInfoReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_find, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        imv_talent_new = (ImageView) view.findViewById(R.id.imv_talent_new);
        view.findViewById(R.id.btn_talentcircles).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_activityclassroom).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_talent).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_nearby).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_invite).setOnClickListener(mClickListener);
    }

    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_talentcircles://才艺圈
                    NavigationHelper.startActivity(getActivity(), TalentCircleActivity.class, null, false);
                    PreferencesHelper.getInstance().putInt(ProjectConstants.Preferences.KEY_TALENT_COUNT,0);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_TALENT_NEW));
                    break;
                case R.id.btn_activityclassroom://活动课堂
                    NavigationHelper.startActivity(getActivity(), ActivityClassListActivity.class, null, false);
                    break;
                case R.id.btn_talent://才艺资讯
                    NavigationHelper.startActivity(getActivity(), TalentQueryActivity.class, null, false);
                    break;

                case R.id.btn_nearby://附近的人
                    NavigationHelper.startActivity(getActivity(), NearbyActivity.class, null, false);
                    break;

                case R.id.btn_invite://邀请好友
                    NavigationHelper.startActivity(getActivity(), UserInviteActivity.class, null, false);
                    break;

                default:
                    break;
            }
        }
    };
}
