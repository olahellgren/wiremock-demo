package io.hellgren.wiremockdemo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WireMockConfig {

  public static final String MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String RESPONSE_CREDIT_OK_JSON = "/wiremock/responses/credit_ok.json";
  public static final String TRANSFORMER_1 = "transformer-1";

  WireMockServer wireMockServer;

  @PostConstruct
  public void initialize() {
    wireMockServer = new WireMockServer(
        new WireMockConfiguration()
            .dynamicPort()
            .extensions(ResponseTransformer.class)
    );
    wireMockServer.start();
    stub();
  }

  public int getPort(){
    return wireMockServer.port();
  }

  @PreDestroy
  public void destroy() {
    wireMockServer.stop();
  }

  private void stub() {
    wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/notify"))
        .willReturn(
            WireMock.aResponse()
              .withStatus(200)
              .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8)
              .withBody(FileHelper.readFileContent(RESPONSE_CREDIT_OK_JSON))
        )
    );

    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/notify/transformer"))
        .willReturn(
            WireMock.aResponse()
              .withStatus(200)
              .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8)
              .withBody(FileHelper.readFileContent(RESPONSE_CREDIT_OK_JSON))
              .withTransformers(TRANSFORMER_1)
        )
    );

    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/notify/delay"))
        .willReturn(
            WireMock.aResponse()
              .withStatus(200)
              .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8)
              .withBody(FileHelper.readFileContent(RESPONSE_CREDIT_OK_JSON))
              .withChunkedDribbleDelay(5, 1000)
        )
    );

    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/notify/chunk-error"))
        .willReturn(
            WireMock.aResponse()
              .withStatus(200)
              .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8)
              .withBody(FileHelper.readFileContent(RESPONSE_CREDIT_OK_JSON))
              .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
        )
    );
  }

}

