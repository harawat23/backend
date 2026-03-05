package com.example.appspringdata.Controllers;

import java.util.List;
import java.util.Optional;
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
import com.example.appspringdata.Services.ShelfService;
import com.example.appspringdata.Utils.ShelfInput;
import com.example.appspringdata.Utils.ShelfOutput;
import com.example.appspringdata.Exceptions.BadRequestsException;
import com.example.appspringdata.Exceptions.OperationFailedException;
import com.example.appspringdata.Exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/shelf")
public class ShelfController {
    private final ShelfService shelfService;

    @Autowired
    public ShelfController(ShelfService shelfService){
        this.shelfService=shelfService;
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<ShelfOutput> getShelfById(@PathVariable String id){
        if (id==null || id.trim().isEmpty()){
            throw new BadRequestsException("Shelf Id is required");
        }
        
        ShelfOutput shelf=shelfService.getShelfId(id).orElseThrow(()->new ResourceNotFoundException("Shelf not found"));
        return ResponseEntity.ok(shelf);
    }

    @GetMapping("/byShelfName/{shelfname}")
    public ResponseEntity<List<ShelfOutput>> getShelfByName(@PathVariable String shelfname){
        if (shelfname==null || shelfname.trim().isEmpty()){
            throw new BadRequestsException("shelf name is required");
        }

        List<ShelfOutput> shelf=shelfService.getShelByfName(shelfname).orElseThrow(()->new ResourceNotFoundException("Shelf not found"));
        return ResponseEntity.ok(shelf);
    }

    @GetMapping("/byPartNumber/{partnumber}")
    public ResponseEntity<List<ShelfOutput>> getShelfByPartNumber(@PathVariable String partnumber){
        List<ShelfOutput> shelf=shelfService.getShelfbyPartnumber(partnumber).orElseThrow(()->new ResourceNotFoundException("Shelfs not found"));
        return ResponseEntity.ok(shelf);
    }

    @PostMapping("/save")
    public ResponseEntity<ShelfOutput> saveShelf(@RequestBody ShelfInput shelfInput){
        if (shelfInput==null){
            throw new BadRequestsException("shelf input is not valid");
        }

        if (shelfInput.getPartNumber()==null || shelfInput.getShelfName()==null || shelfInput.getPartNumber()=="" || shelfInput.getShelfName()==""){
            throw new BadRequestsException("shelf input is not valid");
        }

        ShelfOutput shelf=shelfService.saveShelf(shelfInput.getPartNumber(), shelfInput.getShelfName()).orElseThrow(()->new OperationFailedException("Shelf not saved"));
        
        return ResponseEntity.ok(shelf);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteShelf(@PathVariable String id){
        if (id==null || id.trim().isEmpty()){
            throw new BadRequestsException(id);
        }

        boolean status=shelfService.deleteShelf(id);

        if (status){
            return ResponseEntity.noContent().build();
        }else{
            throw new ResourceNotFoundException("no shelf found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShelfOutput> updateShelf(@RequestBody ShelfInput shelfInput,@PathVariable String id){
        if (id==null || id.trim().isEmpty()){
            throw new BadRequestsException(id);
        }

        if (shelfInput==null || shelfInput.getPartNumber()==null || shelfInput.getShelfName()==null){
            throw new BadRequestsException("shelf input is not valid");
        }

        ShelfOutput shelf=shelfService.updateShelf(id, shelfInput.getShelfName(), shelfInput.getPartNumber()).orElseThrow(()->new ResourceNotFoundException("failed to udpate the device"));

        return ResponseEntity.ok(shelf);
    }

    @GetMapping("fetchAll/{pageNum}")
    public ResponseEntity<List<ShelfOutput>> getAllShelfPositions(@PathVariable Long pageNum){
        if (pageNum<0L){
            throw new BadRequestsException("page number must be >=0");
        }
        List<ShelfOutput> shelf=shelfService.getAllShelfs(pageNum).orElseThrow(()->new ResourceNotFoundException("No shelves found"));
        
        return ResponseEntity.ok(shelf);
    }

    @GetMapping("/getNumberOfShelves")
    public ResponseEntity<Long> getNumberOfShelves(){
        Long num=shelfService.getNumberOfShelves().orElseThrow(()->new OperationFailedException("Failed to fetch devices"));

        return ResponseEntity.ok(num);
    }
}
