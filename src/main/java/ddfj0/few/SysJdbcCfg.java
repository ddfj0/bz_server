package ddfj0.few;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Repository
public class SysJdbcCfg {

    private static final Logger log = LoggerFactory.getLogger(SysJdbcCfg.class);

    public static SysJdbcCfg sysCfgJdbc;

    @Autowired
    private JdbcTemplate jdbcSysCfg;

    @PostConstruct
    public void init() {
        sysCfgJdbc = this;
    }
    
//#region user manager    
    ///-------------------------------------  tbuser  -----------------
    public List<tbUser> fnGetUserList(){
        List<tbUser> ltb;
        ltb = new ArrayList<>();
        try {
            String sql = "SELECT * from tbuser";
            ltb = jdbcSysCfg.query(sql, new RowMapper<tbUser>() {
                @Override
                public tbUser mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                    tbUser ss = new tbUser();
                    ss.user_id     = rs.getLong("user_id");
                    ss.user_name   = rs.getString("user_name");
                    ss.user_passwd = rs.getString("user_passwd");
                    ss.user_rtncode  = rs.getString("user_rtncode");
                    ss.birthday  = rs.getString("birthday");
                    ss.sex       = rs.getString("sex");
                    ss.level     = rs.getString("level");
                    ss.pay_time  = rs.getString("pay_time");
                    ss.reg_time  = rs.getString("reg_time");
                    ss.reg_ip    = rs.getString("reg_ip");
                    return ss;
                }
            });            
        }
        catch(Exception ee) {
            log.error("fnGetUserList " + ee.getMessage());
            System.out.println("fnGetUserList " + ee.getMessage());
        }
        return ltb;
    }

    public tbUser fnGetUserByID(Long nID){

        List<tbUser> ltb;
        tbUser tbk = null;
        ltb = new ArrayList<>();
        try {
            String sql = "SELECT * from tbuser where user_id = " + nID ;
            ltb = jdbcSysCfg.query(sql, new RowMapper<tbUser>() {
                @Override
                public tbUser mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                    tbUser ss = new tbUser();
                    ss.user_id     = rs.getLong("user_id");
                    ss.user_name   = rs.getString("user_name");
                    ss.user_passwd = rs.getString("user_passwd");
                    ss.user_rtncode  = rs.getString("user_rtncode");
                    ss.birthday  = rs.getString("birthday");
                    ss.sex       = rs.getString("sex");
                    ss.level     = rs.getString("level");
                    ss.pay_time  = rs.getString("pay_time");
                    ss.reg_time  = rs.getString("reg_time");
                    ss.reg_ip    = rs.getString("reg_ip");
                    return ss;
                }
            });            
            if( !ltb.isEmpty() ) {
                tbk = ltb.get(0);
            }
        }
        catch(Exception ee) {
            log.error("fnGetUserByID " + ee.getMessage());
            System.out.println("fnGetUserByID " + ee.getMessage());
       }
        return tbk;
    }

    public tbUser fnGetUserByName(String name){

        List<tbUser> ltb;
        tbUser tbk = null;
        ltb = new ArrayList<>();
        try {
            String sql = "SELECT * from tbuser where user_name = '" + name + "'";
            ltb = jdbcSysCfg.query(sql, new RowMapper<tbUser>() {
                @Override
                public tbUser mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                    tbUser ss = new tbUser();
                    ss.user_id     = rs.getLong("user_id");
                    ss.user_name   = rs.getString("user_name");
                    ss.user_passwd = rs.getString("user_passwd");
                    ss.user_rtncode  = rs.getString("user_rtncode");
                    ss.birthday  = rs.getString("birthday");
                    ss.sex       = rs.getString("sex");
                    ss.level     = rs.getString("level");
                    ss.pay_time  = rs.getString("pay_time");
                    ss.reg_time  = rs.getString("reg_time");
                    ss.reg_ip    = rs.getString("reg_ip");
                    return ss;
                }
            });
            if( !ltb.isEmpty() ) {
                tbk = ltb.get(0);
            }
        }
        catch(Exception ee) {
            log.error("fnGetUserByName " + ee.getMessage());
            System.out.println("fnGetUserByName " + ee.getMessage());
        }
        return tbk;
    }

    public boolean fnUpdateUser(tbUser tbu)
    {
        Boolean bRtn;
        try {
            jdbcSysCfg.update(
                "UPDATE tbuser SET user_name=?, user_passwd=?, user_rtncode=?, birthday=?, " + 
                "sex=?, level=?, pay_time=?, reg_time=?, reg_ip=? WHERE user_id=?",
                tbu.user_name, tbu.user_passwd, tbu.user_rtncode, tbu.birthday, 
                tbu.sex, tbu.level, tbu.pay_time, tbu.reg_time, tbu.reg_ip, tbu.user_id);
            bRtn = true;
        }
        catch(Exception ee) {
            log.error("fnUpdateUser " + ee.getMessage());
            System.out.println("fnUpdateUser " + ee.getMessage());
            bRtn = false;
        }
        return bRtn;
    }

    public boolean fnInsertUser(tbUser tbu) {
        Boolean bRtn;
        try {
            jdbcSysCfg.update(
                "INSERT INTO tbuser (user_name, user_passwd, user_rtncode, birthday, sex, level, pay_time, reg_time, reg_ip) " + 
                "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ? )", tbu.user_name, tbu.user_passwd, tbu.user_rtncode,tbu.birthday, 
                tbu.sex, tbu.level, tbu.pay_time, tbu.reg_time, tbu.reg_ip ) ;
            bRtn = true;
        }
        catch(Exception ee) {
            log.error("fnInsertUser " + ee.getMessage());
            System.out.println("fnInsertUser " + ee.getMessage());
            bRtn = false;
        }
        return bRtn;
    }

    public boolean fnDeleteUser(Long id) {  //// 
        Boolean bRtn;
        String sql;
        try {
            sql = "DELETE FROM tbuser WHERE user_id =  " + id.toString();
            jdbcSysCfg.update( sql );
            bRtn = true;
        }
        catch(Exception ee) {
            log.error("fnDeleteUser " + ee.getMessage());
            System.out.println("fnDeleteUser " + ee.getMessage());
            bRtn = false;
        }
        return bRtn;
    }

