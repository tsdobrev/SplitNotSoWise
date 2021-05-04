package bg.sofia.uni.fmi.mjt.splitwise.user;

import java.util.Objects;

public class UserAuthenticationInfo {
    private final String name;
    private final String password;

    public UserAuthenticationInfo(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean passwordMatch(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(password);
    }

    public <T extends UserAuthenticationInfo> boolean passwordMatch(T other) {
        if (this == other) {
            return false;
        }
        if (other == null) {
            return false;
        }
        return (other.passwordMatch(this.password));
    }

    // two users are equal only if their usernames are equal

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        UserAuthenticationInfo that = (UserAuthenticationInfo) object;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
