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

@RestController
@RequestMapping("/shelf")
public class ShelfController {
    private final ShelfService shelfService;

    @Autowired
    public ShelfController(ShelfService shelfService){
        this.shelfService=shelfService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfOutput> getShelfById(@PathVariable String id){
        Optional<ShelfOutput> shelf=shelfService.getShelfId(id);

        return shelf.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<ShelfOutput> saveShelf(@RequestBody ShelfInput shelfInput){
        Optional<ShelfOutput> shelf=shelfService.saveShelf(shelfInput.getPartNumber(), shelfInput.getShelfName());
        
        return shelf.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteShelf(@PathVariable String id){
        boolean status=shelfService.deleteShelf(id);

        if (status){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShelfOutput> updateShelf(@RequestBody ShelfInput shelfInput,@PathVariable String id){
        Optional<ShelfOutput> shelf=shelfService.updateShelf(id, shelfInput.getShelfName(), shelfInput.getPartNumber());

        return shelf.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("fetchAll/{pageNum}")
    public ResponseEntity<List<ShelfOutput>> getAllShelfPositions(@PathVariable Long pageNum){
        Optional<List<ShelfOutput>> shelf=shelfService.getAllShelfs(pageNum);

        return shelf.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
