package com.ecloud.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        String pyPath = System.getProperty("user.dir") + "\\py\\create_video\\project\\create_video.py";
        System.out.println(pyPath);
        String[] arguments = new String[] {"python",pyPath};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),
                    "utf-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();

            int re = process.waitFor();
            if(re==0)
            System.out.println("succeed use py");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}