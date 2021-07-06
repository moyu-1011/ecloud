import java.io.BufferedReader;
import java.io.InputStreamReader;
 
public class App {
  public static void main(String[] args) {
    String[] arguments = new String[] {"python", "./create_video.py"};
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
          System.out.println("succeed use py");
        } catch (Exception e) {
            e.printStackTrace();
        }  
  }
}