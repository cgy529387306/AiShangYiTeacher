package com.weima.aishangyi.jiaoshi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：cgy on 16/12/30 00:32
 * 邮箱：593960111@qq.com
 */
public class CertifyInfoResp extends CommonEntity{
    private CertifyInfo data;

    public CertifyInfo getData() {
        return data;
    }

    public void setData(CertifyInfo data) {
        this.data = data;
    }

    public class CertifyInfo implements Serializable{

        /**
         * id : 4
         * teacher_id : 5
         * name : null
         * idCard : 444444444444444444
         * front_card : http://aishangyi.h5h5h5.cn/file/20161229/731aeeb7baab7b49cf90227af406e35c20161229153954.jpg
         * back_card : http://aishangyi.h5h5h5.cn/file/20161229/e7e47ae938438c805ca7046d5be1982320161229154006.jpg
         * education : null
         * qualify : null
         * created_at : 1483026393
         * updated_at : 1483026393
         * award : null
         */

        private long id;
        private long teacher_id;
        private String name;
        private String idCard;
        private String front_card;
        private String back_card;
        private List<String> education;
        private List<String> qualify;
        private String created_at;
        private String updated_at;
        private List<String> award;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(long teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getFront_card() {
            return front_card;
        }

        public void setFront_card(String front_card) {
            this.front_card = front_card;
        }

        public String getBack_card() {
            return back_card;
        }

        public void setBack_card(String back_card) {
            this.back_card = back_card;
        }

        public List<String> getEducation() {
            return education;
        }

        public void setEducation(List<String> education) {
            this.education = education;
        }

        public List<String> getQualify() {
            return qualify;
        }

        public void setQualify(List<String> qualify) {
            this.qualify = qualify;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public List<String> getAward() {
            return award;
        }

        public void setAward(List<String> award) {
            this.award = award;
        }
    }
}
