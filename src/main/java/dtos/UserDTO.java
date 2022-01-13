package dtos;

import entities.User;

import java.util.List;

public class UserDTO {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
    private int birthYear;
    private String gender;
    private List<RoleDTO> roles;

    public UserDTO(User entity) {
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.address = entity.getAddress();
        this.phone = entity.getPhone();
        this.birthYear = entity.getBirthYear();
        this.gender = entity.getGender();
        this.roles = RoleDTO.getDTOs(entity.getRoleList());
    }

    public UserDTO(String email,
                   String password,
                   String name,
                   String address,
                   String phone,
                   int birthYear,
                   String gender,
                   List<RoleDTO> roles) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.birthYear = birthYear;
        this.gender = gender;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
