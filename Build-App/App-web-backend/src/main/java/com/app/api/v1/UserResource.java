package com.app.api.v1;

import com.app.annotation.ApiController;
import com.app.common.BaseApi;
import com.app.web.dto.UserRequestDto;
import com.app.web.facade.UserFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@ApiController
@RequestMapping("/user")
public class UserResource extends BaseApi {

    @Resource
    private UserFacade userFacade;

    @GetMapping
    public ResponseEntity index() {
        return new ResponseEntity(userFacade.findAll(), HttpStatus.OK);
    }

    @GetMapping("/ad")
    public ResponseEntity indexa() {
        return new ResponseEntity(userFacade.findAll(), HttpStatus.OK);
    }

    @GetMapping("/add")
    @PreAuthorize("access(#id, T(Permission).CREATE, T(Permission).UPDATE)")
    public String add(@RequestParam String id) {
        return "ok";
    }

    @PostMapping("/d")
    public String get(@RequestBody UserRequestDto userRequestDto) {

        return "a";
    }
}
