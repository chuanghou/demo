package com.stellariver.milky.demo.adapter.controller.resp;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResp {
    String userId;
    String name;
    String role;
}
