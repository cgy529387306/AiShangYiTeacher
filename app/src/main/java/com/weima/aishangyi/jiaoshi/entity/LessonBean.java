package com.weima.aishangyi.jiaoshi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class LessonBean implements Serializable{
    private long id;
    private String name;
    private long item;
    private long lesson_item;
    private int is_student;
    private int student_price;
    private int is_teacher;
    private double teacher_price;
    private String lesson_brief;
    private String lesson_detail;
    private long teacher_id;
    private String address;
    private double longitude;
    private double latitude;
    private String created_at;
    private String updated_at;
    private String icon;
    private List<String> images;
    private int number;
    private int status;//0待审核，1已审核，2审核不通过


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getItem() {
        return item;
    }

    public void setItem(long item) {
        this.item = item;
    }

    public long getLesson_item() {
        return lesson_item;
    }

    public void setLesson_item(long lesson_item) {
        this.lesson_item = lesson_item;
    }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }

    public int getStudent_price() {
        return student_price;
    }

    public void setStudent_price(int student_price) {
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

    public long getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(long teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
