package com.app.entity;

import com.app.common.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "role")
@Table(name = "Role")
public class RoleModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = -5659537750770336946L;

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    @Column(name = "code", length = 100, nullable = false)
    public String code;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<AccountModel> accounts = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleModel roleModel = (RoleModel) o;
        return Objects.equals(name, roleModel.name) &&
                Objects.equals(code, roleModel.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
