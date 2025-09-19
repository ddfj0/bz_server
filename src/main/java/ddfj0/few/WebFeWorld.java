package ddfj0.few;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;


//
//  八字后台功能
//
//
@RestController
public class WebFeWorld {

    private static final Logger log = LoggerFactory.getLogger(WebFeWorld.class);

    @Resource
    private Producer producer;
	
    @Autowired
    private SysJdbcCfg sysdb;

//#region 登录 	
    @RequestMapping(value = { "/kaptcha", "/api/kaptcha"})
    public void fnKaptchaImage(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        final HttpSession sessoin = request.getSession();
        
        //生成文字验证码
        String text = producer.createText();

        //个位数字相加
        String s1 = text.substring(0, 1);
        String s2 = text.substring(1, 2);
        Integer count = Integer.valueOf(s1).intValue() + Integer.valueOf(s2).intValue();

		sessoin.setAttribute("kaptcha", count.toString());

        //生成图片验证码
        BufferedImage image = producer.createImage(s1 + "+" + s2 + "=?");

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

	@RequestMapping(value = { "/user/login", "/api/user/login"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserLogin(final HttpServletRequest request, final HttpServletResponse response) {
		String sAcc;
		String sPwd;
		String sKap, ssKap;

		final String sIp = fnGetIpAddr(request);

		final HttpSession sessoin = request.getSession();
		CWebRtn rmsg;
		String sRtn = "";

		try{
			ssKap = sessoin.getAttribute("kaptcha").toString();
		}
		catch(Exception ee) {
			rmsg = new CWebRtn(1099, " 请刷新验证码 !", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}

		//if( TerminalApplication.fnChallenge(1) == false ) {
		//	ssKap = ssKap + "1";
		//}

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		if ("POST".equals(request.getMethod())) {
			try {
				final BufferedReader br = request.getReader();
				String str, wholeStr = "";
				while ((str = br.readLine()) != null) {
					wholeStr += str;
				}

				JSONObject jsonRtn = JSON.parseObject(wholeStr);
				sAcc = jsonRtn.getString("user");
				sPwd = jsonRtn.getString("passwd");
				sKap = jsonRtn.getString("kaptcha");

			} catch (final Exception ee) {
				ee.printStackTrace();
				rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
		} else {
			sAcc = request.getParameter("user"); /// account
			sPwd = request.getParameter("passwd"); /// account
			sKap = request.getParameter("kaptcha");
		}

		if( !ssKap.equals(sKap))
			rmsg = new CWebRtn(1099, " 验证码错 !", "{}");
		else if (sAcc == null)
			rmsg = new CWebRtn(1001, " 名错 !", "{}");
		else if (sPwd == null)
			rmsg = new CWebRtn(1001, " 字错 !", "{}");
		else {
			tbUser user = fnUserCheck(sAcc, sPwd );
			if( user == null ) {
				rmsg = new CWebRtn(1001, sAcc + " 名或者字错误 !", "{}");
			}
			else {
				sessoin.setAttribute("user_id", user.user_id);
				sessoin.setAttribute("usr", sAcc);
				sessoin.setAttribute("pwd", sPwd);
				sessoin.setAttribute("clt", sIp);
				sessoin.setAttribute("time", sTime);

				rmsg = new CWebRtn(1000, sAcc + " welcome !", JSON.toJSONString(user));
				log.info(sAcc + " 登录成功。");

				tbUserLogin ul = new tbUserLogin();
				ul.user_id = user.user_id;
				ul.login_ip = sIp;
				ul.login_time = sTime;
				sysdb.fnInsertUserLogin(ul);
			}
		}
		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/logout", "/api/user/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserLogout(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn;
		final HttpSession sessoin = request.getSession();

		sessoin.removeAttribute("user_id");
		sessoin.removeAttribute("usr");
		sessoin.removeAttribute("pwd");
		sessoin.removeAttribute("clt");
		sessoin.removeAttribute("time");

		rmsg = new CWebRtn(1000, " logout ok !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	@RequestMapping(value = { "/user/getlogin", "/api/user/getlogin"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserLoginGet(final HttpServletRequest request, final HttpServletResponse response) {

		CWebRtn rmsg;
		String sRtn = "";
		Long acc;

		if ( fnCheckSession(request)) {
			rmsg = new CWebRtn(1099, " get cur user error !", sRtn );

			final HttpSession sessoin = request.getSession();
			try {
				acc = Long.parseLong(sessoin.getAttribute("user_id").toString());
			}
			catch(Exception ee) {
				acc = 0L;
			}

			if( sysdb != null) {
				tbUser user = sysdb.fnGetUserByID(acc);
				if( user != null) {
					user.user_passwd = "";
					user.user_rtncode = "";
					sRtn = JSON.toJSONString(user);
					rmsg = new CWebRtn(1000, " get cur user  ok !", sRtn );	
				}
			}
		}
		else {
			rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
		}

		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/browse/compass", "/api/user/browse/compass"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserBrowserCompass(final HttpServletRequest request, final HttpServletResponse response) {
		//// 匿名浏览罗盘
		String key = "";
		String sRtn = "";
		CWebRtn rmsg;

		final String sIp = fnGetIpAddr(request);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		tbUserLogin ul = new tbUserLogin();
		ul.user_id = 3L;
		ul.login_ip = sIp;
		ul.login_time = sTime;
		sysdb.fnInsertUserLogin(ul);

		final HttpSession sessoin = request.getSession();
		key = "compass_" + sIp + "_" + sTime;
		sessoin.setAttribute("compassKey", key);

		//// 用key生成一个md5，返回给前端
		@SuppressWarnings("null")
		String md5Str = DigestUtils.md5DigestAsHex(key.getBytes());

		rmsg = new CWebRtn(1000, " get compass key  ok !", "{\"key\":\"" + md5Str + "\"}" );	
		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/browse/feworld", "/api/user/browse/feworld"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserBrowserFeworld(final HttpServletRequest request, final HttpServletResponse response) {
		//// 匿名浏览排盘
		String key = "";
		String sRtn = "";
		CWebRtn rmsg;

		final String sIp = fnGetIpAddr(request);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		final HttpSession sessoin = request.getSession();
		key = "paipan_" + sIp + "_" + sTime;
		sessoin.setAttribute("paipanKey", key);

		//// 用key生成一个md5，返回给前端
		@SuppressWarnings("null")
		String md5Str = DigestUtils.md5DigestAsHex(key.getBytes());

		rmsg = new CWebRtn(1000, " get paipan key  ok !", "{\"key\":\"" + md5Str + "\"}" );	
		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

//#endregion

//#region user manager 
	///////////---------------------- user manager --------------------------------------------

	@RequestMapping(value = { "/user/list", "/api/user/list"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserList(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn = "";

		if ( fnCheckSession(request)) {
			rmsg = new CWebRtn(1099, " get user list error !", sRtn );
			if( sysdb != null) {
				sRtn = JSON.toJSONString(sysdb.fnGetUserList());
				rmsg = new CWebRtn(1000, " get user list ok !", sRtn );
			}
		}
		else {
			rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
		}

		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/add", "/api/user/add"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserAdd(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn = "";
		tbUser user;
		String sAcc;
		String sPwd;
		String sKap, ssKap;
		String smsg = "";
		String rtncode;

		if ("POST".equals(request.getMethod())) {
			try {
				final BufferedReader br = request.getReader();
				String str, wholeStr = "";
				while ((str = br.readLine()) != null) {
					wholeStr += str;
				}

				JSONObject jsonRtn = JSON.parseObject(wholeStr);
				sAcc = jsonRtn.getString("user_name");
				sPwd = jsonRtn.getString("user_passwd");
				sKap = jsonRtn.getString("kaptcha");
				rtncode = jsonRtn.getString("user_rtncode");

			} catch (final Exception ee) {
				ee.printStackTrace();
				rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
		} else {
			sAcc = request.getParameter("user_name"); /// account
			sPwd = request.getParameter("user_passwd"); /// account
			sKap = request.getParameter("kaptcha");
			rtncode = request.getParameter("user_rtncode");
		}

		if (sAcc == null) {
			rmsg = new CWebRtn(1001, " 名为空 !", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
		else if (sPwd == null) {
			rmsg = new CWebRtn(1001, " 字为空 !", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}

		try{
			ssKap = request.getSession().getAttribute("kaptcha").toString();
		}
		catch(Exception ee) {
			rmsg = new CWebRtn(1099, " 验证码出错 " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}

		if( sKap == null || sKap.isEmpty() ) {  //// 无验证码，登录后添加用户
			if ( fnCheckSession(request) == false ) {
				rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
			smsg = "添加";
		}
		else {  //// 有验证码，是登录页面注册用户
			if( !ssKap.equals(sKap)) {
				rmsg = new CWebRtn(1099, " 验证码错误 !", "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
			smsg = "注册";
		}

		rmsg = new CWebRtn(1099, smsg + " 用户出错 !", sRtn );
		if( sysdb != null) {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String sTime = df.format(new Date());	
	
			user = new tbUser();
			user.user_name = sAcc;
			user.user_passwd = sPwd;
			user.user_rtncode = rtncode;
			user.birthday = "";
			user.level = "0";
			user.pay_time = "";
			user.reg_ip = fnGetIpAddr(request);
			user.reg_time = sTime;
		
			if( sysdb.fnInsertUser(user) == true) {
				rmsg = new CWebRtn(1000, smsg + " 用户成功!", "{}");
			}
			else {
				rmsg = new CWebRtn(1004, smsg + " 用户失败!", "{}");
			}
		}

		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/edit", "/api/user/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserEdit(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn = "";
		tbUser user;

		if ("POST".equals(request.getMethod())) {
			try {
				final BufferedReader br = request.getReader();
				String str, wholeStr = "";
				while ((str = br.readLine()) != null) {
					wholeStr += str;
				}

				user = JSON.parseObject(wholeStr, tbUser.class);
			} catch (final Exception ee) {
				ee.printStackTrace();
				rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
		} else {
			rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}

		if ( fnCheckSession(request)) {
			rmsg = new CWebRtn(1099, " update user error !", sRtn );
			if( sysdb != null) {
				sysdb.fnUpdateUser(user);
				rmsg = new CWebRtn(1000, " 用户更新成功!", "{}");
			}
		}
		else {
			rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
		}

		sRtn = JSON.toJSONString(rmsg);

		return sRtn;
	}

	@RequestMapping(value = { "/user/del", "/api/user/del"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserDel(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn = "";
		Long sAcc;

		if ("POST".equals(request.getMethod())) {
			try {
				final BufferedReader br = request.getReader();
				String str, wholeStr = "";
				while ((str = br.readLine()) != null) {
					wholeStr += str;
				}

				JSONObject jsonRtn = JSON.parseObject(wholeStr);
				sAcc = jsonRtn.getLong("user_id");
			} catch (final Exception ee) {
				ee.printStackTrace();
				rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
		} else {
			try{
				sAcc = Long.parseLong(request.getParameter("user_id")); /// account
			}
			catch(Exception ee) {
				sAcc = 0L;
			}
		}

		if ( fnCheckSession(request)) {
			rmsg = new CWebRtn(1099, " del user error !", sRtn );
			if( sysdb != null) {
				sysdb.fnDeleteUser(sAcc);
				rmsg = new CWebRtn(1000, sAcc + " 用户删除成功 !", "{}");
			}
		}
		else {
			rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
		}

		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	@RequestMapping(value = { "/user/changepasswd", "/api/user/changepasswd"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String fnUserChangePasswd(final HttpServletRequest request, final HttpServletResponse response) {
		CWebRtn rmsg;
		String sRtn = "";
		tbUser user;

		if ("POST".equals(request.getMethod())) {
			try {
				final BufferedReader br = request.getReader();
				String str, wholeStr = "";
				while ((str = br.readLine()) != null) {
					wholeStr += str;
				}

				user = JSON.parseObject(wholeStr, tbUser.class);
			} catch (final Exception ee) {
				ee.printStackTrace();
				rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
				sRtn = JSON.toJSONString(rmsg);
				return sRtn;
			}
		} else {
			rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}

		if ( fnCheckSession(request)) {
			rmsg = new CWebRtn(1099, " change user passwd error !", sRtn );
			if( sysdb != null) {
				Long user_id = (Long)request.getSession().getAttribute("user_id");
				tbUser cur = sysdb.fnGetUserByID(user_id);

				if( cur != null && cur.user_passwd.equals(user.user_passwd) ) {
					cur.user_passwd = user.user_rtncode;
					if( sysdb.fnUpdateUser(cur) )
						rmsg = new CWebRtn(1000, " 更改密码成功!", "{}");
	
				}
				else {
					rmsg = new CWebRtn(1099, " 更新失败，原密码错误 !", sRtn );
				}
			}
		}
		else {
			rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
		}

		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}


//#endregion

//#region user pay 

@RequestMapping(value = { "/user/pay/list", "/api/user/pay/list"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayList(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " get user pay list error !", sRtn );
		if( sysdb != null) {
			Long user_id = (Long)request.getSession().getAttribute("user_id");
			if( user_id != null && user_id > 0) {
				sRtn = JSON.toJSONString(sysdb.fnGetShortUserPayListByUserId(user_id));
				rmsg = new CWebRtn(1000, " get user pay list ok !", sRtn );	
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/pay/fate", "/api/user/pay/fate"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayFate(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取登录用户，选择的一个命理分析结果
	CWebRtn rmsg;
	String sRtn = "";
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sAcc = jsonRtn.getLong("user_pay_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			sAcc = Long.parseLong(request.getParameter("user_pay_id")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " get user pay info error !", sRtn );
		if( sysdb != null) {
			sRtn = JSON.toJSONString(sysdb.fnGetUserPayListById(sAcc));
			rmsg = new CWebRtn(1000, " get user pay info ok !", sRtn );
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/pay/add", "/api/user/pay/add"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayFateAdd(final HttpServletRequest request, final HttpServletResponse response) {
	//// 进行一次新的命理分析
	CWebRtn rmsg;
	String sRtn = "";

	tbUserPay user;
	String birthday;
	String sex;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
 
			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			birthday = jsonRtn.getString("birthday");
			sex = jsonRtn.getString("sex");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " add user pay error !", sRtn );
		if( sysdb != null) {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String sTime = df.format(new Date());
			user = new tbUserPay();
			user.birthday = birthday;
			user.sex = sex;
			user.user_id = (Long)request.getSession().getAttribute("user_id");
			user.pay_ip = fnGetIpAddr(request);
			user.pay_time = sTime;
			user.fate_self = "未分析";

			//sysdb.fnInsertUserPay(user);
			Long key = sysdb.fnInsertUserPayRtnKey(user);
			
			String orderNo = "bz_" + key.toString() + "_" + birthday.replace(' ', '_');
			String orderName = request.getSession().getAttribute("usr").toString() + birthday + " 分析";

			user.user_pay_id = key;
			user.pay_order = orderNo;
			user.pay_info = "未支付";
			sysdb.fnUpdateUserPay(user);

			int amount = 1000;
			String userName = request.getSession().getAttribute("usr").toString();
			if( userName == null ) userName = " ";
			if( userName.equals("礼敬世尊") ) {
				amount = 100;
			}

			String svrName = request.getServerName();
			if( svrName == null ) svrName = " ";
	
			if( userName.equals("礼敬世尊") && svrName.contains("www.ddfj0.com") ) {
				sRtn = "";
			}
			else {
				if( MainApplication.m_WePayNavive != null )
					sRtn = MainApplication.m_WePayNavive.fnWePayPrePay(100, orderNo, orderName, amount );
			}
			
			System.out.println( "wepay PrePay: " + sRtn );
			rmsg = new CWebRtn(1000, " add user pay ok !", "{\"order\":\"" + key + "\", \"qr\":\"" + sRtn + "\" }" );
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/pay/analysis", "/api/user/pay/analysis"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayReAnalysis(final HttpServletRequest request, final HttpServletResponse response) {
	//// 命理分析有升级，重新分析一次命理分析
	CWebRtn rmsg;
	String sRtn = "";

	tbUserPay user;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			user = JSON.parseObject(wholeStr, tbUserPay.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1000, " 命理分析版本未变 20240213 !" + user.user_id.toString(), sRtn );
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/wepay/notify", "/api/wepay/notify"}, method = {RequestMethod.GET, RequestMethod.POST})
public void fnWePayResultNotify(final HttpServletRequest request, final HttpServletResponse response) {
	//// 微信支付返回
	String orderNo = "";

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			orderNo = wholeStr;
 
		} catch (final Exception ee) {
			System.out.println( "fnWePayResultNotify: " + ee.getMessage());
		}
	} else {
		System.out.println( "fnWePayResultNotify: get");
	}

	if( sysdb != null && orderNo != null && MainApplication.m_WePayNavive != null) {

		String payRtn = MainApplication.m_WePayNavive.fnWePayNotify(orderNo);
		JSONObject jsonPay = JSON.parseObject(payRtn);
		String trade_state = jsonPay.getString("trade_state");
		System.out.println(payRtn);

		if( trade_state.toUpperCase().equals("SUCCESS") ) {
			orderNo = jsonPay.getString("out_trade_no");
			if( orderNo.isEmpty() == false) {
				tbUserPay user = sysdb.fnGetUserPayByOrderNo(orderNo);
				if( user != null) {
					user.pay_info = "已支付";
					user.fate_big = payRtn;
					sysdb.fnUpdateUserPay(user);
					System.out.println(orderNo + " 支付成功！");
				}
			}
		}
		
	}
	try {
		response.setContentType("text/xml");
		response.getWriter().println("success");
	}
	catch(Exception ee) {
		System.out.println( "fnWePayResultNotify: response.getWriter " + ee.getMessage());
	}

}

@RequestMapping(value = { "/user/pay/close", "/api/user/pay/close"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayClose(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取登录用户，选择的一个命理分析结果
	CWebRtn rmsg;
	String sRtn = "";
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sAcc = jsonRtn.getLong("user_pay_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			sAcc = Long.parseLong(request.getParameter("user_pay_id")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " 关闭订单出错 !", sRtn );
		if( sysdb != null) {
			tbUserPay user = sysdb.fnGetUserPayListById(sAcc);
			if( user != null) {
				if( MainApplication.m_WePayNavive != null )
					MainApplication.m_WePayNavive.fnWePayCloseOrder(user.pay_order);
				user.pay_info = "已关闭";
				sysdb.fnUpdateUserPay(user);
				rmsg = new CWebRtn(1000, " 关闭订单成功 !", "{}");
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}


//#endregion

//#region wx mp

// 1、今日运势，今天的年月日的天干地支，如果最后一算命时间在31天以内，显示今日运势
// 2、收费算命列表
// 3、算命结果，下载按钮


@RequestMapping(value = { "/wxmp/user/list", "/api/wxmp/user/list"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnMpUserList(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// openid
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}


	rmsg = new CWebRtn(1099, " get user list error !", sRtn );
	if( sysdb != null) {
		sRtn = JSON.toJSONString(sysdb.fnGetUserList());
		rmsg = new CWebRtn(1000, " get user list ok !", sRtn );
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/wxmp/user/edit", "/api/wxmp/user/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnMpUserEdit(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	String appid;
	String openid;
	tbUser user;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			user = jsonRtn.getObject("user", tbUser.class);

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	System.out.println("fnMpUserEdit: " + user.user_id.toString() + " " + user.user_name + " " +  user.birthday + " " + user.user_passwd + " " + user.level);	

	rmsg = new CWebRtn(1099, " update user error !", sRtn );
	if( sysdb != null) {
		if( sysdb.fnUpdateUser(user) == true ) {
			rmsg = new CWebRtn(1000, " 用户更新成功!", "{}");
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/wxmp/userinfo", "/api/user/wxmp/userinfo"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpUserGetInfo(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbUser user;
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// openid
	}

	if (openid == null ) {
		rmsg = new CWebRtn(1001, " 名字为空 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 获取用户信息出错 !", sRtn );
	if( sysdb != null) {
		user = sysdb.fnGetUserByName(openid);
		if( user != null ) {	
			sRtn = JSON.toJSONString(user);
			rmsg = new CWebRtn(1000, " 获取用户信息成功!", sRtn);
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

// 保存用户的生日，每个月算过一个挂后，提供本人的每日运势
@RequestMapping(value = { "/user/wxmp/birthday", "/api/user/wxmp/birthday"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpUserUpdaetBirthday(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbUser user;
	String appid;
	String openid;
	String sBirthday;
	String sSex;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			sBirthday = jsonRtn.getString("birthday");
			sSex = jsonRtn.getString("sex");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// openid
		sBirthday = request.getParameter("birthday"); /// birthday
		sSex = request.getParameter("sex");
	}

	if (openid == null || sBirthday == null) {
		rmsg = new CWebRtn(1001, " 名或者生日为空 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 设置用户生日出错 !", sRtn );
	if( sysdb != null) {
		user = sysdb.fnGetUserByName(openid);
		if( user != null ) {
			user.birthday = sBirthday;
			user.sex = sSex;
	
			if( sysdb.fnUpdateUser(user) == true) {
				rmsg = new CWebRtn(1000, " 设置用户生日成功!", "{}");
			}
			else {
				rmsg = new CWebRtn(1004, " 设置用户生日失败!", "{}");
			}	
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

// 31天内有缴费算命，就提供本人的每日运势，无本人运势则提供通用的五行强弱每日运势
@RequestMapping(value = { "/user/wxmp/dayluck", "/api/user/wxmp/dayluck"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetDayLuck(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbUser user;
	String appid;
	String openid;
	String paipanCurDay;
	String paipanBirthday;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			paipanCurDay = jsonRtn.getString("paipanCurDay");
			paipanBirthday = jsonRtn.getString("paipanBirthday");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 获取每日运势出错 !", sRtn );
	if( sysdb != null) {
		user = sysdb.fnGetUserByName(openid);
		if( user != null ) {

			try {

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				JSONObject jo = new JSONObject();
	
				if( paipanCurDay == null ) {
					jo.put("all", "");
				}
				else {
					//BaZiFateAge bzf = JSON.parseObject(paipanCurDay, BaZiFateAge.class);
					//Ziping zp = new Ziping(bzf);
					//jo.put("all", zp.fnDayFateAnalysis(paipanCurDay));
					jo.put("all", new ArrayList<String>());
				}

				if( paipanBirthday == null ) {
					jo.put("self", "");
				}
				else {
					if( user.pay_time != null && user.pay_time.isEmpty() == false )
					{
						Date dt = df.parse(user.pay_time);
						Date dtCur = new Date();
						/// 31天内有缴费算命，就提供本人的每日运势
						if( (dtCur.getTime() > dt.getTime())  ) {
							jo.put("self", "");
						}
						else {
							//BaZiFateAge bzf = JSON.parseObject(paipanBirthday, BaZiFateAge.class);
							//Ziping zp = new Ziping(bzf);
							//jo.put("self", zp.fnDayFateAnalysisPaid(paipanCurDay));
							
							jo.put("self", new ArrayList<String>());

						}
					}
					else {
						jo.put("self", "");
					}
				}
	
				sRtn = jo.toJSONString();
				
				rmsg = new CWebRtn(1000, " 获取成功!", sRtn);
			}
			catch(Exception ee) {
				System.err.println("fnWxMpGetDayFortune: " + ee.getMessage());
				rmsg = new CWebRtn(1033, " 获取失败!" + ee.getMessage(), sRtn);
			}
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

// 获取干支合冲
@RequestMapping(value = { "/user/wxmp/hechong", "/api/user/wxmp/hechong"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetHeChong(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	String appid;
	String openid;

    String yearGan;
	String yearZhi; 
    String monthGan;
	String monthZhi;
    String dayGan;
	String dayZhi;
    String hourGan;
	String hourZhi;
    String bigGan;
	String bigZhi;
    String liuGan;
	String liuZhi;	

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");

			yearGan = jsonRtn.getString("yearGan");
			yearZhi = jsonRtn.getString("yearZhi");
			monthGan = jsonRtn.getString("monthGan");
			monthZhi = jsonRtn.getString("monthZhi");
			dayGan = jsonRtn.getString("dayGan");
			dayZhi = jsonRtn.getString("dayZhi");
			hourGan = jsonRtn.getString("hourGan");
			hourZhi = jsonRtn.getString("hourZhi");
			bigGan = jsonRtn.getString("bigGan");
			bigZhi = jsonRtn.getString("bigZhi");
			liuGan = jsonRtn.getString("liuGan");
			liuZhi = jsonRtn.getString("liuZhi");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		String svrName = request.getServerName();
		if( svrName == null ) svrName = " ";

		if( svrName.contains("localhost") || svrName.contains("127.0.0.1") ) {
			MainApplication.m_WechatOpenId.add(new WechatOpenId(openid, new Date()));
		}
		else {
			rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}	
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	sRtn = JSON.toJSONString(Ziping2.fnBaziHeChongAll(yearGan, yearZhi, monthGan, monthZhi, dayGan, dayZhi, hourGan, hourZhi, bigGan, bigZhi, liuGan, liuZhi));
	rmsg = new CWebRtn(1000, " 获取干支合冲成功!", sRtn);

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/wxmp/free", "/api/user/wxmp/free"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnFeWorldAnalysisFree(final HttpServletRequest request, final HttpServletResponse response) {
	//// 免费浏览排盘
	String sRtn = "";
	CWebRtn rmsg;
	BaZiFateAge bzf;
	String bazi;
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			bazi = jsonRtn.getString("paipan");
			bzf = JSON.parseObject(bazi, BaZiFateAge.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		
		String svrName = request.getServerName();
		if( svrName == null ) svrName = " ";

		if( svrName.contains("localhost") || svrName.contains("127.0.0.1") ) {
			MainApplication.m_WechatOpenId.add(new WechatOpenId(openid, new Date()));
		}
		else {
			rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}	
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	Ziping2 zp = new Ziping2(bzf, true, true);
	sRtn = zp.fnAnalysisSelfMp();
	rmsg = new CWebRtn(1000, " get paipan self  ok !", sRtn );	
	sRtn = JSON.toJSONString(rmsg);	

	try {
		final String sIp = fnGetIpAddr(request);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		tbUserLogin ul = new tbUserLogin();
		ul.user_id = sysdb.fnGetUserByName(openid).user_id;
		ul.login_ip = sIp;
		ul.login_time = sTime;
		ul.data = bazi;
		sysdb.fnInsertUserLogin(ul);

	}
	catch(Exception ee) {
		System.out.println(ee.getMessage());
	}
	
	return sRtn;
}

// 显示本人收费的算命列表
@RequestMapping(value = { "/user/wxmp/paylist", "/api/user/wxmp/paylist"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetPayList(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbUser user;
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// account
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 未找到微信号 !", sRtn );
	if( sysdb != null) {
		user = sysdb.fnGetUserByName(openid);
		if( user != null ) {
			sRtn = JSON.toJSONString(sysdb.fnGetUserPayListByUserIdPay(user.user_id));
			rmsg = new CWebRtn(1000, " 查询记录成功!", sRtn);
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/wxmp/paydel", "/api/user/wxmp/paydel"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetPayListDel(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbUser user;
	String appid;
	String openid;
	String userPayId;
	String userId;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			userPayId = jsonRtn.getString("user_pay_id");
			userId = jsonRtn.getString("user_id");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 未找到微信号 !", sRtn );
	if( sysdb != null) {
		user = sysdb.fnGetUserByName(openid);
		if( user != null ) {
			try {
				Long user_pay_id = Long.parseLong(userPayId);
				Long user_id = Long.parseLong(userId);
				if( user.user_id == user_id ) {
					sysdb.fnDeleteUserPay(user_pay_id);
					rmsg = new CWebRtn(1000, " 删除记录成功!", sRtn);
				}
			}
			catch(Exception ee) {
				System.err.println("fnWxMpGetPayListDel: " + ee.getMessage());
			}
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

// 显示一个收费的算命结果
@RequestMapping(value = { "/user/wxmp/result", "/api/user/wxmp/result"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetPayResult(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取登录用户，选择的一个命理分析结果
	CWebRtn rmsg;
	String sRtn = "";
	String appid = "";
	String openid = "";
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			sAcc = jsonRtn.getLong("user_pay_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			appid = request.getParameter("appid"); /// appid
			openid = request.getParameter("openid"); /// account
			sAcc = Long.parseLong(request.getParameter("user_pay_id")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		//return sRtn;
	}
	
	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " get user pay info error !", sRtn );
	if( sysdb != null) {
		tbUserPay tbk = sysdb.fnGetUserPayListById(sAcc);
		if( tbk.fate_self.equals("未分析") && tbk.pay_info.equals("已支付") ) {
			try {
				BaZiFateAge bazi = JSON.parseObject(tbk.paipan, BaZiFateAge.class);
				Ziping2 zp = new Ziping2(bazi, true, false);
				sRtn = zp.fnAnalysisSelfMp();
				tbk.fate_self = sRtn;
				sysdb.fnUpdateUserPay(tbk);
			}
			catch(Exception ee) {
				System.out.println( "fnWxMpGetPayResult: " + ee.getMessage());
			}
		}
		sRtn = JSON.toJSONString(tbk);
		rmsg = new CWebRtn(1000, " get user pay info ok !", sRtn );
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

/// 每日运势的支付信息
@RequestMapping(value = { "/user/wxmp/daypay", "/api/user/wxmp/daypay"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpDayFatePayInfo(final HttpServletRequest request, final HttpServletResponse response) {
	//// 返回支付信息
	CWebRtn rmsg;
	String sRtn = "";

	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
 
			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}


	rmsg = new CWebRtn(1099, " add user pay error !", sRtn );
	if( sysdb != null) {

		tbUser uu = sysdb.fnGetUserByName(openid);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		sTime =  sTime.replace(' ', '_');
		sTime =  sTime.replace(':', '_');

		String orderNo = "bzday_" + uu.user_id.toString() + "_" + sTime ;
		String orderName = "每日运势分析" + sTime;

		int amount = 1000;
		if( uu.level.equals("33") ) { /// 给老姑的微信号特殊收费。
			amount = 1;
		}

		if( MainApplication.m_WePayMp != null ) {
			TreeMap<String, String> tm = MainApplication.m_WePayMp.fnWePayPrePayMp(appid, openid, orderNo, orderName, amount );
			tm.put("orderNo", orderNo);
			sRtn = JSON.toJSONString(tm);
		}
		
		System.out.println( "wepay PrePay: " + sRtn );
		rmsg = new CWebRtn(1000, " day fate pay ok !", sRtn );
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}


/// 算命一次的支付，改成捐赠
@RequestMapping(value = { "/user/wxmp/payinfo", "/api/user/wxmp/payinfo"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnWxMpGetPayInfo(final HttpServletRequest request, final HttpServletResponse response) {
	//// 返回支付信息
	CWebRtn rmsg;
	String sRtn = "";

	tbUserPay user;
	String appid;
	String openid;
	String nick_name = "测试";
	String birthday = "1990-01-01 12-00-00";
	String sex = "男";
	String paipan;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
 
			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			nick_name = jsonRtn.getString("nick_name");
			birthday = jsonRtn.getString("birthday");
			sex = jsonRtn.getString("sex");
			paipan = jsonRtn.getString("paipan");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( birthday == null ) {
		rmsg = new CWebRtn(1003, " 生日错 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;

	}

	rmsg = new CWebRtn(1099, " add user pay error !", sRtn );
	if( sysdb != null) {

		tbUser uu = sysdb.fnGetUserByName(openid);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sTime = df.format(new Date());

		if( nick_name == null ) nick_name = "";

		user = new tbUserPay();
		user.nick_name = nick_name;
		user.birthday = birthday;
		user.sex = sex;
		user.paipan = paipan;
		user.user_id = uu.user_id;
		user.pay_ip = fnGetIpAddr(request);
		user.pay_time = sTime;
		user.fate_self = "未分析";

		Long key = sysdb.fnInsertUserPayRtnKey(user);
		birthday =  birthday.replace(' ', '_');
		birthday =  birthday.replace(':', '_');

		String orderNo = "bz_" + key.toString() + "_" + birthday ;
		//String orderName = nick_name + " " + birthday + " 分析";
		String orderName = nick_name + " 感谢捐赠";

		user.user_pay_id = key;
		user.pay_order = orderNo;
		user.pay_info = "未支付";
		sysdb.fnUpdateUserPay(user);

		int amount = 100;
		//if( uu.level.equals("33") ) { /// 给老姑的微信号特殊收费。
		//	amount = 1;
		//}
		try {
			amount = Integer.parseInt(nick_name);
			amount = amount * 100;
		}
		catch(Exception ee) {
			System.out.println( "fnWxMpGetPayInfo: 转换金额出错 " + ee.getMessage());
			amount = 100;
		}


		if( MainApplication.m_WePayMp != null ) {
			TreeMap<String, String> tm = MainApplication.m_WePayMp.fnWePayPrePayMp(appid, openid, orderNo, orderName, amount );
			tm.put("orderNo", orderNo);
			sRtn = JSON.toJSONString(tm);
		}
		
		System.out.println( "wepay PrePay: " + sRtn );
		rmsg = new CWebRtn(1000, " add user pay ok !", sRtn );
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/pay/mp/notify", "/api/user/pay/mp/notify"}, method = {RequestMethod.GET, RequestMethod.POST})
public void fnWePayResultNotifyMp(final HttpServletRequest request, final HttpServletResponse response) {
	//// 微信支付返回
	String orderNo = "";

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			orderNo = wholeStr;
 
		} catch (final Exception ee) {
			System.out.println( "fnWePayResultNotify: " + ee.getMessage());
		}
	} else {
		System.out.println( "fnWePayResultNotify: get");
	}

	if( sysdb != null && orderNo != null && MainApplication.m_WePayMp != null) {

		String payRtn = MainApplication.m_WePayMp.fnWePayNotify(orderNo);
		JSONObject jsonPay = JSON.parseObject(payRtn);
		String trade_state = jsonPay.getString("trade_state");
		System.out.println(payRtn);

		if( trade_state.toUpperCase().equals("SUCCESS") ) {
			orderNo = jsonPay.getString("out_trade_no");
			System.out.println("fnWePayResultNotifyMp 支付回调：" + orderNo);
			if( orderNo.isEmpty() == false) {
				System.out.println("支付成功！" + orderNo);
				if(  orderNo.startsWith("bzday_")) {
					String[] sArr = orderNo.split("_");
					if( sArr.length > 1) {
						//// 更新用户表的pay_time，增加一个月
						try {
							Long id = Long.parseLong(sArr[1]);
							tbUser user = sysdb.fnGetUserByID(id);
							if( user != null) {
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
								Date paytime, curDate;
								curDate = new Date();
								try {
									paytime = df.parse(user.pay_time);
									if( paytime.getTime() < curDate.getTime() ) {
										paytime = curDate;
									}
								}
								catch(Exception ee) {
									System.out.println( "fnWePayResultNotifyMp: " + ee.getMessage());
									paytime = new Date();
								}
								Calendar cal = Calendar.getInstance();
								cal.setTime(paytime);
								cal.add(Calendar.MONTH, 1);
								String sTime = df.format(cal.getTime());	
								user.pay_time = sTime;
								sysdb.fnUpdateUser(user);
								System.out.println(user.user_name + " 支付更新成功！" + sTime);
							}
						}
						catch(Exception ee) {
							System.out.println( "fnWePayResultNotifyMp: " + ee.getMessage());
						}
					}
				}
				else {
					tbUserPay tbk = sysdb.fnGetUserPayByOrderNo(orderNo);
					if( tbk != null) {
						tbk.pay_info = "已支付";
						tbk.fate_big = payRtn;
						sysdb.fnUpdateUserPay(tbk);
						System.out.println(orderNo + " 支付更新成功！");

						//// 进行八字分析	
						try {
							BaZiFateAge bazi = JSON.parseObject(tbk.paipan, BaZiFateAge.class);
							Ziping2 zp = new Ziping2(bazi, true, false);
							String sRtn = zp.fnAnalysisSelfMp();
							tbk.fate_self = sRtn;
							sysdb.fnUpdateUserPay(tbk);
						}
						catch(Exception ee) {
							System.out.println( "fnWePayResultNotifyMp: " + ee.getMessage());
						}
		
					}	
				}
			}
		}
		
	}
	try {
		response.setContentType("text/xml");
		response.getWriter().println("success");
	}
	catch(Exception ee) {
		System.out.println( "fnWePayResultNotify: response.getWriter " + ee.getMessage());
	}

}

@RequestMapping(value = { "/user/pay/mp/close", "/api/user/pay/mp/close"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserPayMpClose(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取登录用户，选择的一个命理分析结果
	CWebRtn rmsg;
	String sRtn = "";
	String appid;
	String openid;
	String orderNo;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
			orderNo = jsonRtn.getString("orderNo");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 关闭订单出错 !", sRtn );
	if( sysdb != null) {
		if( MainApplication.m_WePayMp != null ) {
			System.out.println("fnUserPayMpClose: " + orderNo);
			MainApplication.m_WePayMp.fnWePayCloseOrder(orderNo);
			rmsg = new CWebRtn(1000, " 关闭订单成功 !", sRtn );
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/pay/mp/openid", "/api/user/pay/mp/openid"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnMpOpenId(final HttpServletRequest request, final HttpServletResponse response) {
	//// 打开网址，获取openid，加入用户表
	CWebRtn rmsg;
	String sRtn = "";
	String code;
	String appid;
	String secret;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			code = jsonRtn.getString("code");
			appid = jsonRtn.getString("appid");

		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		code = request.getParameter("code"); /// code
		appid = request.getParameter("appid"); /// appid
		System.out.println("appid: " + appid);
		System.out.println("code : " + code);

	}

	/// 判断 header 里的host，是否支持该网址
	if( request.getServerName().contains("www.ddfj0.com") ) {
		MainApplication.m_WechatOpenId.add(new WechatOpenId(code, new Date()));
		rmsg = new CWebRtn(1000, " 查询openid成功 !", "{\"openid\":\"" +  code + "\"}" );	
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1099, " 查询openid失败 !", sRtn );
	if( sysdb != null && !code.isEmpty()) {
		secret = MainApplication.m_WePayMp.fnGetSupportAppidSecret(appid);
		if( secret.isEmpty() == false) {
			sRtn = MainApplication.m_WePayMp.fnWePayOpenId(appid, secret, code);
			rmsg = new CWebRtn(1000, " 查询openid成功 !", "{\"openid\":\"" +  sRtn + "\"}" );	

			if( sRtn != null && !sRtn.isEmpty() ) {
				tbUser user = sysdb.fnGetUserByName(sRtn);
				if( user == null ) {

					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
					String sTime = df.format(new Date());	

					user = new tbUser();
					user.user_name = sRtn;
					user.level = "0";
					user.birthday = "";
					user.reg_ip = fnGetIpAddr(request);
					user.pay_time = "";
					user.reg_time = sTime;				
					sysdb.fnInsertUser(user);

					/* 
					user = sysdb.fnGetUserByName(sRtn);
					if( user != null ) {
						/// 增加一个预测例子给腾讯审核看

						tbUserPay tbk = new tbUserPay();
						tbk.nick_name = "测试数据";
						tbk.user_id = user.user_id;
						tbk.pay_ip = user.reg_ip;
						tbk.pay_time = sTime;
						tbk.pay_order = "bz_1_2021-01-01_12-00-00";
						tbk.pay_info = "已支付";
						tbk.paipan = "{}";
						tbk.fate_big = "{}";
						tbk.sex="女";
						tbk.birthday="1974-3-21 4:20:00";
						tbk.fate_self = "[\"公历：1974-3-21 4:20\",\"农历：甲寅 虎年 二月 廿八 寅时\",\"中华历：甲寅年 丁卯月 辛酉日 庚寅时\",\"性别：女生\",\"本人为辛，属于阴金。\",\"性格似首饰之金，温顺，灵秀，多敏感。\"]";
						sysdb.fnInsertUserPay(tbk);
					}
					*/
				}
			}
		}
		else {
			rmsg = new CWebRtn(1091, " 不支持该网址 !", "{}" );	
		}
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/mp/alive", "/api/user/mp/alive"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnMpOpenIdAlive(final HttpServletRequest request, final HttpServletResponse response) {
	//// 网址，心跳检测
	CWebRtn rmsg;
	String sRtn = "";
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// openid
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}
	
	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	rmsg = new CWebRtn(1000, " openid alive !", sRtn );

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/user/mp/close", "/api/user/mp/close"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnMpOpenIdClose(final HttpServletRequest request, final HttpServletResponse response) {
	//// x网址关闭，清除openid
	CWebRtn rmsg;
	String sRtn = "";
	String appid;
	String openid;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			appid = jsonRtn.getString("appid");
			openid = jsonRtn.getString("openid");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		appid = request.getParameter("appid"); /// appid
		openid = request.getParameter("openid"); /// openid
	}

	if (openid == null || WechatOpenId.fnOpenIdValied(openid) == false) {
		rmsg = new CWebRtn(1001, " 微信号出错，请重新访问网站 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}
	
	if( MainApplication.m_WePayMp.fnIsSupportAppid(appid) == false ) {
		rmsg = new CWebRtn(1002, " 不支持该网址 !", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	for(WechatOpenId wco : MainApplication.m_WechatOpenId) {
		if( wco.openid.equals(openid) ) {
			MainApplication.m_WechatOpenId.remove(wco);
			break;
		}
	}

	rmsg = new CWebRtn(1000, " openid close !", sRtn );

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

//#endregion

//#region card 

@RequestMapping(value = { "/card/list", "/api/card/list"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardList(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	Long sAcc;
	String namePhone;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			namePhone = jsonRtn.getString("search");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		namePhone = request.getParameter("search"); /// account
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " get card list error !", sRtn );
		if( sysdb != null) {
			try {
				sAcc = Long.parseLong(request.getSession().getAttribute("user_id").toString()); /// account

				if( namePhone == null  ) {
					sRtn = JSON.toJSONString(sysdb.fnGetCardListByUserId(sAcc));
					rmsg = new CWebRtn(1000, " get card list ok !", sRtn );	
				}
				else {
					try {
						Long.parseLong(namePhone);
						sRtn = JSON.toJSONString(sysdb.fnGetCardListByUserIdAndPhone(sAcc, namePhone));
						rmsg = new CWebRtn(1000, " get card list ok !", sRtn );	
							
					}
					catch(Exception ee) {
						sRtn = JSON.toJSONString(sysdb.fnGetCardListByUserIdAndCardName(sAcc, namePhone));
						rmsg = new CWebRtn(1000, " get card list ok !", sRtn );	
					}
				}
			}
			catch(Exception ee) {
				System.out.println( "/api/card/list error " + ee.getMessage());
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/add", "/api/card/add"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardAdd(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	tbCard card;
	Long sAcc;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			card = JSON.parseObject(wholeStr, tbCard.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " add card error !", sRtn );
		if( sysdb != null) {

			try {
				sAcc = Long.parseLong(request.getSession().getAttribute("user_id").toString()); /// account
				card.user_id = sAcc;
				card.card_time = df.format(new Date());
				if(sysdb.fnGetCardListByUserIdAndFullPhone( sAcc, card.card_phone) == null) {
					if(sysdb.fnInsertCard(card) == true) {
						rmsg = new CWebRtn(1000, " add card ok !", "{}");
					}
				}
				else {
					rmsg = new CWebRtn(2099, " card exists !", "{}");
				}
			}
			catch(Exception ee) {
				System.out.println( "/api/card/add error " + ee.getMessage());
			}

		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/edit", "/api/card/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardEdit(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbCard card;
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			card = JSON.parseObject(wholeStr, tbCard.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " update card error !", sRtn );
		if( sysdb != null) {

			try {
				sAcc = Long.parseLong(request.getSession().getAttribute("user_id").toString()); /// account
				card.user_id = sAcc;
				if( sysdb.fnUpdateCard(card) == true ) {
					rmsg = new CWebRtn(1000, " update card ok !", "{}");
				}
				}
			catch(Exception ee) {
				System.out.println( "/api/card/edit error " + ee.getMessage());
			}

		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/del", "/api/card/del"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardDel(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sAcc = jsonRtn.getLong("card_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			sAcc = Long.parseLong(request.getParameter("card_id")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " del card error !", sRtn );
		if( sysdb != null) {
			if( sysdb.fnDeleteCard(sAcc) == true ) {
				rmsg = new CWebRtn(1000, sAcc + " card删除成功 !", "{}");
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);
	return sRtn;
}

@RequestMapping(value = { "/card/use", "/api/card/use"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardUse(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbCard card;
	Long sCard;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sCard = jsonRtn.getLong("card_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " update card error !", sRtn );
		if( sysdb != null) {

			card = sysdb.fnGetCardListByCardId(sCard);
			if( card != null ) {
				card.card_num = card.card_num - 1;
				if( sysdb.fnUpdateCard(card) == true ) {
					tbCardDetail cd = new tbCardDetail();
					cd.card_id = sCard;
					cd.card_num = -1;
					cd.card_time = df.format(new Date());
					sysdb.fnInsertCardDetail(cd);
					rmsg = new CWebRtn(1000, " update card ok !", "{}");
				}
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/addone", "/api/card/addone"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardAddOne(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbCard card;
	Long sCard;
	Integer sNum;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sCard = jsonRtn.getLong("card_id");
			sNum = jsonRtn.getInteger("card_num");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " update card error !", sRtn );
		if( sysdb != null) {

			card = sysdb.fnGetCardListByCardId(sCard);
			if( card != null ) {
				card.card_num = card.card_num + sNum;
				card.card_time = df.format(new Date());
				if( sysdb.fnUpdateCard(card) == true ) {
					tbCardDetail cd = new tbCardDetail();
					cd.card_id = sCard;
					cd.card_num = sNum;
					cd.card_time = df.format(new Date());
					sysdb.fnInsertCardDetail(cd);
					rmsg = new CWebRtn(1000, " update card ok !", "{}");
				}
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

//#endregion

//#region card detail 

@RequestMapping(value = { "/card/detail/list", "/api/card/detail/list"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardDetailList(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sAcc = jsonRtn.getLong("card_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			sAcc = Long.parseLong(request.getParameter("card_id")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " get card detail list error !", sRtn );
		if( sysdb != null) {
			sRtn = JSON.toJSONString(sysdb.fnGetCardDetailListByCardId(sAcc));
			rmsg = new CWebRtn(1000, " get card detail list ok !", sRtn );	
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/detail/add", "/api/card/detail/add"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardDetailAdd(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";

	tbCardDetail card;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			card = JSON.parseObject(wholeStr, tbCardDetail.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " add card detail error !", sRtn );
		if( sysdb != null) {
			if(sysdb.fnInsertCardDetail(card) == true) {
				rmsg = new CWebRtn(1000, " add card detail ok !", "{}");
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/detail/edit", "/api/card/detail/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardDetailEdit(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	tbCardDetail card;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			card = JSON.parseObject(wholeStr, tbCardDetail.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " update card detail error !", sRtn );
		if( sysdb != null) {
			if( sysdb.fnUpdateCardDetail(card) == true ) {
				rmsg = new CWebRtn(1000, " update card detail ok !", "{}");
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

@RequestMapping(value = { "/card/detail/del", "/api/card/detail/del"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnCardDetailDel(final HttpServletRequest request, final HttpServletResponse response) {
	CWebRtn rmsg;
	String sRtn = "";
	Long sAcc;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			sAcc = jsonRtn.getLong("card_did");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		try{
			sAcc = Long.parseLong(request.getParameter("card_did")); /// account
		}
		catch(Exception ee) {
			sAcc = 0L;
		}
	}

	if ( fnCheckSession(request)) {
		rmsg = new CWebRtn(1099, " del card detail error !", sRtn );
		if( sysdb != null) {
			if( sysdb.fnDeleteCardDetail(sAcc) == true ) {
				rmsg = new CWebRtn(1000, sAcc + " card detail删除成功 !", "{}");
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);
	return sRtn;
}

//#endregion


//#region 子平算命 

@RequestMapping(value = { "/user/run/analysis", "/api/user/run/analysis"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnUserRunAnalysis(final HttpServletRequest request, final HttpServletResponse response) {
	//// 匿名浏览并选择了一个日期进行排盘，用来记录到数据库
	String key = "";
	String sRtn = "";
	CWebRtn rmsg;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			key = jsonRtn.getString("key");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		key = request.getParameter("key");
	}

	final String sIp = fnGetIpAddr(request);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	String sTime = df.format(new Date());

	tbUserLogin ul = new tbUserLogin();
	ul.user_id = 5L;
	ul.login_ip = sIp;
	ul.login_time = sTime;
	ul.data = key;
	sysdb.fnInsertUserLogin(ul);

	rmsg = new CWebRtn(1000, " reg analysis  ok !", "{}" );	
	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

//// 本命分析
@RequestMapping(value = { "/analysis/self", "/api/analysis/self"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnFeWorldAnalysisSelf(final HttpServletRequest request, final HttpServletResponse response) {
	//// 匿名浏览排盘
	String key = "", skey = "";
	String sRtn = "";
	CWebRtn rmsg;
	BaZiFateAge bzf;
	String bazi;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			key = jsonRtn.getString("key");
			bazi = jsonRtn.getString("bazi");
			bzf = JSON.parseObject(bazi, BaZiFateAge.class);
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	final HttpSession sessoin = request.getSession();
	try {
		skey = sessoin.getAttribute("paipanKey").toString();
	}
	catch(Exception ee) {
		skey = null;
	}

	if( skey == null ) {
		if ( fnCheckSession(request)) {
			Ziping zp = new Ziping(bzf);
			sRtn = zp.fnAnalysisSelf();
			rmsg = new CWebRtn(1000, " get paipan self  ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		} 
		else {
			rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
			sRtn = JSON.toJSONString(rmsg);
		}
	}
	else {
		@SuppressWarnings("null")
		String md5Str = DigestUtils.md5DigestAsHex(skey.getBytes());

		if( md5Str.equals(key)) {
			Ziping zp = new Ziping(bzf);
			sRtn = zp.fnAnalysisSelf();
			rmsg = new CWebRtn(1000, " get paipan self  ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		}
		else {
			rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
			sRtn = JSON.toJSONString(rmsg);
		}
	}

	return sRtn;
}

@RequestMapping(value = { "/analysis/qtbj", "/api/analysis/qtbj"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnFeWorldAnalysisQtbj(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取穷通宝鉴内容

	String sRtn = "";
	CWebRtn rmsg;
	String skey; //, key;
	Integer dayh, monthh;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			//key = jsonRtn.getString("key");
			dayh = jsonRtn.getInteger("dayh");
			monthh = jsonRtn.getInteger("monthh");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	final HttpSession sessoin = request.getSession();
	try {
		skey = sessoin.getAttribute("paipanKey").toString();
	}
	catch(Exception ee) {
		skey = null;
	}

	if( skey == null ) {
		//if ( fnCheckSession(request)) {

			sRtn = WeiQtbj.fnGetQtbj(dayh, monthh);
			rmsg = new CWebRtn(1000, " get paipan qtbj  ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		//} 
		//else {
		//	rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
		//	sRtn = JSON.toJSONString(rmsg);
		//}
	}
	else {
		//@SuppressWarnings("null")
		//String md5Str = DigestUtils.md5DigestAsHex(skey.getBytes());

		//if( md5Str.equals(key)) {
			sRtn = WeiQtbj.fnGetQtbj(dayh, monthh);
			rmsg = new CWebRtn(1000, " get paipan qtbj  ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		//}
		//else {
		//	rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
		//	sRtn = JSON.toJSONString(rmsg);
		//}
	}

	return sRtn;
}

@RequestMapping(value = { "/analysis/wei", "/api/analysis/wei"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnFeWorldAnalysisWei(final HttpServletRequest request, final HttpServletResponse response) {
	//// 获取韦千里八字提要内容

	String sRtn = "";
	CWebRtn rmsg;
	String skey; //, key;
	Integer dayh, monthh, timee;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			//key = jsonRtn.getString("key");
			dayh = jsonRtn.getInteger("dayh");
			monthh = jsonRtn.getInteger("monthh");
			timee = jsonRtn.getInteger("timee");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	final HttpSession sessoin = request.getSession();
	try {
		skey = sessoin.getAttribute("paipanKey").toString();
	}
	catch(Exception ee) {
		skey = null;
	}

	if( skey == null ) {
		//if ( fnCheckSession(request)) {

			sRtn = WeiQtbj.fnGetWei(dayh, monthh, timee);
			rmsg = new CWebRtn(1000, " get paipan wei ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		//} 
		//else {
		//	rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
		//	sRtn = JSON.toJSONString(rmsg);
		//}
	}
	else {
		//@SuppressWarnings("null")
		//String md5Str = DigestUtils.md5DigestAsHex(skey.getBytes());

		//if( md5Str.equals(key)) {
			sRtn = WeiQtbj.fnGetWei(dayh, monthh, timee);
			rmsg = new CWebRtn(1000, " get paipan wei ok !", sRtn );	
			sRtn = JSON.toJSONString(rmsg);	
		//}
		//else {
		//	rmsg = new CWebRtn(1001, " paipan key  error !", "{}" );	
		//	sRtn = JSON.toJSONString(rmsg);
		//}
	}

	return sRtn;
}


//// 收费命理分析
@RequestMapping(value = { "/analysis/selfpay", "/api/analysis/selfpay"}, method = {RequestMethod.GET, RequestMethod.POST})
public String fnFeWorldAnalysisPay(final HttpServletRequest request, final HttpServletResponse response) {
	//// 登录收费命理分析
	String sRtn = "";
	CWebRtn rmsg;

	String bazi;
	BaZiFateAge bzf;
	Long user_pay_id;

	if ("POST".equals(request.getMethod())) {
		try {
			final BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}

			JSONObject jsonRtn = JSON.parseObject(wholeStr);
			bazi = jsonRtn.getString("bazi");
			bzf = JSON.parseObject(bazi, BaZiFateAge.class);
			user_pay_id = jsonRtn.getLong("user_pay_id");
		} catch (final Exception ee) {
			ee.printStackTrace();
			rmsg = new CWebRtn(10015, " post解析错误 ! " + ee.getMessage(), "{}");
			sRtn = JSON.toJSONString(rmsg);
			return sRtn;
		}
	} else {
		rmsg = new CWebRtn(1003, " 请用Post方式访问。", "{}");
		sRtn = JSON.toJSONString(rmsg);
		return sRtn;
	}

	if ( fnCheckSession(request)) {

		String userName = request.getSession().getAttribute("usr").toString();
		if( userName == null ) userName = " ";
		String svrName = request.getServerName();
		if( svrName == null ) svrName = " ";

		tbUserPay tbk = sysdb.fnGetUserPayListById(user_pay_id);
		if( tbk == null ) {
			rmsg = new CWebRtn(1001, " 无数据 !", "" );
		}
		else {
			if( userName.equals("礼敬世尊") && svrName.contains("www.ddfj0.com") ) {
				if( tbk.fate_self == null || tbk.fate_self.equals("未分析") ) {
					Ziping zp = new Ziping(bzf);
					sRtn = zp.fnAnalysisSelfPay();
					tbk.fate_self = sRtn;
					sysdb.fnUpdateUserPay(tbk);
				}
				sRtn = tbk.fate_self;
				rmsg = new CWebRtn(1000, " 获取命理分析成功 !", sRtn );

			}
			else if(tbk.fate_self == null ) {

				if( tbk.pay_info != null && tbk.pay_info.equals("已支付") ) {
					Ziping zp = new Ziping(bzf);
					sRtn = zp.fnAnalysisSelfPay();
	
					tbk.fate_self = sRtn;
					sysdb.fnUpdateUserPay(tbk);
					rmsg = new CWebRtn(1000, " 获取命理分析成功 !", sRtn );
				}
				else {
					rmsg = new CWebRtn(1001, " 未支付 !", "" );
				}
			}
			else {
				if( tbk.fate_self.equals("未分析") ) {
					if( tbk.pay_info != null && tbk.pay_info.equals("已支付") ) {
						Ziping zp = new Ziping(bzf);
						sRtn = zp.fnAnalysisSelfPay();
			
						tbk.fate_self = sRtn;
						sysdb.fnUpdateUserPay(tbk);	
						rmsg = new CWebRtn(1000, " 获取命理分析成功 !", sRtn );
					}
				}
				sRtn = tbk.fate_self;
				rmsg = new CWebRtn(1000, " 获取命理分析成功 !", sRtn );
			}
		}
	}
	else {
		rmsg = new CWebRtn(1002, " 会话过期，请重新登录 !", "{}");
	}

	sRtn = JSON.toJSONString(rmsg);

	return sRtn;
}

//#endregion

//#region tools
	public boolean fnCheckSession(final HttpServletRequest request) {
		String sAcc = "";
		String sPwd = "";
	
		final HttpSession sessoin = request.getSession();
		try {
	
			sAcc = sessoin.getAttribute("usr").toString();
			sPwd = sessoin.getAttribute("pwd").toString();
	
			if( sAcc == null ) return false;
			if( sPwd == null ) return false;
	
		} 
		catch (final Exception ee) {
			return false;
		}

		return true;
	}

	public void fnAppendTextToFile(String fileName, String data) {
		try {

			//String fn = new String(fileName.getBytes(Charset.defaultCharset()),"utf-8");
			File file  = new File(MainApplication.m_curPath + fileName);

			OutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
			outputStreamWriter.write(data + System.lineSeparator());
			outputStreamWriter.flush();
			outputStreamWriter.close();
			outputStream.close();
		}
		catch(Exception ee) {
			System.out.println("fnAppendTextToFile " + fileName + " " +  ee.getMessage());
		}
	}

	public static void fnWriteTextToFile(String fileName, String data) {
		try {

			//String fn = new String(fileName.getBytes(Charset.defaultCharset()),"utf-8");
			File file  = new File(MainApplication.m_curPath + fileName);

			OutputStream outputStream = new FileOutputStream(file, false);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
			outputStreamWriter.write(data + System.lineSeparator());
			outputStreamWriter.flush();
			outputStreamWriter.close();
			outputStream.close();
		}
		catch(Exception ee) {
			System.out.println("fnAppendTextToFile " + fileName + " " +  ee.getMessage());
		}
	}

	public tbUser fnUserCheck( String name, String passwd ) {
		tbUser tbu = null;
		List<tbUser> lUser = sysdb.fnGetUserList();
		for(int i = 0 ; i < lUser.size() ; i++ ) {
			tbUser ttt = lUser.get(i);
			if( name.equals(ttt.user_name) ) {
				if( passwd.equals(ttt.user_passwd) ) {
					tbu = ttt;
					tbu.user_passwd = "";
					tbu.user_rtncode = "";
					tbu.reg_ip = "";
					break;
				}
			}
		}

		log.info("fnUserCheck " + name);		
		return tbu;
	}

	public String fnGetBakFileList() {
		String rtn;
		JSONArray josnArray;
		JSONObject jsonObj;
		rtn = "[]";

		if( MainApplication.m_sysConfig.bkpath.isEmpty() ) {
			return rtn;
		}

		File ff = new File(MainApplication.m_sysConfig.bkpath);	
		if ( ff.exists() ) {
			josnArray = new JSONArray();

			File fa[] = ff.listFiles();
			for (int i = 0; i < fa.length; i++) {
				File fs = fa[i];
				if (fs.isDirectory()) {
					continue;
				} else {
					if( fs.getName().startsWith("emsbk") ) {
						jsonObj = new JSONObject();
						jsonObj.put("bkpath", fs.getPath());
						josnArray.add(jsonObj);
					}
				}
			}
			rtn = JSON.toJSONString(josnArray);
		}

		return rtn;
	}

	public void fnRunSysBackup(String bkpathname ) {
		String cmd;
		cmd = "tar zcvf " + bkpathname + " " + MainApplication.m_curPath;
		try {
			Runtime.getRuntime().exec(cmd);
		}
		catch(Exception ee) {
			System.out.println("fnRunSysBackup(): " + ee.getMessage());
		}
		System.out.println("执行备份成功！ " + cmd);
	}

	public static String fnGetIpAddr(final HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
						ipAddress = inet.getHostAddress();
					} catch (final UnknownHostException e) {
						ipAddress = "";
						log.error("fnGetIpAddr() 1 " + e.getMessage());
					}
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
				// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (final Exception e) {
			ipAddress = "";
			log.error("fnGetIpAddr() 2 " + e.getMessage());
		}
		// ipAddress = this.getRequest().getRemoteAddr();

		return ipAddress;
	}

//#endregion tools

}

class CWebRtn {
	public Integer code;
	public String msg;
	public String result;

	CWebRtn() {
		code = 1000;
		msg = "ok!";
		result = "{}";
	}

	CWebRtn(final Integer pcode, final String pmsg, final String pdata) {
		code = pcode;
		msg = pmsg;
		result = pdata;
	}
}

///// WebMgSim.java end