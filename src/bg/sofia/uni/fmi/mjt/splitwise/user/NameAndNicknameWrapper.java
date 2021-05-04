package bg.sofia.uni.fmi.mjt.splitwise.user;

import java.util.Objects;

public class NameAndNicknameWrapper {
    private final String name;
    private final String nickname;

    public NameAndNicknameWrapper(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        NameAndNicknameWrapper that = (NameAndNicknameWrapper) object;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getNickname(),getName());
    }
}
