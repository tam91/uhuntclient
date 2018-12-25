package nl.ktam.uhunt.login;

import java.io.IOException;
import java.util.Map;

public class UhuntClient {

    private static String username = ""; //fill in your username
    private static String password = ""; //fill in your password

    public static void main(String[] args) throws IOException {
        UserLoginCache cache = UserLoginCache.getInstance();
        User user = new User(username, password);
        Map<String,String> userCookies = user.login();
        cache.addUser(username, userCookies);
        System.out.println(cache);
        boolean submitSuccess = user.submit(cache.getCookiesFromUser(username), "100", "2", "3");
        System.out.println("submitSuccess = " + submitSuccess);
    }
}
