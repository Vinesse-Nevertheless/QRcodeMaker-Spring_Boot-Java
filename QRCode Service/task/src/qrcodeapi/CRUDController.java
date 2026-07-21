package qrcodeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CRUDController {

    @GetMapping("/api/health")
    public ResponseEntity<String> getHealth(String health){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<String> getQrCode(String qrcode){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }
}
