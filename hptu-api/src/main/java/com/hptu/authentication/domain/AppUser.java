package com.hptu.authentication.domain;

import com.hptu.authentication.dto.UserDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email_address" }, name = "EMAIL_UNIQUE")})
public class AppUser extends BaseEntity {

	private String firstName;

    private String lastName;
    @Column(name = "email_address", nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    private String designation;

    private String cellPhone;


    private boolean isActive;

    @Setter
    private boolean forceChangePass;

    private boolean isAdmin;

    @Transient
    private String resetPass;

    public AppUser() {
    }

    private AppUser(String firstName, String lastName, String username,
                    String designation, String cellPhone, boolean isActive, boolean forceChangePass, boolean isAdmin, PasswordEncoder encoder) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = encoder.encode(username);
        this.designation = designation;
        this.cellPhone = cellPhone;
        this.isActive = isActive;
        this.forceChangePass = forceChangePass;
        this.isAdmin = isAdmin;
    }

    public String getRole() {
        return isAdmin ? "Admin" : "User";
    }

    public String getUserFullName(){
        return String.format("%s, %s", this.firstName, this.lastName);
    }

    public static AppUser createUser(UserDto userDto, PasswordEncoder encoder){
        return new AppUser(userDto.firstName(), userDto.lastName(), userDto.username(), userDto.designation(), userDto.cellPhone(), userDto.isActive(), true, userDto.isAdmin(), encoder);
    }

    public void updateUser(UserDto userDto){
        if(!StringUtils.equals(userDto.firstName(), this.firstName)){
            this.firstName = userDto.firstName();
        }
        if(!StringUtils.equals(userDto.lastName(), this.lastName)){
            this.lastName = userDto.firstName();
        }
        if(!StringUtils.equals(userDto.designation(), this.designation)){
            this.designation = userDto.designation();
        }
        if(!StringUtils.equals(userDto.cellPhone(), this.cellPhone)){
            this.cellPhone = userDto.cellPhone();
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

        AppUser user = (AppUser) o;

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
