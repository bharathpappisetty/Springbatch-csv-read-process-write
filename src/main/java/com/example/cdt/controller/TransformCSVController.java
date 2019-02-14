package com.example.cdt.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class TransformCSVController {
	
	
	Pattern pat = Pattern.compile("DemoApplication  : Started DemoApplication in (.*) seconds");
	
	@Autowired
	JobLauncher jobLauncher;
	
	 
	@Autowired
	Job job;
	
	@GetMapping(value="/transform")
	public String uploadCSV() throws Exception {
		long ts = System.currentTimeMillis();
		JobParameters params = new JobParametersBuilder().addString("JobId", String.valueOf(ts))
				.toJobParameters();
		jobLauncher.run(job, params);
		
		return "Transform Completed..!";
		
	}
	
	
	 
    @GetMapping("/load")
    public String load() throws Exception {
    	
    	List<String> command = new ArrayList<String>();
	    
	    command.add("java");
	    command.add("-jar");
	    command.add("C:\\opt\\load.jar");
	    
	    ProcessBuilder builder = new ProcessBuilder(command);		    
	    Process process = builder.start();	
	    
	    
	    StringBuilder sb = new StringBuilder();
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String read;
	    String result = "";
	    while ((read=br.readLine()) != null) {
	        sb.append(read);
	        if (read.contains("DemoApplication  : Started DemoApplication in")) {
	        	Matcher mat = pat.matcher(read);
	        	if ( mat.find()) {
	        		result = mat.group(1);
	        	}
	        }
	    }

	    br.close();

	    
	    
	    //DemoApplication  : Started DemoApplication in 43.043 seconds
	    
	    
	    System.out.println(sb.toString());
	    
        return "Loading is completed in "+result+" seconds.";
    }
	
	

}
