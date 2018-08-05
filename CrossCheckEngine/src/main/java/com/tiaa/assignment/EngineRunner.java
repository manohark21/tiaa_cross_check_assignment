package com.tiaa.assignment;

import java.util.Timer;

import com.assignment.service.CrossCheckEngine;

public class EngineRunner {

	public static void main(String[] args) {

		// This task is scheduled to run every 10 seconds
		//We can set it as per requirement at 11:59 pm, picks up the files from the location 
		Timer t = new Timer();
		CrossCheckEngine s = new CrossCheckEngine();
		// s.generateXml();
		t.scheduleAtFixedRate(s, 0, 10000);

	}

}
