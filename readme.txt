
1、安装spring boot extension pack
2、ctrl+shift+p create spring maven project
3、spring version 2.54, java11, jar

4、modbus, opc ua, s7, 104, 61850, openadr

mvn -N io.takari:maven:wrapper


///////// vscode java config

    "maven.executable.path": "d:\\devp\\maven381\\bin\\mvn.cmd",
    "java.configuration.runtimes":  [
        {
            "name": "JavaSE-1.8",
            "path": "D:/devp/java/jdk1.8.0_311",
            "default": true
        },
        {
            "name": "JavaSE-11",
            "path": "D:/devp/java/jdk-11.0.11",
        }
    ],

////// 数据库

sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'

innodb_buffer_pool_size=16G
innodb_buffer_pool_instances=4
