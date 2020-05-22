package fong.complete;


import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class VideoService {
    public static void main(String args[]) {
        VideoService videoService = new VideoService();
        videoService.videoList();
    }

    public void videoList() {
        String resourceName = "videos.json";

        List<JSONObject> videoList =
                (new JSONArray(this.readSystemResource(resourceName))).toList().stream().map(object -> new JSONObject((Map) object)).collect(Collectors.toList());

        String[] ids = videoList.stream().map(s -> s.getString("youtubeID")).toArray(String[]::new);


        // You can choose using Oauth2 or api token, the main difference is that
        // Oauth open a web page to gain your google account authorization.
        VideoListResponse listResponse =
                YoutubeAPIByOauth.requestVideoList(ids, new String[]{"snippet", "contentDetails", "statistics"});
//                YoutubeAPIByAPIKey.requestVideoList(ids, new String[]{"snippet", "contentDetails", "statistics"});

        for (String id : ids) {
            JSONObject video =
                    videoList.stream().filter(v -> v.getString("youtubeID").equals(id)).findFirst().orElse(null);
            Video youtubeRecord =
                    listResponse
                            .getItems()
                            .stream()
                            .filter(record -> record.getId().equals(id))
                            .findFirst().orElse(null);


            int views = youtubeRecord.getStatistics().getViewCount().intValue();
            video.put("views", views);
            LocalDate publishedAt = LocalDate.ofEpochDay(youtubeRecord.getSnippet().getPublishedAt().getValue() / 1000 / 60 / 60 / 24);
            LocalDate now = LocalDate.now();

            // https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
            long daysAvailable = Duration.between(publishedAt.atStartOfDay(), now.atStartOfDay()).toDays();
            if (daysAvailable < 30) {
                daysAvailable = 30;
            }
            video.put("monthlyViews", views * 365 / daysAvailable / 12);
        }

        System.out.println(videoList.toString());
        this.writeFile("src/main/resources/videos.output.json", videoList.toString());
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