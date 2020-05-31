package com.app.entity;

import com.app.common.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Account")
@Table(name = "account")
public class AccountModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = -5659537750770336946L;

    @Column(name = "username", length = 25, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "access_token")
    private String token;

    @Column(name = "isBlock")
    private boolean isBlock = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel users;

    public AccountModel() {}

    public AccountModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountModel(String username, String password, UserModel users) {
        this.username = username;
        this.password = password;
        this.users = users;
    }

    /**
     * Get user
     * <p>
     * return value of user
     */
    public UserModel getUser() {
        return users;
    }

    /**
     * Sets user
     */
    public void setUser(UserModel user) {
        this.users = user;
    }

    /**
     * Get roles
     * <p>
     * return value of roles
     */
    public Set<RoleModel> getRoles() {
        return roles;
    }

    /**
     * Sets roles
     */
    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }

    /**
     * Get username
     * <p>
     * return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password
     * <p>
     * return value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get token
     * <p>
     * return value of token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public void addRole(RoleModel role) {
        this.roles.add(role);
        role.getAccounts().add(this);
    }

    public void removeRole(RoleModel role) {
        this.roles.remove(role);
        role.getAccounts().remove(this);
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, token);
    }
}
