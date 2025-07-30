package com.crud.tasks.trello.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TrelloClient {
    private final RestTemplate restTemplate;


}