//#endregion

//#region user login 登录日志 

    ///////----------------------------------------  tbuser_login  -------------------------------------------
    public List<tbUserLogin> fnGetUserLoginListByUserId(Long id){
        List<tbUserLogin> ltb;
        ltb = new ArrayList<>();
        try {
            String sql = "select * from tbuser_login where user_id = " + id.toString();
            ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserLogin>() {
                @Override
                public tbUserLogin mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                    tbUserLogin ss = new tbUserLogin();
                    ss.user_login_id = rs.getLong("user_login_id");
                    ss.user_id       = rs.getLong("user_id");
                    ss.login_time    = rs.getString("login_time");
                    ss.login_ip      = rs.getString("login_ip");
                    ss.data          = rs.getString("data");
                    return ss;
                }
            });            
        }
        catch(Exception ee) {
            log.error("fnGetUserLoginListByUserId " + ee.getMessage());
            System.out.println("fnGetUserLoginListByUserId " + ee.getMessage());
        }
        return ltb;
    }

    public tbUserLogin fnGetUserLoginListById(Long id){
        List<tbUserLogin> ltb;
        tbUserLogin tbk = null;
        ltb = new ArrayList<>();
        try {
            String sql = "select * from tbuser_login where user_login_id = " + id.toString();
            ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserLogin>() {
                @Override
                public tbUserLogin mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                    tbUserLogin ss = new tbUserLogin();
                    ss.user_login_id = rs.getLong("user_login_id");
                    ss.user_id       = rs.getLong("user_id");
                    ss.login_time    = rs.getString("login_time");
                    ss.login_ip      = rs.getString("login_ip");
                    ss.data          = rs.getString("data");
                    return ss;
                }
            });
            if( !ltb.isEmpty() ) {
                tbk = ltb.get(0);
            }
        }
        catch(Exception ee) {
            log.error("fnGetUserLoginListById " + ee.getMessage());
            System.out.println("fnGetUserLoginListById " + ee.getMessage());
        }
        return tbk;
    }

    public boolean fnInsertUserLogin(tbUserLogin tbu) {
        Boolean bRtn;
        try {
            jdbcSysCfg.update(
                "INSERT INTO tbuser_login (user_id, login_time, login_ip, data ) " + 
                "VALUES( ?, ?, ?, ? )", tbu.user_id, tbu.login_time, tbu.login_ip, tbu.data ) ;
            bRtn = true;
        }
        catch(Exception ee) {
            ee.printStackTrace();
            bRtn = false;
        }
        return bRtn;
    }

    public boolean fnDeleteUserLogin(Long id) {
        Boolean bRtn;
        try {
            String sql = "DELETE FROM tbuser_login WHERE user_login_id = " + id.toString();
            jdbcSysCfg.update( sql );
            bRtn = true;
        }
        catch(Exception ee) {
            log.error("fnDeleteUserLogin " + ee.getMessage());
            System.out.println("fnDeleteUserLogin " + ee.getMessage());
            bRtn = false;
        }
        return bRtn;
    }

