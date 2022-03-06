package util;

public class MySqlDbConfig {
    private static String url = "jdbc:mysql://localhost:3306/kurssystem";
    private static String user = "root";
    private static String pwd = "";

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPwd() {
        return pwd;
    }

}
