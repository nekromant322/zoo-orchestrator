package com.nekromant.zoo.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class MetricAspect {
    Map<String, Integer> numberOfVisit = new HashMap<>();

    @Pointcut("@annotation(com.nekromant.zoo.config.aspect.Metric)")
    public void countVisits() {
    }

    @Around("countVisits()")
    public Object count(ProceedingJoinPoint call) throws Throwable {

        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        String key = method.getAnnotation(Metric.class).value();
        if (numberOfVisit.containsKey(key)) {
            int count = numberOfVisit.get(key);
            count++;
            numberOfVisit.put(key, count);
        } else {
            numberOfVisit.put(key, 1);
        }
        for (Map.Entry<String, Integer> set : numberOfVisit.entrySet()) {
            System.out.println(set.getKey() + "-" + set.getValue());
        }
        return call.proceed();
    }
}
