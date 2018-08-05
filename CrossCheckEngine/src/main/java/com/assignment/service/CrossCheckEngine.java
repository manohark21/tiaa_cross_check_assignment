package com.assignment.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiaa.assignment.pojo.Branch;
import com.tiaa.assignment.pojo.BranchType;
import com.tiaa.assignment.pojo.Cmfoodchain;
import com.tiaa.assignment.pojo.CmfoodchainType;
import com.tiaa.assignment.pojo.OrderdetailType;
import com.tiaa.assignment.pojo.OrdersType;
import com.tiaa.assignment.pojo.OutputCrossCheck;

public class CrossCheckEngine extends TimerTask {
	
	@Override
	public void run() {
		try {
			System.out.println("count");
			generateFiles();
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
    
	public void generateFiles() throws JAXBException, IOException {
		File directory = new File("./src/main/resources/inputfiles");
		// get all the files from a directory
		File[] fList = directory.listFiles();

		ArrayList<Branch> matchBrList = new ArrayList<Branch>();
		ArrayList<Branch> misMatchBrList = new ArrayList<Branch>();
		for (File file : fList) {

			// We need to create JAXContext instance
			JAXBContext jaxbContext = JAXBContext.newInstance(CmfoodchainType.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			CmfoodchainType cmfoodchain = (CmfoodchainType) unmarshaller.unmarshal(file);
			OrdersType orders = cmfoodchain.getOrders();
			List<OrderdetailType> orderdetails = orders.getOrderdetail();
			double totalAmnt = 0;
			for (OrderdetailType order : orderdetails) {
				totalAmnt += order.getBillamount();
			}
			Branch branch = new Branch();
			branch.setLocation(cmfoodchain.getBranch().getLocation());
			branch.setLocationid(cmfoodchain.getBranch().getLocationid());
			branch.setSumoforder(String.valueOf(totalAmnt));
			branch.setTotalcollection(String.valueOf(cmfoodchain.getBranch().getTotalcollection()));

			if (branch.getSumoforder().equals(branch.getTotalcollection()))
				matchBrList.add(branch);
			else
				misMatchBrList.add(branch);

		}
		generateOutputfiles(matchBrList, misMatchBrList);

	}

	private void generateOutputfiles(ArrayList<Branch> matchBrList, ArrayList<Branch> misMtchBrList)
			throws JsonGenerationException, JsonMappingException, IOException {

		OutputCrossCheck match = new OutputCrossCheck();
		Cmfoodchain cmfoodchain1 = new Cmfoodchain();
		cmfoodchain1.setBranch(matchBrList);
		match.setCmfoodchain(cmfoodchain1);

		OutputCrossCheck mistMatchJson = new OutputCrossCheck();
		Cmfoodchain cmfoodchain2 = new Cmfoodchain();
		cmfoodchain2.setBranch(misMtchBrList);
		mistMatchJson.setCmfoodchain(cmfoodchain2);

		ObjectMapper mapper = new ObjectMapper();
		File matchFile = new File("./src/main/resources/outputfiles/match.json");
		File mismatchFile = new File("./src/main/resources/outputfiles/mismatch.json");
		mapper.writeValue(matchFile, match);
		mapper.writeValue(mismatchFile, mistMatchJson);
	}
	
	
	public void generateXml(String location,String locId,float totalCollection,List<OrderdetailType> orderdetails) throws FileNotFoundException, JAXBException {
		// creating the JAXB context
		JAXBContext jContext = JAXBContext.newInstance(CmfoodchainType.class);
		Marshaller marshallObj = jContext.createMarshaller();
		marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		OrdersType orders = new OrdersType();
		BranchType branch = new BranchType();
		orders.setOrderdetail(orderdetails);
		branch.setLocation(location);
		branch.setLocationid(locId);
		branch.setTotalcollection(totalCollection);
		CmfoodchainType cmfoodchain = new CmfoodchainType(branch, orders);

		// calling the marshall method
		marshallObj.marshal(cmfoodchain, new FileOutputStream("./src/main/resources/inputfiles/"+location+".xml"));
	}

	

}
