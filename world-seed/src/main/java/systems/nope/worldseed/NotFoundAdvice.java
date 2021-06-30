package systems.nope.worldseed;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import systems.nope.worldseed.exception.*;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AlreadyExistingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String alreadyExistsHandler(AlreadyExistingException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ImpossibleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String impossible(ImpossibleException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DataMissmatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String missmatch(DataMissmatchException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(FilesystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String filesystemHandler(FilesystemException e) {
        return "An error occured in the filesystem.";
    }


}
