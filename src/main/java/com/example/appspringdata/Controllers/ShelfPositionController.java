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
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionService.getShelfPositionById(id);

        return shelfPositionOutput.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<ShelfPositionOutput> saveShelfPosition(@RequestBody ShelfPositionInput shelfPositionInput){
        System.out.println(shelfPositionInput.getDeviceId());
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionService.saveShelfPosition(shelfPositionInput.getDeviceId());

        return shelfPositionOutput.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteShelfPosition(@PathVariable String id){
        boolean flag=shelfPositionService.deleteShelfPosition(id);

        if (flag){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        } 
    }

    @PutMapping("attachshelf/{shelfpositionid}/{shelfid}")
    public ResponseEntity<ShelfPositionOutput> attachShelf(@PathVariable String shelfpositionid,@PathVariable String shelfid){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionService.attachShelfPosition(shelfpositionid, shelfid);

        return shelfPositionOutput.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("detachshelf/{shelfpositionid}/{shelfid}")
    public ResponseEntity<ShelfPositionOutput> detachShelf(@PathVariable String shelfpositionid,@PathVariable String shelfid){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionService.deteachShelfPosition(shelfpositionid, shelfid);

        return shelfPositionOutput.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("fetchAll/{pageNum}")
    public ResponseEntity<List<ShelfPositionOutput>> getAllShelfPositions(@PathVariable Long pageNum){
        Optional<List<ShelfPositionOutput>> shelfPositionOutput=shelfPositionService.getAllShelfPositions(pageNum);

        return shelfPositionOutput.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}