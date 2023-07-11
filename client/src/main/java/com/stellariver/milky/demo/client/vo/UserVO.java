package com.stellariver.milky.demo.client.vo;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVO {
    String userId;
    String name;
    String role;
}
