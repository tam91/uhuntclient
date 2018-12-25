package nl.ktam.uhunt.login;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class User {

    private static final String homeUrl = "https://uva.onlinejudge.org/";
    private static final String submissionUrl = "http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=submit_problem&problemid=";

    private String username;
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public Map<String,String> login() throws IOException {
        Map<String,String> params = new HashMap<>();
        Map<String,String> cookies = null;
        Document login = Jsoup.parse(new URL(homeUrl), 3000);
        Element form = login.getElementById("mod_loginform");
        if (form != null) {

            String loginUrl = form.attr("action");
            Elements inputElements = form.getElementsByTag("input");
            for (Element inputElement : inputElements) {
                String key = inputElement.attr("name");
                String value = inputElement.attr("value");
                params.putIfAbsent(key,value);
                System.out.println("Parameter Name: " + key);
                System.out.println("Parameter Value: " + value);
            }
            params.put("username", username);
            params.put("passwd", password);

            Connection.Response postResponse = Jsoup.connect(loginUrl)
                    .data(params)
                    .method(Connection.Method.POST)
                    .execute();
            Document document = postResponse.parse();
            cookies = postResponse.cookies();

            System.out.println("document.loginForm() = " + document.getElementById("mod_loginform"));
        }
        return cookies;
    }

    public boolean submit(Map<String,String> cookies, String problemId, String language, String code) throws IOException {
        String url = submissionUrl + problemId;
        Connection.Response getResponse = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .cookies(cookies)
                .execute();

        Document document = getResponse.parse();

        Elements elements = document.getElementsByTag("form");
        Element submitForm = elements.last();
        String submitUrl = homeUrl + submitForm.attr("action");
        System.out.println("submitUrl = " + submitUrl);
        Elements inputElements = submitForm.getElementsByTag("input");
        Map<String,String> params = new HashMap<>();
        params.put("localid", problemId);
        params.put("language", language);
        params.put("code", code);

        System.out.println("params = " + params);

        Connection.Response postResponse = Jsoup.connect(submitUrl)
                .method(Connection.Method.POST)
                .data(params)
                .cookies(cookies)
                .execute();

        Document submissionDocument = postResponse.parse();
        if (submissionDocument.html().contains("Submission+received+with+ID")) {
            return true;
        } else {
            return false;
        }
    }

}
