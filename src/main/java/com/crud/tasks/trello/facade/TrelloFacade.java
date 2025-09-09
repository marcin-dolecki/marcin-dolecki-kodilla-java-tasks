package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.service.TrelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrelloFacade {
    private final TrelloService trelloService;

    public List<TrelloBoard> fetchTrelloBoards() {
        List<TrelloBoardDto> trelloBoards = trelloService.fetchTrelloBoards();
    }
}
