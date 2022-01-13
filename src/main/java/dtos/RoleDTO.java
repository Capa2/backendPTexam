package dtos;

import entities.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {
    private String name;

    public RoleDTO(Role entity) {
        this.name = entity.getRoleName();
    }

    public static List<RoleDTO> getDTOs(List<Role> entities) {
        List<RoleDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new RoleDTO(e)));
        return dtos;
    }

    public String getRoleName() {
        return name;
    }
}
