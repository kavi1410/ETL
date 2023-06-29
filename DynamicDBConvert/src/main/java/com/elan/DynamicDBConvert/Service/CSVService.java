package com.elan.DynamicDBConvert.Service;

import com.elan.DynamicDBConvert.CustomException.FileException;
import com.elan.DynamicDBConvert.DTO.CreatedResponse;
import com.elan.DynamicDBConvert.DTO.RecordsResponse;
import com.elan.DynamicDBConvert.Entity.Records;
import com.elan.DynamicDBConvert.Model.ErrorModel;
import com.elan.DynamicDBConvert.Repository.RecordRepository;
import com.elan.DynamicDBConvert.Utils.PasswordEncoderUtil;
import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVParser;

import org.apache.commons.csv.CSVRecord;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CSVService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    CSVParser csvParser;
    Integer FailureCount;
    Integer TotalCount;
    HashMap<String,String> errorMap;
    @Autowired
    RecordRepository recordRepository;
    Records records = new Records();
    public ResponseEntity upload(MultipartFile multipartFile) throws FileException {
        long startTime = System.currentTimeMillis();
        FailureCount=0;
        TotalCount=0;
        errorMap= new HashMap<>();
        //Assigning the CSV File to the filename variable
        String filename = multipartFile.getOriginalFilename();
        filename = filename.substring(0,filename.length()-4);
        List<String> columnNames;
        List<String> columnValues = new ArrayList<>();
        List<String> ErrorList = new ArrayList<>();
        ExecutorService executorService;
        csvParser = dataReader(multipartFile);
        //fetching the HeaderValues (First Row) From the CSV File
        columnNames = csvParser.getHeaderNames();
        //calling CreateTableDynamic method to Create a Table
        createTableDynamic(filename, columnNames);
        //Fetching the all records from the CSV File
        List<CSVRecord> csvRecords = csvParser.getRecords();
        executorService = Executors.newFixedThreadPool(1500);
        for(CSVRecord csvrecord: csvRecords){
            TotalCount++;
            String Values ="";
            int columnIndex =1;
            // Creating a Thread Pool
            for(String columnName:columnNames) {
                //if the column index greater than 0 and less than all record size then it will enter into the below condition
                if(columnIndex >= 0 && columnIndex <= csvrecord.size()) {
                    //to remove last , for writing a insert query
                    String recordValue=csvrecord.get(columnName).replace("\"","'");
                    if (columnName.equals(columnNames.get(columnNames.size() - 1))) {
                        if(columnName.toLowerCase().equals("password")){
                            //Calling PasswordEncode method to encrypt the column Name since the column contains password
                            Values += "\"" + passwordEncode(recordValue) + "\"";
                        }
                        else{
                            Values += "\"" + recordValue + "\"";
                        }
                    }
                    else {
                        Values += "\"" + recordValue + "\"" + ",";
                    }
                    columnIndex++;
                }
                else{
                    if (columnName.equals(columnNames.get(columnNames.size() - 1))) {
                        Values += "\"" + "\"";
                    }
                    else {
                        Values += "\""+ "\"" + ",";
                    }
                }
            }
            columnValues.add(Values);
            String finalValues = Values;
            String finalFilename = filename;
            Runnable insertTask = () ->{
                //calling InsertDynamic method to insert the values in the DB
                insertDynamic(finalFilename, finalValues,csvrecord,columnNames);
            };
            //Calling execute method to run a Thread
            executorService.execute(insertTask);
        }
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(60, TimeUnit.SECONDS)){
                executorService.shutdownNow();
                ErrorModel errorModel = ErrorModel.builder()
                        .Error(HttpStatus.REQUEST_TIMEOUT.name())
                        .TimeStamp(LocalDateTime.now())
                        .Message("Timeout Error occur while inserting some data")
                        .Status(HttpStatus.REQUEST_TIMEOUT.value())
                        .build();
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorModel);
            }
        } catch (InterruptedException e) {
            ErrorModel errorModel = ErrorModel.builder()
                    .Error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .TimeStamp(LocalDateTime.now())
                    .Message("Thread Interrupted")
                    .Status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);

        }
        long endTime = System.currentTimeMillis();
        //Saving the records in DB
        records = Records.builder()
                .id(records.getId())
                .failureRecords(FailureCount)
                .processedRecords(TotalCount-FailureCount)
                .totalRecords(TotalCount)
                .progressTime(String.valueOf((endTime-startTime)/1000.0) +" s")
                .tableName(filename)
                .build();
        recordRepository.save(records);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CreatedResponse.builder()
                                .FailureCount(FailureCount)
                                .TotalCount(TotalCount)
                                .failureDetails(errorMap)
                                .TimeStamp(LocalDateTime.now())
                                .ProcessTime(String.valueOf((endTime-startTime)/1000.0) +" s")
                                .build());
    }
    private CSVParser dataReader(MultipartFile multipartFile) throws FileException {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            return csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withHeader());
        }
        catch (IOException e) {
            throw new FileException("Exception Occur While Reading the file");
        }
    }
    private void insertDynamic(String filename, String Values,CSVRecord csvrecord,List columnNames) {
        try {
            jdbcTemplate.execute("INSERT INTO " + filename + " VALUES (" + Values + ")");
        }
        catch (RuntimeException ex){
            String s="";
            FailureCount++;
            s = ex.getCause().toString();
            int start = s.indexOf(":");
            errorMap.put(csvrecord.get((String) columnNames.get(0)),s.substring(start+1));
        }
    }
    private void createTableDynamic(String filename, List<String> columnNames) {
        List<String> modifiedColumnNames= new ArrayList<>();
        for(String header: columnNames){
            modifiedColumnNames.add(header.replace(" ","_"));
        }
        String column="";
        for(int i=0;i<modifiedColumnNames.size();i++) {
            if (i == modifiedColumnNames.size() - 1) {
                column+=modifiedColumnNames.get(i)+" VARCHAR(255)";
            }
            else if (i==0) {
                column += modifiedColumnNames.get(i) + " VARCHAR(255) PRIMARY KEY, ";
            }
            else{
                column += modifiedColumnNames.get(i) + " VARCHAR(255) , ";
            }
        }
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS "+filename+"( "+column+")");
    }

    private String passwordEncode(String columnName) {
        return PasswordEncoderUtil.encryptPassword(columnName);
    }

    public ResponseEntity getProcess() {
        List<Records> progressRecords = recordRepository.findAll();
        RecordsResponse recordsResponse= new RecordsResponse();
        for(Records progressRecord:progressRecords ){
            recordsResponse.setFailureRecords(progressRecord.getFailureRecords());
            recordsResponse.setProcessedRecords(progressRecord.getProcessedRecords());
            recordsResponse.setTotalRecords(progressRecord.getTotalRecords());
            recordsResponse.setStatus(HttpStatus.OK.value());
            recordsResponse.setProgressTime(progressRecord.getProgressTime());
            recordsResponse.setTableName(progressRecord.getTableName());
            recordsResponse.setTimeStamp(LocalDateTime.now());
        }
        return ResponseEntity.status(HttpStatus.OK).body(recordsResponse);
    }
}
