package com.hptu.score.entity;

import com.hptu.score.dto.UserDto;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email_address" }, name = "EMAIL_UNIQUE")})
public class User extends BaseEntity {

    private static final long serialVersionUID = 6363957582741827151L;

	private String firstname;

    private String lastName;
    @Column(name = "email_address", nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String password2;

    private String designation;

    private String cellPhone;

    private String role;

    private boolean isActive;

    private boolean forceChangePass;

    @Transient
    private String userIsActive;
    @Transient
    private String userIsAdmin;

    @Transient
    private String resetPass;

    public User() {
    }

    public User(String firstname, String lastName, String username, String password,
                String designation, String cellPhone, String role, boolean isActive) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.designation = designation;
        this.cellPhone = cellPhone;
        this.role = role;
        this.isActive = isActive;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public boolean isForceChangePass() {
        return forceChangePass;
    }

    public void setForceChangePass(boolean forceChangePass) {
        this.forceChangePass = forceChangePass;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserFullName(){
        return String.format("%s, %s", this.firstname, this.lastName);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserIsActive() {
        return this.userIsActive;
    }

    public void setUserIsActive(String userIsActive) {
        this.userIsActive = userIsActive;
    }

    public String getUserIsAdmin() {
        return this.userIsAdmin;
    }

    public void setUserIsAdmin(String userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public String getResetPass() {
        return resetPass;
    }

    public void setResetPass(String resetPass) {
        this.resetPass = resetPass;
    }

    public static User createUser(UserDto userDto){
        return new User(userDto.getFirstname(), userDto.getLastName(), userDto.getUsername(), userDto.getUsername(), userDto.getDesignation(), userDto.getCellPhone(), userDto.getRole(), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), user.getId())
                .append(getUsername(), user.getUsername())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getUsername()).toHashCode();
    }
}
