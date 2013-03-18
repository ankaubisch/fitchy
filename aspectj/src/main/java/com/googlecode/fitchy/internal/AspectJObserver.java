package com.googlecode.fitchy.internal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.googlecode.fitchy.AspectJFeatureContext;
import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.annotation.FeatureSwitch;
import com.googlecode.fitchy.exception.InvokeException;
import com.googlecode.fitchy.internal.AnnotatedMethodInvoker;
import com.googlecode.fitchy.resolver.FeatureResolverFactory;

import java.lang.reflect.Method;

/**
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 8/6/12
 * Time: 8:17 PM
 */
@Aspect
public class AspectJObserver {

    @Pointcut("execution(* *(..))")
    public void anyPublicMethod() {}

    @Around("anyPublicMethod() && @annotation(featureSwitch)")
    public Object aroundPublicMethodWithAnnotation(final ProceedingJoinPoint pjp, FeatureSwitch featureSwitch)  throws Throwable {
        FeatureContext context = AspectJFeatureContext.getInstance();
        FeatureResolverFactory resolverFactory = new FeatureResolverFactory(context);
        AnnotatedMethodInvoker.MethodInvoke invoke = new AnnotatedMethodInvoker.MethodInvoke() {
        	public Object invoke(Method method, Object[] args) throws InvokeException  {
                try {
					return pjp.proceed();
				} catch (Throwable e) {
					throw new InvokeException(e);
				}
            }
            
            public Class<?> getTargetClass() {
                return pjp.getTarget().getClass();
            }
        };
        AnnotatedMethodInvoker invoker = new AnnotatedMethodInvoker(invoke, resolverFactory);
        return invoker.invoke(((MethodSignature)pjp.getSignature()).getMethod(), pjp.getArgs());
    }
}
