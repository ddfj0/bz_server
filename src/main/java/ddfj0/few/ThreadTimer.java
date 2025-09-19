package ddfj0.few;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

//// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 周期任务，1、备份
@Service
public class ThreadTimer implements Runnable {

    public String lastTime;
    public Calendar calendarHour;
    public Calendar calendarMinute;
    public Calendar calendar10s;

    //// @Autowired
    //// private SysJdbcCfg sysdbTimer;

    private  Thread m_thread;

    public ThreadTimer() {
    }

    @PostConstruct
    public void init() {
        fnStart();
        MainApplication.m_threadTimer = this;
    }


    public void fnStart() {
        System.out.println( " 开始定时任务...");

        m_thread = new Thread(this);
        m_thread.start();
    }

    public void fnClose() {
        System.out.println( " 停止定时任务。");         
        m_thread.interrupt();

    }

    @Override
    public void run() {  

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

        calendarHour = Calendar.getInstance();
        calendar10s = Calendar.getInstance();
        calendar10s.add(Calendar.SECOND, 10);
        calendarMinute = Calendar.getInstance();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                fnRoutine10s();
                fnRoutineMinute();
                fnRoutineHour();
            } 
            catch (Exception ee) {
                lastTime = df.format(new Date());
                //System.out.println(lastTime + " ServerHttps run error : " + svrUrl + " " + ee.getMessage());
                try {
                    Thread.sleep(3000);
                }
                catch(InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                catch(Exception eee) {}
            }
        }
        lastTime = df.format(new Date());
        System.out.println(lastTime + " ThreadTimer thead exited. ");

    }

    public void fnRoutine10s() {
        Calendar cad = Calendar.getInstance();
        
        if(  calendar10s.compareTo(cad) < 0) {
            //System.out.println("fnRoutineHour : 每 10s 执行一次");
            calendar10s = Calendar.getInstance();
            calendar10s.add(Calendar.SECOND, 10);
        
        }
    }

    public void fnRoutineMinute() {
        Calendar cad = Calendar.getInstance();
        if( cad.get(Calendar.MINUTE) != calendarMinute.get(Calendar.MINUTE)) {
            //System.out.println("fnRoutineMinute : 每 1m 执行一次");
            calendarMinute = Calendar.getInstance();
            
            if( MainApplication.m_WechatOpenId != null) {
                for(WechatOpenId wco : MainApplication.m_WechatOpenId) {
                    if( wco.fnMoveExpired() ) {
                        MainApplication.m_WechatOpenId.remove(wco);
                    }
                }
            }   
        }
    }

    public void fnRoutineHour() {
        Calendar cad = Calendar.getInstance();
        if( cad.get(Calendar.HOUR_OF_DAY) != calendarHour.get(Calendar.HOUR_OF_DAY)) {
            //System.out.println("fnRoutineHour : 每 1h 执行一次");
            calendarHour = Calendar.getInstance();
        
            fnBackupTimer();

            MainApplication.fnChallenge(60);
            //if( TerminalApplication.fnChallenge(60) == false ) {
            //    SpringApplication.exit(TerminalApplication.appCtx);
            //}    
        }
    }

    ///// 每小时备份一次
    public void fnBackupTimer() {
		String cmd, bkpathname;
        String month, day;

        if( MainApplication.m_sysConfig != null) {
            if( MainApplication.m_sysConfig.autobak == false ) {
                return;
            }
        }

        Calendar cad = Calendar.getInstance();

        month = String.format( "%02d", cad.get(Calendar.MONTH) + 1);
        day = String.format( "%02d", cad.get(Calendar.DAY_OF_MONTH));
        bkpathname = MainApplication.m_sysConfig.bkpath + "/emsbk" + String.valueOf(cad.get(Calendar.YEAR)) + month + day + ".tar.gz";

        ////删除现有备份文件
        cmd = "rm -rf " + bkpathname;
        try {
            //System.out.println(cmd);
            fnProcessExec(cmd);
        }
        catch(Exception ee) {
            System.out.println("fnBackupTimer(): " + cmd + " " + ee.getMessage());
        }

        ////进行备份
        cmd = "tar zcvf " + bkpathname + " " + MainApplication.m_curPath;
        try {
            //System.out.println(cmd);
            fnProcessExec(cmd);
        }
        catch(Exception ee) {
            System.out.println("fnBackupTimer(): " + cmd + " " + ee.getMessage());
        }
    
        System.out.println("fnBackupTimer(): 备份成功 ！ " + cmd );
    }

    public static void fnProcessExec(String cmd) {  //// 要求命令前台执行，有stdout。
        Thread thread = new Thread() {
            public void run() {
                try {
                    Process proc = Runtime.getRuntime().exec(cmd);
                    InputStream stderr = proc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    int exitVal = proc.waitFor();
                    System.out.println("Process exitValue: " + exitVal);
                }
                catch(Exception ee) {
                    System.out.println("fnProcessExec(): " + cmd + " " + ee.getMessage());
                }
            }
        };
        thread.start();
    }

}
