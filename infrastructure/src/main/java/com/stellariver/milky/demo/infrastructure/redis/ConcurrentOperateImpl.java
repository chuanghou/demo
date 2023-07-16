package com.stellariver.milky.demo.infrastructure.redis;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.domain.support.dependency.ConcurrentOperate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author houchuang
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConcurrentOperateImpl extends ConcurrentOperate {


    @Override
    @SneakyThrows
    protected Map<String, Result<Void>> batchTryLock(List<Pair<String, Duration>> lockParams) {
        return lockParams.stream().collect(Collectors.toMap(Pair::getKey, p -> Result.success()));
    }

    @Override
    protected Map<String, Result<Void>> batchUnLock(Set<String> unlockIds) {
        return unlockIds.stream().collect(Collectors.toMap(Function.identity(), lockId -> Result.success()));
    }

}
