package com.crud.tasks.controller;

import com.crud.tasks.domain.*;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrelloControllerTest {

    @Mock
    private TrelloFacade trelloFacade;

    private TrelloController trelloController;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        trelloController = new TrelloController(trelloFacade);
        mockMvc = MockMvcBuilders.standaloneSetup(trelloController)
                .setControllerAdvice(new GlobalHttpErrorHandler())
                .build();
    }

    @Test
    void shouldFetchTrelloBoards() throws Exception {
        TrelloListDto listDto = new TrelloListDto("1", "list", false);
        TrelloBoardDto boardDto = new TrelloBoardDto("1", "test", List.of(listDto));
        when(trelloFacade.fetchTrelloBoards()).thenReturn(List.of(boardDto));

        mockMvc.perform(get("/v1/trello/boards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].lists[0].id").value("1"));
    }

    @Test
    void shouldReturnEmptyListWhenNoBoards() throws Exception {
        when(trelloFacade.fetchTrelloBoards()).thenReturn(List.of());

        mockMvc.perform(get("/v1/trello/boards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldCreateTrelloCard() throws Exception {
        TrelloCardDto cardDto = new TrelloCardDto("Card name", "description", "top", "1");
        Badges badges = new Badges();
        badges.setVotes(5);
        badges.setSubscribed(true);
        CreatedTrelloCardDto created = new CreatedTrelloCardDto("323", "Card name", "http://test.com", badges);
        when(trelloFacade.createCard(any(TrelloCardDto.class))).thenReturn(created);

        mockMvc.perform(post("/v1/trello/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("323"))
                .andExpect(jsonPath("$.name").value("Card name"))
                .andExpect(jsonPath("$.shortUrl").value("http://test.com"));
    }

    @Test
    void whenFacadeThrows_thenServerError() throws Exception {
        TrelloCardDto cardDto = new TrelloCardDto("Card name", "description", "top", "1");
        when(trelloFacade.createCard(any(TrelloCardDto.class))).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post("/v1/trello/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().is5xxServerError());
    }
}
