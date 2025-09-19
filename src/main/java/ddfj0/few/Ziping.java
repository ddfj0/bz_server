package ddfj0.few;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Ziping {
    
    public BaZiFateAge bazi;

    public Integer feJin;
    public Integer feMu;
    public Integer feShui;
    public Integer feHuo;
    public Integer feTu;

    public Integer feGanJin;
    public Integer feGanMu;
    public Integer feGanShui;
    public Integer feGanHuo;
    public Integer feGanTu;

    public Integer shenGuan;
    public Integer shenSha;
    public Integer shenYin;
    public Integer shenXiao;
    public Integer shenBi;
    public Integer shenJie;
    public Integer shenShi;
    public Integer shenShang;
    public Integer shenCaiz;
    public Integer shenCaip;

    public Integer zpSheng;
    public Integer zpKe;
    public String  xiYong;
    public String  shenXyz;
    public String  shenXyp;
    
    public boolean bPay;
    public Integer yearShow;

    public Ziping(BaZiFateAge baZiFateAge) {
        bPay = false;
        bazi = baZiFateAge;
        fnBaZiAnalysis();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        try {
            int birthdayYear = Integer.valueOf(baZiFateAge.birthday.split("-")[0]);
            yearShow = year - birthdayYear + 3;
        }
        catch(Exception e) {
            yearShow = 10;
        }   
    }


//#region mp analysis

public String fnAnalysisSelfMp() { /// 免费的八字命理分析 
    ArrayList<String> arrAnalysis = new ArrayList<String>();
    // 开始按 命格分析，本命强弱，五行喜忌，命格，婚宫，性格，子女，合冲等
    String msg;

    bPay = false;

    if( bazi == null ) {
        arrAnalysis.add("无数据");
        return JSON.toJSONString(arrAnalysis);
    }

    arrAnalysis.add("公历：" + bazi.birthday);
    arrAnalysis.add("农历：" + bazi.lunarDate);
    arrAnalysis.add("中华历：" + bazi.yearGan + bazi.yearZhi + "年 " + bazi.monthGan + bazi.monthZhi + "月 " + bazi.dayGan + bazi.dayZhi + "日 " + bazi.hourGan + bazi.hourZhi + "时");
    if( bazi.sex.equals("男")) {
        msg = "性别：男生";
    }
    else {
        msg = "性别：女生";
    }
    arrAnalysis.add(msg);

    // 日干分析
    List<String> list  = fnSelfPayDayGan(bazi.dayGan);
    arrAnalysis.addAll(list);

    // 生助克泄，寒暖燥湿
    msg = fnSelfUpDown();
    arrAnalysis.add(msg);

    msg = fnSelfDayZhiMonthZhiHourGan();
    arrAnalysis.add(msg);
    
    // 十神
    msg = fnSelfPayShiShen(false);
    arrAnalysis.add(msg);
    
    // 天干相合，相冲
    msg = fnSelfGanHeChong(false);
    arrAnalysis.add(msg);

    // 地址六合，三合，三会，相冲，相害
    msg = fnSelfZhiHeChong(false);
    arrAnalysis.add(msg);

    // 分析大运
    list = fnSelfPayBigFate(false);
    arrAnalysis.addAll(list);

    
    // 分析流年
    list = fnSelfPayLiuNian(false);
    arrAnalysis.addAll(list);
    

    String str = JSON.toJSONString(arrAnalysis);
    return str;
}

public String fnAnalysisSelfPayMp() { /// 收费的八字命理分析 
    ArrayList<String> arrAnalysis = new ArrayList<String>();
    // 开始按 命格分析，本命强弱，五行喜忌，命格，婚宫，性格，子女，合冲等
    String msg;

    bPay = true;

    if( bazi == null ) {
        arrAnalysis.add("无数据");
        return JSON.toJSONString(arrAnalysis);
    }

    arrAnalysis.add("公历：" + bazi.birthday);
    arrAnalysis.add("农历：" + bazi.lunarDate);
    arrAnalysis.add("中华历：" + bazi.yearGan + bazi.yearZhi + "年 " + bazi.monthGan + bazi.monthZhi + "月 " + bazi.dayGan + bazi.dayZhi + "日 " + bazi.hourGan + bazi.hourZhi + "时");
    if( bazi.sex.equals("男")) {
        msg = "性别：男生";
    }
    else {
        msg = "性别：女生";
    }
    arrAnalysis.add(msg);

    // 日干分析
    List<String> list = fnSelfPayDayGan(bazi.dayGan);
    arrAnalysis.addAll(list);

    // 生助克泄，寒暖燥湿
    msg = fnSelfPayUpDown();
    arrAnalysis.add(msg);

    msg = fnSelfDayZhiMonthZhiHourGan();
    arrAnalysis.add(msg);
    
    // 十神
    msg = fnSelfPayShiShen(true);
    arrAnalysis.add(msg);
    
    // 天干相合，相冲
    msg = fnSelfGanHeChong(true);
    arrAnalysis.add(msg);

    // 地址六合，三合，三会，相冲，相害
    msg = fnSelfZhiHeChong(true);
    arrAnalysis.add(msg);

    // 分析大运
    list = fnSelfPayBigFate(true);
    arrAnalysis.addAll(list);

    // 分析流年，只分析今年和明年
    list = fnSelfPayLiuNian(true);
    arrAnalysis.addAll(list);
    
    String str = JSON.toJSONString(arrAnalysis);
    return str;
}


public List<String> fnDayFateAnalysis(String paipanCurDay ) { /// 免费的日运分析
    String rtn = "今日信息";
    Integer orderNo = 1;
    ArrayList<String> arrAnalysis = new ArrayList<String>();       
    arrAnalysis.add(rtn);

    BaZiFateAge baziDay = JSON.parseObject(paipanCurDay, BaZiFateAge.class);

    if( baziDay.dayGan.equals("甲") || baziDay.dayGan.equals("乙") ) {
        arrAnalysis.add(orderNo.toString() +  "、甲乙"); orderNo++;
        arrAnalysis.add("与亲友同学有事情。");

        arrAnalysis.add(orderNo.toString() +  "、丙丁"); orderNo++;
        arrAnalysis.add("有人帮忙，做事顺利。");

        arrAnalysis.add(orderNo.toString() +  "、戊己"); orderNo++;
        arrAnalysis.add("做事不顺，压力大。"); 

        arrAnalysis.add(orderNo.toString() +  "、庚辛"); orderNo++;
        arrAnalysis.add("有可能花钱或者赚钱。");

        arrAnalysis.add(orderNo.toString() +  "、壬癸"); orderNo++;
        arrAnalysis.add("需要付出。"); 
    }
    else if( baziDay.dayGan.equals("丙") || baziDay.dayGan.equals("丁") ) {
        arrAnalysis.add(orderNo.toString() +  "、甲乙"); orderNo++;
        arrAnalysis.add("需要付出。"); 

        arrAnalysis.add(orderNo.toString() +  "、丙丁"); orderNo++;
        arrAnalysis.add("与亲友同学有事情。");

        arrAnalysis.add(orderNo.toString() +  "、戊己"); orderNo++;
        arrAnalysis.add("有人帮忙，做事顺利。");

        arrAnalysis.add(orderNo.toString() +  "、庚辛"); orderNo++;
        arrAnalysis.add("做事不顺，压力大。"); 

        arrAnalysis.add(orderNo.toString() +  "、壬癸"); orderNo++;
        arrAnalysis.add("有可能花钱或者赚钱。");

    }
    else if( baziDay.dayGan.equals("戊") || baziDay.dayGan.equals("己") ) {
        arrAnalysis.add(orderNo.toString() +  "、甲乙"); orderNo++;
        arrAnalysis.add("有可能花钱或者赚钱。");

        arrAnalysis.add(orderNo.toString() +  "、丙丁"); orderNo++;
        arrAnalysis.add("需要付出。"); 

        arrAnalysis.add(orderNo.toString() +  "、戊己"); orderNo++;
        arrAnalysis.add("与亲友同学有事情。");

        arrAnalysis.add(orderNo.toString() +  "、庚辛"); orderNo++;
        arrAnalysis.add("有人帮忙，做事顺利。");

        arrAnalysis.add(orderNo.toString() +  "、壬癸"); orderNo++;
        arrAnalysis.add("做事不顺，压力大。"); 

    }
    else if( baziDay.dayGan.equals("庚") || baziDay.dayGan.equals("辛") ) {
        arrAnalysis.add(orderNo.toString() +  "、甲乙"); orderNo++;
        arrAnalysis.add("做事不顺，压力大。"); 

        arrAnalysis.add(orderNo.toString() +  "、丙丁"); orderNo++;
        arrAnalysis.add("有可能花钱或者赚钱。");

        arrAnalysis.add(orderNo.toString() +  "、戊己"); orderNo++;
        arrAnalysis.add("需要付出。"); 

        arrAnalysis.add(orderNo.toString() +  "、庚辛"); orderNo++;
        arrAnalysis.add("与亲友同学有事情。");

        arrAnalysis.add(orderNo.toString() +  "、壬癸"); orderNo++;
        arrAnalysis.add("有人帮忙，做事顺利。");
    }
    else if( baziDay.dayGan.equals("壬") || baziDay.dayGan.equals("癸") ) {
        arrAnalysis.add(orderNo.toString() +  "、甲乙"); orderNo++;
        arrAnalysis.add("有人帮忙，做事顺利。");

        arrAnalysis.add(orderNo.toString() +  "、丙丁"); orderNo++;
        arrAnalysis.add("做事不顺，压力大。"); 

        arrAnalysis.add(orderNo.toString() +  "、戊己"); orderNo++;
        arrAnalysis.add("有可能花钱或者赚钱。");

        arrAnalysis.add(orderNo.toString() +  "、庚辛"); orderNo++;
        arrAnalysis.add("需要付出。"); 

        arrAnalysis.add(orderNo.toString() +  "、壬癸"); orderNo++;
        arrAnalysis.add("与亲友同学有事情。");
    }

    return arrAnalysis;
}

public List<String> fnDayFateAnalysisPaid(String paipanCurDay ) { /// 收费的用户日运分析
    String rtn = "本人今日信息";
    Integer orderNo = 1;
    ArrayList<String> arrAnalysis = new ArrayList<String>();
    arrAnalysis.add(rtn);

    BaZiFateAge baziDay = JSON.parseObject(paipanCurDay, BaZiFateAge.class);

    if( baziDay.dayGan.equals("甲") || baziDay.dayGan.equals("乙") ) {
        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            arrAnalysis.add(orderNo.toString() +  "、 与亲友同学有事情。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有人帮忙，做事顺利。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            arrAnalysis.add(orderNo.toString() +  "、 做事不顺，压力大。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有可能花钱或者赚钱。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            arrAnalysis.add(orderNo.toString() +  "、 需要付出。"); 
            orderNo++;
        }
    }
    else if( baziDay.dayGan.equals("丙") || baziDay.dayGan.equals("丁") ) {
        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            arrAnalysis.add(orderNo.toString() +  "、 需要付出。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            arrAnalysis.add(orderNo.toString() +  "、 与亲友同学有事情。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有人帮忙，做事顺利。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            arrAnalysis.add(orderNo.toString() +  "、 做事不顺，压力大。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有可能花钱或者赚钱。");
            orderNo++;
        }
    }
    else if( baziDay.dayGan.equals("戊") || baziDay.dayGan.equals("己") ) {
        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有可能花钱或者赚钱。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            arrAnalysis.add(orderNo.toString() +  "、 需要付出。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            arrAnalysis.add(orderNo.toString() +  "、 与亲友同学有事情。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有人帮忙，做事顺利。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            arrAnalysis.add(orderNo.toString() +  "、 做事不顺，压力大。"); 
            orderNo++;
        }
    }
    else if( baziDay.dayGan.equals("庚") || baziDay.dayGan.equals("辛") ) {
        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            arrAnalysis.add(orderNo.toString() +  "、 做事不顺，压力大。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有可能花钱或者赚钱。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            arrAnalysis.add(orderNo.toString() +  "、 需要付出。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            arrAnalysis.add(orderNo.toString() +  "、 与亲友同学有事情。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有人帮忙，做事顺利。");
            orderNo++;
        }
    }
    else if( baziDay.dayGan.equals("壬") || baziDay.dayGan.equals("癸") ) {
        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有人帮忙，做事顺利。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            arrAnalysis.add(orderNo.toString() +  "、 做事不顺，压力大。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            arrAnalysis.add(orderNo.toString() +  "、 有可能花钱或者赚钱。");
            orderNo++;
        }
        else if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            arrAnalysis.add(orderNo.toString() +  "、 需要付出。"); 
            orderNo++;
        }
        else if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            arrAnalysis.add(orderNo.toString() +  "、 与亲友同学有事情。");
            orderNo++;
        }
    }

    return arrAnalysis;
}

//#endregion
    
//#region analysis 
    public String fnAnalysisSelf() { /// 免费的八字命理分析 
        ArrayList<String> arrAnalysis = new ArrayList<String>();
        // 开始按 命格分析，本命强弱，五行喜忌，命格，婚宫，性格，子女，合冲等
        String msg;

        bPay = false;

        if( bazi == null ) {
            arrAnalysis.add("null");
            return JSON.toJSONString(arrAnalysis);
        }
        if( bazi.sex.equals("男")) {
            msg = "乾命（男）：" + bazi.birthday + " " + bazi.lunarDate;
        }
        else {
            msg = "坤命（女）：" + bazi.birthday + " " + bazi.lunarDate;
        }
        arrAnalysis.add(msg);

        // 日干分析
        msg = fnSelfDayGan(bazi.dayGan);
        arrAnalysis.add(msg);

        // 生助克泄，寒暖燥湿
        msg = fnSelfUpDown();
        arrAnalysis.add(msg);

        msg = fnSelfDayZhiMonthZhiHourGan();
        arrAnalysis.add(msg);
        
        // 十神
        msg = fnSelfPayShiShen(false);
        arrAnalysis.add(msg);
        
        // 天干相合，相冲
        msg = fnSelfGanHeChong(false);
        arrAnalysis.add(msg);

        // 地址六合，三合，三会，相冲，相害
        msg = fnSelfZhiHeChong(false);
        arrAnalysis.add(msg);

        // 分析大运
        List<String> list = fnSelfPayBigFate(false);
        arrAnalysis.addAll(list);

        
        // 分析流年
        list = fnSelfPayLiuNian(false);
        arrAnalysis.addAll(list);
        

        String str = JSON.toJSONString(arrAnalysis);
        return str;
    }

    public String fnAnalysisSelfPay() { /// 收费的八字命理分析 
        ArrayList<String> arrAnalysis = new ArrayList<String>();
        // 开始按 命格分析，本命强弱，五行喜忌，命格，婚宫，性格，子女，合冲等
        String msg;

        bPay = true;

        if( bazi == null ) {
            arrAnalysis.add("null");
            return JSON.toJSONString(arrAnalysis);
        }
        if( bazi.sex.equals("男")) {
            msg = "乾命（男）：" + bazi.birthday + " " + bazi.lunarDate;
        }
        else {
            msg = "坤命（女）：" + bazi.birthday + " " + bazi.lunarDate;
        }
        arrAnalysis.add(msg);

        msg = bazi.yearGan + bazi.yearZhi + "-" + bazi.monthGan + bazi.monthZhi + "-" + bazi.dayGan + bazi.dayZhi + "-" + bazi.hourGan + bazi.hourZhi;
        arrAnalysis.add(msg);

        // 日干分析
        List<String> list = fnSelfPayDayGan(bazi.dayGan);
        arrAnalysis.addAll(list);

        // 生助克泄，寒暖燥湿
        msg = fnSelfPayUpDown();
        arrAnalysis.add(msg);

        msg = fnSelfDayZhiMonthZhiHourGan();
        arrAnalysis.add(msg);
        
        // 十神
        msg = fnSelfPayShiShen(true);
        arrAnalysis.add(msg);
        
        // 天干相合，相冲
        msg = fnSelfGanHeChong(true);
        arrAnalysis.add(msg);

        // 地址六合，三合，三会，相冲，相害
        msg = fnSelfZhiHeChong(true);
        arrAnalysis.add(msg);

        // 分析大运
        list = fnSelfPayBigFate(true);
        arrAnalysis.addAll(list);

        // 分析流年，只分析今年和明年
        list = fnSelfPayLiuNian(true);
        arrAnalysis.addAll(list);
        
        String str = JSON.toJSONString(arrAnalysis);
        return str;
    }

//#endregion

