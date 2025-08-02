package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrelloClient {
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);

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

        try {
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(url, TrelloBoardDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(p -> Objects.nonNull(p.getId()) && Objects.nonNull(p.getName()))
                    .filter(p -> p.getName().contains("Kodilla"))
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }

    }

    public CreatedTrelloCard createNewCard(TrelloCardDto trelloCardDto) {
        URI url = buildTrelloCardUrl(trelloCardDto);

        return restTemplate.postForObject(url, null, CreatedTrelloCard.class);
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
                .queryParam("lists", "all")
                .build()
                .encode()
                .toUri();
    }

    private URI buildTrelloCardUrl(TrelloCardDto trelloCardDto) {
        URI baseUri = URI.create(trelloApiEndpoint);

        return UriComponentsBuilder.newInstance()
                .scheme(baseUri.getScheme())
                .host(baseUri.getHost())
                .path(baseUri.getPath() + "/cards")
                .queryParam("key", trelloAppKey)
                .queryParam("token", trelloToken)
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .build()
                .encode()
                .toUri();
    }
}