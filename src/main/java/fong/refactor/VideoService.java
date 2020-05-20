package fong.refactor;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.TimeZone;

class VideoService {
    public static void main(String args[]) {
        System.out.println("Hello World");
        VideoService videoService = new VideoService();
        videoService.videoList();
    }

    public void videoList() {
        String filePath = "src/main/resources/videos.json";

        // https://stleary.github.io/JSON-java/
        JSONArray videoList = new JSONArray(VideoService.readFile(filePath));

        String[] ids = new String[videoList.length()];
        for (int i = 0; i < videoList.length(); i++) {
            ids[i] = videoList.getJSONObject(i).getString("youtubeID");
        }

        VideoListResponse listResponse =
                ApiExample.requestVideoList(ids, new String[]{"snippet", "contentDetails", "statistics"});


        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            JSONObject video = videoList.getJSONObject(i);
            // https://dzone.com/articles/how-to-use-map-filter-collect-of-stream-in-java-8
            Video youtubeRecord = listResponse.getItems().get(i);

            int views = youtubeRecord.getStatistics().getViewCount().intValue();
            video.put("views", views);

            LocalDate publishedAt = LocalDate.ofEpochDay(youtubeRecord.getSnippet().getPublishedAt().getValue() / 1000 / 60 / 60 / 24);
            LocalDate now = LocalDate.now();

            // https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
            long daysAvailable = Duration.between(publishedAt.atStartOfDay(), now.atStartOfDay()).toDays();


            System.out.println("---" + publishedAt + "," + now + "," + views + ", " + daysAvailable + ", ");
            video.put("monthlyViews", views * 365 / daysAvailable / 12);
        }

        writeFile("src/main/resources/videos-output.json", videoList.toString());
    }


    public static String readFile(String filePath) {
        String content = null;
        try {
            content = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String filePath, String fileString) {
        try {
            Files.write(Paths.get(filePath), fileString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}