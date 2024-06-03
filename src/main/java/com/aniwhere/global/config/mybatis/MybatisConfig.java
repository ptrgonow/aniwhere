package com.aniwhere.global.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration("mybatisConfig")
@MapperScan(basePackages = {
        "com.aniwhere.domain.board.faq.mapper",
        "com.aniwhere.domain.board.notice.mapper",
        "com.aniwhere.domain.route.mapper",
        "com.aniwhere.domain.shop.mapper",
        "com.aniwhere.domain.user.mapper"
})

public class MybatisConfig {


    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return sessionFactory.getObject();
    }

}
