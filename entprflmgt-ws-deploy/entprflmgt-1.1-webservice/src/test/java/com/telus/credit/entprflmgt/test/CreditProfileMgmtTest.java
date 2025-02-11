package com.telus.credit.entprflmgt.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.customer.domain.common_v1_1.AddressValidationResult;
import com.telus.credit.customer.domain.common_v1_1.ValidationMessage;
import com.telus.credit.entprflmgt.domain_v1_1.UpdateCreditProfile;
//import com.telus.credit.util.EnvUtil;

@ContextConfiguration("classpath:test-spring.xml")
public class CreditProfileMgmtTest
{
    @Autowired
    private DozerBeanMapper mapper;

    public CreditProfileMgmtTest() {
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	protected void setUp() throws Exception {
		try {
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant",
					"true");
			//EnvUtil.setupTestEnv();
			//ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext( EnvUtil.resourcesFolder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

    @Test
    public void test() throws Exception
    {
        UpdateCreditProfile src = read("test/conf/EnterpriseCreditProfileManagementServiceRequestResponse_v1_1.xml", UpdateCreditProfile.class);

        com.telus.credit.entprflmgt.domain.UpdateCreditProfile dest = mapper.map(src, com.telus.credit.entprflmgt.domain.UpdateCreditProfile.class);
        System.out.println(src);
        System.out.println(dest);
    }


    //@Test
    public void test2() throws Exception
    {
        List<ValidationMessage> list = new ArrayList<ValidationMessage>();
        ValidationMessage vm = new ValidationMessage();
        vm.setCode("code");
        list.add(vm);

        AddressValidationResult src = new AddressValidationResult();
        src.setValidationMessages(list);

        com.telus.credit.customer.domain.common_v1_1.AddressValidationResult dest = mapper.map(src, com.telus.credit.customer.domain.common_v1_1.AddressValidationResult.class);
        System.out.println(src);
        System.out.println(dest);
    }
    

    @SuppressWarnings("unchecked")
    private <T> T read(String filename, Class<T> clazz) throws Exception
    {
        JAXBContext ctx = JAXBContext.newInstance(clazz);
        return (T ) ctx.createUnmarshaller().unmarshal(new File(filename));
    }
}
