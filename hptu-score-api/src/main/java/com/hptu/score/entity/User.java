package com.hptu.score.entity;

import com.hptu.score.dto.UserDto;
import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import io.quarkus.elytron.security.common.BcryptUtil;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email_address" }, name = "EMAIL_UNIQUE")})
public class User extends BaseEntity {

    private static final long serialVersionUID = 6363957582741827151L;

	private String firstName;

    private String lastName;
    @Column(name = "email_address", nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String designation;

    private String cellPhone;


    private boolean isActive;

    private boolean forceChangePass;

    private boolean isAdmin;

    @Transient
    private String resetPass;

    public User() {
    }

    private User(String firstName, String lastName, String username,
                String designation, String cellPhone, boolean isActive, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = BcryptUtil.bcryptHash(username);
        this.designation = designation;
        this.cellPhone = cellPhone;
        this.isActive = isActive;
        this.isAdmin = isAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
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

    public String getCellPhone() {
        return cellPhone;
    }

    public String getRole() {
        return isAdmin ? "Admin" : "User";
    }

    public String getUserFullName(){
        return String.format("%s, %s", this.firstName, this.lastName);
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getResetPass() {
        return resetPass;
    }

    public static User createUser(UserDto userDto){
        return new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), userDto.getDesignation(), userDto.getCellPhone(), userDto.isActive(), userDto.isAdmin());
    }

    public void updateUser(UserDto userDto){
        if(!StringUtils.equals(userDto.getFirstName(), this.firstName)){
            this.firstName = userDto.getFirstName();
        }
        if(!StringUtils.equals(userDto.getLastName(), this.lastName)){
            this.lastName = userDto.getFirstName();
        }
        if(!StringUtils.equals(userDto.getDesignation(), this.designation)){
            this.designation = userDto.getDesignation();
        }
        if(!StringUtils.equals(userDto.getCellPhone(), this.cellPhone)){
            this.cellPhone = userDto.getCellPhone();
        }
        if(userDto.isActive() != this.isActive){
            this.isActive = userDto.isActive();
        }
        if(userDto.isAdmin() != this.isAdmin){
            this.isAdmin = userDto.isAdmin();
        }
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
