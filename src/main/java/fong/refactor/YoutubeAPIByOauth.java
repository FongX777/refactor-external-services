package fong.refactor;

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
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    // Plz replace with your own secret file
    private static final String CLIENT_SECRETS = "client_secret.json"; // credential downloaded from google api oauth page
    // Plz replace with your own application name
    private static final String APPLICATION_NAME = "Youtube API Demo"; // created from google api page
    // Plz replace with your whitelisted redirect address port
    private static final int ALLOWED_REDIRECT_ADDRESS_PORT = 62128;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        // Fix:
        // https://stackoverflow.com/questions/793213/getting-the-inputstream-from-a-classpath-resource-xml-file
        InputStream in = ClassLoader.getSystemResourceAsStream(CLIENT_SECRETS);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CLIENT_SECRETS);
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setAccessType("offline")
                        .build();
        // Fix: redirect_uri_mismatch
        // https://stackoverflow.com/questions/39263102/how-to-set-redirect-uri-for-oauth2-for-google-in-java
        Credential credential =
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(ALLOWED_REDIRECT_ADDRESS_PORT).build()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * You can use it to test your configuration.
     * <p>
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void _main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Videos.List request = youtubeService.videos()
                .list("snippet,contentDetails,statistics");
        VideoListResponse response = request.setId("Ks-_Mh1QhMc,c0KYU2j0TM4,eIho2S0ZahI").execute();
        System.out.println(response);
    }

    static public VideoListResponse requestVideoList(String[] ids, String parts[]) {
        YouTube youtubeService = null;
        try {
            youtubeService = getService();
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
}