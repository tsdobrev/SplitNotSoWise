package bg.sofia.uni.fmi.mjt.splitwise.user;

public class UserBasicInfo extends UserAuthenticationInfo {
    private final String nickname;

    public UserBasicInfo(String name, String password, String nickname) {
        super(name,password);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}