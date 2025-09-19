package ddfj0.few;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MainApplication {

	public static String m_curPath = "";
    public static SysCfg m_sysConfig = null;

    public static JsapiNative m_WePayNavive = null;
    public static JsapiMp m_WePayMp = null;
    public static ThreadTimer m_threadTimer = null;

    public static ArrayList<WechatOpenId> m_WechatOpenId = null;

    public static ConfigurableApplicationContext appCtx;

    public static void main(String[] args) {
        
        SpringApplication app = new SpringApplication(MainApplication.class);
		try {
			ApplicationHome home = new ApplicationHome(app.getClass());  
			File jarFile = home.getSource();  
			m_curPath =  jarFile.getParentFile().getPath(); 
		}
		catch(Exception ee) {
			System.out.println("获取启动路径出错！" + ee.getMessage());
		}

        //m_WePayNavive = new JsapiNative();
        //m_WePayNavive.fnStartService();
        
        m_WePayMp = new JsapiMp();
        m_WePayMp.fnStartService();

        m_WechatOpenId = new ArrayList<WechatOpenId>();

		app.setBannerMode(Banner.Mode.OFF);
		appCtx = app.run(args);
		//SpringApplication.run(TerminalApplication.class, args);
	}

	//// /dc?save=1
	//// return "alve yyyy-MM-dd hh:mm:ss"  
    @RequestMapping(value = { "/alive", "/api/alive"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String home(HttpServletRequest request) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return "Alive " + df.format(new Date());
	}

    public static void fnPrintStack() {
        Exception ee = new Exception("--- PrintStack --- : ");
        ee.printStackTrace();
    }

    public static boolean fnChallenge(Integer cmd) {
        boolean bRtn = false;
        //String strRtn;
        //String msg;
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

        bRtn = true;
        /*
        strRtn = HttpClientUtils.doGet( "https://vpp.loongcloud.store:6090/v2/auth?t=challenge");
        if(strRtn != null) {
            CWebRtn rtn = JSON.parseObject(strRtn,  CWebRtn.class);
            if( rtn != null) {
                if( rtn.code == 1000 ) {  //// 有命令
                    if( rtn.result.equals("challenge") ) {
                        bRtn = true;
                    }
                }
            }
            CLog.fnSaveLog("fnChallenge", "", strRtn, "fnChallenge(" + cmd.toString() + ")");
        }
        else {
            msg = df.format(new Date()) + " get Challenge error !";
            System.out.println(msg);
            CLog.fnSaveLog("fnChallenge", "", msg, "fnChallenge(" + cmd.toString() + ")");
        }
        */

        return bRtn;
    }
}
