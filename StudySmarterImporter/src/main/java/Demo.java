import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Demo {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String USER_ID = "ADD YOUR USER ID";
    private static final String USER_AUTH = "ADD YOUR TOKEN";

    public static void main(String[] args) {
        System.out.println("Queriying flashcards");
        try { //We're at a hackathon, try catch is fine
            String json = getCards(args[0]); //"16951"
            //System.out.println(json);

            System.out.println("Parsing JSON");
            ArrayList<Flashcard> list = parseFlashcard(json);


            //At this point we should have a question answer array:
            System.out.println("Writing JSON");
            JSONArray ja = new JSONArray();
            for(Flashcard f : list) {
                //System.out.println(f.question +" - "+f.answer);
                JSONObject jo = new JSONObject();
                jo.put("question", f.question);
                jo.put("answer", f.answer);
                jo.put("cathegory", "General Knowledge");
                ja.put(jo);
            }

            System.out.println(ja);

            //Write to file:
            String filename = "testdeck"+System.currentTimeMillis()+".json";
            try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
                out.print(ja);
            }

            //Upload to server:
            System.out.println("Uploading to server");
            String command = "scp "+filename+" ADD YOUR SERVER URL";
            //Process p = Runtime.getRuntime().exec("make4ht -u latexin.tex");
            //int exitVal = p.waitFor();
            System.out.println("Upload cmd:");
            System.out.println(command);

            //Final link
            System.out.println("Final link:");
            System.out.println("ADD YOUR SERVER URL"+filename);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //DO query here: authenticatiotoken is harcoden
    // HTTP GET request
    private static String getCards(String deckId) throws Exception {

        String url = "https://prod.studysmarter.de/users/"+USER_ID+"/subjects/"+deckId+"/flashcards/?quantity=100&s_bad=true&s_medium=true&s_good=true&s_trash=false&s_unseen=true&order=smart&all=true";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("Authorization", USER_AUTH);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());
        return response.toString();
    }

    private static ArrayList<Flashcard> parseFlashcard(String json) {
        JSONObject obj = new JSONObject(json);
        //String pageName = obj.getJSONObject("pageInfo").getString("pageName");

        //Get the results list into an Flashcards array:
        ArrayList<Flashcard> flashcardlist = new ArrayList<Flashcard>();
        JSONArray arr = obj.getJSONArray("results");

        for(int i=0; i<arr.length(); i++) {
            JSONObject qdata = arr.getJSONObject(i);

            //Get flashcardinfo
            JSONObject flashcardinfo = qdata.getJSONObject("flashcardinfo");
            //System.out.println(flashcardinfo);

            //Get cards:
            try {
                //TODO filer images
                String q = flashcardinfo.getString("question");
                String a = flashcardinfo.getString("answer");
                flashcardlist.add(new Flashcard(q,a));
            } catch (Exception e1) {
                //Do nothing
            }

        }
        return flashcardlist;
    }

    public static class Flashcard {
        public String question;
        public String answer;

        public Flashcard(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

}
