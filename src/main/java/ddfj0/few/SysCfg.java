package ddfj0.few;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;

public class SysCfg {

    public String loginUser;
    public String loginPasswd;

	public String bkpath;
	public Boolean autobak;

	public Boolean bCfgSqlite;
	public String spring_datasource_cfg_url;
	public String spring_datasource_cfg_username;
	public String spring_datasource_cfg_password;
	
	public String spring_datasource_Data_url;
	public String spring_datasource_Data_username;
	public String spring_datasource_Data_password;

	public String title;



	private Lock lock = new ReentrantLock();

	public SysCfg() {
		loginUser = "admin";
		loginPasswd = "827ccb0eea8a706c4c34a16891f84e7b"; //"12345";

		autobak = false;
		bCfgSqlite = true;

		bkpath = "/opt/bak";
		spring_datasource_cfg_username = "root";
		spring_datasource_cfg_password = "QQyy";
		spring_datasource_cfg_url = "jdbc:mysql://127.0.0.1:3306/dbpfdchf?useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
		spring_datasource_Data_url = "root";
		spring_datasource_Data_username = "QQyy";
		spring_datasource_Data_password = "jdbc:mysql://127.0.0.1:3306/dbpfdchf?useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
		title = "五行时空";

	}

	//#region tools
    public static void fnSysConfigLoad()
    {
        try {

			String strFile = MainApplication.m_curPath + "/syscfg.json";
			
			File file  = new File(strFile);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");   
            BufferedReader br = new BufferedReader(isr);
            String strb64 = br.readLine();
            br.close();
			isr.close();

			try {
				byte[] base64decodedBytes = Base64.getDecoder().decode(strb64);
				String strJson = new String(base64decodedBytes, "utf-8");
				MainApplication.m_sysConfig = JSON.parseObject(strJson, SysCfg.class);
			}
			catch(Exception ee) {
				System.out.println("解析系统配置文件失败。");
				MainApplication.m_sysConfig = new SysCfg();
			}

			if( MainApplication.m_sysConfig == null) {
				MainApplication.m_sysConfig = new SysCfg();
			}

			////// 启动定时任务
			//TerminalApplication.m_threadTimer = new ThreadTimer();			
			//TerminalApplication.m_threadTimer.fnStart();
		}
        catch(Exception ee) {
            System.out.println("加载系统配置文件出错..." + ee.getMessage());
			MainApplication.m_sysConfig = new SysCfg();

			//if( TerminalApplication.m_threadTimer == null) {
			//	TerminalApplication.m_threadTimer = new ThreadTimer();			
			//	TerminalApplication.m_threadTimer.fnStart();
			//}
        }
    }

	public boolean fnSysConfigSave() {
		String str;
		//// lock
		lock.lock();
		try {
			if( MainApplication.m_sysConfig != null) {
				str = JSON.toJSONString(MainApplication.m_sysConfig);
				try {
					String dev64 = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
					File file  = new File(MainApplication.m_curPath + "/syscfg.json");
					OutputStream outputStream = new FileOutputStream(file);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
					outputStreamWriter.write(dev64);
					outputStreamWriter.flush();
					outputStreamWriter.close();
					outputStream.close();

					System.out.println("保存系统配置文件成功。" + MainApplication.m_curPath + "/syscfg.json");
				}
				catch(Exception ee) {
					System.out.println("保存系统配置文件出错..." + ee.getMessage());
					return false;
				}			
			}
		}
		catch(Exception ee) {
			System.out.println("保存系统配置文件出错..." + ee.getMessage());
			return false;
		}
		lock.unlock();
		return true;
	}

	//#endregion

}

/////////////////////////////////