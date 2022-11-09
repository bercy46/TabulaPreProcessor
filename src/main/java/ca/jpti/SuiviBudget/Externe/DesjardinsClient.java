package ca.jpti.SuiviBudget.Externe;

import ca.jpti.SuiviBudget.Main.TransactionReport;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Component
@Slf4j
public class DesjardinsClient {
    private String baseUrl = "https://accweb.mouv.desjardins.com";
    private String uri = "/identifiantunique/identification?domaineVirtuel=desjardins&langueCible=fr";
    private WebClient webClient = null;

    public TransactionReport getVISAInfiniteReport() {
        TransactionReport transactionReport = null;

        HttpClient httpClient = HttpClient
                .create()
                .followRedirect(true)
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .build();
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(uri);
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("");
        Mono<String> response = headersSpec.retrieve()
                .bodyToMono(String.class);
        log.info("Home page" + response.block());
        return transactionReport;
    }

}
