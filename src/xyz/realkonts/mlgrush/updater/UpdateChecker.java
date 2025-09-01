package xyz.realkonts.mlgrush.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import xyz.realkonts.mlgrush.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    private static boolean availableUpdate = false;
    private static String version = "";
    private static String updateMessage = "";

    public static void checkUpdate() {
        JsonObject obj = new Gson().fromJson(getcontent("https://raw.githubusercontent.com/realkontsx/mlgrush/refs/heads/main/updatecheck.json"), JsonObject.class).getAsJsonObject();
        if(obj == null) {
            return;
        }
        String current = Main.getInstance().getDescription().getVersion();
        if(!current.equals(obj.get("version").getAsString())) {
            availableUpdate = true;
            version = obj.get("version").getAsString();
            updateMessage = obj.get("newVersionMessage").getAsString();
        }
    }

    public static String getcontent(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Java");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAvailableUpdate() {
        return availableUpdate;
    }

    public static String getNewVersion() {
        return version;
    }

    public static String getUpdateMessage() {
        return updateMessage;
    }
}
