package com.prweb.entity;

public class PersonUser {

    private  int id; //流水号
    private String person_user_no;
    private String person_name;
    private String sex;
    private String id_card;
    private String cell_phone;
    private String is_verified;
    private String id_card_picture_front;
    private String id_card_picture_back;



    public PersonUser() {
    }

    public PersonUser(int id, String person_user_no, String person_name, String sex, String id_card, String cell_phone, String is_verified, String id_card_picture_front, String id_card_picture_back) {
        this.id = id;
        this.person_user_no = person_user_no;
        this.person_name = person_name;
        this.sex = sex;
        this.id_card = id_card;
        this.cell_phone = cell_phone;
        this.is_verified = is_verified;
        this.id_card_picture_front = id_card_picture_front;
        this.id_card_picture_back = id_card_picture_back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPerson_user_no() {
        return person_user_no;
    }

    public void setPerson_user_no(String person_user_no) {
        this.person_user_no = person_user_no;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getId_card_picture_front() {
        return id_card_picture_front;
    }

    public void setId_card_picture_front(String id_card_picture_front) {
        this.id_card_picture_front = id_card_picture_front;
    }

    public String getId_card_picture_back() {
        return id_card_picture_back;
    }

    public void setId_card_picture_back(String id_card_picture_back) {
        this.id_card_picture_back = id_card_picture_back;
    }
}
