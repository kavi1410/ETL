package com.elan.DatabaseConverter.CustomExceptionHandler;

import com.elan.DatabaseConverter.CustomException.FileFormatException;
import com.elan.DatabaseConverter.CustomException.FileNotFoundException;
import com.elan.DatabaseConverter.CustomException.ThreadInterruptedException;
import com.elan.DatabaseConverter.Model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CSVHandler{
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorModel> notFound(FileNotFoundException ex){
        ErrorModel errorModel = ErrorModel.builder()
                .Error(HttpStatus.NOT_FOUND.name())
                .Message(ex.getMsg())
                .Status(HttpStatus.NOT_FOUND.value())
                .TimeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }
    @ExceptionHandler(FileFormatException.class)
    public ResponseEntity<ErrorModel> unSupportFormat(FileFormatException ex){
        ErrorModel errorModel = ErrorModel.builder()
                .Error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name())
                .Status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .Message(ex.getMsg())
                .TimeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorModel);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorModel> maxUploadSize(MaxUploadSizeExceededException ex){
        ErrorModel errorModel = ErrorModel.builder()
                .Error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .Message(ex.getMessage())
                .Status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .TimeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);

    }
    @ExceptionHandler(ThreadInterruptedException.class)
    public ResponseEntity<ErrorModel> interruptedException(ThreadInterruptedException ex){
        ErrorModel errorModel = ErrorModel.builder()
                .Error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .Status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .Message(ex.getMsg())
                .TimeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
    }
}