package dtos;

import entities.User;

import java.util.List;

public class UserDTO {
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
    private int birthYear;
    private String gender;
    private List<RoleDTO> roles;

    public UserDTO(User entity) {
        this.username = entity.getUserName();
        this.password = entity.getUserPass();
        this.address = entity.getAddress();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
        this.birthYear = entity.getBirthYear();
        this.gender = entity.getGender();
        this.roles = RoleDTO.getDTOs(entity.getRoleList());
    }

    public UserDTO(String username, String password, String address, String phone, String email, int birthYear, String gender, List<RoleDTO> roles) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.birthYear = birthYear;
        this.gender = gender;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
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
