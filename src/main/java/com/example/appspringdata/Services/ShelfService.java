package com.example.appspringdata.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.appspringdata.Entity.Shelf;
import com.example.appspringdata.Repositories.ShelfRepository;
import com.example.appspringdata.Utils.ShelfOutput;

@Service
public class ShelfService {
    private final ShelfRepository shelfRepository;

    @Autowired
    public ShelfService(ShelfRepository shelfRepository){
        this.shelfRepository=shelfRepository;
    }

    public Optional<ShelfOutput> getShelfId(String shelfId){
        return shelfRepository.getShelfById(shelfId);
    }

    public Optional<ShelfOutput> saveShelf(String partNumber,String shelfName){
        return shelfRepository.saveShelf(partNumber, shelfName);
    }

    public boolean deleteShelf(String shelfId){
        return shelfRepository.deleteShelf(shelfId);
    }

    public Optional<ShelfOutput> updateShelf(String shelfId,String shelfName,String partNumber){
        return shelfRepository.updateShelf(shelfId, shelfName, partNumber);
    }

    public Optional<List<ShelfOutput>> getAllShelfs(Long pageNum){
        return shelfRepository.getAllShelfPositions(pageNum);
    }
}
