package fong.refactor;


import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class VideoService {
    public static void main(String args[]) {
        System.out.println("Hello World");
        VideoService videoService = new VideoService();
        videoService.videoList();



    }

    public void videoList() {
        String filePath = "src/main/resources/videos.json";

        // https://stleary.github.io/JSON-java/
        JSONArray array = new JSONArray(VideoService.readFile(filePath));

        String[] ids = new String[array.length()];
        for (int i=0; i< array.length(); i++) {
            ids[i] = array.getJSONObject(i).getString("youtubeID");
        }

//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//        Plus plus = new Plus.builder(new NetHttpTransport(),
//                JacksonFactory.getDefaultInstance(),
//                credential)
//                .setApplicationName("Google-PlusSample/1.0")
//                .build();
    }



    public static String readFile(String filePath)  {
        String content = null;
        try {
            content = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}