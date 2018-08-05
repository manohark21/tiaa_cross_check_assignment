package com.assignment.servoce.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.assignment.service.CrossCheckEngine;
import com.tiaa.assignment.pojo.OrderdetailType;

public class CrossCheckEngineTest {
	CrossCheckEngine engine;
	
	@Before
	public void setUp() throws Exception {
		engine = new CrossCheckEngine();
	}

	
	@Test
	public void testGeneratedXML() throws JAXBException, IOException {
		
		List<OrderdetailType> orderdetails = new ArrayList<OrderdetailType>();
		OrderdetailType orderdetailType = new OrderdetailType();
		orderdetailType.setOrderid((byte) 4);
		orderdetailType.setBillamount(123);
		OrderdetailType orderdetailType1 = new OrderdetailType();
		orderdetailType1.setOrderid((byte) 5);
		orderdetailType1.setBillamount(124);
		orderdetails.add(orderdetailType);
		orderdetails.add(orderdetailType1);
		engine.generateXml("Delhi","DELH-1234-231", 123, orderdetails);
		File directory = new File("./src/main/resources/inputfiles");
		// get all the files from a directory
		File[] fList = directory.listFiles();
		String fileName = null ;
		for (File file : fList) {
			if(file.getName().equals("Delhi.xml")) {
				fileName = file.getName();
			}
		}
		assertTrue(fileName.equals("Delhi.xml"));
	}
	
	
	@Test
	public void testOutPutFiles() throws JAXBException, IOException {
		engine.generateFiles();
		File directory = new File("./src/main/resources/outputfiles");
		// get all the files from a directory
		File[] fList = directory.listFiles();

		assertTrue(fList.length == 2);
	}
	
	


}
