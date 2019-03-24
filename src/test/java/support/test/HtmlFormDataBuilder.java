package support.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

public class HtmlFormDataBuilder {
    HttpHeaders header;
    MultiValueMap<String, Object> params;

    public HtmlFormDataBuilder(HttpHeaders header) {
        this.header = header;
        this.params = new LinkedMultiValueMap<>();
    }

    public HtmlFormDataBuilder addParameter(String key, Object value) {
        this.params.add(key, value);
        return this;
    }

    public HttpEntity<MultiValueMap<String, Object>> build() {
        return new HttpEntity<MultiValueMap<String, Object>>(params, header);
    }

    public static HtmlFormDataBuilder urlEncodedForm() {
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HtmlFormDataBuilder(header);
    }
}
