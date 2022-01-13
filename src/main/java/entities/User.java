package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dtos.RoleDTO;
import dtos.UserDTO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 32)
    private String userName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_address", length = 128)
    private String address;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_phone", length = 21)
    private String phone;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_email", length = 64)
    private String email;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_birthYear", length = 4)
    private int birthYear;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_gender", length = 12)
    private String gender;


    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> rolesAsStrings.add(role.getRoleName()));
        return rolesAsStrings;
    }

    public User() {
    }

    public User(UserDTO dto) {
        this.userName = dto.getUsername();
        this.userPass = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        this.address = dto.getAddress();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.birthYear = dto.getBirthYear();
        this.gender = dto.getGender();
    }


    public User(String userName, String userPass, String address, String phone, String email, int birthYear,
                String gender) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.birthYear = birthYear;
        this.gender = gender;

        this.roleList = roleList;
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, userPass);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getGender() {
        return gender;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

}
