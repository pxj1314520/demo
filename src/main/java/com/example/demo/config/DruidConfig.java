package com.example.demo.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*durid 检测我们的sql*/
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.druid")//读取配置文件
   public DruidDataSource duridDataSource(){
       DruidDataSource druidDataSource = new DruidDataSource();
       //Lists.newArrayList()相当于 new 一个arraysList
       druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
       return druidDataSource;
   }
   /*配置过滤数据*/
   @Bean
   public StatFilter statFilter(){
       StatFilter statFilter = new StatFilter();
       statFilter.setLogSlowSql(true);//日志sql
       statFilter.setSlowSqlMillis(5);//慢sql
       statFilter.setMergeSql(true);//合并sql
       return statFilter;
   }
   /*配置访问路径*/
   @Bean
   public ServletRegistrationBean servletRegistrationBean(){
       return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
   }


}
