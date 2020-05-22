package fong.complete;

/**
 * Sample Java code for youtube.videos.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

/**
 * https://developers.google.com/youtube/v3/docs/videos/list?hl=zh-tw&apix_params=%7B%22part%22%3A%22snippet%2CcontentDetails%2Cstatistics%22%2C%22id%22%3A%22Ks-_Mh1QhMc%2Cc0KYU2j0TM4%2CeIho2S0ZahI%22%7D&apix=true
 * https://developers.google.com/people/quickstart/java
 */
public class YoutubeAPIByOauth {
    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

    private static final String REFRESH_TOKEN_DATA_STORE_DIR = System.getProperty("user.home") + "/google-refresh-token";
    // Plz replace with your own secret file
    private static final String CLIENT_SECRETS = "client_secret.json"; // credential downloaded from google api oauth page
    // Plz replace with your own application name
    private static final String APPLICATION_NAME = "Youtube API Demo"; // created from google api page
    // Plz replace with your whitelisted redirect address port
    private static final int ALLOWED_REDIRECT_ADDRESS_PORT = 62128;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * How to use
     * <p>
     * YoutubeAPIByOauth.requestVideoList(
     *      new string[] {"c0KYU2j0TM4"},
     *      new String[] {"snippet", "contentDetails", "statistics"}
     * );
     *
     * @param ids
     * @param parts
     * @return
     */
    static public VideoListResponse requestVideoList(String[] ids, String parts[]) {
        YouTube youtubeService = null;
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            // Fix: redirect_uri_mismatch
            // https://stackoverflow.com/questions/39263102/how-to-set-redirect-uri-for-oauth2-for-google-in-java
            Credential credential = authorize(CLIENT_SECRETS);
            youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            // Define and execute the API request
            YouTube.Videos.List request = youtubeService.videos()
                    .list(String.join(",", parts));
            VideoListResponse response = request.setId(String.join(",", ids)).execute();
            return response;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(String secretResourceName) {
        final NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            InputStream in = ClassLoader.getSystemResourceAsStream(secretResourceName);


            File DATA_STORE_DIR = new File(REFRESH_TOKEN_DATA_STORE_DIR);
            FileDataStoreFactory DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                            // Sets the access type ("online" to request online access or "offline" to request offline access) or null for the default behavior ("online" for web applications and "offline" for installed applications).
                            // to get a long-lived refresh token
                            .setAccessType("offline")
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(ALLOWED_REDIRECT_ADDRESS_PORT).build();
            // https://stackoverflow.com/questions/39263102/how-to-set-redirect-uri-for-oauth2-for-google-in-java
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}