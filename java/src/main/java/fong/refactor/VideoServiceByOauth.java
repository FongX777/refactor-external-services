package fong.refactor;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

class VideoServiceByOauth {
    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

    // Plz replace with your own secret file
    private static final String CLIENT_SECRETS_RESOURCE_NAME = "client_secret.json"; // credential downloaded from google api oauth page
    // Plz replace with your own application name
    private static final String APPLICATION_NAME = "Youtube API Demo"; // created from google api page
    // Plz replace with your whitelisted redirect address port
    private static final int ALLOWED_REDIRECT_ADDRESS_PORT = 62128;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static void main(String args[]) {
        VideoServiceByOauth videoService = new VideoServiceByOauth();
        String statistic = videoService.videoList();
        System.out.println(statistic);
    }

    public String videoList() {
        String resourceName = "videos.json";

        JSONArray videoJSONArray = new JSONArray(this.readSystemResource(resourceName));
        List<JSONObject> videoList = new ArrayList<JSONObject>();
        for (Object obj : videoJSONArray.toList()) {
            videoList.add(new JSONObject((Map) obj));
        }


        String[] ids = new String[videoList.size()];
        for (int i = 0; i < videoList.size(); i++) {
            ids[i] = videoList.get(i).getString("youtubeID");
        }


        Credential credential = this.authorize(CLIENT_SECRETS_RESOURCE_NAME);
        YouTube youtubeService = getYoutubeService(credential);
        VideoListResponse listResponse = null;
        try {
            YouTube.Videos.List request = youtubeService.videos()
                    .list("snippet, contentDetails, statistics");
            listResponse = request.setId(String.join(",", ids)).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        for (String id : ids) {

            JSONObject video = null;
            for (JSONObject each : videoList) {
                if (each.getString("youtubeID").equals(id)) {
                    video = each;
                    break;
                }
            }
            if (video == null) {
                continue;
            }

            Video youtubeRecord = null;
            for (Video each : listResponse.getItems()) {
                if (each.getId().equals(id)) {
                    youtubeRecord = each;
                    break;
                }
            }
            if (youtubeRecord == null) {
                continue;
            }


            int views = youtubeRecord.getStatistics().getViewCount().intValue();
            video.put("views", views);

            LocalDate publishedAt = LocalDate.ofEpochDay(youtubeRecord.getSnippet().getPublishedAt().getValue() / 1000 / 60 / 60 / 24);
            LocalDate now = LocalDate.now();
            long daysAvailable = Duration.between(publishedAt.atStartOfDay(), now.atStartOfDay()).toDays();
            if (daysAvailable < 30) {
                daysAvailable = 30;
            }
            video.put("monthlyViews", views * 365 / daysAvailable / 12);
        }

        return videoList.toString();
    }

    public Credential authorize(String secretResourceName) {
        final NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            InputStream in = ClassLoader.getSystemResourceAsStream(secretResourceName);

            FileDataStoreFactory DATA_STORE_FACTORY;

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                            // Sets the access type ("online" to request online access or "offline" to request offline access) or null for the default behavior ("online" for web applications and "offline" for installed applications).
                            // to get a long-lived refresh token
                            .setAccessType("offline")
                            .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(ALLOWED_REDIRECT_ADDRESS_PORT).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private YouTube getYoutubeService(Credential credential) {
        final NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            return youtubeService;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readSystemResource(String resourceName) {
        String content = null;
        try {
            String path = ClassLoader.getSystemResource(resourceName).getPath();
            content = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void writeFile(String filePath, String fileString) {
        try {
            Files.write(Paths.get(filePath), fileString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
