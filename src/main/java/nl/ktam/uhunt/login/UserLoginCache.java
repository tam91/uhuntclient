package nl.ktam.uhunt.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserLoginCache {

    private static final UserLoginCache instance = new UserLoginCache();
    private static ConcurrentHashMap<String, Map<String,String>> cache;

    private UserLoginCache() {
        cache = new ConcurrentHashMap<>();
    }

    public static UserLoginCache getInstance() {
        return instance;
    }

    public void addUser(String username, Map<String,String> cookies) {
        cache.putIfAbsent(username, cookies);
    }

    public Map<String,String> getCookiesFromUser(String username) {
        return cache.get(username);
    }
    @Override
    public String toString() {
        return cache.toString();
    }
}
