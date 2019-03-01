package com.weima.aishangyi.jiaoshi.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.liangmutian.mypicker.TimePickerDialog;
import com.mb.android.utils.Helper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.entity.TimeBean;
import com.weima.aishangyi.jiaoshi.utils.InputFilterMax;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.Date;

/**
 * Created by cgy
 */
public class EditTimePop extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Context mContext;
    private int number = 1;
    private SelectListener selectListener;
    private EditText edit_count;
    private TextView btn_begin_time,btn_end_time;
    private Dialog beginTimeDialog,endTimeDialog;
    private ImageView imv_isOpen;
    private boolean isOpen = true;
    private TimeBean timeBean;
    public interface SelectListener {
         void onSelected(TimeBean timeBean);
    }

    public EditTimePop(Context context, SelectListener listener,TimeBean time) {
        super(context);
        this.mContext = context;
        this.selectListener = listener;
        this.timeBean = time;
        rootView = View.inflate(mContext, R.layout.pop_edittime_pop, null);
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.MATCH_PARENT);
        setContentView(rootView);
        setBackgroundDrawable(new BitmapDrawable());
        findComponent();
        initComponent();
    }

    private void findComponent() {
        edit_count = (EditText) rootView.findViewById(R.id.edit_count);
        btn_begin_time = (TextView) rootView.findViewById(R.id.btn_begin_time);
        btn_end_time = (TextView) rootView.findViewById(R.id.btn_end_time);
        imv_isOpen = (ImageView) rootView.findViewById(R.id.imv_isOpen);
    }

    private void initComponent() {
        if (Helper.isNotEmpty(timeBean)){
            edit_count.setText(timeBean.getNumber()+"");
            edit_count.setSelection(edit_count.getText().toString().length());
            if (Helper.isNotEmpty(timeBean.getStart_time())){
                btn_begin_time.setText(timeBean.getStart_time());
            }
            if (Helper.isNotEmpty(timeBean.getEnd_time())){
                btn_end_time.setText(timeBean.getEnd_time());
            }
            isOpen = timeBean.getStatus()==1;
        }
        imv_isOpen.setImageResource(isOpen ? R.drawable.ic_sound_open : R.drawable.ic_sound_close);
        rootView.findViewById(R.id.txv_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.txv_comfirm).setOnClickListener(this);
        rootView.findViewById(R.id.btnReduce).setOnClickListener(this);
        rootView.findViewById(R.id.btnAdd).setOnClickListener(this);
        imv_isOpen.setOnClickListener(this);
        btn_begin_time.setOnClickListener(this);
        btn_end_time.setOnClickListener(this);
        edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
        edit_count.setFilters(new InputFilter[]{ new InputFilterMax("1", Integer.MAX_VALUE+"")});
        edit_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Helper.isNotEmpty(edit_count.getText().toString())){
                    number = Integer.parseInt(edit_count.getText().toString());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.outView){
            dismiss();
        }else if (id == R.id.txv_cancel){
            dismiss();
        }else if (id == R.id.txv_comfirm){
            doComfirm();
        }else  if (id ==R.id.btnReduce){
            if (number>1) {
                number--;
                edit_count.setText(number+"");
                edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
            }
        }else  if (id ==R.id.btnAdd){
            if (number<Integer.MAX_VALUE){
                number++;
                edit_count.setText(number+"");
                edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
            }else{
                ToastHelper.showToast("超过人数限制");
            }
        }else if (id == R.id.btn_begin_time){
            showBeginTimePick();
        }else if (id == R.id.btn_end_time){
            showEndimePick();
        }else if (id == R.id.imv_isOpen){
            isOpen = !isOpen;
            imv_isOpen.setImageResource(isOpen ? R.drawable.ic_sound_open : R.drawable.ic_sound_close);
        }
    }

    private void doComfirm(){
        String beginTime = btn_begin_time.getText().toString();
        String endTime = btn_end_time.getText().toString();
        if ("请选择".equals(beginTime)){
            ToastHelper.showToast("请选择开始时间");
            return;
        }
        if ("请选择".equals(endTime)){
            ToastHelper.showToast("请选择结束时间");
            return;
        }
        Date beginDate = Helper.string2Date(beginTime,"HH:mm");
        Date endDate = Helper.string2Date(endTime,"HH:mm");
        if (beginDate.compareTo(endDate) > 0){
            ToastHelper.showToast("结束时间应大于开始时间");
            return;
        }
        if (timeBean==null){
            timeBean = new TimeBean();
        }
        timeBean.setNumber(Integer.parseInt(edit_count.getText().toString().trim()));
        timeBean.setStatus(isOpen?1:0);
        timeBean.setStart_time(beginTime);
        timeBean.setEnd_time(endTime);
        selectListener.onSelected(timeBean);
        dismiss();
    }

    private void showBeginTimePick() {
        if (beginTimeDialog == null) {
            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(mContext);
            beginTimeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {
                    beginTimeDialog.dismiss();
                    btn_begin_time.setText(times[0] + ":" + times[1]);
                }
                @Override
                public void onCancel() {
                    beginTimeDialog.dismiss();
                }
            }).create();
        }
        beginTimeDialog.show();
    }

    private void showEndimePick() {
        if (endTimeDialog == null) {
            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(mContext);
            endTimeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {
                    endTimeDialog.dismiss();
                    btn_end_time.setText(times[0] + ":" + times[1]);
                }
                @Override
                public void onCancel() {
                    endTimeDialog.dismiss();
                }
            }).create();
        }
        endTimeDialog.show();
    }

    public void show(View v) {
        showAsDropDown(v);
    }
}
