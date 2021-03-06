package com.landlordcommunication.web.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "budget")
    private double budget;

    // if true, user is a tenant, if false, user is a landlord - subject to redesign :)
    @Column(name = "is_tenant")
    private boolean isTenant;

    @Column(name = "user_picture")
    private String userPicture;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usertoresidence",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "residence_id")
    )
    private List<Residence> residences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Rating> ratingsTaken = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    public User() {
    }

    public User(int id, String email, String firstName, String surname, boolean isTenant, double budget, String password,String userPicture) {
        setUserId(id);
        setEmail(email);
        setFirstName(firstName);
        setSurname(surname);
        setIsTenant(isTenant);
        setBudget(budget);
        setPassword(password);
        setUserPicture(userPicture);
    }

    // Setters

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setIsTenant(boolean tenant) {
        isTenant = tenant;
    }

    public void setResidences(List<Residence> residences) {
        this.residences = residences;
    }

    public void setRatingsTaken(List<Rating> ratingsTaken) {
        this.ratingsTaken = ratingsTaken;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Getters

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBudget() {
        return budget;
    }

    public boolean getIsTenant() {
        return isTenant;
    }

    public List<Residence> getResidences() {
        return residences;
    }

    public List<Rating> getRatingsTaken() {
        return ratingsTaken;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }
}
