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
import com.example.appspringdata.Utils.DeviceInput;
import com.example.appspringdata.Utils.DeviceOutput;
import com.example.appspringdata.Services.DeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    
    @GetMapping("fetchAll/{pageNum}")
    public ResponseEntity<List<DeviceOutput>> fetchAllDevices(@PathVariable Long pageNum){
        Optional<List<DeviceOutput>> device = deviceService.getAllDevices(pageNum);

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<DeviceOutput> getDeviceById(@PathVariable String id) {
        Optional<DeviceOutput> device = deviceService.getDeviceById(id);

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byDeviceName/{name}")
    public ResponseEntity<List<DeviceOutput>> getDevicebyDeviceName(@PathVariable String name) {
        Optional<List<DeviceOutput>> device = deviceService.getDeviceByDevicename(name);

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byPartNumber/{partnumber}")
    public ResponseEntity<List<DeviceOutput>> getPartNumber(@PathVariable String partnumber) {
        Optional<List<DeviceOutput>> device = deviceService.getDeviceByPartNumber(partnumber);

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byBuildingName/{buildingName}")
    public ResponseEntity<List<DeviceOutput>> getBuildingName(@PathVariable String buildingName){
        Optional<List<DeviceOutput>> device=deviceService.getDeviceByBuildingName(buildingName);
        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("byDeviceType/{deviceType}")
    public ResponseEntity<List<DeviceOutput>> getDeviceType(@PathVariable String deviceType){
        Optional<List<DeviceOutput>> device=deviceService.getDeviceByDeviceType(deviceType);

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<DeviceOutput> saveDevice(@RequestBody DeviceInput deviceInput) {
        System.out.println("Received DeviceInput: " + deviceInput);
        System.out.println("Building Name: " + deviceInput.getBuildingName());
        System.out.println(deviceInput.getBuildingName());
        Optional<DeviceOutput> device = deviceService.createDevice(deviceInput.getDeviceType(),
                deviceInput.getBuildingName(), deviceInput.getPartNumber(), deviceInput.getDeviceName());

        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceOutput> updateDevice(@PathVariable String id, @RequestBody DeviceInput deviceInput) {
        Optional<DeviceOutput> updatedDevice = deviceService.updateDevice(
                id,
                deviceInput.getDeviceType(),
                deviceInput.getBuildingName(),
                deviceInput.getPartNumber(),
                deviceInput.getDeviceName());

        return updatedDevice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        System.out.println(id);
        boolean deleted = deviceService.deleteDevice(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getNumberOfDevices")
    public ResponseEntity<Long> getNumberOfDevices(){
        Optional<Long> num=deviceService.getNumberOfDevices();
        return num.map(ResponseEntity::ok)
            .orElseGet(()->ResponseEntity.notFound().build());
    }
}