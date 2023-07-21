package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.GeneratorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratorMetaUnit extends AbstractMetaUnit{

    GeneratorType generatorType;

}
