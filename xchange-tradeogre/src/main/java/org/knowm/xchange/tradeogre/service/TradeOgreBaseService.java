package org.knowm.xchange.tradeogre.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.knowm.xchange.client.ClientConfigCustomizer;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.tradeogre.TradeOgreAuthenticated;
import org.knowm.xchange.tradeogre.TradeOgreExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.ClientConfigUtil;
import si.mazi.rescu.serialization.jackson.DefaultJacksonObjectMapperFactory;

public class TradeOgreBaseService extends BaseExchangeService<TradeOgreExchange>
    implements BaseService {

  protected final Logger LOG = LoggerFactory.getLogger(getClass());

  protected final TradeOgreAuthenticated tradeOgre;
  protected final String base64UserPwd;
  protected final String apiKey;
  protected final String secretKey;

  protected TradeOgreBaseService(TradeOgreExchange exchange) {

    super(exchange);

    apiKey = exchange.getExchangeSpecification().getApiKey();
    secretKey = exchange.getExchangeSpecification().getSecretKey();

    base64UserPwd = calculateBase64UserPwd(exchange);
    tradeOgre =
        ExchangeRestProxyBuilder.forInterface(
                TradeOgreAuthenticated.class, exchange.getExchangeSpecification())
            .build();
  }

  private String calculateBase64UserPwd(TradeOgreExchange exchange) {
    if (apiKey == null || secretKey == null) {
      throw new IllegalArgumentException("API key and secret key must not be null");
    }
    String userPwd = this.apiKey + ":" + this.secretKey;
    String encodedString = Base64.getEncoder().encodeToString(userPwd.getBytes());
    return "Basic " + encodedString;
  }
}
