package com.crud.tasks.controller;

import com.crud.tasks.trello.facade.TrelloFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TrelloController.class)
class TrelloControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrelloFacade trelloFacade;

    @Test
    void shouldFetchTrelloBoards() throws Exception {
        //Given
        when(trelloFacade.fetchTrelloBoards()).thenReturn(List.of());

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/trello/boards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }
}