package cn.rivamed.log.trace.aspect;

import cn.rivamed.log.core.context.RivamedLogLabel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectConfig extends AbstractAspect {

    @Around("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
//    @Around("within(com.plumeorg..*))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        RivamedLogLabel rivamedLogLabel = new RivamedLogLabel();
        processProviderSide(rivamedLogLabel);
        return aroundExecute(joinPoint);
    }
}
