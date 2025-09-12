package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrelloMapperTest {

    private final TrelloMapper trelloMapper = new TrelloMapper();

    @Test
    void testMapToTrelloBoards() {
        List<TrelloListDto> listsDto = List.of(new TrelloListDto("1", "test_list", false), new TrelloListDto("2", "test_list2", true));
        List<TrelloBoardDto> boardsDto = List.of(new TrelloBoardDto("1", "test_board", listsDto));

        List<TrelloBoard> boards = trelloMapper.mapToTrelloBoards(boardsDto);

        assertThat(boards).hasSize(1);
        assertThat(boards.get(0).getId()).isEqualTo("1");
        assertThat(boards.get(0).getName()).isEqualTo("test_board");
        assertThat(boards.get(0).getLists()).hasSize(2);
        assertThat(boards.get(0).getLists().get(0).getName()).isEqualTo("test_list");
        assertThat(boards.get(0).getLists().get(1).getName()).isEqualTo("test_list2");
        assertThat(boards.get(0).getLists().get(0).isClosed()).isFalse();
        assertThat(boards.get(0).getLists().get(1).isClosed()).isTrue();
    }

    @Test
    void testMapToTrelloBoardsDto() {
        List<TrelloList> lists = List.of(new TrelloList("1", "test_list", false), new TrelloList("2", "test_list2", true));
        List<TrelloBoard> boards = List.of(new TrelloBoard("1", "test_board", lists));

        List<TrelloBoardDto> boardsDto = trelloMapper.mapToTrelloBoardsDto(boards);

        assertThat(boardsDto).hasSize(1);
        assertThat(boardsDto.get(0).getId()).isEqualTo("1");
        assertThat(boardsDto.get(0).getName()).isEqualTo("test_board");
        assertThat(boardsDto.get(0).getLists()).hasSize(2);
        assertThat(boardsDto.get(0).getLists().get(0).getName()).isEqualTo("test_list");
        assertThat(boardsDto.get(0).getLists().get(1).getName()).isEqualTo("test_list2");
        assertThat(boardsDto.get(0).getLists().get(0).isClosed()).isFalse();
        assertThat(boardsDto.get(0).getLists().get(1).isClosed()).isTrue();
    }

    @Test
    void testMapToTrelloCard() {
        TrelloCardDto cardDto = new TrelloCardDto("Card1", "description1", "top", "list123");

        TrelloCard card = trelloMapper.mapToTrelloCard(cardDto);

        assertThat(card.getName()).isEqualTo("Card1");
        assertThat(card.getDescription()).isEqualTo("description1");
        assertThat(card.getPos()).isEqualTo("top");
        assertThat(card.getListId()).isEqualTo("list123");
    }

    @Test
    void testMapToTrelloCardDto() {
        TrelloCard card = new TrelloCard("Card1", "description1", "top", "list123");

        TrelloCardDto cardDto = trelloMapper.mapToTrelloCardDto(card);

        assertThat(cardDto.getName()).isEqualTo("Card1");
        assertThat(cardDto.getDescription()).isEqualTo("description1");
        assertThat(cardDto.getPos()).isEqualTo("top");
        assertThat(cardDto.getListId()).isEqualTo("list123");
    }
}
