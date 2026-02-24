package com.example.appspringdata.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appspringdata.Repositories.ShelfPositionRepository;
import com.example.appspringdata.Utils.ShelfOutput;
import com.example.appspringdata.Utils.ShelfPositionOutput;

@Service
public class ShelfPositionService {
    private final ShelfPositionRepository shelfPositionRepository;

    @Autowired
    public ShelfPositionService(ShelfPositionRepository shelfPositionRepository){
        this.shelfPositionRepository=shelfPositionRepository;
    }

    public Optional<ShelfPositionOutput> getShelfPositionById(String shelfPositionId){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionRepository.getShelfPositionById(shelfPositionId);

        return shelfPositionOutput;
    }

    public Optional<ShelfPositionOutput> saveShelfPosition(String deviceId){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionRepository.createShelfPosition(deviceId);

        return shelfPositionOutput;
    }

    public boolean deleteShelfPosition(String shelfPositionId){
        boolean del=shelfPositionRepository.deleteShelfPosition(shelfPositionId);

        return del;
    }

    public Optional<ShelfPositionOutput> attachShelfPosition(String shelfPositionId,String shelfId){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionRepository.attachShelf(shelfPositionId, shelfId);

        return shelfPositionOutput;
    }

    public Optional<ShelfPositionOutput> deteachShelfPosition(String shelfPositionId,String shelfId){
        Optional<ShelfPositionOutput> shelfPositionOutput=shelfPositionRepository.detachShelf(shelfPositionId, shelfId);

        return shelfPositionOutput;
    }

    public Optional<List<ShelfPositionOutput>> getAllShelfPositions(Long pageNum){
        return shelfPositionRepository.getAllShelfPositions(pageNum);
    }
}
