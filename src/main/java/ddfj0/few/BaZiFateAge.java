package ddfj0.few;

public class BaZiFateAge {    //// 本命，大运，流年
    public String version;

    public String sex;
    public String birthday;
    public String lunarDate;

    public String hourGan;
    public String hourZhi;
    public String dayGan;
    public String dayZhi;
    public String monthGan;
    public String monthZhi;
    public String yearGan;
    public String yearZhi;

    public String shenHourGan;
    public String shenHourZhi;
    public String shenDayGan;
    public String shenDayZhi;
    public String shenMonthGan;
    public String shenMonthZhi;
    public String shenYearGan;
    public String shenYearZhi;

    public Dzcg[] dzcgYearList;
    public Dzcg[] dzcgMonthList;
    public Dzcg[] dzcgDayList;
    public Dzcg[] dzcgHourList;

    public DayunLiunian[] bigyunList;

    //#region 旧版 大运数据 
    public Integer age1;   //// 大运的起始年龄 
    public Integer fateYear1;   //// 大运的起始年份 
    public String fateGan1;
    public String fateZhi1;
    public String fateYearGan1[];  //// 十个流年的干支
    public String fateYearZhi1[];

    public Integer age2;   //// 大运的起始年龄 
    public String fateYear2;
    public String fateGan2;
    public String fateZhi2;
    public String fateYearGan2[];  //// 十个流年的干支
    public String fateYearZhi2[];

    public Integer age3;   //// 大运的起始年龄 
    public String fateYear3;
    public String fateGan3;
    public String fateZhi3;
    public String fateYearGan3[];  //// 十个流年的干支
    public String fateYearZhi3[];

    public Integer age4;   //// 大运的起始年龄 
    public String fateYear4; 
    public String fateGan4;
    public String fateZhi4;
    public String fateYearGan4[];  //// 十个流年的干支
    public String fateYearZhi4[];

    public Integer age5;   //// 大运的起始年龄 
    public String fateYear5;
    public String fateGan5;
    public String fateZhi5;
    public String fateYearGan5[];  //// 十个流年的干支
    public String fateYearZhi5[];

    public Integer age6;   //// 大运的起始年龄 
    public String fateYear6;
    public String fateGan6;
    public String fateZhi6;
    public String fateYearGan6[];  //// 十个流年的干支
    public String fateYearZhi6[];


    public Integer age7;   //// 大运的起始年龄 
    public String fateYear7;
    public String fateGan7;
    public String fateZhi7;
    public String fateYearGan7[];  //// 十个流年的干支
    public String fateYearZhi7[];

    public Integer age8;   //// 大运的起始年龄 
    public String fateYear8;
    public String fateGan8;
    public String fateZhi8;
    public String fateYearGan8[];  //// 十个流年的干支
    public String fateYearZhi8[];
    //#endregion

    public BaZiFateAge() {
        version = "子平法八字分析版本 20240213";
    }
}

class Dzcg {
    public String tg;
    public String ss;
    public String color;
}

/**
 * InnerZiping
 */
class DayunLiunian {
    public Integer year;
    public Integer age;
    public String tg;
    public String dz;
    public Dzcg[] dzcg;
    public String liunian; //// 十个干支字符串
}

