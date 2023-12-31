package com.elan.DynamicDBConvert.Controller;

import com.elan.DynamicDBConvert.CustomException.FileException;
import com.elan.DynamicDBConvert.CustomException.FileNotFoundException;
import com.elan.DynamicDBConvert.Service.CSVService;
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
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) throws FileNotFoundException, FileException{
        if(multipartFile.isEmpty()){
            throw new FileNotFoundException("File Not Found");
        }
        String fileFormat=multipartFile.getContentType();
        if(!fileFormat.equals("text/csv")){
            throw new FileException("Unsupported Format");
        }
        return service.upload(multipartFile);
    }
    @GetMapping("/progress")
    public ResponseEntity getProcess(){
        return service.getProcess();
    }
}
