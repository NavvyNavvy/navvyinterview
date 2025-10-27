import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class DynamicProxyTest {

    public static interface NavvyService {
        String execute(String param);
    }

    public static class NavvyServiceImpl implements NavvyService {
        @Override
        public String execute(String param) {
            log.info("原方法执行: {}", param);
            return "execute: " + param;
        }
    }

    public static class NavvyServiceInvocationHandler implements InvocationHandler {
        private NavvyService raw;

        public NavvyServiceInvocationHandler(NavvyService raw) {
            this.raw = raw;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log.info("前");
            method.invoke(raw, args);
            log.info("后");
            return "来自JDK代理类的返回值";
        }
    }


    /**
     * JDK的代理要求被代理类必须实现接口
     */
    @Test
    public void testJdkDynamicProxy() {
        NavvyService navvyService = new NavvyServiceImpl();
        NavvyService proxy = (NavvyService)Proxy.newProxyInstance(navvyService.getClass().getClassLoader(), navvyService.getClass().getInterfaces(), new NavvyServiceInvocationHandler(navvyService));
        String result = proxy.execute("test");
        log.info("result: {}", result);
    }


    public static class NavvyServiceImpl2  {
        public String execute(String param) {
            log.info("原方法执行: {}", param);
            return "execute: " + param;
        }
    }

    /**
     * CGLIB的代理要求被代理类不能是final类，也不能是final、private方法
     */
    @Test
    public void testCglibDynamicProxy() {
        NavvyServiceImpl2 navvyService2 = new NavvyServiceImpl2();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(NavvyServiceImpl2.class);
        enhancer.setCallback(new MethodInterceptor() {
            /**
             * @param o 代理对象
             * @param method 被代理的方法
             * @param objects 方法参数
             * @param methodProxy CGLIB的方法代理对象，用于调用被代理类的方法
             * @return 代理对象返回值
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                log.info("前");
                methodProxy.invokeSuper(o, objects);
                log.info("后");
                return "来自CGLIB代理类的返回值";
            }
        });
        NavvyServiceImpl2 navvyServiceImpl2 = (NavvyServiceImpl2)enhancer.create();
        String result = navvyServiceImpl2.execute("test");
        log.info("result: {}", result);
    }
}
