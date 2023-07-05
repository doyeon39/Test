package com.team4.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Spring method에서 비동기 기능을 사용가능하게 활성화 한다.
public class AsyncConfig implements AsyncConfigurer {

    /*
        ★★★★★★★
        setCorePoolSize이 다차면 setQueueCapacity에 쌓아둔다.
        그러다가 setQueueCapacity가 다차면 그때 max만큼 스레드를 생성해서 처리하게된다.
    */
    @Bean("signUpEmailSender")
    public Executor signUpEmailSender() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //기본적으로 대기 중인 Thread 수(기본 스레드 수)
        executor.setMaxPoolSize(5); //동시에 동작하는 최대 Thread 수
        //setCorePoolSize 초과 요청에서 Thread 생성 요청 시,
        //해당 요청을 Queue에 저장하는데 이때 수용가능한 Queue의 수
        // Queue에 저장했다가 Thread에 자리가 생기면 하나씩 빼감
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("authMail"); // 생성되는 Thread 접두사
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        //이거 안쓰면 사용못함 tomcat의 servlet에서 init으로 처음서버실행할 때 초기설정? 해주는거처럼 생각해라
        executor.initialize();
        return executor;
    }
}
