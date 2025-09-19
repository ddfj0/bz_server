package ddfj0.few;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class SpringStopListener  implements ApplicationListener<ContextClosedEvent>  {

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent contextClosedEvent) {

        System.out.println("Stop...");
        
        if( MainApplication.m_threadTimer != null ) {
            MainApplication.m_threadTimer.fnClose();
        }
        
    }
}
