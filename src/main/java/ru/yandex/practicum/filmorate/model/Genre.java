package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {


    private Integer id;
    private String name;

}