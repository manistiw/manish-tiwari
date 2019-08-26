package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestRequestSpecBuilder {
    private RequestSpecBuilder reqBuilder;
    private ContentType conType;

    @Value("${environment.baseurl}")
    private String baseUrl;

    @Autowired
    public RestRequestSpecBuilder(@Value("${environment.baseurl}") String baseUrl) {
        this.reqBuilder = new RequestSpecBuilder();
        this.reqBuilder.setContentType(ContentType.JSON);
        this.reqBuilder.addHeader("Content-Type", "application/json");
        this.reqBuilder.setBaseUri(baseUrl);
    }
    public RestRequestSpecBuilder() {
    }
    public RequestSpecBuilder getReqBuilder() {
        return this.reqBuilder;
    }
    public void setReqBuilder(RequestSpecBuilder reqBuilder) {
        this.reqBuilder = reqBuilder;
        this.reqBuilder.setContentType(ContentType.JSON);
        this.reqBuilder.setBaseUri(baseUrl);
    }
    public void setQueryParams(Map<String, String> queryParam) {
        this.reqBuilder.addQueryParams(queryParam);
    }
}
