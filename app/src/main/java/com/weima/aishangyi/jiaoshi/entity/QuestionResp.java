package com.weima.aishangyi.jiaoshi.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class QuestionResp extends CommonEntity{
    private DataBean data;

    public static class DataBean extends CommonListData{
        private List<QuestionBean> data;

        public List<QuestionBean> getData() {
            return data;
        }

        public void setData(List<QuestionBean> data) {
            this.data = data;
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
