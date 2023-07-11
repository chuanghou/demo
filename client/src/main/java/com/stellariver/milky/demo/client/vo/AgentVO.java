package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.client.vo.UnitVO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentVO {

    String userId;
    String userName;
    List<UnitVO> unitVOs;

}
