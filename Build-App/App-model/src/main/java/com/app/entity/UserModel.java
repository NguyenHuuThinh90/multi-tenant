package com.app.entity;


import com.app.common.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Users")
@Table(name = "users")
public class UserModel extends BaseModel implements Serializable {

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "address", length = 10)
    private String address;

    public UserModel() { }

    public UserModel(String firstName, String lastName, String phone, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public UserModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserModel(String firstName, String lastName, Set<AccountModel> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountModel> accounts = new HashSet<>();

    /**
     * Get accounts
     * <p>
     * return value of accounts
     */
    public Set<AccountModel> getAccounts() {
        return accounts;
    }

    /**
     * Sets accounts
     */
    public void setAccounts(Set<AccountModel> accounts) {
        this.accounts = accounts;
    }

    /**
     * Get firstName
     * <p>
     * return value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get lastName
     * <p>
     * return value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void removeAccount(AccountModel account) {
//        this.accounts.remove(account);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(firstName, userModel.firstName) &&
                Objects.equals(lastName, userModel.lastName) &&
                Objects.equals(phone, userModel.phone) &&
                Objects.equals(email, userModel.email) &&
                Objects.equals(address, userModel.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone, email, address);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
