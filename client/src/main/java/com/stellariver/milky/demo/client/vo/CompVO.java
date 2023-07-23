package com.stellariver.milky.demo.client.vo;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompVO {

    String compId;
    String date;
    String name;
    String stage;

}
