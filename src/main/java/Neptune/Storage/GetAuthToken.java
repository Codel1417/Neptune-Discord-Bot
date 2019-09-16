package Neptune.Storage;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GetAuthToken {

    public Map GetToken(File file){
        try {
            String json;
            Scanner scanner = new Scanner(file);
            json = scanner.nextLine();
            Type typeOfHashMap = new TypeToken<Map<String, Object>>() { }.getType();
            scanner.close();
            return new GsonBuilder().create().fromJson(json, typeOfHashMap);
        } catch (FileNotFoundException e) {
            System.out.println("AUTH: Error: Unable to find Auth file: " + file.getAbsolutePath());
            System.exit(1);
        }
        return null;
    }
    public static void main(String[] args){
        HashMap<String, String> map = new HashMap<>();
        System.out.println("Generate the Json File for the Discord Token(s) ");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Discord Token: ");
        map.put("token", scanner.nextLine().trim().strip());
        System.out.println("Discord DevMode Token: ");
        map.put("dev-token", scanner.nextLine().trim().strip());
        System.out.println("Discord Owner ID: ");
        map.put("owner-id", scanner.nextLine().trim().strip());
        scanner.close();
        String gson = new GsonBuilder().create().toJson(map);
        try {
            PrintWriter printWriter = new PrintWriter(new File("NepAuth.json"));
            printWriter.write(gson);
            printWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
