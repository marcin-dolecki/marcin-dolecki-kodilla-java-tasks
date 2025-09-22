package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.CreatedTrelloCardDto;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrelloServiceTest {

    @Mock
    private TrelloClient trelloClient;

    @Mock
    private SimpleEmailService emailService;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private TrelloService trelloService;

    @Test
    void shouldFetchTrelloBoards() {
        TrelloBoardDto board = new TrelloBoardDto("1", "Test board", List.of());
        when(trelloClient.getTrelloBoards()).thenReturn(List.of(board));

        List<TrelloBoardDto> result = trelloService.fetchTrelloBoards();

        assertEquals(1, result.size());
        assertEquals("Test board", result.get(0).getName());
    }

    @Test
    void shouldCreateTrelloCardAndSendEmail() {
        TrelloCardDto cardDto = new TrelloCardDto("Card name", "description", "top", "1");
        CreatedTrelloCardDto createdCard = new CreatedTrelloCardDto("1", "Card name", "url", null);

        when(trelloClient.createNewCard(cardDto)).thenReturn(createdCard);
        when(adminConfig.getAdminMail()).thenReturn("test@test.com");

        CreatedTrelloCardDto result = trelloService.createTrelloCard(cardDto);

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(emailService, times(1)).send(any(Mail.class));
    }

    @Test
    void shouldNotSendEmailWhenCardIsNull() {
        TrelloCardDto cardDto = new TrelloCardDto("Card name", "description", "top", "1");
        when(trelloClient.createNewCard(cardDto)).thenReturn(null);

        CreatedTrelloCardDto result = trelloService.createTrelloCard(cardDto);

        assertNull(result);
        verify(emailService, never()).send(any(Mail.class));
    }
}
