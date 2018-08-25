package wannagohome.support;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class RequestEntity {
    private String url;
    private HttpEntity body;
    private HttpMethod method;
    private Class returnType;

    private RequestEntity() {

    }

    public String getUrl() {
        return url;
    }

    public HttpEntity getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Class getReturnType() {
        return returnType;
    }

    public static class Builder {
        private RequestEntity requestEntity;

        public Builder() {
            requestEntity = new RequestEntity();
        }

        public Builder withUrl(String url) {
            requestEntity.url = url;
            return this;
        }

        public Builder withBody(Object body) {
            requestEntity.body = creaetHttpEntity(body);
            return this;
        }

        public Builder withMethod(HttpMethod method) {
            requestEntity.method = method;
            return this;
        }

        public Builder withReturnType(Class returnType) {
            requestEntity.returnType = returnType;
            return this;
        }

        private HttpEntity creaetHttpEntity(Object body) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity(body, headers);
        }

        public RequestEntity build() {
            return requestEntity;
        }
    }
}