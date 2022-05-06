package support.test;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HtmlFormDataBuilder {
	private HttpHeaders headers;
	private MultiValueMap<String, Object> params;

	public HtmlFormDataBuilder(HttpHeaders headers) {
		this.headers = headers;
		params = new LinkedMultiValueMap<>();
	}

	public HtmlFormDataBuilder addParameter(String key, Object value) {
		params.add(key, value);
		return this;
	}

	public HttpEntity<MultiValueMap<String, Object>> build() {
		return new HttpEntity<>(params, headers);
	}

	public static HtmlFormDataBuilder urlEncodeForm() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return new HtmlFormDataBuilder(headers);
	}
}
