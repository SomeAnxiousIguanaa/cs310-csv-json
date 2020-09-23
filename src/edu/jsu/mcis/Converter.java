package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class Converter {
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            JSONArray colHeaders = new JSONArray();
            JSONObject object = new JSONObject();
            String[] line = iterator.next();
            for(String i : line){
            colHeaders.add(i);
            }
            JSONArray ids = new JSONArray();
            JSONArray data = new JSONArray();
            
            while(iterator.hasNext()){
                JSONArray dataLine = new JSONArray();
                line = iterator.next();
                ids.add(line[0]);
                for(int i = 1; i < line.length; i++){
                    dataLine.add(Integer.parseInt(line[i]));
                }
                data.add(dataLine);
            }
            object.put("rowHeaders", ids);
            object.put("data", data);
            object.put("colHeaders", colHeaders);
            results = object.toJSONString();    
        }        
        catch(Exception e) { return e.toString(); }
        return results.trim();
    }
    
    public static String jsonToCsv(String jsonString) {    
        String results = " ";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            // INSERT YOUR CODE HERE
			
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            JSONArray msg1 = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray msg2 = (JSONArray) jsonObject.get("data");
            JSONArray msg3 = (JSONArray) jsonObject.get("colHeaders");
            
            Iterator<String> id = msg1.iterator();
            Iterator<JSONArray> data = msg2.iterator();
            Iterator<String> colHeaders = msg3.iterator();
            
            ArrayList<String> allID = new ArrayList<String>();
            ArrayList<JSONArray> allData = new ArrayList<JSONArray>();
            ArrayList<String> allCols = new ArrayList<String>();
            
            while(id.hasNext())
            {
                allID.add(id.next());
            }
            while(data.hasNext())
            {
                allData.add(data.next());
            }
            while(colHeaders.hasNext())
            {
                allCols.add(colHeaders.next());
            }
            
            String[] cols = allCols.toArray(new String[0]);
            csvWriter.writeNext(cols);
            ArrayList<String[]> datas = new ArrayList<String[]>();
            for (int i=0;i<allData.size();i++)
            { 
                String[] dat = allData.get(i).toString().split(",");
                dat[0] = dat[0].split("\[")[1];
                dat[dat.length-1] = dat[dat.length-1].split("]")[0];
                datas.add(dat);
            }
            for(int i = 0; i < datas.size(); i++)
            {
                String[] line = new String[datas.get(0).length+1];
                String[] dat = datas.get(i);
                line[0] = allID.get(i);
                for(int j = 1; j < dat.length+1;j++)
                {
                    line[j] = dat[j-1];
                }
                csvWriter.writeNext(line);
            }
            results = writer.toString();      
        }
        catch(Exception e) { return e.toString(); }
        return results.trim();
    } 
}