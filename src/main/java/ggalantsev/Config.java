package ggalantsev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


//@Configuration
//@ComponentScan("ggalantsev")
//@EnableTransactionManagement
public class Config {

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/mydb");
//        ds.setUsername("root");
//        ds.setPassword("password");
//
//        return ds;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf); //??? ?????????? ?????????? ??? JPA
//    }
//
//    @Bean
//    public JpaVendorAdapter jpaVendorAdapter() {
//        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setShowSql(false); // ??? ??????????? ??? ????????
//        adapter.setGenerateDdl(true); // ??? ????????? ??????
//        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
//
//        return adapter;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory
//            (DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
//        Properties jpaProp = new Properties(); // ??????? ?????????
//        jpaProp.put("hibernate.hbm2ddl.auto", "create"); //??????? ? ??????? ???????
//
//        //????????? ?????? ????????? ?? ???????
//        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource);
//        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
//        entityManagerFactory.setJpaProperties(jpaProp);
//        entityManagerFactory.setPackagesToScan("ggalantsev");
//        return entityManagerFactory;
//    }
//
//
////    @Bean
////    public ITemplateResolver templateResolver() {
////        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//////        resolver.setPrefix("static/client");
////        // For Spring Boot
////        resolver.setPrefix("classpath:/templates/");
////        resolver.setSuffix(".jsp");
//////        resolver.setTemplateMode("HTML5");
////        resolver.setCacheable(true);
////        return resolver;
////    }
}