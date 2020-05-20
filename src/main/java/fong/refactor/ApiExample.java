package fong.refactor;

/**
 * Sample Java code for youtube.videos.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

public class ApiExample {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String API_KEY = "";

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName("API Project")
                .setGoogleClientRequestInitializer(new YouTubeRequestInitializer(API_KEY))
                .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Videos.List request = youtubeService.videos()
                .list("snippet,contentDetails,statistics");
        VideoListResponse response = request.setId("Ks-_Mh1QhMc,c0KYU2j0TM4,eIho2S0ZahI").execute();
        System.out.println(response);
    }

    /**
     * @param ids
     * @param parts
     * @return
     * @example
     * ids: ["Ks-_Mh1QhMc,c0KYU2j0TM4,eIho2S0ZahI"]
     */
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