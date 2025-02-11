package com.telus.formletters.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.framework.test.TestUtil;

@RunWith(TelusJUnitClassRunner.class)
@ContextConfiguration("classpath:credit-test-spring.xml")
public class HelloWorldTest
{
    private static final Log s_log = LogFactory.getLog(HelloWorldTest.class);


    @Test
    public void test02_sayHello()
    {
        s_log.info("Hello World #2");
    }


    @Test
    public void test01_sayHello()
    {
        s_log.info("Hello World #1");
    }


    @Test
    public void testDump1() throws Exception
    {
        List<String> list = new ArrayList<String>();
        list.add("item1");
        list.add("item2");
        TestUtil.dump(list);
    }


    @Test
    public void testDump2() throws Exception
    {
        DumpHelper d1 = new DumpHelper();
        d1.name = "d1";
        d1.list = new ArrayList<DumpHelper>();
        d1.list.add(new DumpHelper("d1.1"));
        d1.list.add(new DumpHelper("d1.2"));
        TestUtil.dump(d1);
    }


    private static class DumpHelper
    {
        public int id;
        public String name;
        public List<DumpHelper> list;

        public DumpHelper()
        {
        }

        public DumpHelper(String name)
        {
            this.name = name;
        }
    }
}
