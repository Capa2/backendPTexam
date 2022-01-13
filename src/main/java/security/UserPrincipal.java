package security;

import entities.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String email;
  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class User*/
  public UserPrincipal(User user) {
    this.email = user.getEmail();
    this.roles = user.getRolesAsStrings();
  }

  public UserPrincipal(String email, String[] roles) {
    super();
    this.email = email;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return email;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}
