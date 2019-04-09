package fr.hoc.dap.cmdline;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App {

    public App() {
    }

    public static void main(String[] args) throws Exception {

        String action = null;
        String userKey = null;
        /**
         * pour créer un compte : args = create + nouveau nom dans la pop-in.
         */
        if (args.length == 2) {
            action = args[0];
            userKey = args[1];
        } else if (args.length == 1) {
            action = "view";
            userKey = args[0];
        } else {
            System.out.println("Usage : ");
            System.out.println("Créer un compte cmdline.jar -c {login DaP}");
            System.out.println("Afficher mes infos : cmdline.jar {login DaP}");
        }

        System.out.println("Bienvenue sur DAP ! Vous avez demandé : " + action + " " + userKey);
        // TODO Auto-generated method stub

        /*
         * Scanner sc = new Scanner(System.in); System.out.println("Veuillez saisir votre user Key :"); String str =
         * sc.nextLine(); System.out.println("Vous avez saisi : " + str);
         * 
         * System.out.println("Avez-vous déjà un compte DAP ? (O/N)"); String response = sc.nextLine();
         */

        if ("create".equals(action)) {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/account/add/" + userKey));
        } else if ("view".equals(action)) {

            String nbUnread = getData("http://localhost:8080/email/nbunread?userKey=" + userKey);
            System.out.println("Nombre d'email non lus: " + nbUnread);

            String nextEvent = getData("http://localhost:8080/event/nextString?userKey=" + userKey);
            System.out.println("Votre prochain évenement c'est: " + nextEvent);

        } else {
            System.out.println("Action [" + action + "] non reconnue");
        }

    }

    public static String getData(final String desiredUrl) throws Exception {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            // create the HttpURLConnection
            url = new URL(desiredUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // close the reader.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