//#endregion

//#region user pay

public List<tbUserPay> fnGetShortUserPayListByUserId(Long id){
    List<tbUserPay> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_id = " + id.toString() + " order by pay_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                String fate;
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.user_id     = rs.getLong("user_id");
                ss.pay_time    = rs.getString("pay_time");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan    = rs.getString("paipan");
                fate   = rs.getString("fate_self");
                ss.fate_self = "没分析";
                if( fate != null  ) {
                    if( fate.length() > 50) {
                        ss.fate_self = fate.substring(8, 50);
                    }
                }
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetShortUserPayListByUserId " + ee.getMessage());
        System.out.println("fnGetShortUserPayListByUserId " + ee.getMessage());
    }
    return ltb;
}

public List<tbUserPay> fnGetUserPayListByUserId(Long id){
    List<tbUserPay> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_id = " + id.toString();
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListByUserId " + ee.getMessage());
        System.out.println("fnGetUserPayListByUserId " + ee.getMessage());
    }
    return ltb;
}

public List<tbUserPay> fnGetUserPayListByUserIdPay(Long id){
    List<tbUserPay> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_id = " + id.toString() + " and pay_info = '已支付' order by pay_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListByUserId " + ee.getMessage());
        System.out.println("fnGetUserPayListByUserId " + ee.getMessage());
    }
    return ltb;
}

public tbUserPay fnGetUserPayListByUserIdBirthdaySex(Long id, String birthday, String sex){
    List<tbUserPay> ltb;
    tbUserPay tbk = null;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_id = " + id.toString() + " and birthday = '" + birthday + "' and sex = '" + sex + "'";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListByUserIdBirthdaySex " + ee.getMessage());
        System.out.println("fnGetUserPayListByUserIdBirthdaySex " + ee.getMessage());
    }
    return tbk;
}

public tbUserPay fnGetUserPayListById(Long id){
    List<tbUserPay> ltb;
    tbUserPay tbk = null;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_pay_id = " + id.toString();
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListById " + ee.getMessage());
        System.out.println("fnGetUserPayListById " + ee.getMessage());
    }
    return tbk;
}

public String fnGetUserPaidDateById(Long id){
    String rtn = "";
    List<tbUserPay> ltb;
    tbUserPay tbk = null;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where user_pay_id = " + id.toString() + " and pay_info = '已支付' ORDER BY pay_time DESC";   
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
            rtn = tbk.pay_time;
        }
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListById " + ee.getMessage());
        System.out.println("fnGetUserPayListById " + ee.getMessage());
    }
    return rtn;
}

public tbUserPay fnGetUserPayByOrderNo(String orderNo){
    List<tbUserPay> ltb;
    tbUserPay tbk = null;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbuser_pay where pay_order = '" + orderNo + "'";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbUserPay>() {
            @Override
            public tbUserPay mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbUserPay ss = new tbUserPay();
                ss.user_pay_id = rs.getLong("user_pay_id");
                ss.user_id     = rs.getLong("user_id");
                ss.nick_name   = rs.getString("nick_name");
                ss.pay_time    = rs.getString("pay_time");
                ss.pay_ip      = rs.getString("pay_ip");
                ss.pay_order   = rs.getString("pay_order");
                ss.pay_info    = rs.getString("pay_info");
                ss.sex         = rs.getString("sex");
                ss.birthday    = rs.getString("birthday");
                ss.paipan      = rs.getString("paipan");
                ss.fate_self   = rs.getString("fate_self");
                ss.fate_big    = rs.getString("fate_big");
                ss.remark   = rs.getString("remark");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetUserPayListById " + ee.getMessage());
        System.out.println("fnGetUserPayListById " + ee.getMessage());
    }
    return tbk;
}


