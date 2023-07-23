package com.stellariver.milky.demo.infrastructure.redis;

import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.domain.support.dependency.ConcurrentOperate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author houchuang
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConcurrentOperateImpl extends ConcurrentOperate {


    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    static private final String compClassName = "com.stellariver.milky.demo.domain.Comp";

    @Override
    @SneakyThrows
    protected Map<String, Result<Void>> batchTryLock(List<Pair<String, Duration>> lockParams) {
        Map<String, Result<Void>> resultMap = new HashMap<>();
        lockParams.forEach(lockParam -> {
            String key = lockParam.getLeft();
            boolean b = key.startsWith(compClassName);
            if (b) {
                resultMap.put(key, Result.success());
            } else {
                ReentrantLock reentrantLock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
                if (reentrantLock.tryLock()) {
                    resultMap.put(key, Result.success());
                } else {
                    resultMap.put(key, Result.error(ErrorEnums.CONCURRENCY_VIOLATION.message("不要操作太快"), ExceptionType.BIZ));
                }
            }
        });
        return resultMap;
    }

    @Override
    protected Map<String, Result<Void>> batchUnLock(Set<String> unlockIds) {
        Map<String, Result<Void>> resultMap = new HashMap<>();
        unlockIds.forEach(key -> {
            boolean b = key.startsWith(compClassName);
            if (b) {
                resultMap.put(key, Result.success());
            } else {
                ReentrantLock reentrantLock = lockMap.get(key);
                if (reentrantLock == null) {
                    throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
                } else {
                    reentrantLock.unlock();
                    resultMap.put(key, Result.success());
                }
            }
        });
        return resultMap;
    }

}
