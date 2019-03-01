package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.db.SearchHistoryDao;
import com.weima.aishangyi.jiaoshi.pop.SelectSearchTypePop;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：cgy on 16/11/24 22:01
 * 邮箱：593960111@qq.com
 * 搜索
 */
public class HomeSearchActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView;
    private EditText edt_home_search;
    private TextView txv_select_type;
    private SearchHistoryDao searchHistoryDao;
    private List<String> dataList = new ArrayList<>();
    private LinearLayout actionbar;
    private int currentType = 1;
    private SelectSearchTypePop selectSearchTypePop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_search);
        initUI();
        fillListData();
    }

    private void initUI() {
        searchHistoryDao = new SearchHistoryDao();
        actionbar = findView(R.id.actionbar);
        edt_home_search = findView(R.id.edt_home_search);
        txv_select_type = findView(R.id.txv_select_type);
        listView = findView(R.id.listView);
        listView.addHeaderView(getHeaderView());
        listView.addFooterView(getFooterView());
        txv_select_type.setOnClickListener(this);
        findView(R.id.lin_actionbar_back).setOnClickListener(this);
        findView(R.id.txv_search).setOnClickListener(this);
        selectSearchTypePop = new SelectSearchTypePop(HomeSearchActivity.this, new SelectSearchTypePop.SelectListener() {
            @Override
            public void onSelected(int type) {
                currentType = type;
                if (type == 1){
                    txv_select_type.setText("老师");
                }else {
                    txv_select_type.setText("课程");
                }
            }
        });
    }

    private void fillListData(){
        List<String> data = searchHistoryDao.getList();
        if (Helper.isNotEmpty(data)){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data));
        }else{
            listView.setVisibility(View.GONE);
        }
    }


    private View getHeaderView(){
        View view = LayoutInflater.from(HomeSearchActivity.this).inflate(R.layout.header_home_search, null);
        return view;
    }

    private View getFooterView(){
        View view = LayoutInflater.from(HomeSearchActivity.this).inflate(R.layout.footer_home_search, null);
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHistoryDao.clear();
                fillListData();
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lin_actionbar_back){
            finish();
        }else if (id == R.id.txv_search){
            if (Helper.isNotEmpty(edt_home_search.getText().toString())){
                searchHistoryDao.add(edt_home_search.getText().toString());
                fillListData();
            }
        }else if (id == R.id.txv_select_type){
            selectSearchTypePop.show(actionbar);
        }
    }
}