public boolean fnInsertUserPay(tbUserPay tbu) {
    Boolean bRtn;
    try {
        if(tbu.nick_name == null) {
            tbu.nick_name = "";
        }
        jdbcSysCfg.update(
            "INSERT INTO tbuser_pay (user_id, nick_name, pay_time, pay_ip, pay_order, pay_info, sex, birthday, paipan, fate_self, " +
            "fate_big, remark ) " + 
            "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", 
            tbu.user_id, tbu.nick_name, tbu.pay_time, tbu.pay_ip, tbu.pay_order, tbu.pay_info, tbu.sex, tbu.birthday, tbu.paipan,
            tbu.fate_self, tbu.fate_big, tbu.remark ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnInsertUserPay " + ee.getMessage());
        System.out.println("fnInsertUserPay " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public Long fnInsertUserPayRtnKey(tbUserPay tbu) {
    Long key = 0L;
    try {
        if(tbu.nick_name == null) {
            tbu.nick_name = "";
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tbuser_pay (user_id, nick_name, pay_time, pay_ip, pay_order, pay_info, sex, birthday, paipan, fate_self, " +
        "fate_big, remark ) " + 
        "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        jdbcSysCfg.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, new String[] {"user_pay_id"});
            ps.setLong(1, tbu.user_id);
            ps.setString(2, tbu.nick_name);
            ps.setString(3, tbu.pay_time);
            ps.setString(4, tbu.pay_ip);
            ps.setString(5, tbu.pay_order);
            ps.setString(6, tbu.pay_info);
            ps.setString(7, tbu.sex);
            ps.setString(8, tbu.birthday);
            ps.setString(9, tbu.paipan);
            ps.setString(10, tbu.fate_self);
            ps.setString(11, tbu.fate_big);
            ps.setString(12, tbu.remark);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            Number kkk = keyHolder.getKey(); 
            if( kkk != null ) {
                key = kkk.longValue();
            }
        }
    }
    catch(Exception ee) {
        log.error("fnInsertUserPay " + ee.getMessage());
        System.out.println("fnInsertUserPay " + ee.getMessage());
    }
    return key;
}

public boolean fnUpdateUserPay(tbUserPay tbu) {
    Boolean bRtn;
    try {
        if(tbu.nick_name == null) {
            tbu.nick_name = "";
        }
        jdbcSysCfg.update(
            "UPDATE tbuser_pay SET user_id=?, nick_name=?, pay_time=?, pay_ip=?, pay_order=?, pay_info=?, sex=?, birthday=?, paipan=?, fate_self=?, " + 
            "fate_big=?, remark=? WHERE user_pay_id=?", 
            tbu.user_id, tbu.nick_name, tbu.pay_time, tbu.pay_ip, tbu.pay_order, tbu.pay_info, tbu.sex, tbu.birthday, tbu.paipan, tbu.fate_self, tbu.fate_big,
            tbu.remark, tbu.user_pay_id ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnUpdateUserPay " + ee.getMessage());
        System.out.println("fnUpdateUserPay " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public boolean fnDeleteUserPay(Long id) {
    Boolean bRtn;
    try {
        String sql = "DELETE FROM tbuser_pay WHERE user_pay_id = " + id.toString();
        jdbcSysCfg.update( sql );
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnDeleteUserPay " + ee.getMessage());
        System.out.println("fnDeleteUserPay " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

//#endregion

//#region card

public List<tbCard> fnGetCardListByUserId(Long id){
    List<tbCard> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbcard where user_id = " + id.toString() + " order by card_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCard>() {
            @Override
            public tbCard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCard ss = new tbCard();
                ss.card_id   = rs.getLong("card_id");
                ss.user_id   = rs.getLong("user_id");
                ss.card_name = rs.getString("card_name");
                ss.card_phone = rs.getString("card_phone");
                ss.card_num  = rs.getInt("card_num");
                ss.card_time = rs.getString("card_time");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetCardListByUserId " + ee.getMessage());
        System.out.println("fnGetCardListByUserId " + ee.getMessage());
    }
    return ltb;
}

public List<tbCard> fnGetCardListByUserIdAndCardName(Long id, String card_name){
    List<tbCard> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbcard where user_id = " + id.toString() + " and card_name like '%" + card_name + "%' order by card_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCard>() {
            @Override
            public tbCard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCard ss = new tbCard();
                ss.card_id   = rs.getLong("card_id");
                ss.user_id   = rs.getLong("user_id");
                ss.card_name = rs.getString("card_name");
                ss.card_phone = rs.getString("card_phone");
                ss.card_num  = rs.getInt("card_num");
                ss.card_time = rs.getString("card_time");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetCardListByUserId " + ee.getMessage());
        System.out.println("fnGetCardListByUserId " + ee.getMessage());
    }
    return ltb;
}

public List<tbCard> fnGetCardListByUserIdAndPhone(Long id, String phone){
    List<tbCard> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbcard where user_id = " + id.toString() + " and card_phone like '%" + phone + "%' order by card_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCard>() {
            @Override
            public tbCard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCard ss = new tbCard();
                ss.card_id   = rs.getLong("card_id");
                ss.user_id   = rs.getLong("user_id");
                ss.card_name = rs.getString("card_name");
                ss.card_phone = rs.getString("card_phone");
                ss.card_num  = rs.getInt("card_num");
                ss.card_time = rs.getString("card_time");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetCardListByUserId " + ee.getMessage());
        System.out.println("fnGetCardListByUserId " + ee.getMessage());
    }
    return ltb;
}

public tbCard fnGetCardListByCardId(Long id){
    List<tbCard> ltb;
    ltb = new ArrayList<>();
    tbCard tbk = null;
    try {
        String sql = "select * from tbcard where card_id = " + id.toString();
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCard>() {
            @Override
            public tbCard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCard ss = new tbCard();
                ss.card_id   = rs.getLong("card_id");
                ss.user_id   = rs.getLong("user_id");
                ss.card_name = rs.getString("card_name");
                ss.card_phone = rs.getString("card_phone");
                ss.card_num  = rs.getInt("card_num");
                ss.card_time = rs.getString("card_time");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetCardListById " + ee.getMessage());
        System.out.println("fnGetCardListById " + ee.getMessage());
    }
    return tbk;
}

public tbCard fnGetCardListByUserIdAndFullPhone(Long id, String phone){
    List<tbCard> ltb;
    ltb = new ArrayList<>();
    tbCard tbk = null;
    try {
        String sql = "select * from tbcard where user_id = " + id.toString() + " and card_phone = '" + phone + "' order by card_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCard>() {
            @Override
            public tbCard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCard ss = new tbCard();
                ss.card_id   = rs.getLong("card_id");
                ss.user_id   = rs.getLong("user_id");
                ss.card_name = rs.getString("card_name");
                ss.card_phone = rs.getString("card_phone");
                ss.card_num  = rs.getInt("card_num");
                ss.card_time = rs.getString("card_time");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetCardListById " + ee.getMessage());
        System.out.println("fnGetCardListById " + ee.getMessage());
    }
    return tbk;
}


public boolean fnInsertCard(tbCard tbu) {
    Boolean bRtn;
    try {
        jdbcSysCfg.update(
            "INSERT INTO tbcard (user_id, card_name, card_phone, card_num, card_time ) " + 
            "VALUES( ?, ?, ?, ?, ? )", tbu.user_id, tbu.card_name, tbu.card_phone, tbu.card_num, tbu.card_time ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnInsertCard " + ee.getMessage());
        System.out.println("fnInsertCard " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public boolean fnUpdateCard(tbCard tbu) {
    Boolean bRtn;
    try {
        jdbcSysCfg.update(
            "UPDATE tbcard SET user_id=?, card_name=?, card_phone=?, card_num=?, card_time=? WHERE card_id=?", 
            tbu.user_id, tbu.card_name, tbu.card_phone, tbu.card_num, tbu.card_time, tbu.card_id ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnUpdateCard " + ee.getMessage());
        System.out.println("fnUpdateCard " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public boolean fnDeleteCard(Long id) {
    Boolean bRtn;
    try {
        String sql = "DELETE FROM tbcard WHERE card_id = " + id.toString();
        jdbcSysCfg.update( sql );
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnDeleteCard " + ee.getMessage());
        System.out.println("fnDeleteCard " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

//#endregion

//#region card detail 

public List<tbCardDetail> fnGetCardDetailListByCardId(Long id){
    List<tbCardDetail> ltb;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbcard_detail where card_id = " + id.toString() + " order by card_time desc";
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCardDetail>() {
            @Override
            public tbCardDetail mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCardDetail ss = new tbCardDetail();
                ss.card_did = rs.getLong("card_did");
                ss.card_id  = rs.getLong("card_id");
                ss.card_time = rs.getString("card_time");
                ss.card_num  = rs.getInt("card_num");
                return ss;
            }
        });            
    }
    catch(Exception ee) {
        log.error("fnGetCardDetailListByCardId " + ee.getMessage());
        System.out.println("fnGetCardDetailListByCardId " + ee.getMessage());
    }
    return ltb;
}

public tbCardDetail fnGetCardDetailListById(Long id){
    List<tbCardDetail> ltb;
    tbCardDetail tbk = null;
    ltb = new ArrayList<>();
    try {
        String sql = "select * from tbcard_detail where card_did = " + id.toString();
        ltb = jdbcSysCfg.query(sql, new RowMapper<tbCardDetail>() {
            @Override
            public tbCardDetail mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                tbCardDetail ss = new tbCardDetail();
                ss.card_did = rs.getLong("card_did");
                ss.card_id  = rs.getLong("card_id");
                ss.card_time = rs.getString("card_time");
                ss.card_num  = rs.getInt("card_num");
                return ss;
            }
        });
        if( !ltb.isEmpty() ) {
            tbk = ltb.get(0);
        }
    }
    catch(Exception ee) {
        log.error("fnGetCardDetailListById " + ee.getMessage());
        System.out.println("fnGetCardDetailListById " + ee.getMessage());
    }
    return tbk;
}

public boolean fnInsertCardDetail(tbCardDetail tbu) {
    Boolean bRtn;
    try {
        jdbcSysCfg.update(
            "INSERT INTO tbcard_detail (card_id, card_time, card_num ) " + 
            "VALUES( ?, ?, ? )", tbu.card_id, tbu.card_time, tbu.card_num ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnInsertCardDetail " + ee.getMessage());
        System.out.println("fnInsertCardDetail " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public boolean fnUpdateCardDetail(tbCardDetail tbu) {
    Boolean bRtn;
    try {
        jdbcSysCfg.update(
            "UPDATE tbcard_detail SET card_id=?, card_time=?, card_num=? WHERE card_did=?", 
            tbu.card_id, tbu.card_time, tbu.card_num, tbu.card_did ) ;
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnUpdateCardDetail " + ee.getMessage());
        System.out.println("fnUpdateCardDetail " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

public boolean fnDeleteCardDetail(Long id) {
    Boolean bRtn;
    try {
        String sql = "DELETE FROM tbcard_detail WHERE card_did = " + id.toString();
        jdbcSysCfg.update( sql );
        bRtn = true;
    }
    catch(Exception ee) {
        log.error("fnDeleteCardDetail " + ee.getMessage());
        System.out.println("fnDeleteCardDetail " + ee.getMessage());
        bRtn = false;
    }
    return bRtn;
}

//#endregion

////---------------------------------------- other tools --------------------------


}

//--------------------------------------------- table class ---------------------------
class tbUser {
    public Long   user_id;
    public String user_name;    //// 名,wx_openid
    public String user_passwd;  //// 字
    public String user_rtncode;  //// 号，找回密码用
    public String birthday;    //// 
    public String sex;
    public String level;
    public String pay_time;   //// 收费用户的有效期的截至日期
    public String reg_time;
    public String reg_ip;
}

class tbUserLogin {
    public Long   user_login_id;
    public Long   user_id;
    public String login_time;
    public String login_ip;
    public String data;
}

class tbUserPay {
    public Long   user_pay_id;
    public Long   user_id;
    public String nick_name;
    public String pay_time;
    public String pay_ip;
    public String pay_order;
    public String pay_info;
    public String sex;
    public String birthday;
    public String paipan;
    public String fate_self;  //// 八字命运通俗版
    public String fate_big;  //// 八字大运专业版
    public String remark;  //// 八字第一大运的每个流年
}

class tbCard {
    public Long   card_id;
    public Long   user_id;
    public String card_name;
    public String card_phone;
    public Integer card_num;
    public String card_time;
}
 
class tbCardDetail {
    public Long   card_did;
    public Long   card_id;
    public String card_time;
    public Integer card_num;
    
}

////--------------------------------