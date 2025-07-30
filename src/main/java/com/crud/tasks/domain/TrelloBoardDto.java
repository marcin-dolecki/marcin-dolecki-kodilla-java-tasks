package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*@AllArgsConstructor
@NoArgsConstructor
@Getter*/
// we can replace above annotations by @Data
@Data
public class TrelloBoardDto {
    private String id;
    private String name;
}
