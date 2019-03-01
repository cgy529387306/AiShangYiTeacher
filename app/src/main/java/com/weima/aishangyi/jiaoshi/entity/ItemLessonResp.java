package com.weima.aishangyi.jiaoshi.entity;

import java.util.List;

/**
 * 作者：cgy on 17/1/12 01:37
 * 邮箱：593960111@qq.com
 */
public class ItemLessonResp extends CommonEntity {
    private DataBean data;

    public static class DataBean extends CommonListData{
        private List<LessonBean> data;

        public List<LessonBean> getData() {
            return data;
        }

        public void setData(List<LessonBean> data) {
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
