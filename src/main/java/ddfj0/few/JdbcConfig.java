package ddfj0.few;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
public class JdbcConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.cfg")
    public DataSource dataSourceCfg() {

        String username, passwd, url, dcn;
        
        if( MainApplication.m_sysConfig == null ) {
            SysCfg.fnSysConfigLoad();
        }

        if(  MainApplication.m_sysConfig.bCfgSqlite == true) {
            username = "";
            passwd = "";
            url = "jdbc:sqlite:" + MainApplication.m_curPath + "/dbfew.db";
            dcn = "org.sqlite.JDBC";
        }
        else {
            username = MainApplication.m_sysConfig.spring_datasource_cfg_username;
            passwd = MainApplication.m_sysConfig.spring_datasource_cfg_password + "00";
            url = MainApplication.m_sysConfig.spring_datasource_cfg_url;
            dcn = "com.mysql.cj.jdbc.Driver";
        }

        return DataSourceBuilder
        .create()
        .username(username)
        .password(passwd)
        .url(url)
        .driverClassName(dcn)
        .build();
    }

    @Bean
    public JdbcTemplate jdbcSysCfg(@Qualifier("dataSourceCfg") @NonNull DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.data")
    public DataSource dataSourceData() {

        String username, passwd, url, dcn;
        
        if( MainApplication.m_sysConfig == null ) {
            SysCfg.fnSysConfigLoad();
        }

        username = MainApplication.m_sysConfig.spring_datasource_Data_username;
        passwd = MainApplication.m_sysConfig.spring_datasource_Data_password + "00";
        url = MainApplication.m_sysConfig.spring_datasource_Data_url;
        dcn = "com.mysql.cj.jdbc.Driver";

        return DataSourceBuilder
        .create()
        .username(username)
        .password(passwd)
        .url(url)
        .driverClassName(dcn)
        .build();
    }

    @Bean
    public JdbcTemplate jdbcDataCfg( @Qualifier("dataSourceData") @NonNull DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }    
}