//#region tools 
    public String fnSelfDayGan(String dayGan) {
        // 日干分析
        String benMing = fnGz2Wu(bazi.dayGan);
      
        // 日主旺衰，喜忌
        String str = "本命为" + bazi.dayGan + "(" + benMing + ")。";

        if( dayGan.equals("甲")) {
            str = str + "似参天之木，乐于助人，不屈服，不变通。";
            if( bazi.monthZhi.equals("子") || bazi.monthZhi.equals("丑") || bazi.monthZhi.equals("寅") ) {
                str = str + "，生冬季";
                if(feHuo > 0) {
                    str = str + "有火得温，一生多顺。";
                }
                else {
                    str = str + "无火，运中带火时较顺。";
                }
            }
        }
        else if( dayGan.equals("乙")) {
            str = str + "似花草之木，柔和，敏感，无主见。";
            if( feShui > 0 && feTu > 0 && feHuo > 0) {
                    str = str + "有土，有水，有火，靓丽多姿，秀美可爱。";
            }
        }
        else if( dayGan.equals("丙")) {
            str = str + "似猛烈之火，热情，大方，易冲动。";            
        }
        else if( dayGan.equals("丁")) {
            str = str + "似油灯之火，外柔，内刚，多愁疑。";             
        }
        else if( dayGan.equals("戊")) {
            str = str + "似高原之土，稳重，守诺，较保守。";            
        }
        else if( dayGan.equals("己")) {
            str = str + "似田园之土，仁慈，心细，多忧郁。";            
        }
        else if( dayGan.equals("庚")) {
            str = str + "似剑斧之金，爽快，义气，多直率。";            
        }
        else if( dayGan.equals("辛")) {
            str = str + "似首饰之金，温顺，灵秀，多敏感。";            
        }
        else if( dayGan.equals("壬")) {
            str = str + "似江海之水，深沉，随和，多变化。";
        }
        else if( dayGan.equals("癸")) {
            str = str + "似雨露之水，外冷，内热，易悲观。";
        }

        return str;
    }

    public String fnSelfMingGe(String monthZhi) {
        // 命格分析
        String str = "未成正格。";
        // 月支为官煞，印枭，食伤，财才，
        if( bazi.shenMonthZhi.equals("官")) {
            if( bazi.shenHourGan.equals("官") ||
                bazi.shenMonthGan.equals("官") || 
                bazi.shenYearGan.equals("官") ) {
                if( shenShang > 0 || shenShi > 0) {
                    str = "有食伤，未成正官格。";
                }
                else if(shenSha > 0 ) {
                    str = "官杀混杂，未成正官格。";
                }
                else {
                    if( (bazi.shenHourGan.equals("财") || bazi.shenHourGan.equals("才") ) 
                        && ( shenCaip == 1 || shenCaiz == 1) ) {
                        str = "成正官格，且有财，上贵之命。";
                    }
                    else if( (shenYin > 0 || shenXiao > 0) && (shenCaip > 0 || shenCaiz > 0 )) {
                        str = "成正官格，且有财有印，大富大贵命。";
                    }
                    else {
                        str = "成正官格，富贵之命。";
                    }
                }
            }
        }
        else if( bazi.shenMonthZhi.equals("煞")) {
            if( bazi.shenHourGan.equals("煞") ||
                bazi.shenMonthGan.equals("煞") || 
                bazi.shenYearGan.equals("煞") ) {
                if( shenShang == 1 || shenShi == 1) {
                    if(shenCaip == 1 || shenCaiz == 1 ) {
                        if(shenSha == 2) {
                            str = "有食伤，带财，成偏官格，大富大贵之命。";
                        }
                        else {
                            str = "有食伤，带财，成偏官格。";
                        }
                    }
                    else {
                        str = "有食伤，成偏官格。";
                    }
                }
                else if(shenGuan > 0 ) {
                    str = "官杀混杂，未成官煞格。";
                }
                else if(shenShang == 0 && shenShi == 0){
                    if( bazi.shenHourGan.equals("煞") && shenSha == 2 ) {
                        str = "成七煞格，大富大贵之命。";
                    }
                    else if( shenSha == 2 ) {
                        str = "成七煞格，富贵之命。";
                    }
                }
            }
        }
        else if( bazi.shenMonthZhi.equals("印")) {
            if( bazi.shenHourGan.equals("印") ||
                bazi.shenMonthGan.equals("印") || 
                bazi.shenYearGan.equals("印") ) {
                if( shenYin == 2 && shenXiao == 0 && (shenCaip + shenCaiz) <= 2) {
                    str = "成正印格。";
                }
            }
        }
        else if( bazi.shenMonthZhi.equals("食")) {
            if( bazi.shenHourGan.equals("食") ||
                bazi.shenMonthGan.equals("食") || 
                bazi.shenYearGan.equals("食") ) {
                if( zpSheng > zpKe && shenShi == 2 && shenShang == 0 && 
                    shenGuan == 0 && shenSha == 0 && shenYin == 0 && shenXiao == 0 ) {
                    str = "成食神格。";
                }
            }
        }
        else if( bazi.shenMonthZhi.equals("伤")) {
            if( bazi.shenHourGan.equals("伤") ||
                bazi.shenMonthGan.equals("伤") || 
                bazi.shenYearGan.equals("伤") ) {
                if( zpSheng > zpKe && shenShang == 2 && shenShi == 0 && shenGuan == 0 && shenSha == 0 && (shenCaip + shenCaiz) > 0 ) {
                    str = "成伤官格。";
                } 
            }
        }
        else if( bazi.shenMonthZhi.equals("财")) {
            if( bazi.shenHourGan.equals("财") ||
                bazi.shenMonthGan.equals("财") || 
                bazi.shenYearGan.equals("财") ) {
                if( zpSheng > zpKe && shenXiao == 0 && shenBi == 0 && shenJie == 0 ) {
                    str = "成正财格，富贵命。";
                }
            }
        }
        else if( bazi.shenMonthZhi.equals("才")) {
            if( bazi.shenHourGan.equals("才") ||
                bazi.shenMonthGan.equals("才") || 
                bazi.shenYearGan.equals("才") ) {
                if( zpSheng > zpKe && shenCaip == 2 && shenBi == 0 && shenJie == 0 ) {
                    str = "成偏财格。";
                }
            }
        }

        return str;
    }

    public String fnSelfUpDown() {
        String str = "";

        xiYong = fnComputeXiYong( zpSheng, zpKe);
      
        // 日主旺衰，喜忌
        if( zpSheng > zpKe ) {
            str = "身旺，";
        }
        else if( zpSheng < zpKe ) {
            str = "身弱，";
        }
        else {
            str = "身旺一点点，";
        }
        //str   = "生助" + zpSheng + ", 克泄" + zpKe + 
        str = str +  "喜用为" + xiYong + ", " + fnWuHelp(xiYong) +
                    "如懂风水，在工作和生活的环境，布置" + xiYong + "局，" +
                    "或者在" + xiYong + "位。通过风水的调整，改变命运。";

        return str;
    }

    public String fnSelfDayZhiMonthZhiHourGan() {
        //// 日支，月支，时干分析
        String str = "";
        if( bazi.sex.equals("男") ) {
            if( bazi.shenDayZhi.equals("财") ) {
                if ((shenCaiz + shenCaip) > 1 ) {
                    str = str + "虽然有多段情感，但是妻子在婚宫，夫妻感情和睦。";
                }
                else {
                    str = str + "妻子在婚宫，夫妻感情和睦。";
                }
            }
            else if( bazi.shenMonthZhi.equals("财") || 
                    bazi.shenHourGan.equals("财") 
                )
            {
                if ((shenCaiz + shenCaip) > 1 ) {
                    str = str + "虽然有多段情感，但是与妻子关系良好。";
                }
                else {
                    str = str + "与妻子关系良好。";
                }
            }
            else if( bazi.shenDayZhi.equals("才") ) {
                if(  (shenCaiz ) > 0 ) {
                    str = str + "有多段感情，或者宠爱情人冷落妻子，或者后面的感情才能长久。";
                }
                else {
                    if ((shenCaiz + shenCaip) > 1 ) {
                        str = str + "虽然有多段情感，但是妻子在婚宫，夫妻感情和睦。";
                    }
                    else {
                        str = str + "妻子在婚宫，夫妻感情和睦。";
                    }
                }
            }
            else if( bazi.shenMonthZhi.equals("才") || 
                    bazi.shenHourGan.equals("才") 
                )
            {
                if ((shenCaiz + shenCaip) > 1 ) {
                    str = str + "有多段情感，与情人关系较好。";
                }
                else {
                    str = str + "与妻子关系良好。";
                }
            }
            else {
                if( (shenCaiz + shenCaip) > 0 ) {
                    str = str + "妻子与日主较远，可表现为夫妻感情淡薄，或者夫妻的家庭不在一地。";
                }
            }
            if( bazi.dayGan.equals("戊") && (bazi.dayZhi.equals("子") ||
                    bazi.hourGan.equals("癸") || bazi.monthGan.equals("癸") ) ) {
                str = str + "丈夫年龄大于妻年龄好几岁。";
            }

            //// 判断婚宫
            if( bazi.shenDayZhi.equals("官") || bazi.shenDayZhi.equals("煞")) {
                str = str + "妻子较强势。";
            }
            else if( bazi.shenDayZhi.equals("印") || bazi.shenDayZhi.equals("枭")) {
                str = str + "妻子对丈夫有帮助。";
            }
            else if( bazi.shenDayZhi.equals("比") || bazi.shenDayZhi.equals("劫")) {
                str = str + "妻子的性格较独立。";
            }
            else if( bazi.shenDayZhi.equals("食") ) {
                str = str + "丈夫对妻子有帮助。";
            }
            else if( bazi.shenDayZhi.equals("伤")) {
                str = str + "妻子对丈夫不利。";
            }
        }
        else {
            if( bazi.shenDayZhi.equals("官") ) {
                if( (shenGuan + shenSha) > 1 ) {
                    str = str + "虽然有多段情感，但是夫妻感情和睦。";
                }
                else {
                    str = str + "夫妻感情和睦。";
                }
            }
            else if( bazi.shenMonthZhi.equals("官") || 
                    bazi.shenHourGan.equals("官") 
                )
            {
                if( (shenGuan + shenSha) > 1 ) {
                    str = str + "虽然有多段情感，但是夫妻感情良好。";
                }
                else {
                    str = str + "夫妻感情良好。";
                }
            }
            else if( bazi.shenDayZhi.equals("煞") ) {
                if(  (shenGuan ) > 0 ) {
                    str = str + "有多段感情，或者宠爱情人冷落丈夫，或者后面的感情才能长久。";
                }
                else {
                    if( (shenGuan + shenSha) > 1 ) {
                        str = str + "虽然有多段情感，但是夫在婚宫，夫妻感情和睦。";
                    }
                    else {
                        str = str + "夫在婚宫，夫妻感情和睦。";
                    }
                }
            }
            else {
                if( (shenCaiz + shenCaip) > 0 ) {
                    str = str + "丈夫与日主较远，可表现为夫妻感情淡薄，或者夫妻的家庭不在一地。";
                }
            }
        
            if( bazi.shenDayZhi.equals("财") || bazi.shenDayZhi.equals("才")) {
                str = str + "妻子较强势。";
            }
            else if( bazi.shenDayZhi.equals("印") || bazi.shenDayZhi.equals("枭")) {
                str = str + "丈夫对妻子有帮助。";
            }
            else if( bazi.shenDayZhi.equals("比") || bazi.shenDayZhi.equals("劫")) {
                str = str + "夫妻之间互相较独立。";
            }
            else if( bazi.shenDayZhi.equals("食") || bazi.shenDayZhi.equals("伤") ) {
                str = str + "妻子对丈夫有帮助。";
            }
        
        }
        return str;
    }

    public String fnSelfShiShen() {
        String str = "";

        if( (shenBi + shenJie) > 1 ) {
            str = str + "性格具有强硬一面，容易被亲友所累。";
        }

        if( (shenYin + shenXiao) > 1 ) {
            str = str + "兴趣广泛，易受宠爱，子女缘薄。";
        }

        if( (shenShang + shenShi) > 1 ) {
            str = str + "性格外向，表达能力好。";
        }     
        
        if( (shenCaip + shenCaiz) > 1 ) {
            str = str + "赚钱途径多变化。";
        }

        if( bazi.sex.equals("男")) {
            if( (shenGuan + shenSha) > 1 ) {
                str = str + "有机会吃财政饭，身弱则工作辛苦，压力大。可以有多个儿女。";
            }
            if( shenShang > 0 && (shenYin + shenXiao) == 0 ) {
                str = str + "易有不好的官司。";
            }
            if( (shenCaip + shenCaiz) > 1 ) {
                str = str + "多次婚姻或者多个女友的可能性大。";
            }
            if( (shenCaip + shenCaiz) == 0 ) {
                str = str + "如果大运也无财，则不易成婚，除非在地址藏干或大运中有财。";
            }
        }
        else {
            if( (shenGuan + shenSha) > 1 ) {
                str = str + "多次婚姻或者多个男友的可能性大。";
            }
            if( (shenGuan + shenSha) == 0 ) {
                str = str + "不易成婚，除非在地址藏干或大运中有官煞。";
            }
            if( shenShang > 0 && (shenYin + shenXiao) == 0) {
                str = str + "婚姻不易和睦。";
            }
            if( (shenShang + shenShi) > 1 ) {
                str = str + "易有多个儿女。";
            }
        }

        return str;
    }

    public String fnSelfGanHeChong(boolean key) {
        String str = "";

        //#region 天干五合
        if( bazi.dayGan.equals("甲") && ( bazi.hourGan.equals("己") || bazi.monthGan.equals("己") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }
        if( bazi.dayGan.equals("己") && ( bazi.hourGan.equals("甲") || bazi.monthGan.equals("甲") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }
        if( ( bazi.monthGan.equals("甲") && bazi.yearGan.equals("己") ) || 
            ( bazi.monthGan.equals("己") && bazi.yearGan.equals("甲") ) ) {
                if( feTu > 0 ) {
                    str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
                }
                else {    
                    str = str + (key == true ? "甲己合土未成，" : "");
                }
            }

        if( bazi.dayGan.equals("乙") && ( bazi.hourGan.equals("庚") || bazi.monthGan.equals("庚") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        }
        if( bazi.dayGan.equals("庚") && ( bazi.hourGan.equals("乙") || bazi.monthGan.equals("乙") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        }
        if( ( bazi.monthGan.equals("乙") && bazi.yearGan.equals("庚") ) || 
            ( bazi.monthGan.equals("庚") && bazi.yearGan.equals("乙") ) ) {
                if( feJin > 0 ) {
                    str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
                }
                else {                    
                    str = str + (key == true ? "乙庚合金未成。" : "");
                }
            }

        if( bazi.dayGan.equals("丙") && ( bazi.hourGan.equals("辛") || bazi.monthGan.equals("辛") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }
        if( bazi.dayGan.equals("辛") && ( bazi.hourGan.equals("丙") || bazi.monthGan.equals("丙") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }
        if( ( bazi.monthGan.equals("辛") && bazi.yearGan.equals("丙") ) || 
            ( bazi.monthGan.equals("丙") && bazi.yearGan.equals("辛") ) ) {
                if( feShui > 0 ) {
                    str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
                }
                else {                    
                    str = str + (key == true ? "丙辛合水未成。" : "");
                }
            }

        if( bazi.dayGan.equals("丁") && ( bazi.hourGan.equals("壬") || bazi.monthGan.equals("壬") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }
        if( bazi.dayGan.equals("壬") && ( bazi.hourGan.equals("丁") || bazi.monthGan.equals("丁") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }
        if( ( bazi.monthGan.equals("壬") && bazi.yearGan.equals("丁") ) || 
            ( bazi.monthGan.equals("丁") && bazi.yearGan.equals("壬") ) ) {
                if( feMu > 0 ) {
                    str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
                }
                else {                    
                    str = str + (key == true ? "丁壬合木未成，" : "");
                }
            }

        if( bazi.dayGan.equals("戊") && ( bazi.hourGan.equals("癸") || bazi.monthGan.equals("癸") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        if( bazi.dayGan.equals("癸") && ( bazi.hourGan.equals("戊") || bazi.monthGan.equals("戊") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        if( ( bazi.monthGan.equals("戊") && bazi.yearGan.equals("癸") ) || 
            ( bazi.monthGan.equals("癸") && bazi.yearGan.equals("戊") ) ) {
                if( feHuo > 0 ) {
                    str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
                }
                else {                    
                    str = str + (key == true ? "戊癸合火未成，" : "");
                }
        }
        //#endregion

        //#region 天干四冲
        if( bazi.hourGan.equals("甲") && ( bazi.dayGan.equals("庚") || bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }
        else if( bazi.dayGan.equals("甲") && ( bazi.hourGan.equals("庚") || bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }
        else if( bazi.monthGan.equals("甲") && ( bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }
        else if( bazi.yearGan.equals("甲") && ( bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || bazi.monthGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }

        if( bazi.hourGan.equals("乙") && ( bazi.dayGan.equals("辛") || bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }
        else if( bazi.dayGan.equals("乙") && ( bazi.hourGan.equals("辛") || bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }
        else if( bazi.monthGan.equals("乙") && ( bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }
        else if( bazi.yearGan.equals("乙") && ( bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || bazi.monthGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }

        if( bazi.hourGan.equals("丙") && ( bazi.dayGan.equals("壬") || bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }
        else if( bazi.dayGan.equals("丙") && ( bazi.hourGan.equals("壬") || bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }
        else if( bazi.monthGan.equals("丙") && ( bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }
        else if( bazi.yearGan.equals("丙") && ( bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || bazi.monthGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }

        if( bazi.hourGan.equals("丁") && ( bazi.dayGan.equals("癸") || bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        else if( bazi.dayGan.equals("丁") && ( bazi.hourGan.equals("癸") || bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        else if( bazi.monthGan.equals("丁") && ( bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        else if( bazi.yearGan.equals("丁") && ( bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || bazi.monthGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        //#endregion

        return str;        
    }

    public String fnSelfZhiHeChong(boolean key) {
        String str = "";

        //#region 地支六合
        if( bazi.hourZhi.equals("子") && ( bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("子") && ( bazi.hourZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("子") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("子") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }

        if( bazi.hourZhi.equals("寅") && ( bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("寅") && ( bazi.hourZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("寅") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("寅") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }

        if( bazi.hourZhi.equals("卯") && ( bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("卯") && ( bazi.hourZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("卯") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("卯") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }

        if( bazi.hourZhi.equals("辰") && ( bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("辰") && ( bazi.hourZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("辰") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("辰") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }

        if( bazi.hourZhi.equals("巳") && ( bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("巳") && ( bazi.hourZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("巳") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("巳") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }

        
        if( bazi.hourZhi.equals("午") && ( bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        else if( bazi.dayZhi.equals("午") && ( bazi.hourZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        else if( bazi.monthZhi.equals("午") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        else if( bazi.yearZhi.equals("午") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        
        //#endregion

        //#region 地支三会局
        if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "寅卯辰三会木。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅卯辰三会木未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
            if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "巳午未三会火。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳午未三会火未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
            if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "申酉戌三会金。" : "");
                    }
                    else {
                        str = str + (key == true ? "申酉戌三会金未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
            if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "亥子丑三会水。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥子丑三会水未成。" : "");
                    }
                }
            }
        }

        //#endregion

        //#region 地支三合局
        if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "亥卯未三合木。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥卯未三合木未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
            if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "寅午戌三合火。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅午戌三合火未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
            if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "申子辰三合水。" : "");
                    }
                    else {
                        str = str + (key == true ? "申子辰三合水未成。" : "");
                    }
                }
            }
        }

        if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
            if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "巳酉丑三合金。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳酉丑三合金未成。" : "");
                    }
                }
            }
        }

        //#endregion

        //#region 地支六冲
        if( bazi.hourZhi.equals("子") && ( bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }
        else if( bazi.dayZhi.equals("子") && ( bazi.hourZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }
        else if( bazi.monthZhi.equals("子") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }
        else if( bazi.yearZhi.equals("子") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }

        if( bazi.hourZhi.equals("丑") && ( bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }
        else if( bazi.dayZhi.equals("丑") && ( bazi.hourZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }
        else if( bazi.monthZhi.equals("丑") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }
        else if( bazi.yearZhi.equals("丑") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }

        if( bazi.hourZhi.equals("寅") && ( bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }
        else if( bazi.dayZhi.equals("寅") && ( bazi.hourZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }
        else if( bazi.monthZhi.equals("寅") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }
        else if( bazi.yearZhi.equals("寅") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }

        if( bazi.hourZhi.equals("卯") && ( bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }
        else if( bazi.dayZhi.equals("卯") && ( bazi.hourZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }
        else if( bazi.monthZhi.equals("卯") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }
        else if( bazi.yearZhi.equals("卯") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }

        if( bazi.hourZhi.equals("辰") && ( bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }
        else if( bazi.dayZhi.equals("辰") && ( bazi.hourZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }
        else if( bazi.monthZhi.equals("辰") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }
        else if( bazi.yearZhi.equals("辰") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }

        if( bazi.hourZhi.equals("巳") && ( bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }
        else if( bazi.dayZhi.equals("巳") && ( bazi.hourZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }
        else if( bazi.monthZhi.equals("巳") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }
        else if( bazi.yearZhi.equals("巳") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }

        //#endregion

        //#region 地址六害 
        if( bazi.hourZhi.equals("子") && ( bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }
        else if( bazi.dayZhi.equals("子") && ( bazi.hourZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }
        else if( bazi.monthZhi.equals("子") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }
        else if( bazi.yearZhi.equals("子") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }

        if( bazi.hourZhi.equals("丑") && ( bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.dayZhi.equals("丑") && ( bazi.hourZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.monthZhi.equals("丑") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.yearZhi.equals("丑") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }        

        if( bazi.hourZhi.equals("寅") && ( bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }
        else if( bazi.dayZhi.equals("寅") && ( bazi.hourZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }
        else if( bazi.monthZhi.equals("寅") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }
        else if( bazi.yearZhi.equals("寅") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }

        if( bazi.hourZhi.equals("卯") && ( bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.dayZhi.equals("卯") && ( bazi.hourZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.monthZhi.equals("卯") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        else if( bazi.yearZhi.equals("卯") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }

        if( bazi.hourZhi.equals("申") && ( bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }
        else if( bazi.dayZhi.equals("申") && ( bazi.hourZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }
        else if( bazi.monthZhi.equals("申") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }
        else if( bazi.yearZhi.equals("申") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }

        if( bazi.yearZhi.equals("酉") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        else if( bazi.hourZhi.equals("酉") && ( bazi.yearZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        else if( bazi.monthZhi.equals("酉") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        else if( bazi.dayZhi.equals("酉") && ( bazi.hourZhi.equals("戌") || bazi.yearZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }        
        
        //#endregion

        //#region 地支相刑 
        if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                    str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
                }
            }
        }
        
        if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
            if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
                }
            }
        }

        if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                str = str + (key == true ? "子卯二刑，" : "") + "与上级或者下级不和。";
            }
        }

        if( bazi.hourZhi.equals("辰") && ( bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "辰辰自刑，" : "") + "有始无终，常陷困境。";
        }
        if( bazi.hourZhi.equals("午") && ( bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "午午自刑，" : "") + "有始无终，常陷困境。";
        }
        if( bazi.hourZhi.equals("酉") && ( bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "酉酉自刑，" : "") + "有始无终，常陷困境。";
        }
        if( bazi.hourZhi.equals("亥") && ( bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "亥亥自刑，" : "") + "有始无终，常陷困境。";
        }

        //#endregion

        return str;        
    }

    public List<String> fnSelfBigFate() {
        //// 简单分析大运
        List<String> list = new ArrayList<>();
        String str = "----";
        list.add(str);
        str = "     大运分析     ";
        list.add(str);

        str = bazi.fateYear1 + "年 0" + bazi.age1 + "岁开始 " + bazi.fateGan1 + bazi.fateZhi1 + "大运：" + fnSelfBigFate(1);
        list.add(str);

        str = bazi.fateYear2 + "年 " + bazi.age2 + "岁开始 " + bazi.fateGan2 + bazi.fateZhi2 + "大运：" + fnSelfBigFate(2);
        list.add(str);

        str = bazi.fateYear3 + "年 " + bazi.age3 + "岁开始 " + bazi.fateGan3 + bazi.fateZhi3 + "大运：" + fnSelfBigFate(3);
        list.add(str);

        str = bazi.fateYear4 + "年 " + bazi.age4 + "岁开始 " + bazi.fateGan4 + bazi.fateZhi4 + "大运：" + fnSelfBigFate(4);
        list.add(str);

        str = bazi.fateYear5 + "年 " + bazi.age5 + "岁开始 " + bazi.fateGan5 + bazi.fateZhi5 + "大运：" + fnSelfBigFate(5);
        list.add(str);

        str = bazi.fateYear6 + "年 " + bazi.age6 + "岁开始 " + bazi.fateGan6 + bazi.fateZhi6 + "大运：" + fnSelfBigFate(6);
        list.add(str);

        str = bazi.fateYear7 + "年 " + bazi.age7 + "岁开始 " + bazi.fateGan7 + bazi.fateZhi7 + "大运：" + fnSelfBigFate(7);
        list.add(str);

        str = bazi.fateYear8 + "年 " + bazi.age8 + "岁开始 " + bazi.fateGan8 + bazi.fateZhi8 + "大运：" + fnSelfBigFate(8);
        list.add(str);

        return list;
    }

    public String fnSelfBigFate(Integer idx) {
        //// 算每一个大运的运程
        String str = fnSelfBigFateWangShuai(idx);

        switch (idx) {
            case 1:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan1), fnComputeShishen(bazi.dayGan, bazi.fateZhi1) );
                break;
            case 2:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan2), fnComputeShishen(bazi.dayGan, bazi.fateZhi2) );
                break;
            case 3:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan3), fnComputeShishen(bazi.dayGan, bazi.fateZhi3) );
                break;
            case 4:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan4), fnComputeShishen(bazi.dayGan, bazi.fateZhi4) );
                break;
            case 5:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan5), fnComputeShishen(bazi.dayGan, bazi.fateZhi5) );
                break;
            case 6:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan6), fnComputeShishen(bazi.dayGan, bazi.fateZhi6) );
                break;
            case 7:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan7), fnComputeShishen(bazi.dayGan, bazi.fateZhi7) );
                break;
            case 8:
                str = str + fnSelfBigFateShiShen( fnComputeShishen(bazi.dayGan, bazi.fateGan8), fnComputeShishen(bazi.dayGan, bazi.fateZhi8) );
                break;
            default:
                break;
        }
        
        str = str + fnSelfBigFateHeChong(idx);

        return str;
    }
    public String fnSelfBigFateShiShen(String shenGan, String shenZhi) {
        /// 算每一个大运的运程
        String str =  shenGan + shenZhi + "，";

        if( shenGan.equals("官") || shenGan.equals("煞")) {
            if( shenZhi.equals("官") || shenZhi.equals("煞")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("印") || shenZhi.equals("枭")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("比") || shenZhi.equals("劫")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("食") || shenZhi.equals("伤")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("财") || shenZhi.equals("才")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
        }
        else if( shenGan.equals("印") || shenGan.equals("枭")) {
            if( shenZhi.equals("官") || shenZhi.equals("煞")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("印") || shenZhi.equals("枭")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运不好。";
                }
                else {
                    str = str + "大运好。";
                }
            }
            else if( shenZhi.equals("比") || shenZhi.equals("劫")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运不好。";
                }
                else {
                    str = str + "大运好。";
                }
            }
            else if( shenZhi.equals("食") || shenZhi.equals("伤")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("财") || shenZhi.equals("才")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
        }
        else if( shenGan.equals("比") || shenGan.equals("劫")) {
            if( shenZhi.equals("官") || shenZhi.equals("煞")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("印") || shenZhi.equals("枭")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运不好。";
                }
                else {
                    str = str + "大运好。";
                }
            }
            else if( shenZhi.equals("比") || shenZhi.equals("劫")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运不好。";
                }
                else {
                    str = str + "大运好。";
                }
            }
            else if( shenZhi.equals("食") || shenZhi.equals("伤")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("财") || shenZhi.equals("才")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
        }
        else if( shenGan.equals("食") || shenGan.equals("伤")) {
            if( shenZhi.equals("官") || shenZhi.equals("煞")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("印") || shenZhi.equals("枭")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("比") || shenZhi.equals("劫")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("食") || shenZhi.equals("伤")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("财") || shenZhi.equals("才")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
        }
        else if( shenGan.equals("财") || shenGan.equals("才")) {
            if( shenZhi.equals("官") || shenZhi.equals("煞")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("印") || shenZhi.equals("枭")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("比") || shenZhi.equals("劫")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运一般。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运一般。";
                }
                else {
                    str = str + "大运一般。";
                }
            }
            else if( shenZhi.equals("食") || shenZhi.equals("伤")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
            else if( shenZhi.equals("财") || shenZhi.equals("才")) {
                if( zpKe > zpSheng ) {  /// 身弱
                    str = str + "大运不好。";
                }
                else if( zpKe < zpSheng ) { /// 身强
                    str = str + "大运好。";
                }
                else {
                    str = str + "大运不好。";
                }
            }
        }

        return str;
    }
    public String fnSelfBigFateWangShuai(Integer idx) {
        /// 地支旺衰
        //状态 甲 丙 戊 庚 壬 乙 丁 己 辛 癸
        //长生 亥 寅 寅 巳 申 午 酉 酉 子 卯
        //沐浴 子 卯 卯 午 酉 巳 申 申 亥 寅
        //冠带 丑 辰 辰 未 戌 辰 未 未 戌 丑
        //临官 寅 巳 巳 申 亥 卯 午 午 酉 子
        //帝旺 卯 午 午 酉 子 寅 巳 巳 申 亥
        //衰   辰 未 未 戌 丑 丑 辰 辰 未 戌
        //病   巳 申 申 亥 寅 子 卯 卯 午 酉
        //死   午 酉 酉 子 卯 亥 寅 寅 巳 申
        //墓   未 戌 戌 丑 辰 戌 丑 丑 辰 未
        //绝   申 亥 亥 寅 巳 酉 子 子 卯 午
        //胎   酉 子 子 卯 午 申 亥 亥 寅 巳
        //养   戌 丑 丑 辰 未 未 戌 戌 丑 辰

        String str = "", gan = "", zhi = "";

        String[] wangShuai = new String[] {"长生", "沐浴", "冠带", "临官", "帝旺", "　衰", "　病", "　死", "　墓", "　绝", "　胎", "　养"};
        String jia  = "亥子丑寅卯辰巳午未申酉戌";
        String yi   = "午巳辰卯寅丑子亥戌酉申未";
        String bing = "寅卯辰巳午未申酉戌亥子丑";
        String ding = "酉申未午巳辰卯寅丑子亥戌";
        String wu   = "寅卯辰巳午未申酉戌亥子丑";
        String ji   = "酉申未午巳辰卯寅丑子亥戌";
        String geng = "巳午未申酉戌亥子丑寅卯辰";
        String xin  = "子亥戌酉申未午巳辰卯寅丑";
        String ren  = "申酉戌亥子丑寅卯辰巳午未";
        String gui  = "卯寅丑子亥戌酉申未午巳辰";

        gan = bazi.dayGan;
        switch (idx) {
            case 1:
                zhi = bazi.fateZhi1;
                break;
            case 2:
                zhi = bazi.fateZhi2;
                break;
            case 3:
                zhi = bazi.fateZhi3;
                break;
            case 4:
                zhi = bazi.fateZhi4;
                break;
            case 5:
                zhi = bazi.fateZhi5;
                break;
            case 6:
                zhi = bazi.fateZhi6;
                break;
            case 7:
                zhi = bazi.fateZhi7;
                break;
            case 8:
                zhi = bazi.fateZhi8;
                break;
            default:
                break;
        }

        if( gan.equals("甲") ) {
            int idxZhi = jia.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("乙") ) {
            int idxZhi = yi.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("丙") ) {
            int idxZhi = bing.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("丁") ) {
            int idxZhi = ding.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("戊") ) {
            int idxZhi = wu.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("己") ) {
            int idxZhi = ji.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("庚") ) {
            int idxZhi = geng.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("辛") ) {
            int idxZhi = xin.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("壬") ) {
            int idxZhi = ren.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("癸") ) {
            int idxZhi = gui.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        
        return str + "，";
    }
    public String fnSelfBigFateHeChong(Integer idx) {
        String str = "", gan = "", zhi = "";

        gan = bazi.dayGan;
        switch (idx) {
            case 1:
                zhi = bazi.fateZhi1;
                break;
            case 2:
                zhi = bazi.fateZhi2;
                break;
            case 3:
                zhi = bazi.fateZhi3;
                break;
            case 4:
                zhi = bazi.fateZhi4;
                break;
            case 5:
                zhi = bazi.fateZhi5;
                break;
            case 6:
                zhi = bazi.fateZhi6;
                break;
            case 7:
                zhi = bazi.fateZhi7;
                break;
            case 8:
                zhi = bazi.fateZhi8;
                break;
            default:
                break;
        }

        str = fnSelfGanHeChongFate(gan, false);
        str = str + fnSelfZhiHeChongFate(zhi, false);

        return str;
    }

    public String fnSelfGanHeChongFate(String gan, boolean key) { 
        /// 加上大运的天干合冲
        String str = "";

        //#region 天干五合
        if( gan.equals("甲") && ( bazi.hourGan.equals("己") || bazi.dayGan.equals("己") || 
                                            bazi.monthGan.equals("己") || bazi.yearGan.equals("己") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }
        if( gan.equals("己") && ( bazi.hourGan.equals("甲") || bazi.dayGan.equals("甲") || 
                                            bazi.monthGan.equals("甲") || bazi.yearGan.equals("甲") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }

        if( gan.equals("乙") && ( bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || 
                                            bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        }
        if( gan.equals("庚") && ( bazi.hourGan.equals("乙") || bazi.dayGan.equals("乙") || 
                                            bazi.monthGan.equals("乙") || bazi.yearGan.equals("乙") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        } 

        if( gan.equals("丙") && ( bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || 
                                            bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }
        if( gan.equals("辛") && ( bazi.hourGan.equals("丙") || bazi.dayGan.equals("丙") || 
                                            bazi.monthGan.equals("丙") || bazi.yearGan.equals("丙") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }

        if( gan.equals("丁") && ( bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || 
                                            bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }
        if( gan.equals("壬") && ( bazi.hourGan.equals("丁") || bazi.dayGan.equals("丁") || 
                                            bazi.monthGan.equals("丁") || bazi.yearGan.equals("丁") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }

        if( gan.equals("戊") && ( bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || 
                                            bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        if( gan.equals("癸") && ( bazi.hourGan.equals("戊") || bazi.dayGan.equals("戊") || 
                                            bazi.monthGan.equals("戊") || bazi.yearGan.equals("戊") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        //#endregion

        //#region 天干四冲
        if( gan.equals("庚") && ( bazi.hourGan.equals("甲") || bazi.dayGan.equals("甲") || 
                                            bazi.monthGan.equals("甲") || bazi.yearGan.equals("甲") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }
        if( gan.equals("甲") && ( bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || 
                                            bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }

        if( gan.equals("乙") && ( bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || 
                                            bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }
        if( gan.equals("辛") && ( bazi.hourGan.equals("乙") || bazi.dayGan.equals("乙") || 
                                            bazi.monthGan.equals("乙") || bazi.yearGan.equals("乙") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }

        if( gan.equals("丙") && ( bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || 
                                            bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }
        if( gan.equals("壬") && ( bazi.hourGan.equals("丙") || bazi.dayGan.equals("丙") || 
                                            bazi.monthGan.equals("丙") || bazi.yearGan.equals("丙") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        } 

        if( gan.equals("丁") && ( bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || 
                                            bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        if( gan.equals("癸") && ( bazi.hourGan.equals("丁") || bazi.dayGan.equals("丁") || 
                                            bazi.monthGan.equals("丁") || bazi.yearGan.equals("丁") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        //#endregion

        return str;        
    }
    public String fnSelfZhiHeChongFate(String zhi, boolean key) {
        /// 加上大运的地支合冲
        String str = "";

        //#region 地支六合
        if( zhi.equals("子") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                                            bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }
        if( zhi.equals("丑") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                                            bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }

        if( zhi.equals("寅") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                                            bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }
        if( zhi.equals("亥") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                                            bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }

        if( zhi.equals("卯") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                                            bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }
        if( zhi.equals("戌") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                                            bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }

        if( zhi.equals("辰") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                                            bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }
        if( zhi.equals("酉") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                                            bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }

        if( zhi.equals("巳") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                                            bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }
        if( zhi.equals("申") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                                            bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }

        
        if( zhi.equals("午") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                                            bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        if( zhi.equals("未") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                                            bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        
        //#endregion

        //#region 地支三会局
        if( zhi.equals("寅")) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "寅卯辰三会木。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅卯辰三会木未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("卯")) {
            if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "寅卯辰三会木。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅卯辰三会木未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("辰")) {
            if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "寅卯辰三会木。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅卯辰三会木未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("巳")) {
            if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "巳午未三会火。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳午未三会火未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("午")) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "巳午未三会火。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳午未三会火未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("未")) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "巳午未三会火。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳午未三会火未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "申酉戌三会金。" : "");
                    }
                    else {
                        str = str + (key == true ? "申酉戌三会金未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "申酉戌三会金。" : "");
                    }
                    else {
                        str = str + (key == true ? "申酉戌三会金未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "申酉戌三会金。" : "");
                    }
                    else {
                        str = str + (key == true ? "申酉戌三会金未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("亥")) {
            if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "亥子丑三会水。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥子丑三会水未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("子")) {
            if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "亥子丑三会水。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥子丑三会水未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
                if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "亥子丑三会水。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥子丑三会水未成。" : "");
                    }
                }
            }
        }

        //#endregion

        //#region 地支三合局
        if( zhi.equals("亥")) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "亥卯未三合木。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥卯未三合木未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("卯")) {
            if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
                if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "亥卯未三合木。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥卯未三合木未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("未")) {
            if( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) {
                if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                    if( feGanMu > 0) {
                        str = str + (key == true ? "亥卯未三合木。" : "");
                    }
                    else {
                        str = str + (key == true ? "亥卯未三合木未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("寅")) {
            if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "寅午戌三合火。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅午戌三合火未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("午")) {
            if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "寅午戌三合火。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅午戌三合火未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                if( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) {
                    if( feGanHuo > 0) {
                        str = str + (key == true ? "寅午戌三合火。" : "");
                    }
                    else {
                        str = str + (key == true ? "寅午戌三合火未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "申子辰三合水。" : "");
                    }
                    else {
                        str = str + (key == true ? "申子辰三合水未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("子")) {
            if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                if( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "申子辰三合水。" : "");
                    }
                    else {
                        str = str + (key == true ? "申子辰三合水未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("辰")) {
            if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                if( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) {
                    if( feGanShui > 0) {
                        str = str + (key == true ? "申子辰三合水。" : "");
                    }
                    else {
                        str = str + (key == true ? "申子辰三合水未成。" : "");
                    }
                }
            }
        }

        if( zhi.equals("巳")) {
            if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "巳酉丑三合金。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳酉丑三合金未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "巳酉丑三合金。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳酉丑三合金未成。" : "");
                    }
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) {
                    if( feGanJin > 0) {
                        str = str + (key == true ? "巳酉丑三合金。" : "");
                    }
                    else {
                        str = str + (key == true ? "巳酉丑三合金未成。" : "");
                    }
                }
            }
        }

        //#endregion

        //#region 地支六冲
        if( zhi.equals("子") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }
        if( zhi.equals("午") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }

        if( zhi.equals("丑") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }
        if( zhi.equals("未") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }

        if( zhi.equals("寅") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }
        if( zhi.equals("申") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }

        if( zhi.equals("卯") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }
        if( zhi.equals("酉") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }

        if( zhi.equals("辰") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }
        if( zhi.equals("戌") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }

        if( zhi.equals("巳") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }
        if( zhi.equals("亥") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }

        //#endregion

        //#region 地址六害 
        if( zhi.equals("子") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }
        if( zhi.equals("未") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }

        if( zhi.equals("丑") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        if( zhi.equals("午") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }

        if( zhi.equals("寅") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }
        if( zhi.equals("巳") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }

        if( zhi.equals("卯") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        if( zhi.equals("辰") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }

        if( zhi.equals("申") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                str = str + (key == true ? "申亥相害，" : "");
            }
        if( zhi.equals("亥") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }

        if( zhi.equals("酉") && ( bazi.yearZhi.equals("戌") || bazi.hourZhi.equals("戌") || 
                bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        if( zhi.equals("戌") && ( bazi.yearZhi.equals("酉") || bazi.hourZhi.equals("酉") || 
                bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        
        //#endregion

        //#region 地支相刑 
        if( zhi.equals("寅")) {
            if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                    str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
                }
            }
        }
        else if( zhi.equals("巳")) {
            if( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) {
                if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                    str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
                }
            }
        }
        else if( zhi.equals("申")) {
            if( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) {
                if( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) {
                    str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
                }
            }
        }

        if(zhi.equals("未")) {
            if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                if( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) {
                    str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) {
                if( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) {
                    str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
                }
            }
        }

        if( zhi.equals("卯") && (bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子卯二刑，" : "") + "与上级或者下级不和。";
        }
        else if( zhi.equals("子") && (bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "子卯二刑，" : "") + "与上级或者下级不和。";
        }

        if( zhi.equals("辰") && (bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "辰辰自刑，" : "") + "有始无终，常陷困境。";
        }

        if( zhi.equals("午") && (bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "午午自刑，" : "") + "有始无终，常陷困境。";
        }

        if( zhi.equals("酉") && ( bazi.hourZhi.equals("酉")  || bazi.dayZhi.equals("酉") || 
                bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "酉酉自刑，" : "") + "有始无终，常陷困境。";
        }
        if( zhi.equals("亥") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "亥亥自刑，" : "") + "有始无终，常陷困境。";
        }

        //#endregion

        return str;        
    }

    public String fnSelfGanHeChongLiu(String yunGan, String gan, boolean key) { 
        /// 加上大运，流年的天干合冲
        String str = "";

        //#region 天干五合
        if( gan.equals("甲") && ( yunGan.equals("己") || bazi.hourGan.equals("己") || bazi.dayGan.equals("己") || 
                bazi.monthGan.equals("己") || bazi.yearGan.equals("己") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }
        if( gan.equals("己") && ( yunGan.equals("甲") || bazi.hourGan.equals("甲") || bazi.dayGan.equals("甲") || 
                bazi.monthGan.equals("甲") || bazi.yearGan.equals("甲") ) ) {
            if( feTu > 0 ) {
                str = str + (key == true ? "甲己合化土，" : "")  +  "易安分守信。";
            }
            else {    
                str = str + (key == true ? "甲己合土未成，" : "");
            }
        }

        if( gan.equals("乙") && ( yunGan.equals("庚") || bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || 
                bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        }
        if( gan.equals("庚") && ( yunGan.equals("乙") || bazi.hourGan.equals("乙") || bazi.dayGan.equals("乙") || 
                bazi.monthGan.equals("乙") || bazi.yearGan.equals("乙") ) ) {
            if( feJin > 0 ) {
                str = str + (key == true ? "乙庚合化金，" : "")  +  "易有仁有义。";
            }
            else {                    
                str = str + (key == true ? "乙庚合金未成。" : "");
            }
        } 

        if( gan.equals("丙") && ( yunGan.equals("辛") || bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || 
                bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }
        if( gan.equals("辛") && ( yunGan.equals("丙") || bazi.hourGan.equals("丙") || bazi.dayGan.equals("丙") || 
                bazi.monthGan.equals("丙") || bazi.yearGan.equals("丙") ) ) {
            if( feShui > 0 ) {
                str = str + (key == true ? "丙辛合化水，" : "")  +  "易聪明贵气。";
            }
            else {                    
                str = str + (key == true ? "丙辛合水未成。" : "");
            }
        }

        if( gan.equals("丁") && ( yunGan.equals("壬") || bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || 
                bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }
        if( gan.equals("壬") && ( yunGan.equals("丁") || bazi.hourGan.equals("丁") || bazi.dayGan.equals("丁") || 
                bazi.monthGan.equals("丁") || bazi.yearGan.equals("丁") ) ) {
            if( feMu > 0 ) {
                str = str + (key == true ? "丁壬合化木，" : "")  +  "虽然积极努力，但后果不确定。";
            }
            else {                    
                str = str + (key == true ? "丁壬合木未成，" : "");
            }
        }

        if( gan.equals("戊") && ( yunGan.equals("癸") || bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || 
                bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        if( gan.equals("癸") && ( yunGan.equals("戊") || bazi.hourGan.equals("戊") || bazi.dayGan.equals("戊") || 
                bazi.monthGan.equals("戊") || bazi.yearGan.equals("戊") ) ) {
            if( feHuo > 0 ) {
                str = str + (key == true ? "戊癸合化火，" : "")  +  "易俊秀薄情。";
            }
            else {                    
                str = str + (key == true ? "戊癸合火未成，" : "");
            }
        }
        //#endregion

        //#region 天干四冲
        if( gan.equals("庚") && ( yunGan.equals("甲") || bazi.hourGan.equals("甲") || bazi.dayGan.equals("甲") || 
                bazi.monthGan.equals("甲") || bazi.yearGan.equals("甲") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }
        if( gan.equals("甲") && ( yunGan.equals("庚") || bazi.hourGan.equals("庚") || bazi.dayGan.equals("庚") || 
                bazi.monthGan.equals("庚") || bazi.yearGan.equals("庚") ) ) {
            str = str + (key == true ? "甲庚相冲。" : "");
        }

        if( gan.equals("乙") && ( yunGan.equals("辛") || bazi.hourGan.equals("辛") || bazi.dayGan.equals("辛") || 
                bazi.monthGan.equals("辛") || bazi.yearGan.equals("辛") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }
        if( gan.equals("辛") && ( yunGan.equals("乙") || bazi.hourGan.equals("乙") || bazi.dayGan.equals("乙") || 
                bazi.monthGan.equals("乙") || bazi.yearGan.equals("乙") ) ) {
            str = str + (key == true ? "乙辛相冲。" : "");
        }

        if( gan.equals("丙") && ( yunGan.equals("壬") || bazi.hourGan.equals("壬") || bazi.dayGan.equals("壬") || 
                bazi.monthGan.equals("壬") || bazi.yearGan.equals("壬") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        }
        if( gan.equals("壬") && ( yunGan.equals("丙") || bazi.hourGan.equals("丙") || bazi.dayGan.equals("丙") || 
                bazi.monthGan.equals("丙") || bazi.yearGan.equals("丙") ) ) {
            str = str + (key == true ? "丙壬相冲。" : "");
        } 

        if( gan.equals("丁") && ( yunGan.equals("癸") || bazi.hourGan.equals("癸") || bazi.dayGan.equals("癸") || 
                bazi.monthGan.equals("癸") || bazi.yearGan.equals("癸") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        if( gan.equals("癸") && ( yunGan.equals("丁") || bazi.hourGan.equals("丁") || bazi.dayGan.equals("丁") || 
                bazi.monthGan.equals("丁") || bazi.yearGan.equals("丁") ) ) {
            str = str + (key == true ? "丁癸相冲。" : "");
        }
        //#endregion

        return str;        
    }
    public String fnSelfZhiHeChongLiu(String yunZhi, String zhi, boolean key) {
        /// 加上大运的地支合冲
        String str = "";

        //#region 地支六合
        if( zhi.equals("子") && ( yunZhi.equals("丑") || bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }
        if( zhi.equals("丑") && ( yunZhi.equals("子") || bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            if( feGanTu > 0) {
                str = str + (key == true ? "子丑克合土。" : "");
            }
            else {
                str = str + (key == true ? "子丑克合土未成。" : "");
            }
        }

        if( zhi.equals("寅") && ( yunZhi.equals("亥") || bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }
        if( zhi.equals("亥") && ( yunZhi.equals("寅") || bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            if( feGanMu > 0) {
                str = str + (key == true ? "寅亥生合木。" : "");
            }
            else {
                str = str + (key == true ? "寅亥生合木未成。" : "");
            }
        }

        if( zhi.equals("卯") && ( yunZhi.equals("戌") || bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }
        if( zhi.equals("戌") && ( yunZhi.equals("卯") || bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            if( feGanHuo > 0) {
                str = str + (key == true ? "卯戌克合火。" : "");
            }
            else {
                str = str + (key == true ? "卯戌克合火未成。" : "");
            }
        }

        if( zhi.equals("辰") && ( yunZhi.equals("酉") || bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }
        if( zhi.equals("酉") && ( yunZhi.equals("辰") || bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            if( feGanJin > 0) {
                str = str + (key == true ? "辰酉生合金。" : "");
            }
            else {
                str = str + (key == true ? "辰酉生合金未成。" : "");
            }
        }

        if( zhi.equals("巳") && ( yunZhi.equals("申") || bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }
        if( zhi.equals("申") && ( yunZhi.equals("巳") || bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            if( feGanShui > 0) {
                str = str + (key == true ? "巳申克合水。" : "");
            }
            else {
                str = str + (key == true ? "巳申克合水未成。" : "");
            }
        }

        
        if( zhi.equals("午") && ( yunZhi.equals("未") || bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        if( zhi.equals("未") && ( yunZhi.equals("午") || bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            if( feGanTu > feGanHuo && feGanTu > 0) {
                str = str + (key == true ? "午未生合土。" : "");
            }
            else if( feGanTu < feGanHuo && feGanHuo > 0) {
                str = str + (key == true ? "午未生合火。" : "");
                str = str + "午未生合火，增加火的力量。";
            }
            else {
                str = str + (key == true ? "午未生合火土未成。" : "");
            }
        }
        
        //#endregion

        //#region 地支三会局
        if( zhi.equals("寅")) {
            if( yunZhi.equals("卯") && (bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }
            }
            if( (yunZhi.equals("未") && bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                    bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }
            }
        }
        else if( zhi.equals("卯")) {
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }
            }
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }
            }
        }
        else if( zhi.equals("未")) {
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                    bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }                
            }
            if( yunZhi.equals("卯") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "寅卯辰三会木。" : "");
                }
                else {
                    str = str + (key == true ? "寅卯辰三会木未成。" : "");
                }                
            }
        }

        if( zhi.equals("巳")) {
            if( yunZhi.equals("午") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                    bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
        }
        else if( zhi.equals("午")) {
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
        }
        else if( zhi.equals("未")) {
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                    bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
            if( yunZhi.equals("午") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "巳午未三会火。" : "");
                }
                else {
                    str = str + (key == true ? "巳午未三会火未成。" : "");
                }
            }
        }

        if( zhi.equals("申")) {
            if( yunZhi.equals("酉") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                    bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                    bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
            if( yunZhi.equals("酉") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "申酉戌三会金。" : "");
                }
                else {
                    str = str + (key == true ? "申酉戌三会金未成。" : "");
                }
            }
        }

        if( zhi.equals("亥")) {
            if( yunZhi.equals("子") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                    bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
        }
        else if( zhi.equals("子")) {
            if( yunZhi.equals("亥") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                    bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( yunZhi.equals("亥") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                    bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
            if( yunZhi.equals("子") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                    bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "亥子丑三会水。" : "");
                }
                else {
                    str = str + (key == true ? "亥子丑三会水未成。" : "");
                }
            }
        }

        //#endregion

        //#region 地支三合局
        if( zhi.equals("亥")) {
            if( yunZhi.equals("卯") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }                
            }
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                    bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }                
            }
        }
        else if( zhi.equals("卯")) {
            if( yunZhi.equals("亥") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }
            }
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                    bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }
            }
        }
        else if( zhi.equals("未")) {
            if( yunZhi.equals("亥") && ( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                    bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }
            }
            if( yunZhi.equals("卯") && ( bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                    bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                if( feGanMu > 0) {
                    str = str + (key == true ? "亥卯未三合木。" : "");
                }
                else {
                    str = str + (key == true ? "亥卯未三合木未成。" : "");
                }
            }
        }

        if( zhi.equals("寅")) {
            if( yunZhi.equals("午") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                    bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
        }
        else if( zhi.equals("午")) {
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                    bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
            if( yunZhi.equals("午") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                if( feGanHuo > 0) {
                    str = str + (key == true ? "寅午戌三合火。" : "");
                }
                else {
                    str = str + (key == true ? "寅午戌三合火未成。" : "");
                }
            }
        }

        if( zhi.equals("申")) {
            if( yunZhi.equals("子") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                    bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
            if( yunZhi.equals("辰") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                    bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
        }
        else if( zhi.equals("子")) {
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                    bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
            if( yunZhi.equals("辰") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
        }
        else if( zhi.equals("辰")) {
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                    bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
            if( yunZhi.equals("子") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                if( feGanShui > 0) {
                    str = str + (key == true ? "申子辰三合水。" : "");
                }
                else {
                    str = str + (key == true ? "申子辰三合水未成。" : "");
                }
            }
        }

        if( zhi.equals("巳")) {
            if( yunZhi.equals("酉") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                    bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                    bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
            if( yunZhi.equals("酉") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                if( feGanJin > 0) {
                    str = str + (key == true ? "巳酉丑三合金。" : "");
                }
                else {
                    str = str + (key == true ? "巳酉丑三合金未成。" : "");
                }
            }
        }

        //#endregion

        //#region 地支六冲
        if( zhi.equals("子") && ( yunZhi.equals("午") || bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }
        if( zhi.equals("午") && ( yunZhi.equals("子") || bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子午相冲，" : "") + "易不安。";
        }

        if( zhi.equals("丑") && ( yunZhi.equals("未") || bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }
        if( zhi.equals("未") && ( yunZhi.equals("丑") || bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            str = str + (key == true ? "丑未相冲，" : "") + "事多阻。";
        }

        if( zhi.equals("寅") && ( yunZhi.equals("申") || bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }
        if( zhi.equals("申") && ( yunZhi.equals("寅") || bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            str = str + (key == true ? "寅申相冲，" : "") + "多情或者管闲事。";
        }

        if( zhi.equals("卯") && ( yunZhi.equals("酉") || bazi.hourZhi.equals("酉") || bazi.dayZhi.equals("酉") || 
                bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }
        if( zhi.equals("酉") && ( yunZhi.equals("卯") || bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "卯酉相冲，" : "") + "多劳碌，易情色纠纷。";
        }

        if( zhi.equals("辰") && ( yunZhi.equals("戌") || bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }
        if( zhi.equals("戌") && ( yunZhi.equals("辰") || bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "辰戌相冲，" : "") + "克亲人，帮他人。";
        }

        if( zhi.equals("巳") && ( yunZhi.equals("亥") || bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }
        if( zhi.equals("亥") && ( yunZhi.equals("巳") || bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "巳亥相冲，" : "") + "易帮他人。";
        }

        //#endregion

        //#region 地址六害 
        if( zhi.equals("子") && ( yunZhi.equals("未") || bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }
        if( zhi.equals("未") && ( yunZhi.equals("子") || bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子未相害，" : "") + "无助于子孙。";
        }

        if( zhi.equals("丑") && ( yunZhi.equals("午") || bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        if( zhi.equals("午") && ( yunZhi.equals("丑") || bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
            str = str + (key == true ? "丑午相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }

        if( zhi.equals("寅") && ( yunZhi.equals("巳") || bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }
        if( zhi.equals("巳") && ( yunZhi.equals("寅") || bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
            str = str + (key == true ? "寅巳相害，" : "") + "多疾病。";
        }

        if( zhi.equals("卯") && ( yunZhi.equals("辰") || bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }
        if( zhi.equals("辰") && ( yunZhi.equals("卯") || bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "卯辰相害，" : "") + "易怒，无耐心，大运逢弱易有伤残。";
        }

        if( zhi.equals("申") && ( yunZhi.equals("亥") || bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
                str = str + (key == true ? "申亥相害，" : "");
            }
        if( zhi.equals("亥") && ( yunZhi.equals("申") || bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
            str = str + (key == true ? "申亥相害，" : "");
        }

        if( zhi.equals("酉") && ( yunZhi.equals("戌") || bazi.yearZhi.equals("戌") || bazi.hourZhi.equals("戌") || 
                bazi.dayZhi.equals("戌") || bazi.monthZhi.equals("戌") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        if( zhi.equals("戌") && ( yunZhi.equals("酉") || bazi.yearZhi.equals("酉") || bazi.hourZhi.equals("酉") || 
                bazi.dayZhi.equals("酉") || bazi.monthZhi.equals("酉") ) ) {
            str = str + (key == true ? "酉戌相害，" : "");
        }
        
        //#endregion

        //#region 地支相刑 
        if( zhi.equals("寅")) {
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
            }
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
            }
        }
        else if( zhi.equals("巳")) {
            if( yunZhi.equals("申") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";                
            }
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("申") || bazi.dayZhi.equals("申") || 
                    bazi.monthZhi.equals("申") || bazi.yearZhi.equals("申") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";                
            }
        }
        else if( zhi.equals("申")) {
            if( yunZhi.equals("寅") && ( bazi.hourZhi.equals("巳") || bazi.dayZhi.equals("巳") || 
                    bazi.monthZhi.equals("巳") || bazi.yearZhi.equals("巳") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
            }
            if( yunZhi.equals("巳") && ( bazi.hourZhi.equals("寅") || bazi.dayZhi.equals("寅") || 
                    bazi.monthZhi.equals("寅") || bazi.yearZhi.equals("寅") ) ) {
                str = str + (key == true ? "寅巳申三刑，" : "") + "易官司或是非，女不易怀孕或易流产。";
            }
        }

        if(zhi.equals("未")) {
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
        }
        else if( zhi.equals("丑")) {
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("戌") || bazi.dayZhi.equals("戌") || 
                    bazi.monthZhi.equals("戌") || bazi.yearZhi.equals("戌") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
            if( yunZhi.equals("戌") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
        }
        else if( zhi.equals("戌")) {
            if( yunZhi.equals("未") && ( bazi.hourZhi.equals("丑") || bazi.dayZhi.equals("丑") || 
                    bazi.monthZhi.equals("丑") || bazi.yearZhi.equals("丑") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
            if( yunZhi.equals("丑") && ( bazi.hourZhi.equals("未") || bazi.dayZhi.equals("未") || 
                    bazi.monthZhi.equals("未") || bazi.yearZhi.equals("未") ) ) {
                str = str + (key == true ? "未丑戌三刑，" : "") + "易挫折，女易孤单。";
            }
        }

        if( zhi.equals("卯") && (yunZhi.equals("子") || bazi.hourZhi.equals("子") || bazi.dayZhi.equals("子") || 
                bazi.monthZhi.equals("子") || bazi.yearZhi.equals("子") ) ) {
            str = str + (key == true ? "子卯二刑，" : "") + "与上级或者下级不和。";
        }
        else if( zhi.equals("子") && (yunZhi.equals("卯") || bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || 
                bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) ) {
            str = str + (key == true ? "子卯二刑，" : "") + "与上级或者下级不和。";
        }

        if( zhi.equals("辰") && (yunZhi.equals("辰") || bazi.hourZhi.equals("辰") || bazi.dayZhi.equals("辰") || 
                bazi.monthZhi.equals("辰") || bazi.yearZhi.equals("辰") ) ) {
            str = str + (key == true ? "辰辰自刑，" : "") + "有始无终，常陷困境。";
        }

        if( zhi.equals("午") && (yunZhi.equals("午") || bazi.hourZhi.equals("午") || bazi.dayZhi.equals("午") || 
                bazi.monthZhi.equals("午") || bazi.yearZhi.equals("午") ) ) {
            str = str + (key == true ? "午午自刑，" : "") + "有始无终，常陷困境。";
        }

        if( zhi.equals("酉") && (yunZhi.equals("酉") || bazi.hourZhi.equals("酉")  || bazi.dayZhi.equals("酉") || 
                bazi.monthZhi.equals("酉") || bazi.yearZhi.equals("酉") ) ) {
            str = str + (key == true ? "酉酉自刑，" : "") + "有始无终，常陷困境。";
        }
        if( zhi.equals("亥") && (yunZhi.equals("亥") || bazi.hourZhi.equals("亥") || bazi.dayZhi.equals("亥") || 
                bazi.monthZhi.equals("亥") || bazi.yearZhi.equals("亥") ) ) {
            str = str + (key == true ? "亥亥自刑，" : "") + "有始无终，常陷困境。";
        }

        //#endregion

        return str;        
    }


    //// 收费版的本命大运流年分析
    public List<String> fnSelfPayDayGan(String dayGan) {
        // 日干分析
        List<String> list = new ArrayList<String>();
        String benMing = fnGz2Wu(bazi.dayGan);
      
        // 日主旺衰，喜忌
        //String str = "本命(日干) " + bazi.dayGan + "-" + benMing + "。";
        String str = "本人为" + bazi.dayGan + "，五行属于" + benMing + "。";
        list.add(str);

        str = "性格";
        if( dayGan.equals("甲")) {
            str = str + "似参天之木，乐于助人，不屈服，不变通。";
            if( bazi.monthZhi.equals("子") || bazi.monthZhi.equals("丑") || bazi.monthZhi.equals("寅") ) {
                str = str + "，生冬季";
                if(feHuo > 0) {
                    str = str + "有火得温，一生多顺。";
                }
                else {
                    str = str + "无火，运中带火时较顺。";
                }
            }
        }
        else if( dayGan.equals("乙")) {
            str = str + "似花草之木，柔和，敏感，无主见。";
            if( feShui > 0 && feTu > 0 && feHuo > 0) {
                    str = str + "有土，有水，有火，靓丽多姿，秀美可爱。";
            }
        }
        else if( dayGan.equals("丙")) {
            str = str + "似猛烈之火，热情，大方，易冲动。";            
        }
        else if( dayGan.equals("丁")) {
            str = str + "似油灯之火，外柔，内刚，多愁疑。";             
        }
        else if( dayGan.equals("戊")) {
            str = str + "似高原之土，稳重，守诺，较保守。";            
        }
        else if( dayGan.equals("己")) {
            str = str + "似田园之土，仁慈，心细，多忧郁。";            
        }
        else if( dayGan.equals("庚")) {
            str = str + "似剑斧之金，爽快，义气，多直率。";            
        }
        else if( dayGan.equals("辛")) {
            str = str + "似首饰之金，温顺，灵秀，多敏感。";            
        }
        else if( dayGan.equals("壬")) {
            str = str + "似江海之水，深沉，随和，多变化。";
        }
        else if( dayGan.equals("癸")) {
            str = str + "似雨露之水，外冷，内热，易悲观。";
        }

        list.add(str);
        return list;
    }

    public String fnSelfPayUpDown() {
        String str = "";

        String zpXiYong = fnComputeXiYong( zpSheng, zpKe);
      
        // 日主旺衰，喜忌
        str   = "生助" + zpSheng + ", 克泄" + zpKe + 
                ", 喜用为" + zpXiYong + ", " + fnWuHelp(zpXiYong) + 
                "如懂风水，在工作和生活的环境，布置" + zpXiYong + "局，" +
                "或者在" + zpXiYong + "位。通过风水的调整，改变命运。";

        return str;
    }

    public String fnSelfPayShiShen(boolean key) {
        String str = "";

        if( (shenBi + shenJie) > 1 ) {
            str = str + (key == true ? "多比劫，": "") + "性格具有强硬一面，容易被亲友所累。";
            if( (shenCaiz + shenCaip) == 1) {
                str = str + "易发生比劫夺食，与朋友合作时多保护自己。";
            }
            if( shenJie > 0 ) {
                str = str + "性格豪爽。";
            }
        }

        if( (shenYin + shenXiao) > 1 ) {
            str = str + (key == true ? "多印枭，" : "" ) + "兴趣广泛，易受宠爱，子女缘薄，适合文职，技术，手艺性质的工作。";
            if( (shenShang + shenShi) > 1 ) {
                str = str + (key == true ? "印克制食伤，" : "" ) + "仕途有保护，同时也限制了财运。";
                if( (shenYin + shenXiao) > (shenShang + shenShi) ) {
                    str = str + "本命适合从官不易做生意，具体情况视大运流年的不同而变化。";
                }
                else {
                    str = str + (key == true ? "食伤大于印，" : "") + "不适合从官。";
                }
            }
            if( (shenCaip + shenCaiz) > 1 ) {
                str = str + (key == true ? "印星被财所克，" : "");
                if( bazi.sex.equals("男")) {
                    str = str + "易婆媳关系不好，";
                }
                if( (shenYin + shenXiao) > (shenCaip + shenCaiz) ) {
                    str = str + (key == true ? "印大于财，" : "") + "有能力而没能赚到更多的钱。";
                }
                else if( (shenYin + shenXiao) > (shenCaip + shenCaiz) ) {
                    str = str + (key == true ? "印小于财，" : "" ) + "能力不够导致赚不到更多的钱。";
                }
                else {
                    str = str + "有机会赚钱。";
                }
            }
            if( (shenGuan + shenSha) > 1 ) {
                str = str + (key == true ? "官升印，印生身，" : "" ) + "适合从事官场工作。";
            }
        }

        if( (shenShang + shenShi) > 1 ) {
            str = str + (key == true ? "多食伤，" : "") + "性格外向，表达能力好。";
            if( zpKe > zpSheng ) {
                str = str + "易身弱，易得伤病。";
            }
        }     
        
        if( (shenCaip + shenCaiz) > 1 ) {
            str = str + (key == true ? "多财，" : "" ) + "赚钱途径多变化。";
            if( zpKe > zpSheng ) {
                str = str + (key == true ? "财多身弱，" : "") + "不易赚到钱，或者赚钱很辛苦，或者赚钱伤身。";
            }
        }

        if( bazi.sex.equals("男")) {
            if( (shenGuan + shenSha) > 1 ) {
                str = str + (key == true ? "多官煞，" : "") + "有机会吃财政饭，工作辛苦，压力大。可以有多个儿女。";
            }
            if( shenShang > 0 && (shenYin + shenXiao) == 0) {
                str = str + (key == true ? "有伤官，" : "") + "易有不好的官家事。";
            }
            if( (shenCaip + shenCaiz) > 1 ) {
                str = str + "多次婚姻或者多个女友的可能性大。";
            }
            if( (shenCaip + shenCaiz) == 0 ) {
                int guan = fnDzcgFindGuanCai(false);
                if( guan == 0 ) {
                    str = str + "如果大运也无财，则不易成婚。";
                }
                else if( guan == 1) {
                    str = str + (key == true ? "地支藏干有财，" : "" ) + "有比较弱的成婚机会。";
                }
                else {
                    str = str + (key == true ? "地支藏干有多财，" : "") + "有多段感情的机会。";
                }
            }
        }
        else {
            if( (shenGuan + shenSha) > 1 ) {
                str = str + (key == true ? "多官，" : "") + "多次婚姻或者多个男友的可能性大。有机会吃财政饭。";
            }
            if( (shenGuan + shenSha) == 0 ) {
                int guan = fnDzcgFindGuanCai(true);
                if( guan == 0 ) {
                    str = str + "如果大运也无官，则不易成婚。";
                }
                else if( guan == 1) {
                    str = str + (key == true ? "地支藏干有官，" : "") + "有比较弱的成婚机会。";
                }
                else {
                    str = str + (key == true ? "地支藏干有多官，" : "") + "有多段感情的机会。";
                }
            }
            if( shenShang > 0 && (shenYin + shenXiao) == 0) {
                str = str + (key == true ? "有伤官，" : "") + "婚姻不易和睦。";
            }
            if( (shenShang + shenShi) > 1 ) {
                str = str + (key == true ? "多食伤，" : "") + "易有多个儿女。";
            }
        
            for( int ii = 0 ; ii < bazi.dzcgDayList.length ; ii++ ) {
                if( bazi.dzcgDayList[ii].ss.equals("伤") ) {
                    str = str + (key == true ? "婚宫藏干有伤官，" : "") + "易分居。";
                    break;
                }
            }
        }

        if( fnComputeTianYi(bazi.dayGan, bazi.yearZhi) ) {
            str = str + "少年有贵人相帮。";
        }
        if( fnComputeTianYi(bazi.dayGan, bazi.monthZhi) ) {
            str = str + "青年有贵人相帮。";
        }
        if( fnComputeTianYi(bazi.dayGan, bazi.dayZhi) ) {
            str = str + "中年有贵人相帮。";
        }
        if( fnComputeTianYi(bazi.dayGan, bazi.hourZhi) ) {
            str = str + "晚年有贵人相帮。";
        }

        return str;
    }

    public int fnDzcgFindGuanCai(boolean guan) {
        int iRtn = 0;
        int ii;
        if( guan == true ) {
            for( ii = 0 ; ii < bazi.dzcgYearList.length ; ii++ ) {
                if( bazi.dzcgYearList[ii].ss.equals("官") || bazi.dzcgYearList[ii].ss.equals("煞")) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgMonthList.length ; ii++ ) {
                if( bazi.dzcgMonthList[ii].ss.equals("官") || bazi.dzcgMonthList[ii].ss.equals("煞") ) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgDayList.length ; ii++ ) {
                if( bazi.dzcgDayList[ii].ss.equals("官") || bazi.dzcgDayList[ii].ss.equals("煞") ) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgHourList.length ; ii++ ) {
                if( bazi.dzcgHourList[ii].ss.equals("官") || bazi.dzcgHourList[ii].ss.equals("煞") ) {
                    iRtn++;
                }
            }
        }
        else {
            for( ii = 0 ; ii < bazi.dzcgYearList.length ; ii++ ) {
                if( bazi.dzcgYearList[ii].ss.equals("财") || bazi.dzcgYearList[ii].ss.equals("才")) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgMonthList.length ; ii++ ) {
                if( bazi.dzcgMonthList[ii].ss.equals("财") || bazi.dzcgMonthList[ii].ss.equals("才") ) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgDayList.length ; ii++ ) {
                if( bazi.dzcgDayList[ii].ss.equals("财") || bazi.dzcgDayList[ii].ss.equals("才") ) {
                    iRtn++;
                }
            }
            for( ii = 0 ; ii < bazi.dzcgHourList.length ; ii++ ) {
                if( bazi.dzcgHourList[ii].ss.equals("财") || bazi.dzcgHourList[ii].ss.equals("才") ) {
                    iRtn++;
                }
            }            
        }
        return iRtn;
    }

    public String fnSelfPayBigFateWangShuai(String gan, String zhi) {
        /// 地支旺衰
        //状态 甲 丙 戊 庚 壬 乙 丁 己 辛 癸
        //长生 亥 寅 寅 巳 申 午 酉 酉 子 卯
        //沐浴 子 卯 卯 午 酉 巳 申 申 亥 寅
        //冠带 丑 辰 辰 未 戌 辰 未 未 戌 丑
        //临官 寅 巳 巳 申 亥 卯 午 午 酉 子
        //帝旺 卯 午 午 酉 子 寅 巳 巳 申 亥
        //衰   辰 未 未 戌 丑 丑 辰 辰 未 戌
        //病   巳 申 申 亥 寅 子 卯 卯 午 酉
        //死   午 酉 酉 子 卯 亥 寅 寅 巳 申
        //墓   未 戌 戌 丑 辰 戌 丑 丑 辰 未
        //绝   申 亥 亥 寅 巳 酉 子 子 卯 午
        //胎   酉 子 子 卯 午 申 亥 亥 寅 巳
        //养   戌 丑 丑 辰 未 未 戌 戌 丑 辰

        String str = "";

        String[] wangShuai = new String[] {"长生", "沐浴", "冠带", "临官", "帝旺", "　衰", "　病", "　死", "　墓", "　绝", "　胎", "　养"};
        String jia  = "亥子丑寅卯辰巳午未申酉戌";
        String yi   = "午巳辰卯寅丑子亥戌酉申未";
        String bing = "寅卯辰巳午未申酉戌亥子丑";
        String ding = "酉申未午巳辰卯寅丑子亥戌";
        String wu   = "寅卯辰巳午未申酉戌亥子丑";
        String ji   = "酉申未午巳辰卯寅丑子亥戌";
        String geng = "巳午未申酉戌亥子丑寅卯辰";
        String xin  = "子亥戌酉申未午巳辰卯寅丑";
        String ren  = "申酉戌亥子丑寅卯辰巳午未";
        String gui  = "卯寅丑子亥戌酉申未午巳辰";

        if( gan.equals("甲") ) {
            int idxZhi = jia.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("乙") ) {
            int idxZhi = yi.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("丙") ) {
            int idxZhi = bing.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("丁") ) {
            int idxZhi = ding.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("戊") ) {
            int idxZhi = wu.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("己") ) {
            int idxZhi = ji.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("庚") ) {
            int idxZhi = geng.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("辛") ) {
            int idxZhi = xin.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("壬") ) {
            int idxZhi = ren.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        else if( gan.equals("癸") ) {
            int idxZhi = gui.indexOf(zhi);
            if( idxZhi >= 0 && idxZhi < 12) {
                str = wangShuai[idxZhi];
            }
        }
        
        return str;
    }

    public String fnSelfPayBigFateShiShen(String gan, String zhi, boolean key, int fate) {
        /// 算每一个大运的运程, 把大运合算进来以后，重新计算十神

        String  str = "";

        String yearGan = bazi.yearGan;
        String yearZhi = bazi.yearZhi;
        String monthGan = bazi.monthGan;
        String monthZhi = bazi.monthZhi;
        String dayGan = bazi.dayGan;
        String dayZhi = bazi.dayZhi;
        String hourGan = bazi.hourGan;
        String hourZhi = bazi.hourZhi;
        String yunGan = gan;
        String yunZhi = zhi;

        String shenYearGan = "";
        String shenYearZhi = "";
        String shenMonthGan = "";
        String shenMonthZhi = "";
        String shenDayZhi = "";
        String shenHourGan = "";
        String shenHourZhi = "";
        String shenYunGan = "";
        String shenYunZhi = "";

        Integer guan = 0;
        Integer sha = 0;
        Integer yin = 0;
        Integer xiao = 0;
        Integer bi = 0;
        Integer jie = 0;
        Integer shi = 0;
        Integer shang = 0;
        Integer caiz = 0;
        Integer caip = 0;
        Integer sheng = 1;
        Integer ke = 0;

        /// 根据天干五合，重新计算天干的五行
        //#region 天干五合
        if( gan.equals("甲") && ( hourGan.equals("己") || dayGan.equals("己") || 
                                            monthGan.equals("己") || yearGan.equals("己") ) ) {
            if( feTu > 0 ) {
                yunGan = "己";
                if( key == true ) str = str + "天干由甲己合化土，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
            }
        }
        if( gan.equals("己") ) {
            if( hourGan.equals("甲") ) {
                if( feTu > 0 ) {
                    hourGan = "己";
                }
            }
            else if( monthGan.equals("甲") ) {
                if( feTu > 0 ) {
                    monthGan = "己";
                }
            }
            else if( yearGan.equals("甲") ) {
                if( feTu > 0 ) {
                    yearGan = "己";
                }
            }
        }

        if( gan.equals("乙") && ( hourGan.equals("庚") || dayGan.equals("庚") || 
                                            monthGan.equals("庚") || yearGan.equals("庚") ) ) {
            if( feJin > 0 ) {
                yunGan = "庚";
                if( key == true ) str = str + "天干由乙庚合化金，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
            }
        }
        if( gan.equals("庚") ) {
            if( hourGan.equals("乙") ) {
                if( feJin > 0 ) {
                    hourGan = "庚";
                }
            }
            else if( monthGan.equals("乙") ) {
                if( feJin > 0 ) {
                    monthGan = "庚";
                }
            }
            else if( yearGan.equals("乙") ) {
                if( feJin > 0 ) {
                    yearGan = "庚";
                }
            }
        } 

        if( gan.equals("丙") ) {
            if( hourGan.equals("辛") ) {
                if( feShui > 0 ) {
                    hourGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("辛") ) {
                if( feShui > 0 ) {
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("辛") ) {
                if( feShui > 0 ) {
                    monthGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("辛") ) {
                if( feShui > 0 ) {
                    yearGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }
        if( gan.equals("辛") ) {
            if( hourGan.equals("丙") ) {
                if( feShui > 0 ) {
                    hourGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("丙") ) {
                if( feShui > 0 ) {
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("丙") ) {
                if( feShui > 0 ) {
                    monthGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("丙") ) {
                if( feShui > 0 ) {
                    yearGan = "壬";
                    yunGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }

        if( gan.equals("丁") ) {
            if( hourGan.equals("壬") ) {
                if( feMu > 0 ) {
                    hourGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("壬") ) {
                if( feMu > 0 ) {
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("壬") ) {
                if( feMu > 0 ) {
                    monthGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("壬") ) {
                if( feMu > 0 ) {
                    yearGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }
        if( gan.equals("壬") ) {
            if( hourGan.equals("丁") ) {
                if( feMu > 0 ) {
                    hourGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("丁") ) {
                if( feMu > 0 ) {
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("丁") ) {
                if( feMu > 0 ) {
                    monthGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("丁") ) {
                if( feMu > 0 ) {
                    yearGan = "乙";
                    yunGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }

        if( gan.equals("戊") ) {
            if( hourGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    hourGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    monthGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    yearGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }
        if( gan.equals("癸") ) {
            if( hourGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    hourGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( monthGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    monthGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    yearGan = "丁";
                    yunGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
        }
        //#endregion

        //#region 地支六合
        if( zhi.equals("子") && ( hourZhi.equals("丑") || dayZhi.equals("丑") || 
                                            monthZhi.equals("丑") || yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                yunZhi = "丑";
                if( key == true ) str = str + "地支由子丑合化土，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
            }
        }
        if( zhi.equals("丑") ) {
            if( hourZhi.equals("子") ) {
                if( feGanTu > 0) {
                    hourZhi = "丑";
                    yunZhi = "丑";
                }
            }
            else if( dayZhi.equals("子") ) {
                if( feGanTu > 0) {
                    dayZhi = "丑";
                    yunZhi = "丑";
                }
            }
            else if( monthZhi.equals("子") ) {
                if( feGanTu > 0) {
                    monthZhi = "丑";
                    yunZhi = "丑";
                }
            }
            else if( yearZhi.equals("子") ) {
                if( feGanTu > 0) {
                    yearZhi = "丑";
                    yunZhi = "丑";
                }
            }
        }

        if( zhi.equals("亥") && ( hourZhi.equals("寅") || dayZhi.equals("寅") || 
                                            monthZhi.equals("寅") || yearZhi.equals("寅") ) ) {
            if( feGanMu > 0) {
                yunZhi = "寅";
                if( key == true ) str = str + "地支由亥寅合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
            }
        }
        if( zhi.equals("寅") ) {
            if( hourZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    hourZhi = "寅";
                }    
            }
            else if( dayZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    dayZhi = "寅";
                }
            }
            else if( monthZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    monthZhi = "寅";
                }
            }
            else if( yearZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    yearZhi = "寅";
                }
            }
        }

        if( zhi.equals("卯") ) {
            if ( hourZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    hourZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    dayZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
            else if( monthZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    monthZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    yearZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
        }
        if( zhi.equals("戌") ) {
            if ( hourZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    hourZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    dayZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
            else if( monthZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    monthZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    yunZhi = "巳";
                    yearZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
        }

        if( zhi.equals("辰") && ( hourZhi.equals("酉") || dayZhi.equals("酉") || 
                                            monthZhi.equals("酉") || yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                yunZhi = "酉";
                if( key == true ) str = str + "地支由辰酉合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
            }
        }
        if( zhi.equals("酉") ) {
            if( hourZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    hourZhi = "酉";
                }
            }
            else if( dayZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    dayZhi = "酉";
                }
            } 
            else if( monthZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    monthZhi = "酉";
                }
            }
            else if( yearZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    yearZhi = "酉";
                }
            }   
        }

        if( zhi.equals("巳") ) {
            if( hourZhi.equals("申") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    hourZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( dayZhi.equals("申") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    dayZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( monthZhi.equals("申") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    monthZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( yearZhi.equals("申") )  {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    yearZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            }
        }
        if( zhi.equals("申") ) {
            if( hourZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    hourZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( dayZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    dayZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( monthZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    monthZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            } 
            else if( yearZhi.equals("巳") )  {
                if( feGanShui > 0) {
                    yunZhi = "癸";
                    yearZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }    
            }
        }

        /// 午未合土火，他们本来就是土火
        if( zhi.equals("未") ) {
            if( hourZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    hourZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    hourZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    dayZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    dayZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }  
            else if( monthZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    monthZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    monthZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    yearZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    yearZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
            }
        }
        if( zhi.equals("午") ) {
            if( hourZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    hourZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    hourZhi = "午";
                }
            } 
            else if( dayZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    dayZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    dayZhi = "午";
                }
            }  
            else if( monthZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    monthZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    monthZhi = "午";
                }
            } 
            else if( yearZhi.equals("未") )  {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    yunZhi = "未";
                    yearZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    yearZhi = "午";
                }
            }
        }

        //#endregion

        //#region 地支三会局
        if( zhi.equals("寅")) {
            if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                    }
                }
            }
        }
        else if( zhi.equals("卯")) {
            if( hourZhi.equals("寅") || dayZhi.equals("寅") || monthZhi.equals("寅") || yearZhi.equals("寅") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                    }
                }
            }
        }
        else if( zhi.equals("未")) {
            if( hourZhi.equals("寅") || dayZhi.equals("寅") || monthZhi.equals("寅") || yearZhi.equals("寅") ) {
                if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        if( key == true ) str = str + "地支由寅卯辰会木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("巳")) {
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "巳";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "巳";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "巳";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "巳";
                    }
                }
            }
        }
        else if( zhi.equals("午")) {
            if( hourZhi.equals("巳") || dayZhi.equals("巳") || monthZhi.equals("巳") || yearZhi.equals("巳") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "巳";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "巳";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "巳";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "巳";
                    }
                }
            }
        }
        else if( zhi.equals("未")) {
            if( hourZhi.equals("巳") || dayZhi.equals("巳") || monthZhi.equals("巳") || yearZhi.equals("巳") ) {
                if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "巳";
                        if( key == true ) str = str + "地支由巳午未会火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                    }
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( hourZhi.equals("申") || dayZhi.equals("申") || monthZhi.equals("申") || yearZhi.equals("申") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                    }
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( hourZhi.equals("申") || dayZhi.equals("申") || monthZhi.equals("申") || yearZhi.equals("申") ) {
                if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        if( key == true ) str = str + "地支由辰酉戌会金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("亥")) {
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        hourZhi = "亥";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        dayZhi = "亥";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        monthZhi = "亥";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yearZhi = "亥";
                    }
                }
            }
        }
        else if( zhi.equals("子")) {
            if( hourZhi.equals("亥") || dayZhi.equals("亥") || monthZhi.equals("亥") || yearZhi.equals("亥") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        hourZhi = "亥";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        dayZhi = "亥";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        monthZhi = "亥";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yearZhi = "亥";
                    }
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( hourZhi.equals("亥") || dayZhi.equals("亥") || monthZhi.equals("亥") || yearZhi.equals("亥") ) {
                if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                    if( feGanShui > 0) {
                        yunZhi = "亥";
                        if( key == true ) str = str + "地支由亥子丑会水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }

        //#endregion

        //#region 地支三合局
        if( zhi.equals("寅")) {
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        hourZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        dayZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        monthZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        yearZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("戌")) {
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( hourZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        hourZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        dayZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        monthZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "午";
                        yearZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("午")) {
            if( hourZhi.equals("寅") ) {
                if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        monthZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        yearZhi = "午";
                    }
                }
            } 
            else if( dayZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        monthZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        yearZhi = "午";
                    }
                }
            } 
            else if( monthZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        yearZhi = "午";
                    }
                }
            } 
            else if( yearZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        monthZhi = "午";
                    }
                } 
            }
        }

        if( zhi.equals("亥")) {
            if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        hourZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        dayZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        monthZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        yearZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("未")) {
            if( bazi.hourZhi.equals("卯") || bazi.dayZhi.equals("卯") || bazi.monthZhi.equals("卯") || bazi.yearZhi.equals("卯") ) {
                if( hourZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        hourZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        dayZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        monthZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                        yearZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("卯")) {
            if( hourZhi.equals("亥") ) {
                if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yearZhi = "卯";
                    }
                }
            } 
            else if( dayZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        yearZhi = "卯";
                    }
                }
            } 
            else if( monthZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        yearZhi = "卯";
                    }
                }
            } 
            else if( yearZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        monthZhi = "卯";
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        hourZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        dayZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        monthZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        yearZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("辰")) {
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( hourZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        hourZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        dayZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        monthZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        yunZhi = "子";
                        yearZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("子")) {
            if( hourZhi.equals("申") ) {
                if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        monthZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yearZhi = "子";
                    }
                }
            } 
            else if( dayZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        monthZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        yearZhi = "子";
                    }
                }
            } 
            else if( monthZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        yearZhi = "子";
                    }
                }
            } 
            else if( yearZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        monthZhi = "子";
                    }
                } 
            }
        }

        if( zhi.equals("巳")) {
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        hourZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        dayZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        monthZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        yearZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("丑")) {
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( hourZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        hourZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        dayZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        monthZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                        yearZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，改为" + fnComputeShishen(dayGan, yunZhi) + "运，"; 
                    }
                }
            }
        }
        else if( zhi.equals("酉")) {
            if( hourZhi.equals("巳") ) {
                if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yearZhi = "酉";
                    }
                }
            } 
            else if( dayZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        yearZhi = "酉";
                    }
                }
            } 
            else if( monthZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        yearZhi = "酉";
                    }
                }
            }
            else if( yearZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
            }
        }

        //#endregion

        shenYearGan = fnComputeShishen(dayGan, yearGan);
        shenYearZhi = fnComputeShishen(dayGan, yearZhi);
        shenMonthGan = fnComputeShishen(dayGan, monthGan);
        shenMonthZhi = fnComputeShishen(dayGan, monthZhi);
        shenDayZhi = fnComputeShishen(dayGan, dayZhi);
        shenHourGan = fnComputeShishen(dayGan, hourGan);
        shenHourZhi = fnComputeShishen(dayGan, hourZhi);
        shenYunGan = fnComputeShishen(dayGan, yunGan);
        shenYunZhi = fnComputeShishen(dayGan, yunZhi);

        if( gan.equals(yunGan) ) {
            if( key == true ) str = str + "天干大运为" + shenYunGan + "运，";
        }
        if( zhi.equals(yunZhi) ) {
            if( key == true ) str = str + "地支大运为" + shenYunZhi + "运，";
        }

        //#region 统计十神个数，生克个数
        ///年干
        if( shenYearGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYearGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYearGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYearGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYearGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYearGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYearGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYearGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYearGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYearGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///年支
        if( shenYearZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYearZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYearZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYearZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYearZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYearZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYearZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYearZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYearZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYearZhi.equals("才") ) {
            caip++;
            ke++;
        }
        
        ///月干
        if( shenMonthGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenMonthGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenMonthGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenMonthGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenMonthGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenMonthGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenMonthGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenMonthGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenMonthGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenMonthGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///月支
        if( shenMonthZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenMonthZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenMonthZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenMonthZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenMonthZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenMonthZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenMonthZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenMonthZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenMonthZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenMonthZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///日支
        if( shenDayZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenDayZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenDayZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenDayZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenDayZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenDayZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenDayZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenDayZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenDayZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenDayZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///时干
        if( shenHourGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenHourGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenHourGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenHourGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenHourGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenHourGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenHourGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenHourGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenHourGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenHourGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///时支
        if( shenHourZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenHourZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenHourZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenHourZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenHourZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenHourZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenHourZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenHourZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenHourZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenHourZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///运干
        if( shenYunGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYunGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYunGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYunGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYunGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYunGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYunGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYunGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYunGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYunGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///运支
        if( shenYunZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYunZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYunZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYunZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYunZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYunZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYunZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYunZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYunZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYunZhi.equals("才") ) {
            caip++;
            ke++;
        }

        //#endregion

        /// 克   官主财印食官
        /// 生   印主食财官印 

        if( key == true) str = str + "走" + shenYunGan + shenYunZhi + "运：";
        if( fate == 0 || fate == 1 ) { /// 第一，第二个大运
            if( shenYunGan.equals("官") || shenYunGan.equals("煞")) {
                if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习一般，虽然调皮但是讨人喜欢。";
                    }
                    else { /// 身弱
                        str = str + "学习一般，爱惹事情。";
                    }
                }
                else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，学习好。";
                    }
                    else { /// 身弱
                        str = str + "比较调皮，学习好。";
                    }
                }
                else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，比较要强。";
                    }
                    else { /// 身弱
                        str = str + "爱惹事，比较任性。";
                    }
                }
                else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "比较调皮，易有病。";
                    }
                }
                else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，讨人喜欢，不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "爱惹事。不爱学习。易有病。";
                    }
                }
                if(fate == 1) str = str + "有可能当学生干部。";            
            }
            else if( shenYunGan.equals("印") || shenYunGan.equals("枭")) {
                if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，学习好。";
                    }
                    else { /// 身弱
                        str = str + "比较调皮，学习好。";
                    }
                    if(fate == 1) str = str + "有可能当学生干部。";                
                }
                else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "爱学习。比较要强。";
                    }
                    else { /// 身弱
                        str = str + "学习好，有个性。";
                    }
                }
                else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习好，比较调皮，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习好，比较要强。";
                    }
                }
                else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习好，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习好，有些任性。";
                    }
                }
                else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习有波动，最后往好的方向发展。";
                    }
                    else { /// 身弱
                        str = str + "学习有波动，最后往坏的方向发展。";
                    }
                }
            }
            else if( shenYunGan.equals("比") || shenYunGan.equals("劫")) {
                if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，有些要强。";
                    }
                    else { /// 身弱
                        str = str + "爱惹事，有些任性。";
                    }
                    if(fate == 1) str = str + "有可能当学生干部。";
                }
                else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习好，比较调皮，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习好，比较要强。";
                    }

                }
                else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习一般，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "学习一般，比较要强。";
                    }
                }
                else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习一般，比较要强，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习一般，比较要强，有些任性。";
                    }
                }
                else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "不爱学习，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "不爱学习，比较要强。";
                    }
                }
            }
            else if( shenYunGan.equals("食") || shenYunGan.equals("伤")) {
                if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "比较调皮，易有病。";
                    }
                    if(fate == 1) str = str + "有可能当学生干部。";
                }
                else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习好，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习好，有些任性。";
                    }
                }
                else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习一般，比较要强，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习一般，比较要强，有些任性。";
                    }
                }
                else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习一般，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "学习一般，有些任性，易有病。";
                    }
                }
                else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "不爱学习，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "不爱学习，有些任性，易有病。";
                    }
                }
            }
            else if( shenYunGan.equals("财") || shenYunGan.equals("才")) {
                if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "比较懂事，讨人喜欢，不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "爱惹事。不爱学习。易有病。";
                    }
                    if(fate == 1) str = str + "有可能当学生干部。";
                }
                else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "学习有波动，最后往好的方向发展。";
                    }
                    else { /// 身弱
                        str = str + "学习有波动，最后往坏的方向发展。";
                    }
                }
                else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "不爱学习，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "不爱学习，比较要强。";
                    }
                }
                else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "不爱学习，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "不爱学习，有些任性，易有病。";
                    }
                }
                else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "不爱学习，易有病。";
                    }
                }
            }

            return str;
        }

        if( shenYunGan.equals("官") || shenYunGan.equals("煞")) {
            if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，事业有发展。";
                    if( (bi + jie ) >= 2 ) {
                        str = str + "有同事或者朋友帮忙解决事业问题。";
                    }
                    if( (yin + xiao ) >= 2 ) {
                        str = str + "有长辈或者贵人帮忙解决事业问题。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + (key == true ? "走官官运：" : "") + "大运相对较好，事业有发展。";
                    }
                    else {
                        str = str + (key == true ? " 走官官运：" : "") + "大运相对不好，即使有仕途，但工作压力变大，或者会有官司缠身。";
                    }
                    if( shang > 0 && (yin + xiao) == 0 ) {
                        str = str + (key == true ? "有伤官，" : "") + "有官司缠身的概率较大。";
                    }
                }
                if( !shenYunGan.equals(shenYunZhi) ) {
                    str = str + (key == true ? "官杀混杂，" : "") + "事业易有波折。";
                }

                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + (key == true ? "女性走官运，" : "") + "可能会有婚姻。";                 
                    }
                    else {
                        str = str + "有异性缘。";
                        if( ke > sheng ) {
                            str = str + "易有情色纠纷。";
                        }
                    }
                }
            }
            else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，事业有发展，有贵人相助。在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，事业有发展。学业，技术，手艺等方面可有提高。对财运不好。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，事业发展较好，有同事或者朋友相助。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，事业有发展。财运可能会变好一些。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (yin + xiao) > 0) {
                        if( (caip + caiz) > 0 ) {
                            str = str + (key == true ? " 有印克制食伤，": "") + "大运相对较好，财运官运都有发展。";
                        }
                        else {
                            str = str + (key == true ? " 有印克制食伤，": "") + "大运相对还行，事业有发展。";
                        }
                    }
                    else {
                        str = str + (key == true ? " 无印克制，": "") + "大运相对一般，事业有波动，但是往好的方向发展。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + " 大运相对较好，事业有波动，但是往好的方向发展。";
                    }
                    else {
                        str = str + " 大运相对不好，事业下行，还有可能官司缠身。易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有异性缘，但不易成婚。";
                    }
                    else {
                        str = str + "有异性缘，也容易伤及家庭。";
                    }
                }
            }
            else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，财运官运都有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str+ " 大运相对较好，财运官运都有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，事业有波动，财运有波动，或者往好的方向发展则很辛苦，或者往不好方向发展。天克地冲易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenYunGan.equals("印") || shenYunGan.equals("枭")) {
            if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，事业有发展，有贵人相助。在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，事业有发展。学业，技术，手艺等方面可有提高。对财运不好。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "大运相对较好，财运官运都有发展。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 大运相对不好，在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，各个方面都会顺利。财运不佳。";
                }
            }
            else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "大运相对较好，财运官运都有发展。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 大运相对不好，在学业，技术，手艺等方面可有提高。对财运不好，被亲朋所累。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，有同事朋友相助，各个方面都会顺利。";
                }
            }
            else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0 ) {
                        str = str + " 大运相对较好，在学业，技术，手艺等方面可有提高。财运较好。";
                    }
                    else {
                        str = str + " 大运相对一般，在学业，技术，手艺等方面可有提高。事业会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对一般，学业，技术，手艺等方面可有提高。财运会有发展的机会。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (shi + shang) > 0 ) {
                        str = str + " 大运相对较好，在学业，技术，手艺等方面可有提高。财运较好。";
                    }
                    else {
                        str = str + " 大运相对一般，在学业，技术，手艺等方面可有提高。财运会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对一般，学业，技术，手艺等方面可有提高。或者财运或者事业会有不好的影响。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenYunGan.equals("比") || shenYunGan.equals("劫")) {
            if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，事业发展较好，有同事或者朋友相助。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，事业有发展。财运可能会变好一些。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "大运相对较好，财运官运都有发展。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 大运相对不好，在学业，技术，手艺等方面可有提高。对财运不好，被亲朋所累。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，有同事朋友相助，各个方面都会顺利。";
                }
            }
            else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "大运相对较好，财运官运都有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，对财运不好，被亲朋所累。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对一般，有同事朋友相助，事业相对顺利。财运相对变好一些。";
                }
            }
            else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 大运相对一般，有财运，易被亲朋所累。";
                    }
                    else {
                        str = str + " 大运相对一般，被亲朋所累，事业往不利的方向有点儿波动。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对不好，虽然有同事朋友相助，事业有波动相对向不利方向发展。或者易破财，易得伤病。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，会有财运，小心亲朋夺财。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，会有点儿财运，但要小心努力获取。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenYunGan.equals("食") || shenYunGan.equals("伤")) {
            if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (yin + xiao) > 0) {
                        if( (caip + caiz) > 0 ) {
                            str = str + " 大运相对较好，有印克制食伤，财运官运都有发展。";
                        }
                        else {
                            str = str + " 大运相对一般，有印克制食伤，事业有发展。";
                        }
                    }
                    else {
                        str = str + (key == true ? " 无印克制，": "") + "大运相对一般，事业有波动，但是往好的方向发展。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，事业有波动，但是往好的方向发展。";
                    }
                    else {
                        str = str + " 大运相对不好，事业下行，还有可能官司缠身。易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有异性缘，但不易成婚。";
                    }
                    else {
                        str = str + "有异性缘，也容易伤及家庭。";
                    }
                }
            }
            else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0 ) {
                        str = str + " 大运相对较好，在学业，技术，手艺等方面可有提高。财运较好。";
                    }
                    else {
                        str = str + " 大运相对一般，在学业，技术，手艺等方面可有提高。事业会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对一般，学业，技术，手艺等方面可有提高。财运会有发展的机会。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 大运相对一般，有财运，被亲朋所累。";
                    }
                    else {
                        str = str + " 大运相对一般，被亲朋所累，事业往不利的方向有点儿波动。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对不好，虽有同事朋友相助，事业有波动相对向不利方向发展。或者易破财，易得伤病。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 大运相对一般，有财运，事业上能有发展。";
                    }
                    else {
                        str = str + " 大运相对一般，在事业上有表现得机会，事业能得到发展。易得小伤小病。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，对事业不好。易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，有财运。事业有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，对事业不好。易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenYunGan.equals("财") || shenYunGan.equals("才")) {
            if( shenYunZhi.equals("官") || shenYunZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，财运官运都有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，财运官运都有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，事业有波动，财运有波动，或者往好的方向发展则很辛苦，或者往不好方向发展。天冲地克易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("印") || shenYunZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (shi + shang) > 0 ) {
                        str = str + " 大运相对较好，在学业，技术，手艺等方面可有提高。财运较好。";
                    }
                    else {
                        str = str + " 大运相对一般，在学业，技术，手艺等方面可有提高。财运会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 大运相对一般，学业，技术，手艺等方面可有提高。或者财运或者事业会有不好的影响。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("比") || shenYunZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，会有财运，小心亲朋夺财。";
                }
                else { /// 身弱
                    str = str + " 大运相对一般，会有点儿财运，但要小心努力获取。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("食") || shenYunZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，有财运。事业有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，对事业不好。易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenYunZhi.equals("财") || shenYunZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 大运相对较好，有财运，事业有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "大运相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 大运相对不好，对事业不好。易破财。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        
        return str;
    }

    public String fnSelfPayLiuNianShiShen(String gan, String zhi, String yGan, String yZhi, boolean key, Integer age) {
        /// 算每一个流年的运程, 把大运合流年的合算进来以后，重新计算十神

        String  str = "";

        String yearGan = bazi.yearGan;
        String yearZhi = bazi.yearZhi;
        String monthGan = bazi.monthGan;
        String monthZhi = bazi.monthZhi;
        String dayGan = bazi.dayGan;
        String dayZhi = bazi.dayZhi;
        String hourGan = bazi.hourGan;
        String hourZhi = bazi.hourZhi;
        String yunGan = yGan;
        String yunZhi = yZhi;
        String liuGan = gan;
        String liuZhi = zhi;
        String shenYearGan = "";
        String shenYearZhi = "";
        String shenMonthGan = "";
        String shenMonthZhi = "";
        String shenDayZhi = "";
        String shenHourGan = "";
        String shenHourZhi = "";
        String shenYunGan = "";
        String shenYunZhi = "";
        String shenLiuGan = "";
        String shenLiuZhi = "";

        Integer guan = 0;
        Integer sha = 0;
        Integer yin = 0;
        Integer xiao = 0;
        Integer bi = 0;
        Integer jie = 0;
        Integer shi = 0;
        Integer shang = 0;
        Integer caiz = 0;
        Integer caip = 0;
        Integer sheng = 1;
        Integer ke = 0;

        /// 根据天干五合，重新计算天干的五行

        //#region 天干五合
        if( gan.equals("甲") && ( yGan.equals("己") || hourGan.equals("己") || dayGan.equals("己") || 
                monthGan.equals("己") || yearGan.equals("己") ) ) {
            if( feTu > 0 ) {
                liuGan = "己";
                if( key == true ) str = str + "天干由甲己合化土，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
            }
        }
        if( gan.equals("己") ) {
            if( hourGan.equals("甲") ) {
                if( feTu > 0 ) {
                    hourGan = "己";
                }
            }
            else if( monthGan.equals("甲") ) {
                if( feTu > 0 ) {
                    monthGan = "己";
                }
            }
            else if( yearGan.equals("甲") ) {
                if( feTu > 0 ) {
                    yearGan = "己";
                }
            }
            else if( yGan.equals("甲") ) {
                if( feTu > 0 ) {
                    yunGan = "己";
                }
            }
        }

        if( gan.equals("乙") && (  yGan.equals("庚") || hourGan.equals("庚") || dayGan.equals("庚") || 
                monthGan.equals("庚") || yearGan.equals("庚") ) ) {
            if( feJin > 0 ) {
                liuGan = "庚";
                if( key == true ) str = str + "天干由乙庚合化金，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
            }
        }
        if( gan.equals("庚") ) {
            if( hourGan.equals("乙") ) {
                if( feJin > 0 ) {
                    hourGan = "庚";
                }
            }
            else if( monthGan.equals("乙") ) {
                if( feJin > 0 ) {
                    monthGan = "庚";
                }
            }
            else if( yearGan.equals("乙") ) {
                if( feJin > 0 ) {
                    yearGan = "庚";
                }
            }
            else if( yGan.equals("乙") ) {
                if( feJin > 0 ) {
                    yunGan = "庚";
                }
            }
        } 

        if( gan.equals("丙") ) {
            if( hourGan.equals("辛") ) {
                if( feShui > 0 ) {
                    hourGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( dayGan.equals("辛") ) {
                if( feShui > 0 ) {
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("辛") ) {
                if( feShui > 0 ) {
                    monthGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yearGan.equals("辛") ) {
                if( feShui > 0 ) {
                    yearGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yGan.equals("辛") ) {
                if( feShui > 0 ) {
                    yunGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }
        if( gan.equals("辛") ) {
            if( hourGan.equals("丙") ) {
                if( feShui > 0 ) {
                    hourGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("丙") ) {
                if( feShui > 0 ) {
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("丙") ) {
                if( feShui > 0 ) {
                    monthGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yearGan.equals("丙") ) {
                if( feShui > 0 ) {
                    yearGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yGan.equals("丙") ) {
                if( feShui > 0 ) {
                    yunGan = "壬";
                    liuGan = "壬";
                    if( key == true ) str = str + "天干由丙辛合化水，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }

        if( gan.equals("丁") ) {
            if( hourGan.equals("壬") ) {
                if( feMu > 0 ) {
                    hourGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( dayGan.equals("壬") ) {
                if( feMu > 0 ) {
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("壬") ) {
                if( feMu > 0 ) {
                    monthGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yearGan.equals("壬") ) {
                if( feMu > 0 ) {
                    yearGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, yunGan) + "运，"; 
                }
            }
            else if( yGan.equals("壬") ) {
                if( feMu > 0 ) {
                    yunGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }
        if( gan.equals("壬") ) {
            if( hourGan.equals("丁") ) {
                if( feMu > 0 ) {
                    hourGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( dayGan.equals("丁") ) {
                if( feMu > 0 ) {
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("丁") ) {
                if( feMu > 0 ) {
                    monthGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yearGan.equals("丁") ) {
                if( feMu > 0 ) {
                    yearGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yGan.equals("丁") ) {
                if( feMu > 0 ) {
                    yunGan = "乙";
                    liuGan = "乙";
                    if( key == true ) str = str + "天干由丁壬合化木，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }

        if( gan.equals("戊") ) {
            if( hourGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    hourGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( dayGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    monthGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yearGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    yearGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yGan.equals("癸") ) {
                if( feHuo > 0 ) {
                    yunGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }
        if( gan.equals("癸") ) {
            if( hourGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    hourGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( dayGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( monthGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    monthGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yearGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    yearGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
            else if( yGan.equals("戊") ) {
                if( feHuo > 0 ) {
                    yunGan = "丁";
                    liuGan = "丁";
                    if( key == true ) str = str + "天干由戊癸合化火，流年天干改为" + fnComputeShishen(dayGan, liuGan) + "运，"; 
                }
            }
        }
        //#endregion

        //#region 地支六合
        if( zhi.equals("子") && ( yZhi.equals("丑") || hourZhi.equals("丑") || dayZhi.equals("丑") || 
                                            monthZhi.equals("丑") || yearZhi.equals("丑") ) ) {
            if( feGanTu > 0) {
                liuZhi = "丑";
                if( key == true ) str = str + "地支由子丑合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
            }
        }
        if( zhi.equals("丑") ) {
            if( hourZhi.equals("子") ) {
                if( feGanTu > 0) {
                    hourZhi = "丑";
                    liuZhi = "丑";
                }
            }
            else if( dayZhi.equals("子") ) {
                if( feGanTu > 0) {
                    dayZhi = "丑";
                    liuZhi = "丑";
                }
            }
            else if( monthZhi.equals("子") ) {
                if( feGanTu > 0) {
                    monthZhi = "丑";
                    liuZhi = "丑";
                }
            }
            else if( yearZhi.equals("子") ) {
                if( feGanTu > 0) {
                    yearZhi = "丑";
                    liuZhi = "丑";
                }
            }
            else if( yZhi.equals("子") ) {
                if( feGanTu > 0) {
                    yunZhi = "丑";
                    liuZhi = "丑";
                }
            }
        }

        if( zhi.equals("亥") && ( yZhi.equals("寅") || hourZhi.equals("寅") || dayZhi.equals("寅") || 
                                            monthZhi.equals("寅") || yearZhi.equals("寅") ) ) {
            if( feGanMu > 0) {
                liuZhi = "寅";
                if( key == true ) str = str + "地支由亥寅合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
            }
        }
        if( zhi.equals("寅") ) {
            if( hourZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    hourZhi = "寅";
                }    
            }
            else if( dayZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    dayZhi = "寅";
                }
            }
            else if( monthZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    monthZhi = "寅";
                }
            }
            else if( yearZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    yearZhi = "寅";
                }
            }
            else if( yZhi.equals("亥") ) {
                if( feGanMu > 0) {
                    yunZhi = "寅";
                }
            }
        }

        if( zhi.equals("卯") ) {
            if ( hourZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    hourZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    dayZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( monthZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    monthZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    yearZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( yZhi.equals("戌") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    yunZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
        }
        if( zhi.equals("戌") ) {
            if ( hourZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    hourZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    dayZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( monthZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    monthZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    yearZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( yZhi.equals("卯") ) {
                if( feGanHuo > 0) {
                    liuZhi = "巳";
                    yunZhi = "巳";
                    if( key == true ) str = str + "地支由卯戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
        }

        if( zhi.equals("辰") && ( yZhi.equals("酉") || hourZhi.equals("酉") || dayZhi.equals("酉") || 
                                            monthZhi.equals("酉") || yearZhi.equals("酉") ) ) {
            if( feGanJin > 0) {
                liuZhi = "酉";
                if( key == true ) str = str + "地支由辰酉合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
            }
        }
        if( zhi.equals("酉") ) {
            if( hourZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    hourZhi = "酉";
                }
            }
            else if( dayZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    dayZhi = "酉";
                }
            } 
            else if( monthZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    monthZhi = "酉";
                }
            }
            else if( yearZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    yearZhi = "酉";
                }
            }   
            else if( yZhi.equals("辰") ) {
                if( feGanJin > 0) {
                    yunZhi = "酉";
                }
            }   
        }

        if( zhi.equals("巳") ) {
            if( hourZhi.equals("申") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    hourZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( dayZhi.equals("申") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    dayZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( monthZhi.equals("申") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    monthZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( yearZhi.equals("申") )  {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    yearZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            }
            else if( yZhi.equals("申") )  {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    yunZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            }
        }
        if( zhi.equals("申") ) {
            if( hourZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    hourZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( dayZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    dayZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( monthZhi.equals("巳") ) {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    monthZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            } 
            else if( yearZhi.equals("巳") )  {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    yearZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            }
            else if( yZhi.equals("巳") )  {
                if( feGanShui > 0) {
                    liuZhi = "癸";
                    yunZhi = "癸";
                    if( key == true ) str = str + "地支由巳申合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }    
            }
        }

        /// 午未合土火，他们本来就是土火
        if( zhi.equals("未") ) {
            if( hourZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    hourZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    hourZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( dayZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    dayZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    dayZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }  
            else if( monthZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    monthZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    monthZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            } 
            else if( yearZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    yearZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    yearZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
            else if( yZhi.equals("午") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    yunZhi = "未";
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    yunZhi = "午";
                    if( key == true ) str = str + "地支由午未合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
            }
        }
        if( zhi.equals("午") ) {
            if( hourZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    hourZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    hourZhi = "午";
                }
            } 
            else if( dayZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    dayZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    dayZhi = "午";
                }
            }  
            else if( monthZhi.equals("未") ) {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    monthZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    yunZhi = "午";
                    monthZhi = "午";
                }
            } 
            else if( yearZhi.equals("未") )  {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    yearZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    yearZhi = "午";
                }
            }
            else if( yZhi.equals("未") )  {
                if( feGanTu > feGanHuo && feGanTu > 0) {
                    liuZhi = "未";
                    yunZhi = "未";
                    if( key == true ) str = str + "地支由午未合化土，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                }
                else if( feGanTu < feGanHuo && feGanHuo > 0) {
                    liuZhi = "午";
                    yunZhi = "午";
                }
            }
        }

        //#endregion

        //#region 地支三会局
        if( zhi.equals("寅")) {
            if( yZhi.equals("卯") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                    }
                }
            }
            if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yunZhi = "卯";
                    }
                } 
            }
        }
        else if( zhi.equals("卯")) {
            if( yZhi.equals("寅") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                    }
                }
            }
            if( hourZhi.equals("寅") || dayZhi.equals("寅") || monthZhi.equals("寅") || yearZhi.equals("寅") ) {
                if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yZhi = "卯";
                    }
                } 
            }
        }
        else if( zhi.equals("未")) {
            if( yZhi.equals("寅") || hourZhi.equals("寅") || dayZhi.equals("寅") || monthZhi.equals("寅") || yearZhi.equals("寅") ) {
                if( yZhi.equals("卯") || hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        if( key == true ) str = str + "地支由寅卯辰会木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("巳")) {
            if( yZhi.equals("午") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "巳";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "巳";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "巳";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "巳";
                    }
                }
            }
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( yZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "巳";
                    }
                } 
            }
        }
        else if( zhi.equals("午")) {
            if( yZhi.equals("巳") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "巳";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "巳";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "巳";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "巳";
                    }
                }
            }
            if( hourZhi.equals("巳") || dayZhi.equals("巳") || monthZhi.equals("巳") || yearZhi.equals("巳") ) {
                if( yZhi.equals("未") ) {
                    if( feGanHuo > 0) {
                        yunZhi = "巳";
                    }
                } 
            }
        }
        else if( zhi.equals("未")) {
            if( yZhi.equals("巳") || hourZhi.equals("巳") || dayZhi.equals("巳") || monthZhi.equals("巳") || yearZhi.equals("巳") ) {
                if( yZhi.equals("午") || hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "巳";
                        if( key == true ) str = str + "地支由巳午未会火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( yZhi.equals("酉") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                    }
                }
            }
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( yZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                    }
                } 
            }
        }
        else if( zhi.equals("酉")) {
            if( yZhi.equals("申") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                    }
                }
            }
            if( hourZhi.equals("申") || dayZhi.equals("申") || monthZhi.equals("申") || yearZhi.equals("申") ) {
                if( yZhi.equals("戌") ) {
                    if( feGanJin > 0) {
                        yunZhi = "酉";
                    }
                } 
            }
        }
        else if( zhi.equals("戌")) {
            if( yZhi.equals("申") || hourZhi.equals("申") || dayZhi.equals("申") || monthZhi.equals("申") || yearZhi.equals("申") ) {
                if( yZhi.equals("酉") || hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        if( key == true ) str = str + "地支由辰酉戌会金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
        }

        if( zhi.equals("亥")) {
            if( yZhi.equals("子") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        hourZhi = "亥";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        dayZhi = "亥";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        monthZhi = "亥";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yearZhi = "亥";
                    }
                }
            }
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( yZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yunZhi = "亥";
                    }
                } 
            }
        }
        else if( zhi.equals("子")) {
            if( yZhi.equals("亥") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        hourZhi = "亥";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        dayZhi = "亥";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        monthZhi = "亥";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yearZhi = "亥";
                    }
                }
            }
            if( hourZhi.equals("亥") || dayZhi.equals("亥") || monthZhi.equals("亥") || yearZhi.equals("亥") ) {
                if( yZhi.equals("丑") ) {
                    if( feGanShui > 0) {
                        yunZhi = "亥";
                    }
                } 
            }
        }
        else if( zhi.equals("丑")) {
            if( yZhi.equals("亥") || hourZhi.equals("亥") || dayZhi.equals("亥") || monthZhi.equals("亥") || yearZhi.equals("亥") ) {
                if( yZhi.equals("子") || hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                    if( feGanShui > 0) {
                        liuZhi = "亥";
                        if( key == true ) str = str + "地支由亥子丑会水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
        }

        //#endregion

        //#region 地支三合局
        if( zhi.equals("寅")) {
            if( yZhi.equals("午") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        hourZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        dayZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        monthZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        yearZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( yZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        yunZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("戌")) {
            if( yZhi.equals("午") ) {
                if( hourZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        hourZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        dayZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        monthZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        yearZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("午") || dayZhi.equals("午") || monthZhi.equals("午") || yearZhi.equals("午") ) {
                if( yZhi.equals("寅") ) {
                    if( feGanHuo > 0) {
                        liuZhi = "午";
                        yunZhi = "午";
                        if( key == true ) str = str + "地支由寅午戌合化火，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("午")) {
            if( hourZhi.equals("寅") ) {
                if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        monthZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        yearZhi = "午";
                    }
                }
                else if( yZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        hourZhi = "午";
                        yunZhi = "午";
                    }
                }
            } 
            else if( dayZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        monthZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        yearZhi = "午";
                    }
                }
                else if( yZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        dayZhi = "午";
                        yunZhi = "午";
                    }
                }
            } 
            else if( monthZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( yearZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        yearZhi = "午";
                    }
                }
                else if( yZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        yunZhi = "午";
                    }
                }
            } 
            else if( yearZhi.equals("寅") ) {
                if( hourZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        hourZhi = "午";
                    }
                } 
                else if( dayZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        dayZhi = "午";
                    }
                } 
                else if( monthZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        yearZhi = "午";
                        monthZhi = "午";
                    }
                } 
                else if( yZhi.equals("戌") ) {
                    if( feGanHuo > 0) {
                        monthZhi = "午";
                        yunZhi = "午";
                    }
                }
            }
        }

        if( zhi.equals("亥")) {
            if( yZhi.equals("卯") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        hourZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        dayZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        monthZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        yearZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        yunZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("未")) {
            if( yZhi.equals("卯") ) {
                if( hourZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        hourZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        dayZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        monthZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        yearZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("卯") || dayZhi.equals("卯") || monthZhi.equals("卯") || yearZhi.equals("卯") ) {
                if( yZhi.equals("亥") ) {
                    if( feGanMu > 0) {
                        liuZhi = "卯";
                        yunZhi = "卯";
                        if( key == true ) str = str + "地支由亥卯未合化木，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("卯")) {
            if( hourZhi.equals("亥") ) {
                if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yearZhi = "卯";
                    }
                }
                else if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yunZhi = "卯";
                    }
                }
            } 
            else if( dayZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        monthZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        dayZhi = "卯";
                        yearZhi = "卯";
                    }
                }
                else if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yunZhi = "卯";
                    }
                }
            } 
            else if( monthZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( yearZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        monthZhi = "卯";
                        yearZhi = "卯";
                    }
                }
                else if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yunZhi = "卯";
                    }
                }
            } 
            else if( yearZhi.equals("亥") ) {
                if( hourZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        hourZhi = "卯";
                    }
                } 
                else if( dayZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        dayZhi = "卯";
                    }
                } 
                else if( monthZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        yearZhi = "卯";
                        monthZhi = "卯";
                    }
                }
                else if( yZhi.equals("未") ) {
                    if( feGanMu > 0) {
                        hourZhi = "卯";
                        yunZhi = "卯";
                    }
                }
            }
        }

        if( zhi.equals("申")) {
            if( yZhi.equals("子") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        hourZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        dayZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        monthZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        yearZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( yZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        yunZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("辰")) {
            if( yZhi.equals("子") ) {
                if( hourZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        hourZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        dayZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("申") ) {
                    if( feGanShui > 0) {
                       liuZhi = "子";
                        monthZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        yearZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("子") || dayZhi.equals("子") || monthZhi.equals("子") || yearZhi.equals("子") ) {
                if( yZhi.equals("申") ) {
                    if( feGanShui > 0) {
                        liuZhi = "子";
                        yunZhi = "子";
                        if( key == true ) str = str + "地支由申子辰合化水，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("子")) {
            if( hourZhi.equals("申") ) {
                if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        monthZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yearZhi = "子";
                    }
                }
                else if( yZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yunZhi = "子";
                    }
                }
            } 
            else if( dayZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        monthZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        dayZhi = "子";
                        yearZhi = "子";
                    }
                }
                else if( yZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yunZhi = "子";
                    }
                }
            } 
            else if( monthZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( yearZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        monthZhi = "子";
                        yearZhi = "子";
                    }
                }
                else if( yZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yunZhi = "子";
                    }
                }
            } 
            else if( yearZhi.equals("申") ) {
                if( hourZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        hourZhi = "子";
                    }
                } 
                else if( dayZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        dayZhi = "子";
                    }
                } 
                else if( monthZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        yearZhi = "子";
                        monthZhi = "子";
                    }
                } 
                else if( yZhi.equals("辰") ) {
                    if( feGanShui > 0) {
                        hourZhi = "子";
                        yunZhi = "子";
                    }
                }
            }
        }

        if( zhi.equals("巳")) {
            if( yZhi.equals("酉") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        hourZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        dayZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        monthZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        yearZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( yZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        yunZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("丑")) {
            if( yZhi.equals("酉") ) {
                if( hourZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        hourZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( dayZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        dayZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( monthZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        monthZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
                else if( yearZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        yearZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                }
            }
            if( hourZhi.equals("酉") || dayZhi.equals("酉") || monthZhi.equals("酉") || yearZhi.equals("酉") ) {
                if( yZhi.equals("巳") ) {
                    if( feGanJin > 0) {
                        liuZhi = "酉";
                        yunZhi = "酉";
                        if( key == true ) str = str + "地支由巳酉丑合化金，流年地支改为" + fnComputeShishen(dayGan, liuZhi) + "运，"; 
                    }
                } 
            }
        }
        else if( zhi.equals("酉")) {
            if( hourZhi.equals("巳") ) {
                if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yearZhi = "酉";
                    }
                }
                else if( yZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yunZhi = "酉";
                    }
                }
            } 
            else if( dayZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        dayZhi = "酉";
                        yearZhi = "酉";
                    }
                }
                else if( yZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yunZhi = "酉";
                    }
                }
            } 
            else if( monthZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( yearZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        monthZhi = "酉";
                        yearZhi = "酉";
                    }
                }
                else if( yZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yunZhi = "酉";
                    }
                }
            }
            else if( yearZhi.equals("巳") ) {
                if( hourZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        hourZhi = "酉";
                    }
                } 
                else if( dayZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        dayZhi = "酉";
                    }
                } 
                else if( monthZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        yearZhi = "酉";
                        monthZhi = "酉";
                    }
                } 
                else if( yZhi.equals("丑") ) {
                    if( feGanJin > 0) {
                        hourZhi = "酉";
                        yunZhi = "酉";
                    }
                }
            }
        }

        //#endregion

        shenYearGan = fnComputeShishen(dayGan, yearGan);
        shenYearZhi = fnComputeShishen(dayGan, yearZhi);
        shenMonthGan = fnComputeShishen(dayGan, monthGan);
        shenMonthZhi = fnComputeShishen(dayGan, monthZhi);
        shenDayZhi = fnComputeShishen(dayGan, dayZhi);
        shenHourGan = fnComputeShishen(dayGan, hourGan);
        shenHourZhi = fnComputeShishen(dayGan, hourZhi);
        shenYunGan = fnComputeShishen(dayGan, yunGan);
        shenYunZhi = fnComputeShishen(dayGan, yunZhi);
        shenLiuGan = fnComputeShishen(dayGan, liuGan);
        shenLiuZhi = fnComputeShishen(dayGan, liuZhi);

        if( gan.equals(yunGan) ) {
            if( key == true ) str = str + "天干流年为" + shenYunGan + "运，";
        }
        if( zhi.equals(yunZhi) ) {
            if( key == true ) str = str + "地支流年为" + shenYunZhi + "运，";
        }

        //#region 统计十神个数，生克个数
        ///年干
        if( shenYearGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYearGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYearGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYearGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYearGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYearGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYearGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYearGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYearGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYearGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///年支
        if( shenYearZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYearZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYearZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYearZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYearZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYearZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYearZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYearZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYearZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYearZhi.equals("才") ) {
            caip++;
            ke++;
        }
        
        ///月干
        if( shenMonthGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenMonthGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenMonthGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenMonthGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenMonthGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenMonthGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenMonthGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenMonthGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenMonthGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenMonthGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///月支
        if( shenMonthZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenMonthZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenMonthZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenMonthZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenMonthZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenMonthZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenMonthZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenMonthZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenMonthZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenMonthZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///日支
        if( shenDayZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenDayZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenDayZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenDayZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenDayZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenDayZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenDayZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenDayZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenDayZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenDayZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///时干
        if( shenHourGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenHourGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenHourGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenHourGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenHourGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenHourGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenHourGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenHourGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenHourGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenHourGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///时支
        if( shenHourZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenHourZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenHourZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenHourZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenHourZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenHourZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenHourZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenHourZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenHourZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenHourZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///运干
        if( shenYunGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYunGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYunGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYunGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYunGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYunGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYunGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYunGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYunGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYunGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///运支
        if( shenYunZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenYunZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenYunZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenYunZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenYunZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenYunZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenYunZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenYunZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenYunZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenYunZhi.equals("才") ) {
            caip++;
            ke++;
        }

        ///流年干
        if( shenLiuGan.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenLiuGan.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenLiuGan.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenLiuGan.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenLiuGan.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenLiuGan.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenLiuGan.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenLiuGan.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenLiuGan.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenLiuGan.equals("才") ) {
            caip++;
            ke++;
        }

        ///运支
        if( shenLiuZhi.equals("官") ) {
            guan++;
            ke++;
        }
        if( shenLiuZhi.equals("煞") ) {
            sha++;
            ke++;
        }
        if( shenLiuZhi.equals("印") ) {
            yin++;
            sheng++;
        }
        if( shenLiuZhi.equals("枭") ) {
            xiao++;
            sheng++;            
        }
        if( shenLiuZhi.equals("比") ) {
            bi++;
            sheng++;
        }
        if( shenLiuZhi.equals("劫") ) {
            jie++;
            sheng++;
        }
        if( shenLiuZhi.equals("食") ) {
            shi++;
            ke++;
        }
        if( shenLiuZhi.equals("伤") ) {
            shang++;
            ke++;
        }
        if( shenLiuZhi.equals("财") ) {
            caiz++;
            ke++;
        }
        if( shenLiuZhi.equals("才") ) {
            caip++;
            ke++;
        }

        //#endregion

        /// 克   官主财印食官
        /// 生   印主食财官印 

        if( fnComputeTianYi(bazi.dayGan, gan) ) {
            str = str + "有天乙贵人相帮。";
        }
        else if( fnComputeTianYi(bazi.dayGan, zhi) ) {
            str = str + "有天乙贵人相帮。";
        }

        if( fnComputeYiMa(bazi.dayZhi, zhi) ) {
            str = str + "驿马：事业有变动，或者生活有变迁，或者长期出差在外。";
        }
        else if( fnComputeYiMa(bazi.yearZhi, zhi) ) {
            str = str + "驿马：事业有变动，或者生活有变迁，或者长期出差在外。";
        }

        if( key == true ) str = str + "走" + shenLiuGan + shenLiuZhi + "运：";

        if( age < 23 ) {

            if( shenLiuGan.equals("官") || shenLiuGan.equals("煞")) {
                if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习一般，虽然调皮但是讨人喜欢。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习一般，爱惹事情。";
                    }
                }
                else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，学习好。";
                    }
                    else { /// 身弱
                        str = str + "本年，比较调皮，学习好。";
                    }
                }
                else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，比较要强。";
                    }
                    else { /// 身弱
                        str = str + "本年，爱惹事，比较任性。";
                    }
                }
                else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，比较调皮，易有病。";
                    }
                }
                else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，讨人喜欢，不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "本年，爱惹事。不爱学习。易有病。";
                    }
                }
                str = str + "有可能当学生干部。";
            }
            else if( shenLiuGan.equals("印") || shenLiuGan.equals("枭")) {
                if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，学习好。";
                    }
                    else { /// 身弱
                        str = str + "本年，比较调皮，学习好。";
                    }
                    str = str + "有可能当学生干部。";
                }
                else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，爱学习。比较要强。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习好，有个性。";
                    }
                }
                else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习好，比较调皮，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习好，比较要强。";
                    }
                }
                else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习好，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习好，有些任性。";
                    }
                }
                else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习有波动，最后往好的方向发展。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习有波动，最后往坏的方向发展。";
                    }
                }
            }
            else if( shenLiuGan.equals("比") || shenLiuGan.equals("劫")) {
                if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，有些要强。";
                    }
                    else { /// 身弱
                        str = str + "本年，爱惹事，有些任性。";
                    }
                    str = str + "有可能当学生干部。";
                }
                else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习好，比较调皮，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习好，比较要强。";
                    }

                }
                else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习一般，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习一般，比较要强。";
                    }
                }
                else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习一般，比较要强，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习一般，比较要强，有些任性。";
                    }
                }
                else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，不爱学习，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "本年，不爱学习，比较要强。";
                    }
                }
            }
            else if( shenLiuGan.equals("食") || shenLiuGan.equals("伤")) {
                if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，比较调皮，易有病。";
                    }
                    str = str + "有可能当学生干部。";
                }
                else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习好，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习好，有些任性。";
                    }
                }
                else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习一般，比较要强，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习一般，比较要强，有些任性。";
                    }
                }
                else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习一般，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习一般，有些任性，易有病。";
                    }
                }
                else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，不爱学习，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，不爱学习，有些任性，易有病。";
                    }
                }
            }
            else if( shenLiuGan.equals("财") || shenLiuGan.equals("才")) {
                if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，比较懂事，讨人喜欢，不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "本年，爱惹事。不爱学习。易有病。";
                    }
                    str = str + "有可能当学生干部。";
                }
                else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，学习有波动，最后往好的方向发展。";
                    }
                    else { /// 身弱
                        str = str + "本年，学习有波动，最后往坏的方向发展。";
                    }
                }
                else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，不爱学习，比较任性。";
                    }
                    else { /// 身弱
                        str = str + "本年，不爱学习，比较要强。";
                    }
                }
                else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，不爱学习，有才艺，表现好。";
                    }
                    else { /// 身弱
                        str = str + "本年，不爱学习，有些任性，易有病。";
                    }
                }
                else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                    if( sheng >= ke  ) {  /// 身强
                        str = str + "本年，不爱学习。";
                    }
                    else { /// 身弱
                        str = str + "本年，不爱学习，易有病。";
                    }
                }
            }

            return str;
        }
        if( shenLiuGan.equals("官") || shenLiuGan.equals("煞")) {
            if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年事业会变化，往好的方向发展。";
                    if( (bi + jie ) >= 2 ) {
                        str = str + "有同事或者朋友帮忙解决事业问题。";
                    }
                    if( (yin + xiao ) >= 2 ) {
                        str = str + "有长辈或者贵人帮忙解决事业问题。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年事业会有变化，往好的方向发展。";
                    }
                    else {
                        str = str + " 本年相对不好，事业会有变动，即使事业变好，但工作压力也会变大，或者会有官司缠身。";
                    }
                    if( shang > 0 && (yin + xiao) == 0 ) {
                        str = str + (key == true ? "有伤官，" : "") + "本年相对不好，事业会有变动，有官司缠身的概率较大。";
                    }
                }

                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + (key == true ? "女性走官运，" : "") + "可能会有婚姻。";                 
                    }
                    else {
                        str = str + "有异性缘。";
                        if( ke > sheng ) {
                            str = str + "易有情色纠纷。";
                        }
                    }
                }
            }
            else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，事业有发展，有贵人相助。在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                }
                else { /// 身弱
                    str = str + " 本年相对一般，事业有发展，也比较辛苦。学业，技术，手艺等方面可有提高。对财运不好。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，事业有变化，发展较好，有同事或者朋友相助。";
                }
                else { /// 身弱
                    str = str + " 本年相对一般，即使事业有发展，但也比较辛苦。财运可能会变好一些。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (yin + xiao) > 0) {
                        if( (caip + caiz) > 0 ) {
                            str = str + (key == true ? " 有印克制食伤，": "") + "本年相对较好，事业财运都有变化，财运官运都往好的方向有发展。";
                        }
                        else {
                            str = str + (key == true ? " 有印克制食伤，": "") + "本年相对还行，事业有变化，往好的方向发展。";
                        }
                    }
                    else {
                        str = str + (key == true ? " 无印克制，": "") + "本年相对一般，事业有波动，如果往好的方向发展，就会压力变大。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + " 本年相对较好，事业有波动，但是往好的方向发展，工作压力也会变大。";
                    }
                    else {
                        str = str + " 本年相对不好，事业有变化，如果往好的方向发展就会工作压力变大，或者往不好方向发展，或者有可能官司缠身。易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有异性缘，但不易成婚。";
                    }
                    else {
                        str = str + "有异性缘，也容易伤及家庭。";
                    }
                }
            }
            else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，财运官运都变化，都会往好的方向发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + " 本年相对较好，财运官运都会往好的方向发展。";
                    }
                    else {
                        str = str + " 本年相对不好，事业有波动，财运有波动，或者往好的方向发展则很辛苦，或者往不好方向发展。天克地冲易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenLiuGan.equals("印") || shenLiuGan.equals("枭")) {
            if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，事业有发展，有贵人相助。在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                }
                else { /// 身弱
                    str = str + " 本年相对一般，事业有变化。学业，技术，手艺等方面可有提高。对财运不好。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "本年相对较好，财运官运都有发展。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 本年相对不好，在学业，技术，手艺等方面可有提高。对财运不好。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，各个方面都会顺利。财运不佳。";
                }
            }
            else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "本年相对较好，财运官运都会顺利。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 本年相对不好，在学业，技术，手艺等方面可有提高。对财运不好，被亲朋所累。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，有同事朋友相助，各个方面都会顺利。";
                }
            }
            else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0 ) {
                        str = str + " 本年相对较好，在学业，技术，手艺等方面可有提高。财运较好。事业也可能会有变化。";
                    }
                    else {
                        str = str + " 本年相对一般，在学业，技术，手艺等方面可有提高。事业会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对一般，学业，技术，手艺等方面可有提高。财运会有发展的机会。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (shi + shang) > 0 ) {
                        str = str + " 本年相对较好，在学业，技术，手艺等方面可有提高。财运较好。";
                    }
                    else {
                        str = str + " 本年相对一般，在学业，技术，手艺等方面可有提高。财运会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对一般，学业，技术，手艺等方面可有提高。或者财运或者事业会有不好的影响。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenLiuGan.equals("比") || shenLiuGan.equals("劫")) {
            if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，事业有变化，往好的方向发展较好，有同事或者朋友相助。";
                }
                else { /// 身弱
                    str = str + " 本年相对一般，事业有发展。财运可能会变好一些。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "本年相对较好，财运官运都有发展。在学业，技术，手艺等方面可有提高。";
                    }
                    else {
                        str = str + " 本年相对不好，在学业，技术，手艺等方面可有提高。对财运不好，被亲朋所累。可能会名誉受损。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对较好，学业，技术，手艺等方面可有提高。有贵人相帮，有同事朋友相助，各个方面都会顺利。";
                }
            }
            else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( zpSheng < zpKe ) {
                        str = str + "本年相对较好，财运官运都有发展。";
                    }
                    else {
                        str = str + " 本年相对不好，对财运不好，被亲朋所累。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对一般，有同事朋友相助，事业相对顺利。财运相对变好一些。";
                }
            }
            else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 本年相对一般，有财运变化，，易被亲朋所累。";
                    }
                    else {
                        str = str + " 本年相对一般，被亲朋所累，事业往不利的方向有点儿波动。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对不好，虽然有同事朋友相助，事业有波动相对向不利方向发展。或者易破财，易得伤病。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，会有财运，小心亲朋夺财。";
                }
                else { /// 身弱
                    str = str + " 本年相对一般，会有点儿财运，但要小心努力获取。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenLiuGan.equals("食") || shenLiuGan.equals("伤")) {
            if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (yin + xiao) > 0) {
                        if( (caip + caiz) > 0 ) {
                            str = str + (key == true ? " 有印克制食伤，": "") + "本年相对较好，财运官运都会有变化，都往好的方向发展。";
                        }
                        else {
                            str = str + (key == true ? " 有印克制食伤，": "") + "本年相对一般，事业有变化，虽然可能会有波动，还是往好的方向发展。";
                        }
                    }
                    else {
                        str = str + (key == true ? " 无印克制，": "") + "本年相对一般，事业有波动，但是往好的方向发展。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年相对较好，事业有波动，但是往好的方向发展。";
                    }
                    else {
                        str = str + " 本年相对不好，事业下行，还有可能官司缠身。易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有异性缘，但不易成婚。";
                    }
                    else {
                        str = str + "有异性缘，也容易伤及家庭。";
                    }
                }
            }
            else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0 ) {
                        str = str + " 本年相对较好，在学业，技术，手艺等方面可有提高。财运有变化，往好的方向发展。";
                    }
                    else {
                        str = str + " 本年相对一般，在学业，技术，手艺等方面可有提高。事业会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对一般，学业，技术，手艺等方面可有提高。财运会有发展的机会，需要小心努力获取。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 本年相对一般，会有财运，易被亲朋所累。";
                    }
                    else {
                        str = str + " 本年相对一般，被亲朋所累，事业往不利的方向有点儿波动。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对不好，虽有同事朋友相助，事业有波动相对向不利方向发展。或者易破财，易得伤病。";
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (caip + caiz) > 0) {
                        str = str + " 本年相对一般，有财运，事业上能有发展。";
                    }
                    else {
                        str = str + " 本年相对一般，在事业上有表现得机会，事业能得到发展。易得小伤小病。";
                    }
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 本年相对不好，对事业不好。易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
            }
            else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，有财运。事业有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 本年相对不好，对事业不好。有财运，需要辛苦获得，或者易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        else if( shenLiuGan.equals("财") || shenLiuGan.equals("才")) {
            if( shenLiuZhi.equals("官") || shenLiuZhi.equals("煞")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，财运官运都有变化，都会往好的方向发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str +"本年相对较好，财运官运都有发展。";
                    }
                    else {
                        str = str + " 本年相对不好，事业有波动，财运有波动，或者往好的方向发展则很辛苦，或者往不好方向发展。天冲地克易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) == 0 ) {
                        str = str + "女性走官运，可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("印") || shenLiuZhi.equals("枭")) {
                if( sheng >= ke  ) {  /// 身强
                    if( (shi + shang) > 0 ) {
                        str = str + " 本年相对较好，在学业，技术，手艺等方面可有提高。财运有变化，会往好的方向发展。";
                    }
                    else {
                        str = str + " 本年相对一般，在学业，技术，手艺等方面可有提高。财运会有波动，相对向好的方向发展。";
                    }
                }
                else { /// 身弱
                    str = str + " 本年相对一般，学业，技术，手艺等方面可有提高。或者财运或者事业有变化，或者有发展但很辛苦，或者会有不好的影响。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("比") || shenLiuZhi.equals("劫")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，会有财运，小心亲朋夺财。";
                }
                else { /// 身弱
                    str = str + "本年相对一般，会有点儿财运，但要小心努力获取。";
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("食") || shenLiuZhi.equals("伤")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + " 本年相对较好，有财运和事业有变化，都往好的方向发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + " 本年相对不好，财运和事业都有变化，或者很辛苦的有发展，或者走往不好的方向发展。易破财，易得伤病。";
                    }
                }
                if( bazi.sex.equals("女")) {
                    if( (guan + sha) > 0 ) {
                        str = str + "对家庭或婚姻有不好的影响。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
            else if( shenLiuZhi.equals("财") || shenLiuZhi.equals("才")) {
                if( sheng >= ke  ) {  /// 身强
                    str = str + "本年相对较好，有财运，事业有发展。";
                }
                else { /// 身弱
                    if( zpSheng >= zpKe ) {
                        str = str + "本年相对较好，事业、财运上能有发展。";
                    }
                    else {
                        str = str + "本年相对不好，财运和事业都有变化，或者很辛苦的有发展，或者往不好方向发展。易破财。";
                    }
                }
                if( bazi.sex.equals("男")) {
                    if( (caip + caiz) == 0 ) {
                        str = str + "可能会有婚姻。";
                    }
                    else {
                        str = str + "有异性缘。";
                    }
                }
            }
        }
        
        return str;
    }

    public List<String> fnSelfPayBigFate(boolean key) {
        //// 分析大运
        List<String> list = new ArrayList<>();
        String str = "　";
        list.add(str);
        str = "     大运分析     ";
        list.add(str);
        str = "----";
        list.add(str);

        DayunLiunian dln;
        String yearGan, yearZhi;
        for( int ii = 0 ; ii < bazi.bigyunList.length ; ii++ ) {
            dln = bazi.bigyunList[ii];
            yearGan = fnComputeShishen(bazi.dayGan, dln.tg);
            yearZhi = fnComputeShishen(bazi.dayGan, dln.dz);
            if( key == true ) {
                str = dln.year + "年 " + (dln.age < 10 ? "0" + dln.age: dln.age.toString()) + "岁开始 " + dln.tg + dln.dz + "大运：行" + 
                yearGan + yearZhi + "运， " + fnSelfPayBigFateWangShuai(bazi.dayGan, dln.dz) +"。 ";
            }
            else {
                Integer yearend = dln.year + 9;
                str = dln.year + "~" + yearend + "年 " + fnSelfPayBigFateWangShuai(bazi.dayGan, dln.dz);
            }

            list.add(str);
            str =  fnSelfGanHeChongFate(dln.tg, key) + fnSelfZhiHeChongFate(dln.dz, key);
            list.add(str);
            str = fnSelfPayBigFateShiShen( dln.tg, dln.dz, key, ii);
            list.add(str);
            str = "　";
            list.add(str);
            if( bPay == false ) {
                if( (bazi.bigyunList[0].age + 1 + (ii + 1) * 10 ) >= yearShow ) break;
            }
        }

        return list;
    }

    public List<String> fnSelfPayLiuNian(boolean key) {
        //// 分析流年
        List<String> list = new ArrayList<>();
        String str = "     流年分析     ";
        list.add(str);
        str = "----";
        list.add(str);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        DayunLiunian dln;
        String yearGan, yearZhi;
        for( int ii = 0 ; ii < bazi.bigyunList.length ; ii++ ) {
            dln = bazi.bigyunList[ii];
            for( int jj = 0 ; jj < 10 ; jj++ ) {
                yearGan = dln.liunian.substring(jj * 2, jj * 2 +1);
                yearZhi = dln.liunian.substring(jj * 2 + 1, jj * 2 + 2);
                str = (dln.year + jj) + "年 " + ((dln.age + jj) < 10 ? "0" + (dln.age + jj): (dln.age + jj)) + "岁 " + yearGan + yearZhi + " 流年：";

                if( (dln.year + jj) == year || (dln.age + jj) == (year + 1) ) {

                    list.add(str);

                    str =  fnSelfGanHeChongLiu(dln.tg, yearGan, key) + fnSelfZhiHeChongLiu(dln.dz, yearZhi, key);
                    list.add(str);
        
                    str = fnSelfPayLiuNianShiShen( yearGan, yearZhi, dln.tg, dln.dz, key, dln.age + jj);
                    list.add(str);

                    str = "　";
                    list.add(str);
                    
                    if( bPay == false ) {
                        if( (ii * 10 + jj + bazi.bigyunList[0].age + 1 ) >= yearShow ) {
                            break;
                        }
                    }    
                }
            }  
            if( bPay == false ) {
                if( (bazi.bigyunList[0].age + 1 + (ii + 1) * 10 ) >= yearShow ) break;
            }
        }

        return list;
    }


    //// 其他工具
    public String fnComputeShishen(String daytg, String gz) {
        String rtn = "";
        String tg = "甲乙丙丁戊己庚辛壬癸"; //char of TianGan
        String dz = "子丑寅卯辰巳午未申酉戌亥"; //char of DiZhi
        int dayIdx = tg.indexOf(daytg);
        if( dayIdx == -1 ) return rtn;
        int gzIdx = tg.indexOf(gz);
        
        if( gzIdx >= 0 )   { //// 天干十神
            //    局主天干　甲　　乙 　丙　 丁　 戊　 己　 庚　 辛　 壬　 癸 
            ////  甲 　　　比肩 劫财 偏印 正印 七杀 正官 偏财 正财 食神 伤官 
            ////  乙 　　　劫财 比肩 正印 偏印 正官 七杀 正财 偏财 伤官 食神 
            ////  丙 　　　食神 伤官 比肩 劫财 偏印 正印 七杀 正官 偏财 正财 
            ////  丁 　　　伤官 食神 劫财 比肩 正印 偏印 正官 七杀 正财 偏财 
            ////  戊 　　　偏财 正财 食神 伤官 比肩 劫财 偏印 正印 七杀 正官 
            ////  己 　　　正财 偏财 伤官 食神 劫财 比肩 正印 偏印 正官 七杀 
            ////  庚 　　　七杀 正官 偏财 正财 食神 伤官 比肩 劫财 偏印 正印 
            ////  辛 　　　正官 七杀 正财 偏财 伤官 食神 劫财 比肩 正印 偏印 
            ////  壬 　　　偏印 正印 七杀 正官 偏财 正财 食神 伤官 比肩 劫财 
            ////  癸 　　　正印 偏印 正官 七杀 正财 偏财 伤官 食神 劫财 比肩 
            
                switch( dayIdx ) {
                  case 0: //// 甲
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "比";
                        break;
                      case 1: //// 乙
                        rtn = "劫";
                        break;
                      case 2: //// 丙
                        rtn = "食";
                        break;
                      case 3: //// 丁
                        rtn = "伤";
                        break;
                      case 4: //// 戊
                        rtn = "才";
                        break;
                      case 5: //// 己
                        rtn = "财";
                        break;
                      case 6: //// 庚
                        rtn = "煞";
                        break;
                      case 7: //// 辛
                        rtn = "官";
                        break;
                      case 8: //// 壬
                        rtn = "枭";
                        break;
                      case 9: //// 癸
                        rtn = "印";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 1: //// 乙
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "劫";
                        break;
                      case 1: //// 乙
                        rtn = "比";
                        break;
                      case 2: //// 丙
                        rtn = "伤";
                        break;
                      case 3: //// 丁
                        rtn = "食";
                        break;
                      case 4: //// 戊
                        rtn = "财";
                        break;
                      case 5: //// 己
                        rtn = "才";
                        break;
                      case 6: //// 庚
                        rtn = "官";
                        break;
                      case 7: //// 辛
                        rtn = "煞";
                        break;
                      case 8: //// 壬
                        rtn = "印";
                        break;
                      case 9: //// 癸
                        rtn = "枭";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 2: //// 丙
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "枭";
                        break;
                      case 1: //// 乙
                        rtn = "印";
                        break;
                      case 2: //// 丙
                        rtn = "比";
                        break;
                      case 3: //// 丁
                        rtn = "劫";
                        break;
                      case 4: //// 戊
                        rtn = "食";
                        break;
                      case 5: //// 己
                        rtn = "伤";
                        break;
                      case 6: //// 庚
                        rtn = "才";
                        break;
                      case 7: //// 辛
                        rtn = "财";
                        break;
                      case 8: //// 壬
                        rtn = "煞";
                        break;
                      case 9: //// 癸
                        rtn = "官";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 3: //// 丁
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "印";
                        break;
                      case 1: //// 乙
                        rtn = "枭";
                        break;
                      case 2: //// 丙
                        rtn = "劫";
                        break;
                      case 3: //// 丁
                        rtn = "比";
                        break;
                      case 4: //// 戊
                        rtn = "伤";
                        break;
                      case 5: //// 己
                        rtn = "食";
                        break;
                      case 6: //// 庚
                        rtn = "财";
                        break;
                      case 7: //// 辛
                        rtn = "才";
                        break;
                      case 8: //// 壬
                        rtn = "官";
                        break;
                      case 9: //// 癸
                        rtn = "煞";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 4: //// 戊
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "煞";
                        break;
                      case 1: //// 乙
                        rtn = "官";
                        break;
                      case 2: //// 丙
                        rtn = "枭";
                        break;
                      case 3: //// 丁
                        rtn = "印";
                        break;
                      case 4: //// 戊
                        rtn = "比";
                        break;
                      case 5: //// 己
                        rtn = "劫";
                        break;
                      case 6: //// 庚
                        rtn = "食";
                        break;
                      case 7: //// 辛
                        rtn = "伤";
                        break;
                      case 8: //// 壬
                        rtn = "才";
                        break;
                      case 9: //// 癸
                        rtn = "财";
                        break;
                      default:
                        break;
                    }      
                    break;
                  case 5: //// 己
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "官";
                        break;
                      case 1: //// 乙
                        rtn = "煞";
                        break;
                      case 2: //// 丙
                        rtn = "印";
                        break;
                      case 3: //// 丁
                        rtn = "枭";
                        break;
                      case 4: //// 戊
                        rtn = "劫";
                        break;
                      case 5: //// 己
                        rtn = "比";
                        break;
                      case 6: //// 庚
                        rtn = "伤";
                        break;
                      case 7: //// 辛
                        rtn = "食";
                        break;
                      case 8: //// 壬
                        rtn = "财";
                        break;
                      case 9: //// 癸
                        rtn = "才";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 6: //// 庚
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "才";
                        break;
                      case 1: //// 乙
                        rtn = "财";
                        break;
                      case 2: //// 丙
                        rtn = "煞";
                        break;
                      case 3: //// 丁
                        rtn = "官";
                        break;
                      case 4: //// 戊
                        rtn = "枭";
                        break;
                      case 5: //// 己
                        rtn = "印";
                        break;
                      case 6: //// 庚
                        rtn = "比";
                        break;
                      case 7: //// 辛
                        rtn = "劫";
                        break;
                      case 8: //// 壬
                        rtn = "食";
                        break;
                      case 9: //// 癸
                        rtn = "伤";
                        break;
                      default:
                        break;
                    }      
                    break;
                  case 7: //// 辛
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "财";
                        break;
                      case 1: //// 乙
                        rtn = "才";
                        break;
                      case 2: //// 丙
                        rtn = "官";
                        break;
                      case 3: //// 丁
                        rtn = "煞";
                        break;
                      case 4: //// 戊
                        rtn = "印";
                        break;
                      case 5: //// 己
                        rtn = "枭";
                        break;
                      case 6: //// 庚
                        rtn = "劫";
                        break;
                      case 7: //// 辛
                        rtn = "比";
                        break;
                      case 8: //// 壬
                        rtn = "伤";
                        break;
                      case 9: //// 癸
                        rtn = "食";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 8: //// 壬
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "食";
                        break;
                      case 1: //// 乙
                        rtn = "伤";
                        break;
                      case 2: //// 丙
                        rtn = "才";
                        break;
                      case 3: //// 丁
                        rtn = "财";
                        break;
                      case 4: //// 戊
                        rtn = "煞";
                        break;
                      case 5: //// 己
                        rtn = "官";
                        break;
                      case 6: //// 庚
                        rtn = "枭";
                        break;
                      case 7: //// 辛
                        rtn = "印";
                        break;
                      case 8: //// 壬
                        rtn = "比";
                        break;
                      case 9: //// 癸
                        rtn = "劫";
                        break;
                      default:
                        break;
                    }
                    break;
                  case 9: //// 癸
                    switch( gzIdx ) {
                      case 0: //// 甲
                        rtn = "伤";
                        break;
                      case 1: //// 乙
                        rtn = "食";
                        break;
                      case 2: //// 丙
                        rtn = "财";
                        break;
                      case 3: //// 丁
                        rtn = "才";
                        break;
                      case 4: //// 戊
                        rtn = "官";
                        break;
                      case 5: //// 己
                        rtn = "煞";
                        break;
                      case 6: //// 庚
                        rtn = "印";
                        break;
                      case 7: //// 辛
                        rtn = "枭";
                        break;
                      case 8: //// 壬
                        rtn = "劫";
                        break;
                      case 9: //// 癸
                        rtn = "比";
                        break;
                      default:
                        break;
                    }
                    break;
                  default:
                    break;
                }
              }  
              else {
                gzIdx = dz.indexOf(gz);
                if( gzIdx >= 0 ) { //// 地支十神
            // 局主地支　甲　　乙 　丙　 丁　 戊　 己　 庚　 辛　 壬　 癸
            // 子 　　　正印 偏印 正官 七杀 正财 偏财 伤官 食神 劫财 比肩 
            // 丑 　　　正财 偏财 伤官 食神 劫财 比肩 正印 偏印 正官 七杀 
            // 寅 　　　比肩 劫财 偏印 正印 七杀 正官 偏财 正财 食神 伤官 
            // 卯　　　 劫财 比肩 正印 偏印 正官 七杀 正财 偏财 伤官 食神 
            // 辰　　　 偏财 正财 食神 伤官 比肩 劫财 偏印 正印 七杀 正官 
            // 巳　　　 伤官 食神 劫财 比肩 正印 偏印 正官 七杀 正财 偏财 
            // 午　　　 食神 伤官 比肩 劫财 偏印 正印 七杀 正官 偏财 正财 
            // 未　　　 正财 偏财 伤官 食神 劫财 比肩 正印 偏印 正官 七杀 
            // 申　　　 七杀 正官 偏财 正财 食神 伤官 比肩 劫财 偏印 正印 
            // 酉　　　 正官 七杀 正财 偏财 伤官 食神 劫财 比肩 正印 偏印 
            // 戌　　　 偏财 正财 食神 伤官 比肩 劫财 偏印 正印 七杀 正官 
            // 亥　　　 偏印 正印 七杀 正官 偏财 正财 食神 伤官 比肩 劫财      
                  switch( dayIdx ) {
                    case 0: //// 甲
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "印";
                          break;
                        case 1: //// 丑
                          rtn = "财";
                          break;
                        case 2: //// 寅
                          rtn = "比";
                          break;
                        case 3: //// 卯
                          rtn = "劫";
                          break;
                        case 4: //// 辰
                          rtn = "才";
                          break;
                        case 5: //// 巳
                          rtn = "伤";
                          break;
                        case 6: //// 午
                          rtn = "食";
                          break;
                        case 7: //// 未
                          rtn = "财";
                          break;
                        case 8: //// 申
                          rtn = "煞";
                          break;
                        case 9: //// 酉
                          rtn = "官";
                          break;
                        case 10: //// 戌
                          rtn = "才";
                          break;
                        case 11: //// 亥
                          rtn = "枭";
                          break;
                        default:
                          break;
                      }
                      break;
                    case 1: //// 乙
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "枭";
                          break;
                        case 1: //// 丑
                          rtn = "才";
                          break;
                        case 2: //// 寅
                          rtn = "劫";
                          break;
                        case 3: //// 卯
                          rtn = "比";
                          break;
                        case 4: //// 辰
                          rtn = "财";
                          break;
                        case 5: //// 巳
                          rtn = "食";
                          break;
                        case 6: //// 午
                          rtn = "伤";
                          break;
                        case 7: //// 未
                          rtn = "才";
                          break;
                        case 8: //// 申
                          rtn = "官";
                          break;
                        case 9: //// 酉
                          rtn = "煞";
                          break;
                        case 10: //// 戌
                          rtn = "财";
                          break;
                        case 11: //// 亥
                          rtn = "印";
                          break;
                      }
                      break;
                    case 2: //// 丙
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "官";
                          break;
                        case 1: //// 丑
                          rtn = "伤";
                          break;
                        case 2: //// 寅
                          rtn = "枭";
                          break;
                        case 3: //// 卯
                          rtn = "印";
                          break;
                        case 4: //// 辰
                          rtn = "食";
                          break;
                        case 5: //// 巳
                          rtn = "劫";
                          break;
                        case 6: //// 午
                          rtn = "比";
                          break;
                        case 7: //// 未
                          rtn = "伤";
                          break;
                        case 8: //// 申
                          rtn = "才";
                          break;
                        case 9: //// 酉
                          rtn = "财";
                          break;
                        case 10: //// 戌
                          rtn = "食";
                          break;
                        case 11: //// 亥
                          rtn = "煞";
                          break;
                      }
                      break;
                    case 3: //// 丁
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "煞";
                          break;
                        case 1: //// 丑
                          rtn = "食";
                          break;
                        case 2: //// 寅
                          rtn = "印";
                          break;
                        case 3: //// 卯
                          rtn = "枭";
                          break;
                        case 4: //// 辰
                          rtn = "伤";
                          break;
                        case 5: //// 巳
                          rtn = "比";
                          break;
                        case 6: //// 午
                          rtn = "劫";
                          break;
                        case 7: //// 未
                          rtn = "食";
                          break;
                        case 8: //// 申
                          rtn = "财";
                          break;
                        case 9: //// 酉
                          rtn = "才";
                          break;
                        case 10: //// 戌
                          rtn = "伤";
                          break;
                        case 11: //// 亥
                          rtn = "官";
                          break;
                      }
                      break;
                    case 4: //// 戊
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "财";
                          break;
                        case 1: //// 丑
                          rtn = "劫";
                          break;
                        case 2: //// 寅
                          rtn = "煞";
                          break;
                        case 3: //// 卯
                          rtn = "官";
                          break;
                        case 4: //// 辰
                          rtn = "比";
                          break;
                        case 5: //// 巳
                          rtn = "印";
                          break;
                        case 6: //// 午
                          rtn = "枭";
                          break;
                        case 7: //// 未
                          rtn = "劫";
                          break;
                        case 8: //// 申
                          rtn = "食";
                          break;
                        case 9: //// 酉
                          rtn = "伤";
                          break;
                        case 10: //// 戌
                          rtn = "比";
                          break;
                        case 11: //// 亥
                          rtn = "才";
                          break;
                      }
                      break;
                    case 5: //// 己
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "才";
                          break;
                        case 1: //// 丑
                          rtn = "比";
                          break;
                        case 2: //// 寅
                          rtn = "官";
                          break;
                        case 3: //// 卯
                          rtn = "煞";
                          break;
                        case 4: //// 辰
                          rtn = "劫";
                          break;
                        case 5: //// 巳
                          rtn = "枭";
                          break;
                        case 6: //// 午
                          rtn = "印";
                          break;
                        case 7: //// 未
                          rtn = "比";
                          break;
                        case 8: //// 申
                          rtn = "伤";
                          break;
                        case 9: //// 酉
                          rtn = "食";
                          break;
                        case 10: //// 戌
                          rtn = "劫";
                          break;
                        case 11: //// 亥
                          rtn = "财";
                          break;
                      }
                      break;
                    case 6: //// 庚
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "伤";
                          break;
                        case 1: //// 丑
                          rtn = "印";
                          break;
                        case 2: //// 寅
                          rtn = "才";
                          break;
                        case 3: //// 卯
                          rtn = "财";
                          break;
                        case 4: //// 辰
                          rtn = "枭";
                          break;
                        case 5: //// 巳
                          rtn = "官";
                          break;
                        case 6: //// 午
                          rtn = "煞";
                          break;
                        case 7: //// 未
                          rtn = "印";
                          break;
                        case 8: //// 申
                          rtn = "比";
                          break;
                        case 9: //// 酉
                          rtn = "劫";
                          break;
                        case 10: //// 戌
                          rtn = "枭";
                          break;
                        case 11: //// 亥
                          rtn = "食";
                          break;
                      }
                      break;
                    case 7: //// 辛
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "食";
                          break;
                        case 1: //// 丑
                          rtn = "枭";
                          break;
                        case 2: //// 寅
                          rtn = "财";
                          break;
                        case 3: //// 卯
                          rtn = "才";
                          break;
                        case 4: //// 辰
                          rtn = "印";
                          break;
                        case 5: //// 巳
                          rtn = "煞";
                          break;
                        case 6: //// 午
                          rtn = "官";
                          break;
                        case 7: //// 未
                          rtn = "枭";
                          break;
                        case 8: //// 申
                          rtn = "劫";
                          break;
                        case 9: //// 酉
                          rtn = "比";
                          break;
                        case 10: //// 戌
                          rtn = "印";
                          break;
                        case 11: //// 亥
                          rtn = "伤";
                          break;
                      }
                      break;
                    case 8: //// 壬
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "劫";
                          break;
                        case 1: //// 丑
                          rtn = "官";
                          break;
                        case 2: //// 寅
                          rtn = "食";
                          break;
                        case 3: //// 卯
                          rtn = "伤";
                          break;
                        case 4: //// 辰
                          rtn = "煞";
                          break;
                        case 5: //// 巳
                          rtn = "财";
                          break;
                        case 6: //// 午
                          rtn = "才";
                          break;
                        case 7: //// 未
                          rtn = "官";
                          break;
                        case 8: //// 申
                          rtn = "枭";
                          break;
                        case 9: //// 酉
                          rtn = "印";
                          break;
                        case 10: //// 戌
                          rtn = "煞";
                          break;
                        case 11: //// 亥
                          rtn = "比";
                          break;
                      }
                      break;
                    case 9: //// 癸
                      switch( gzIdx ) {
                        case 0: //// 子
                          rtn = "比";
                          break;
                        case 1: //// 丑
                          rtn = "煞";
                          break;
                        case 2: //// 寅
                          rtn = "伤";
                          break;
                        case 3: //// 卯
                          rtn = "食";
                          break;
                        case 4: //// 辰
                          rtn = "官";
                          break;
                        case 5: //// 巳
                          rtn = "才";
                          break;
                        case 6: //// 午
                          rtn = "财";
                          break;
                        case 7: //// 未
                          rtn = "煞";
                          break;
                        case 8: //// 申
                          rtn = "印";
                          break;
                        case 9: //// 酉
                          rtn = "枭";
                          break;
                        case 10: //// 戌
                          rtn = "官";
                          break;
                        case 11: //// 亥
                          rtn = "劫";
                          break;
                      }
                      break;        
                  }
                }
              }
                  
        return rtn;
    }

    public String fnComputeDzcg(String daytg, String dz) {
        String rtn = "";
        switch(daytg) {
            case "甲":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "乙":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "丙":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "丁":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"}]";
                        break;
                    case "亥":  
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;  
                    default:
                        break;
                }
                break;
            case "戊":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    default:       
                        break;
                }
                break;
            case "己":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "庚":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "辛":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"枭\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"食\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            case "壬":
                switch(dz) {
                    case "子":
                        rtn = "[{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "丑":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "寅":
                        rtn = "[{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"},{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "卯":
                        rtn = "[{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "辰":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"},{\"tg\":\"癸\",\"ss\":\"印\",\"color\":\"#10c0b0\"}]";
                        break;
                    case "巳":
                        rtn = "[{\"tg\":\"丙\",\"ss\":\"食\",\"color\":\"red\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"}]";
                        break;
                    case "午":
                        rtn = "[{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"}]";
                        break;
                    case "未":
                        rtn = "[{\"tg\":\"己\",\"ss\":\"财\",\"color\":\"grey\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"},{\"tg\":\"乙\",\"ss\":\"劫\",\"color\":\"green\"}]";
                        break;
                    case "申":
                        rtn = "[{\"tg\":\"庚\",\"ss\":\"煞\",\"color\":\"white\"},{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"}]";
                        break;
                    case "酉":
                        rtn = "[{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"}]";
                        break;
                    case "戌":
                        rtn = "[{\"tg\":\"戊\",\"ss\":\"才\",\"color\":\"grey\"},{\"tg\":\"辛\",\"ss\":\"官\",\"color\":\"white\"},{\"tg\":\"丁\",\"ss\":\"伤\",\"color\":\"red\"}]";
                        break;
                    case "亥":
                        rtn = "[{\"tg\":\"壬\",\"ss\":\"枭\",\"color\":\"#10c0b0\"},{\"tg\":\"甲\",\"ss\":\"比\",\"color\":\"green\"}]";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return rtn;
    }

    public String fnGz2Wu(String gz) {
        String rtn = "";
        switch(gz) {
            case "甲":
                rtn = "阳木";
                break;
            case "乙":
                rtn = "阴木";
                break;
            case "丙":
                rtn = "阳火";
                break;
            case "丁":
                rtn = "阴火";
                break;
            case "戊":
                rtn = "阳土";
                break;
            case "己":
                rtn = "阴土";
                break;
            case "庚":
                rtn = "阳金";
                break;
            case "辛":
                rtn = "阴金";
                break;
            case "壬":
                rtn = "阳水";
                break;
            case "癸":
                rtn = "阴水";
                break;
            case "子":
                rtn = "阳水";
                break;
            case "丑":
                rtn = "阴土";
                break;
            case "寅":
                rtn = "阳木";
                break;
            case "卯":
                rtn = "阴木";
                break;
            case "辰":
                rtn = "阳土";
                break;
            case "巳":
                rtn = "阴火";
                break;
            case "午":
                rtn = "阳火";
                break;
            case "未":
                rtn = "阴土";
                break;
            case "申":
                rtn = "阳金";
                break;
            case "酉":
                rtn = "阴金";
                break;
            case "戌":
                rtn = "阳土";
                break;
            case "亥":
                rtn = "阴水";
                break;
            default:
                break;
        }
        return rtn;
    }

    public Integer fnComputeShengKe(String sk) {
        Integer rtn = 0;
        Integer sheng = 1;
        Integer ke = 0;
        if(fnComputeShengKeShen(bazi.shenDayZhi) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenMonthGan) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenMonthZhi) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenYearGan) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenYearZhi) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenHourGan) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(fnComputeShengKeShen(bazi.shenHourZhi) > 0) {
            sheng = sheng + 1;
        } else {
            ke = ke + 1;
        }
        if(sk.equals("生")) {
            rtn = sheng;
        }
        if(sk.equals("克")) {
            rtn = ke;
        }
        return rtn;
    }

    public Integer fnComputeShengKeShen(String shen) {
        Integer rtn = 0;
        switch(shen) {
            case "比":
                rtn = 1;
                break;
            case "劫":
                rtn = 1;
                break;
            case "官":
                rtn = -1;
                break;
            case "煞":
                rtn = -1;
                break;
            case "印":
                rtn = 1;
                break;
            case "枭":
                rtn = 1;
                break;
            case "食":
                rtn = -1;
                break;
            case "伤":
                rtn = -1;
                break;
            case "财":
                rtn = -1;
                break;
            case "才":
                rtn = -1;
                break;
            default:
                break;
        }
        return rtn;    
    }

    public String fnComputeXiYong(Integer zpSheng, Integer zpKe) {
        String rtn = "";
        if(zpKe > zpSheng) {
            if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                rtn = "水";
                shenXyz = "印";
                shenXyp = "枭";
            }
            if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                rtn = "木";
                shenXyz = "印";
                shenXyp = "枭";
            }
            if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                rtn = "火";
                shenXyz = "印";
                shenXyp = "枭";
            }
            if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                rtn = "土";
                shenXyz = "印";
                shenXyp = "枭";
            }
            if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                rtn = "金";
                shenXyz = "印";
                shenXyp = "枭";
            }
        } else {
            if( (shenBi + shenJie) > (shenYin + shenXiao) ) { /// 比劫多于印，用财冲比劫，克印
                if( (shenCaip + shenCaiz) < (shenShi + shenShang) ) {
                    if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                        rtn = "土";
                        shenXyz = "财";
                        shenXyp = "才";
                    }
                    if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                        rtn = "金";
                        shenXyz = "财";
                        shenXyp = "才";
                    }
                    if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                        rtn = "水";
                        shenXyz = "财";
                        shenXyp = "才";
                    }
                    if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                        rtn = "木";
                        shenXyz = "财";
                        shenXyp = "才";
                    }
                    if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                        rtn = "火";
                        shenXyz = "财";
                        shenXyp = "才";
                    }
                }
                else {
                    if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                        rtn = "火";
                        shenXyz = "食";
                        shenXyp = "伤";
                    }
                    if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                        rtn = "土";
                        shenXyz = "食";
                        shenXyp = "伤";
                    }
                    if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                        rtn = "金";
                        shenXyz = "食";
                        shenXyp = "伤";
                    }
                    if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                        rtn = "水";
                        shenXyz = "食";
                        shenXyp = "伤";
                    }
                    if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                        rtn = "木";
                        shenXyz = "食";
                        shenXyp = "伤";
                    }    
                }
            }
            else if( (shenCaip + shenCaiz) > (shenShi + shenShang) ) {  /// 财多于食伤, 用食伤
                if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                    rtn = "火";
                    shenXyz = "食";
                    shenXyp = "伤";
                }
                if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                    rtn = "土";
                    shenXyz = "食";
                    shenXyp = "伤";
                }
                if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                    rtn = "金";
                    shenXyz = "食";
                    shenXyp = "伤";
                }
                if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                    rtn = "水";
                    shenXyz = "食";
                    shenXyp = "伤";
                }
                if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                    rtn = "木";
                    shenXyz = "食";
                    shenXyp = "伤";
                }

            }
            else if( (shenCaip + shenCaiz) < (shenGuan + shenSha) ) {  /// 有食伤，有官，无财
                if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                    rtn = "土";
                    shenXyz = "财";
                    shenXyp = "才";
                }
                if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                    rtn = "金";
                    shenXyz = "财";
                    shenXyp = "才";
                }
                if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                    rtn = "水";
                    shenXyz = "财";
                    shenXyp = "才";
                }
                if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                    rtn = "木";
                    shenXyz = "财";
                    shenXyp = "才";
                }
                if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                    rtn = "火";
                    shenXyz = "财";
                    shenXyp = "才";
                }
            }
            else {  //// 有食伤，有财多于官，用官
                if(bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙")) {
                    rtn = "金";
                    shenXyz = "官";
                    shenXyp = "煞";
                }
                if(bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁")) {
                    rtn = "水";
                    shenXyz = "官";
                    shenXyp = "煞";
                }
                if(bazi.dayGan.equals("戊") || bazi.dayGan.equals("己")) {
                    rtn = "木";
                    shenXyz = "官";
                    shenXyp = "煞";
                }
                if(bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛")) {
                    rtn = "火";
                    shenXyz = "官";
                    shenXyp = "煞";
                }
                if(bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸")) {
                    rtn = "土";
                    shenXyz = "官";
                    shenXyp = "煞";
                }
            }
        }
        return rtn;
    }

    public String fnWuHelp(String wx) {
        String rtn = "喜用不明。";
        if(wx.equals("金")) {
            rtn = "选择西方位，选择白色或黄色，可佩戴金银，可有帮助。";
        }
        if(wx.equals("木")) {
            rtn = "选择东方位，选择绿色，可佩木属性的手串，首饰，可有帮助。";
        }
        if(wx.equals("水")) {
            rtn = "选择北方位，选择黑色，环境有水，可有帮助。";
        }
        if(wx.equals("火")) {
            rtn = "选择南方位，选择红色，可有帮助。";
        }
        if(wx.equals("土")) {
            rtn = "选择中间方位，选择棕色，可佩戴玉石类首饰，可有帮助。";
        }
        return rtn;
    }    

    public void fnBaZiAnalysis() {
        // 计算八字的五行，十神的个数
        feJin = 0;
        feMu = 0;
        feShui = 0;
        feHuo = 0;
        feTu = 0;

        feGanJin = 0;
        feGanMu = 0;
        feGanShui = 0;
        feGanHuo = 0;
        feGanTu = 0;
        
        shenGuan = 0;
        shenSha = 0;
        shenYin = 0;
        shenXiao = 0;
        shenBi = 0;
        shenJie = 0;
        shenCaiz = 0;
        shenCaip = 0;
        shenShi = 0;
        shenShang = 0;

//#region 计算八字的五行个数
        if( bazi.hourGan.equals("甲") || bazi.hourGan.equals("乙") ) {
            feMu = feMu + 1;
            feGanMu = feGanMu + 1;
        }
        if( bazi.hourGan.equals("丙") || bazi.hourGan.equals("丁") ) {
            feHuo = feHuo + 1;
            feGanHuo = feGanHuo + 1;
        }
        if( bazi.hourGan.equals("戊") || bazi.hourGan.equals("己") ) {
            feTu = feTu + 1;
            feGanTu = feGanTu + 1;
        }
        if( bazi.hourGan.equals("庚") || bazi.hourGan.equals("辛") ) {
            feJin = feJin + 1;
            feGanJin = feGanJin + 1;
        }
        if( bazi.hourGan.equals("壬") || bazi.hourGan.equals("癸") ) {
            feShui = feShui + 1;
            feGanShui = feGanShui + 1;
        }

        if( bazi.hourZhi.equals("子") || bazi.hourZhi.equals("亥") ) {
            feShui = feShui + 1;
        }
        if( bazi.hourZhi.equals("寅") || bazi.hourZhi.equals("卯") ) {
            feMu = feMu + 1;
        }
        if( bazi.hourZhi.equals("丑") || bazi.hourZhi.equals("辰") ||bazi.hourZhi.equals("戌") ||  bazi.hourZhi.equals("未") ) {
            feTu = feTu + 1;
        }
        if( bazi.hourZhi.equals("巳") || bazi.hourZhi.equals("午") ) {
            feHuo = feHuo + 1;
        }
        if( bazi.hourZhi.equals("申") || bazi.hourZhi.equals("酉") ) {
            feJin = feJin + 1;
        }

        if( bazi.dayGan.equals("甲") || bazi.dayGan.equals("乙") ) {
            feMu = feMu + 1;
            feGanMu = feGanMu + 1;
        }
        if( bazi.dayGan.equals("丙") || bazi.dayGan.equals("丁") ) {
            feHuo = feHuo + 1;
            feGanHuo = feGanHuo + 1;
        }
        if( bazi.dayGan.equals("戊") || bazi.dayGan.equals("己") ) {
            feTu = feTu + 1;
            feGanTu = feGanTu + 1;
        }
        if( bazi.dayGan.equals("庚") || bazi.dayGan.equals("辛") ) {
            feJin = feJin + 1;
            feGanJin = feGanJin + 1;
        }
        if( bazi.dayGan.equals("壬") || bazi.dayGan.equals("癸") ) {
            feShui = feShui + 1;
            feGanShui = feGanShui + 1;
        }

        if( bazi.dayZhi.equals("子") || bazi.dayZhi.equals("亥") ) {
            feShui = feShui + 1;
        }
        if( bazi.dayZhi.equals("寅") || bazi.dayZhi.equals("卯") ) {
            feMu = feMu + 1;
        }
        if( bazi.dayZhi.equals("丑") || bazi.dayZhi.equals("辰") ||bazi.dayZhi.equals("戌") ||  bazi.dayZhi.equals("未") ) {
            feTu = feTu + 1;
        }
        if( bazi.dayZhi.equals("巳") || bazi.dayZhi.equals("午") ) {
            feHuo = feHuo + 1;
        }
        if( bazi.dayZhi.equals("申") || bazi.dayZhi.equals("酉") ) {
            feJin = feJin + 1;
        }

        if( bazi.monthGan.equals("甲") || bazi.monthGan.equals("乙") ) {
            feMu = feMu + 1;
            feGanMu = feGanMu + 1;
        }
        if( bazi.monthGan.equals("丙") || bazi.monthGan.equals("丁") ) {
            feHuo = feHuo + 1;
            feGanHuo = feGanHuo + 1;
        }
        if( bazi.monthGan.equals("戊") || bazi.monthGan.equals("己") ) {
            feTu = feTu + 1;
            feGanTu = feGanTu + 1;
        }
        if( bazi.monthGan.equals("庚") || bazi.monthGan.equals("辛") ) {
            feJin = feJin + 1;
            feGanJin = feGanJin + 1;
        }
        if( bazi.monthGan.equals("壬") || bazi.monthGan.equals("癸") ) {
            feShui = feShui + 1;
            feGanShui = feGanShui + 1;
        }

        if( bazi.monthZhi.equals("子") || bazi.monthZhi.equals("亥") ) {
            feShui = feShui + 1;
        }
        if( bazi.monthZhi.equals("寅") || bazi.monthZhi.equals("卯") ) {
            feMu = feMu + 1;
        }
        if( bazi.monthZhi.equals("丑") || bazi.monthZhi.equals("辰") ||bazi.monthZhi.equals("戌") ||  bazi.monthZhi.equals("未") ) {
            feTu = feTu + 1;
        }
        if( bazi.monthZhi.equals("巳") || bazi.monthZhi.equals("午") ) {
            feHuo = feHuo + 1;
        }
        if( bazi.monthZhi.equals("申") || bazi.monthZhi.equals("酉") ) {
            feJin = feJin + 1;
        }

        if( bazi.yearGan.equals("甲") || bazi.yearGan.equals("乙") ) {
            feMu = feMu + 1;
            feGanMu = feGanMu + 1;
        }
        if( bazi.yearGan.equals("丙") || bazi.yearGan.equals("丁") ) {
            feHuo = feHuo + 1;
            feGanHuo = feGanHuo + 1;
        }
        if( bazi.yearGan.equals("戊") || bazi.yearGan.equals("己") ) {
            feTu = feTu + 1;
            feGanTu = feGanTu + 1;
        }
        if( bazi.yearGan.equals("庚") || bazi.yearGan.equals("辛") ) {
            feJin = feJin + 1;
            feGanJin = feGanJin + 1;
        }
        if( bazi.yearGan.equals("壬") || bazi.yearGan.equals("癸") ) {
            feShui = feShui + 1;
            feGanShui = feGanShui + 1;
        }

        if( bazi.yearZhi.equals("子") || bazi.yearZhi.equals("亥") ) {
            feShui = feShui + 1;
        }
        if( bazi.yearZhi.equals("寅") || bazi.yearZhi.equals("卯") ) {
            feMu = feMu + 1;
        }
        if( bazi.yearZhi.equals("丑") || bazi.yearZhi.equals("辰") ||bazi.yearZhi.equals("戌") ||  bazi.yearZhi.equals("未") ) {
            feTu = feTu + 1;
        }
        if( bazi.yearZhi.equals("巳") || bazi.yearZhi.equals("午") ) {
            feHuo = feHuo + 1;
        }
        if( bazi.yearZhi.equals("申") || bazi.yearZhi.equals("酉") ) {
            feJin = feJin + 1;
        }
//#endregion

//#region 十神 
        if( bazi.shenHourGan.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenHourGan.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenHourGan.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenHourGan.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenHourGan.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenHourGan.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenHourGan.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenHourGan.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenHourGan.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenHourGan.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenHourZhi.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenHourZhi.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenHourZhi.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenHourZhi.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenHourZhi.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenHourZhi.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenHourZhi.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenHourZhi.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenHourZhi.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenHourZhi.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenDayZhi.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenDayZhi.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenDayZhi.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenDayZhi.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenDayZhi.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenDayZhi.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenDayZhi.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenDayZhi.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenDayZhi.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenDayZhi.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenMonthGan.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenMonthGan.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenMonthGan.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenMonthGan.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenMonthGan.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenMonthGan.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenMonthGan.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenMonthGan.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenMonthGan.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenMonthGan.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenMonthZhi.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenMonthZhi.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenMonthZhi.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenMonthZhi.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenMonthZhi.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenMonthZhi.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenMonthZhi.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenMonthZhi.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenMonthZhi.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenMonthZhi.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenYearGan.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenYearGan.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenYearGan.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenYearGan.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenYearGan.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenYearGan.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenYearGan.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenYearGan.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenYearGan.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenYearGan.equals("才") ) {
            shenCaip = shenCaip + 1;
        }

        if( bazi.shenYearZhi.equals("比") ) {
            shenBi = shenBi + 1;
        }
        if( bazi.shenYearZhi.equals("劫") ) {
            shenJie = shenJie + 1;
        }
        if( bazi.shenYearZhi.equals("官") ) {
            shenGuan = shenGuan + 1;
        }
        if( bazi.shenYearZhi.equals("煞") ) {
            shenSha = shenSha + 1;
        }
        if( bazi.shenYearZhi.equals("印") ) {
            shenYin = shenYin + 1;
        }
        if( bazi.shenYearZhi.equals("枭") ) {
            shenXiao = shenXiao + 1;
        }
        if( bazi.shenYearZhi.equals("食") ) {
            shenShi = shenShi + 1;
        }
        if( bazi.shenYearZhi.equals("伤") ) {
            shenShang = shenShang + 1;
        }
        if( bazi.shenYearZhi.equals("财") ) {
            shenCaiz = shenCaiz + 1;
        }
        if( bazi.shenYearZhi.equals("才") ) {
            shenCaip = shenCaip + 1;
        }
//#endregion

        zpSheng = fnComputeShengKe("生");
        zpKe = fnComputeShengKe("克");

    }

    public boolean fnComputeTianYi(String dayTg, String zhi) {
        boolean bRtn = false;
        switch(dayTg) {
            case "甲":
            case "戊":
            case "庚":
                if(zhi.equals("丑") || zhi.equals("未") ) {
                    bRtn = true;
                }
                break;
            case "乙":
            case "己":
                if(zhi.equals("子") || zhi.equals("申") ) {
                    bRtn = true;
                }
                break;
            case "丙":
            case "丁":
                if(zhi.equals("酉") || zhi.equals("亥") ) {
                    bRtn = true;
                }
                break;
        case "辛":
                if(zhi.equals("寅") || zhi.equals("午") ) {
                    bRtn = true;
                }
                break;
            case "壬":
            case "癸":
                if(zhi.equals("卯") || zhi.equals("巳") ) {
                    bRtn = true;
                }
                break;
        }
        return bRtn;
    }

    public boolean fnComputeYiMa(String dayZhi, String zhi) {
        boolean bRtn = false;
        /// 用12地址法计算驿马
        switch(dayZhi) {
            case "子":
            case "辰":
            case "申":
                if(zhi.equals("寅") ) {
                    bRtn = true;
                }
                break;
            case "丑":
            case "巳":
            case "酉":
                if(zhi.equals("亥") ) {
                    bRtn = true;
                }
                break;
            case "寅":
            case "午":
            case "戌":
                if(zhi.equals("申") ) {
                    bRtn = true;
                }
                break;
            case "卯":
            case "未":
            case "亥":
                if(zhi.equals("巳") ) {
                    bRtn = true;
                }
                break;
        }
        return bRtn;
    }

//#endregion


}

////----------------  zi ping file end -------------------