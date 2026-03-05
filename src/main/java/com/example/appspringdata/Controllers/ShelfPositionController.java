package com.example.appspringdata.Controllers;

import java.util.List;
import java.util.Optional;
import com.example.appspringdata.Exceptions.BadRequestsException;
import com.example.appspringdata.Exceptions.OperationFailedException;
import com.example.appspringdata.Exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.appspringdata.Services.ShelfPositionService;
import com.example.appspringdata.Utils.ShelfPositionInput;
import com.example.appspringdata.Utils.ShelfPositionOutput;

@RestController
@RequestMapping("/shelfposition")
public class ShelfPositionController {
    private final ShelfPositionService shelfPositionService;

    @Autowired
    public ShelfPositionController(ShelfPositionService shelfPositionService){
        this.shelfPositionService=shelfPositionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfPositionOutput> getShelfPositionById(@PathVariable String id){
        if (id==null || id.trim().isEmpty()){
          throw new BadRequestsException("id is invalid");  
        }
        ShelfPositionOutput shelfPositionOutput=shelfPositionService.getShelfPositionById(id).orElseThrow(()->new ResourceNotFoundException("unable to find shelf position with id : "+id));

        return ResponseEntity.ok(shelfPositionOutput);
    }

    @PostMapping("/save")
    public ResponseEntity<ShelfPositionOutput> saveShelfPosition(@RequestBody ShelfPositionInput shelfPositionInput){
        // System.out.println(shelfPositionInput.getDeviceId());
        if (shelfPositionInput==null || shelfPositionInput.getDeviceId()==null || shelfPositionInput.getDeviceId().trim().isEmpty()){
            throw new BadRequestsException("invalid input");
        }
        ShelfPositionOutput shelfPositionOutput=shelfPositionService.saveShelfPosition(shelfPositionInput.getDeviceId()).orElseThrow(()->new ResourceNotFoundException("unable to find the shelfPosition"));

        return ResponseEntity.ok(shelfPositionOutput);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteShelfPosition(@PathVariable String id){
        if (id==null || id.trim().isEmpty()){
            throw new BadRequestsException("invalid input");
        }
        boolean flag=shelfPositionService.deleteShelfPosition(id);

        if (flag){
            return ResponseEntity.noContent().build();
        }else{
            throw new ResourceNotFoundException("shelf position not found");
        } 
    }

    @PutMapping("attachshelf/{shelfpositionid}/{shelfid}")
    public ResponseEntity<ShelfPositionOutput> attachShelf(@PathVariable String shelfpositionid,@PathVariable String shelfid){
        if (shelfpositionid==null || shelfpositionid.trim().isEmpty() || shelfid==null || shelfid.trim().isEmpty()){
            throw new BadRequestsException("invalid input");
        }

        ShelfPositionOutput shelfPositionOutput=shelfPositionService.attachShelfPosition(shelfpositionid, shelfid).orElseThrow(()->new ResourceNotFoundException("unable to find shelf position or shelf"));

        return ResponseEntity.ok(shelfPositionOutput);
    }

    @PutMapping("detachshelf/{shelfpositionid}/{shelfid}")
    public ResponseEntity<ShelfPositionOutput> detachShelf(@PathVariable String shelfpositionid,@PathVariable String shelfid){
        if (shelfpositionid==null || shelfpositionid.trim().isEmpty() || shelfid==null || shelfid.trim().isEmpty()){
            throw new BadRequestsException("invalid input");
        }

        ShelfPositionOutput shelfPositionOutput=shelfPositionService.deteachShelfPosition(shelfpositionid, shelfid).orElseThrow(()->new ResourceNotFoundException("unable to find shelf position or shelf"));
    
        return ResponseEntity.ok(shelfPositionOutput);
    }

    @GetMapping("fetchAll/{pageNum}")
    public ResponseEntity<List<ShelfPositionOutput>> getAllShelfPositions(@PathVariable Long pageNum){
        if (pageNum<0){
            throw new BadRequestsException("page number must be >=0");
        }

        List<ShelfPositionOutput> shelfPositionOutput=shelfPositionService.getAllShelfPositions(pageNum).orElseThrow(()->new OperationFailedException("Unable to fecth devices"));

        return ResponseEntity.ok(shelfPositionOutput);
    }
}