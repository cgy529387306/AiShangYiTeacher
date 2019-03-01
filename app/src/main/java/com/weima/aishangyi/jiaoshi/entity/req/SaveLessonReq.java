package com.weima.aishangyi.jiaoshi.entity.req;

import java.util.List;

/**
 * 作者：cgy on 16/12/25 09:21
 * 邮箱：593960111@qq.com
 */
public class SaveLessonReq {
    private long id;
    private List<String> images;
    private String name;
    private int item;
    private int lesson_item;//1一对一2拼课
    private int is_student;//1学生上门
    private double student_price;
    private int is_teacher;
    private double teacher_price;
    private String lesson_brief;
    private String lesson_detail;
    private String test_intro;
    private int is_test;//1支持试课
    private double test_price;
    private List<String> lesson_time;
    private String address;
    private double longitude;
    private double latitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getLesson_item() {
        return lesson_item;
    }

    public void setLesson_item(int lesson_item) {
        this.lesson_item = lesson_item;
    }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }

    public double getStudent_price() {
        return student_price;
    }

    public void setStudent_price(double student_price) {
        this.student_price = student_price;
    }

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public double getTeacher_price() {
        return teacher_price;
    }

    public void setTeacher_price(double teacher_price) {
        this.teacher_price = teacher_price;
    }

    public String getLesson_brief() {
        return lesson_brief;
    }

    public void setLesson_brief(String lesson_brief) {
        this.lesson_brief = lesson_brief;
    }

    public String getLesson_detail() {
        return lesson_detail;
    }

    public void setLesson_detail(String lesson_detail) {
        this.lesson_detail = lesson_detail;
    }

    public String getTest_intro() {
        return test_intro;
    }

    public void setTest_intro(String test_intro) {
        this.test_intro = test_intro;
    }

    public int getIs_test() {
        return is_test;
    }

    public void setIs_test(int is_test) {
        this.is_test = is_test;
    }

    public double getTest_price() {
        return test_price;
    }

    public void setTest_price(double test_price) {
        this.test_price = test_price;
    }

    public List<String> getLesson_time() {
        return lesson_time;
    }

    public void setLesson_time(List<String> lesson_time) {
        this.lesson_time = lesson_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
