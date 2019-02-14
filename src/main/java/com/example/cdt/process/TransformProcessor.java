package com.example.cdt.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.item.ItemProcessor;

import com.example.cdt.model.Coesui;

public class TransformProcessor implements ItemProcessor<Coesui,Coesui> {
	
	Pattern pat = Pattern.compile("(.*)\\s(.*)");
	public static String SLASH ="/";
	
	public Coesui process(Coesui coesui) throws Exception
    {
		
        if (coesui.getMetricvalue() == null){

        	return null;
        }
         
        try
        {
        	// metric value * 100
        	double value = Double.valueOf(coesui.getMetricvalue());
        	String newValue = ""+(value*100);
        	coesui.setMetricvalue(newValue);
        	
        	
        	try {
        		Matcher mat = pat.matcher(coesui.getDateTime());
        		if(mat.find()) {
        			String str = mat.group(1);
        			if (str.contains("-")) {
        				String tokens[] = str.split("-");
        				if(tokens.length == 3) {
        					coesui.setDateTime(tokens[1]+SLASH+tokens[2]+SLASH+tokens[0]);
        				}
        			}
        			
        		}
        	}catch(Exception e){}
        	
        	
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid Coesui id : " + coesui.getMetricvalue());
            return null;
        }
        return coesui;
    }
	
	
	public static void main(String[] args) {
		
		String s = "2019-01-20 20:45:00.0";
		Pattern pat = Pattern.compile("(.*)\\s(.*)");
		Matcher mat = pat.matcher(s);
		
		if(mat.find()) {
			String str = mat.group(1);
			if (str.contains("-")) {
				String tokens[] = str.split("-");
				if(tokens.length == 3) {
					System.out.println(tokens[1]+SLASH+tokens[2]+SLASH+tokens[0]);
				}
			}
			System.out.println(mat.group(1));
		}
		
		
		
	}
	

}
