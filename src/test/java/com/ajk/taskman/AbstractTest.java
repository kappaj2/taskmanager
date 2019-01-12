package com.ajk.taskman;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.AbstractAssert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import static junit.framework.TestCase.assertSame;
import static org.assertj.core.api.Assertions.assertThat;


public class AbstractTest {

    protected static HttpHeaders headers = new HttpHeaders();
    protected Fixture fixture;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper mapper;

    @Before
    public void setup() {
        fixture = new Fixture();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.set(HttpHeaders.ACCEPT, "application/json");

    }

    public final class Fixture {

        private ResponseEntity response;
        private String url;
        private HttpHeaders headers = new HttpHeaders();


        public void givenURLIs(String url) {
            this.url = url;
        }

        public void withHttpHeaders(String name, String value) {
            headers.set(name, value);
        }

        public void whenExecuteHttpRequest(Class targetClass, HttpMethod method, HttpEntity<?> responseEntity, Object... urlVariables) {
            response = restTemplate.exchange(
                    UriComponentsBuilder.fromUriString(url).build().toString(),
                    method,
                    responseEntity,
                    targetClass,
                    urlVariables);
        }

        public void whenExecuteHttpRequest(Class targetClass, HttpMethod method, Object... urlVariables) {
            response = restTemplate.exchange(
                    UriComponentsBuilder.fromUriString(url).build().toString(),
                    method,
                    null,
                    targetClass,
                    urlVariables);
        }

        public void thenHttpResponseCodeMustBe(HttpStatus httpStatus) {
            assertSame(httpStatus, response.getStatusCode());
            assertThat(response.getStatusCode()).isEqualTo(httpStatus);
        }

        public void evaluate(AbstractAssert assertions) {
            assertThat(assertions);

        }

        public ResponseEntity<?> getResponse() {
            return this.response;
        }

        public Object getBody(TypeReference typeReference) {
            return mapper.convertValue(fixture.getResponse().getBody(), typeReference);
        }
    }


}
