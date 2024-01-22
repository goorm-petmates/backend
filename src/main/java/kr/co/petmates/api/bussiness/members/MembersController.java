package kr.co.petmates.api.bussiness.members;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MembersController {
    @GetMapping("/members/test")
//    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> memberResponse() {
        return ResponseEntity.ok("{test : user}");
    }
}
