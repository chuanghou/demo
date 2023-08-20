package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("unit_init_state")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitInitState {

  Long id;
  String dt;
  Boolean initState;
  Double initOutput;
  Double initHrUp;
  Double initHrDn;
  @TableId(type = IdType.INPUT)
  Integer unitId;

}
