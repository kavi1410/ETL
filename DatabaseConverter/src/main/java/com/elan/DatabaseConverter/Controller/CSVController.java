package com.elan.DatabaseConverter.Controller;

import com.elan.DatabaseConverter.CustomException.FileFormatException;
import com.elan.DatabaseConverter.CustomException.FileNotFoundException;
import com.elan.DatabaseConverter.CustomException.TimeOutException;
import com.elan.DatabaseConverter.Service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("CSV")
public class CSVController {

    @Autowired
    CSVService service;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) throws FileNotFoundException, FileFormatException, TimeOutException {
        if(multipartFile.isEmpty()){
            throw new FileNotFoundException("File Not Found");
        }
        String fileFormat=multipartFile.getContentType();
        if(!fileFormat.equals("text/csv")){
            throw new FileFormatException("Unsupported Format");
        }
        return service.upload(multipartFile);
    }
    @GetMapping("/progress")
    public ResponseEntity getProcess(){
        return service.getProcess();
    }
}
