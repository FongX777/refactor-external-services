# Refactor Remote API Practice

This project implements Martin Fowler's blog post [Refactoring code that accesses external services](https://martinfowler.com/articles/refactoring-external-service.html) to serve as an exercise for the study group [ddd-tw/2020-legacycode-studygroup: Resources for the 2020 legacy code study group](https://github.com/ddd-tw/2020-legacycode-studygroup).


## Installation


```shell script
mvn clean install
```

After you set up the project properly, try run `VideoService.java` file.


## How Enable your youtube data API

checkout: https://developers.google.com/youtube/v3/getting-started

1. You need a [Google Account](https://www.google.com/accounts/NewAccount) to access the Google API Console, request an API key, and register your application.
2. Create a project in the [Google Developers Console](https://console.developers.google.com) and [obtain authorization credentials](https://developers.google.com/youtube/registering_an_application) so your application can submit API requests.
3. After creating your project, make sure the YouTube Data API is one of the services that your application is registered to use:
    * Go to the [API Console](https://console.developers.google.com/) and select the project that you just registered. 
    * Visit the [Enabled APIs page](https://console.developers.google.com/apis/enabled). In the list of APIs, make sure the status is **ON** for the **YouTube Data API v3**.  
4. Then you can [obtain authorization credentials](https://developers.google.com/youtube/registering_an_application?hl=zh-tw)
    1. Oauth 2.0 (need user login): Whenever your application requests private user data, it must send an OAuth 2.0 token along with the request.
    2. API key: The key identifies your project and provides API access, quota, and reports.

If you want to make it quick, I suggest using API key; however, if you want to stick to the tutorial from Martin Fowler's blog, you can choose Oauth 2.0.

Note: If you choose Oauth 2.0, don't forget to set the 'Authorized redirect URIs', or you will get 'URL mismatch' error.


## Installed Modules

### org.json

Maven Usage:
```xml
<dependency>
  <groupId>com.google.oauth-client</groupId>
  <artifactId>google-oauth-client-jetty</artifactId>
  <version>1.30.4</version>
</dependency>

```


### JUnit

For testing.

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
```

### google-api-client

Maven:
```xml
<dependency>
    <groupId>com.google.api-client</groupId>
    <artifactId>google-api-client</artifactId>
    <version>1.30.9</version>
</dependency>
```

### google-api-services-youtube
```xml
<dependency>
    <groupId>com.google.apis</groupId>
    <artifactId>google-api-services-youtube</artifactId>
    <version>v3-rev20200423-1.30.9</version>
</dependency>
```

### google-auth-library-oauth2-http

```xml
<dependency>
    <groupId>com.google.auth</groupId>
    <artifactId>google-auth-library-oauth2-http</artifactId>
    <version>0.20.0</version>
</dependency>
```

### google-oauth-client-jetty


Jetty extensions to the Google OAuth Client Library for Java (google-oauth-client-jetty) support authorization code flow for installed applications. This module depends on google-oauth-client-java6.

Maven Usage:
```xml
<dependency>
  <groupId>com.google.oauth-client</groupId>
  <artifactId>google-oauth-client-jetty</artifactId>
  <version>1.30.4</version>
</dependency>
```

## Reference

* Youtube Data API: https://developers.google.com/youtube/v3/docs/videos/list (try the code generator)