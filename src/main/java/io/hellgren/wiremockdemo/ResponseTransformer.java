package io.hellgren.wiremockdemo;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class ResponseTransformer extends ResponseDefinitionTransformer {

  public static final String NAME = "transformer-1";
  public static final boolean APPLY_GLOBALLY = false;

  @Override
  public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource,
      Parameters parameters) {
      return ResponseDefinitionBuilder.responseDefinition()
          .withStatus(200)
          .withHeader("Content-Type", "application/json; charset=utf-8")
          .withBody(FileHelper.readFileContent("/wiremock/responses/credit_ok_wrong_format.json"))
          .build();
  }

  @Override
  public String getName() {
      return NAME;
  }

  @Override
  public boolean applyGlobally() {
    return APPLY_GLOBALLY;
  }
}
