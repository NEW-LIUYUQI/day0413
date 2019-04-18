package com.lyq.test;

import com.lyq.mapper.SpecilMapper;
import com.lyq.po.Customer;
import com.lyq.util.MybatisUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;


import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest {
    //根据客户编号查询客户信息
    @Test
    public void findCustomerByIdTest() throws  Exception{
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession  sqlSession = sqlSessionFactory.openSession();

        Customer customer = sqlSession .selectOne("findCustomerById",1);
        System.out.println(customer.toString());

        sqlSession.close();
    }
    //模糊查询
    @Test
    public void findCustomerByNameTest() throws Exception{
        //读取配置文件
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //根据配置文件构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
        //3创建SqlSession
        SqlSession sqlSession =sqlSessionFactory.openSession();
        //4执行
        List<Customer> customers = sqlSession.selectList("com.lyq.mapper"+".CustomerMapper.findCustomerByName","o");
        for (Customer customer : customers){
            System.out.println(customer);
        }
        sqlSession.close();
    }
    //增
    @Test
    public void addCustomerTest() throws Exception{
        //1读取配置文件
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //根据配置文件构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
        //3创建SqlSession
        SqlSession sqlSession =sqlSessionFactory.openSession();
        //4sqlsession执行添加操作
        Customer customer =new Customer();
        customer.setUsername("rose");
        customer.setJobs("student");
        customer.setPhone("1333333533333");
        //执行插入方法
        int rows = sqlSession.insert("com.lyq.mapper"+".CustomerMapper.addCustomer",customer);

        if (rows>0){
            System.out.println("您成功插入了"+rows+"条数据！");
        }else {
            System.out.println("执行插入失败！！！");
        }
        sqlSession.commit();
    }
    //改
    @Test
    public void updateCustomerTest() throws Exception{
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //根据配置文件构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
        //3创建SqlSession
        SqlSession sqlSession =sqlSessionFactory.openSession();

        Customer customer = new Customer();
        customer.setId(0);
        customer.setUsername("sober");
        customer.setJobs("programmer");
        customer.setPhone("15073773708");

        int rows =sqlSession.update("com.lyq.mapper"+".CustomerMapper.updateCustomer",customer);

        if (rows>0){
            System.out.println("您成功修改了"+rows+"条数据");
        }else {
            System.out.println("修改失败！！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    //删
    @Test
    public void deleteCustomerTest() throws Exception{
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //根据配置文件构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
        //3创建SqlSession
        SqlSession sqlSession =sqlSessionFactory.openSession();
        int rows = sqlSession.delete("deleteCustomer",0);

        if (rows>0){
            System.out.println("成功删除"+rows+"条数据");
        }else {
            System.out.println("删除数据失败！！！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    //zuoye
    public static class TestSpecil{
        public static void main(String[] args) {
            SqlSession sqlSession = MybatisUtils.getSqlSession(false);
            int pageNum=1;
            int pageSize=5;
            int start=(pageNum-1)*pageSize;
            SpecilMapper specilMapper = sqlSession.getMapper(SpecilMapper.class);

            Map<String,Object>map = new HashMap<>();
            map.put("start",start);
            map.put("pageSize",pageSize);

            List<SpecilMapper> specilMappers =specilMapper.findSpecilSplit(map);

            for (SpecilMapper specilMapper1 : specilMappers){
                System.out.println(specilMapper);

            }
            MybatisUtils.closeSqlSession(sqlSession);
        }
    }

}
