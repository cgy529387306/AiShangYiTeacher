package com.weima.aishangyi.jiaoshi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：cgy on 17/2/21 02:15
 * 邮箱：593960111@qq.com
 */
public class TeacherBean implements Serializable {


    private long id;
    private String appid;
    private String phone;
    private String password;
    private String nickname;
    private int is_third;
    private String icon;
    private String major;
    private double amount;
    private String name;
    private int age;
    private int sex;
    private String detail;
    private int idCertification;
    private int edCertification;
    private int qlCertification;
    private int star;
    private int status;
    private String created_at;
    private String updated_at;
    private String birth;
    private String address;
    private String interest;
    private int is_test;
    private String signature;
    private int test_price;
    private String message;
    private String test_brief;
    private int device;
    private String bind_phone;
    private double distance;
    private double price;
    private List<LessonBean> lesson;
    private String good_at;
//    private List<String> lesson_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIs_third() {
        return is_third;
    }

    public void setIs_third(int is_third) {
        this.is_third = is_third;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIdCertification() {
        return idCertification;
    }

    public void setIdCertification(int idCertification) {
        this.idCertification = idCertification;
    }

    public int getEdCertification() {
        return edCertification;
    }

    public void setEdCertification(int edCertification) {
        this.edCertification = edCertification;
    }

    public int getQlCertification() {
        return qlCertification;
    }

    public void setQlCertification(int qlCertification) {
        this.qlCertification = qlCertification;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public int getIs_test() {
        return is_test;
    }

    public void setIs_test(int is_test) {
        this.is_test = is_test;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getTest_price() {
        return test_price;
    }

    public void setTest_price(int test_price) {
        this.test_price = test_price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTest_brief() {
        return test_brief;
    }

    public void setTest_brief(String test_brief) {
        this.test_brief = test_brief;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(String bind_phone) {
        this.bind_phone = bind_phone;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<LessonBean> getLesson() {
        return lesson;
    }

    public void setLesson(List<LessonBean> lesson) {
        this.lesson = lesson;
    }

    public String getGood_at() {
        return good_at;
    }

    public void setGood_at(String good_at) {
        this.good_at = good_at;
    }


//    public List<String> getLesson_time() {
//        return lesson_time;
//    }
//
//    public void setLesson_time(List<String> lesson_time) {
//        this.lesson_time = lesson_time;
//    }
}
