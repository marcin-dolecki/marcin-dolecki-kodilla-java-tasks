package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class TrelloFacade {
    private final TrelloService trelloService;
    private final TrelloMapper trelloMapper;

    public List<TrelloBoard> fetchTrelloBoards() {
        return trelloMapper.mapToTrelloBoards(trelloService.fetchTrelloBoards());
    }
}
