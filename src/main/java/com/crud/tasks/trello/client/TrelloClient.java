package com.crud.tasks.trello.client;

import com.crud.tasks.domain.TrelloBoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TrelloClient {
    private final RestTemplate restTemplate;

    @Value("${trello.api.endpoint.prod}")
    private String trelloApiEndpoint;
    @Value("${trello.app.key}")
    private String trelloAppKey;
    @Value("${trello.app.token}")
    private String trelloToken;
    @Value("${trello.username}")
    private String trelloUsername;

    public List<TrelloBoardDto> getTrelloBoards() {

        URI url = buildTrelloBoardsUrl();

        TrelloBoardDto[] boardsResponse = restTemplate.getForObject(url, TrelloBoardDto[].class);

        return Optional.ofNullable(boardsResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    private URI buildTrelloBoardsUrl() {
        URI baseUri = URI.create(trelloApiEndpoint);

        return UriComponentsBuilder.newInstance()
                .scheme(baseUri.getScheme())
                .host(baseUri.getHost())
                .path(baseUri.getPath() + "/members/" + trelloUsername + "/boards")
                .queryParam("key", trelloAppKey)
                .queryParam("token", trelloToken)
                .queryParam("fields", "name, id")
                .build()
                .encode()
                .toUri();
    }
}