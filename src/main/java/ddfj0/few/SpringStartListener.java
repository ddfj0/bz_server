package ddfj0.few;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class SpringStartListener  implements ApplicationListener<ContextRefreshedEvent> {
    
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        System.out.println("Start...");
        if( MainApplication.m_sysConfig == null ) {  //// 会加载 config， 执行 timer
            SysCfg.fnSysConfigLoad();
        }
    }
}
