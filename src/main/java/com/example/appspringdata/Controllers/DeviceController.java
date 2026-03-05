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
    public ResponseEntity<List<DeviceOutput>> fetchAllDevices(@PathVariable Long pageNum) {
        if (pageNum < 0L) {
            throw new BadRequestsException("page number must be > 0");
        }
        List<DeviceOutput> device = deviceService.getAllDevices(pageNum)
                .orElseThrow(() -> new ResourceNotFoundException("no device found"));

        return ResponseEntity.ok(device);

    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<DeviceOutput> getDeviceById(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestsException("Device Id is required");
        }
        DeviceOutput device = deviceService.getDeviceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not found with id : " + id));

        return ResponseEntity.ok(device);
    }

    @GetMapping("/byDeviceName/{name}")
    public ResponseEntity<List<DeviceOutput>> getDevicebyDeviceName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestsException("Device name is required");
        }

        List<DeviceOutput> device = deviceService.getDeviceByDevicename(name)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not found with name : " + name));

        return ResponseEntity.ok(device);
    }

    @GetMapping("/byPartNumber/{partnumber}")
    public ResponseEntity<List<DeviceOutput>> getPartNumber(@PathVariable String partnumber) {
        if (partnumber == null || partnumber.trim().isEmpty()) {
            throw new BadRequestsException("Device name is required");
        }

        List<DeviceOutput> device = deviceService.getDeviceByPartNumber(partnumber)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not found with partnumber : " + partnumber));

        return ResponseEntity.ok(device);
    }

    @GetMapping("/byBuildingName/{buildingName}")
    public ResponseEntity<List<DeviceOutput>> getBuildingName(@PathVariable String buildingName) {
        if (buildingName == null || buildingName.trim().isEmpty()) {
            throw new BadRequestsException("Device name is required");
        }

        List<DeviceOutput> device = deviceService.getDeviceByBuildingName(buildingName)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not found with building name : " + buildingName));

        return ResponseEntity.ok(device);
    }

    @GetMapping("byDeviceType/{deviceType}")
    public ResponseEntity<List<DeviceOutput>> getDeviceType(@PathVariable String deviceType) {
        if (deviceType == null || deviceType.trim().isEmpty()) {
            throw new BadRequestsException("Device name is required");
        }

        List<DeviceOutput> device = deviceService.getDeviceByDeviceType(deviceType)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not found with device type : " + deviceType));

        return ResponseEntity.ok(device);
    }

    @PostMapping("/save")
    public ResponseEntity<DeviceOutput> saveDevice(@RequestBody DeviceInput deviceInput) {
        if (deviceInput == null) {
            throw new BadRequestsException("device input is null");
        }

        if (deviceInput.getBuildingName() == null || deviceInput.getBuildingName() == "\t"
                || deviceInput.getBuildingName() == "\n" || deviceInput.getBuildingName().trim().isEmpty()
                || deviceInput.getDeviceName() == null || deviceInput.getDeviceName() == "\t"
                || deviceInput.getDeviceName() == "\n" || deviceInput.getDeviceName().trim().isEmpty()
                || deviceInput.getDeviceType() == null || deviceInput.getDeviceType() == "\t"
                || deviceInput.getDeviceType() == "\n" || deviceInput.getDeviceType().trim().isEmpty()
                || deviceInput.getPartNumber() == null || deviceInput.getPartNumber() == "\t"
                || deviceInput.getPartNumber() == "\n" || deviceInput.getPartNumber().trim().isEmpty()) {
            throw new BadRequestsException("fields are invalid");
        }
        System.out.println("Received DeviceInput: " + deviceInput);
        System.out.println("Building Name: " + deviceInput.getBuildingName());
        System.out.println(deviceInput.getBuildingName());
        DeviceOutput device = deviceService.createDevice(deviceInput.getDeviceType(),
                deviceInput.getBuildingName(), deviceInput.getPartNumber(), deviceInput.getDeviceName())
                .orElseThrow(() -> new OperationFailedException("Failed to save device"));

        return ResponseEntity.ok(device);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceOutput> updateDevice(@PathVariable String id, @RequestBody DeviceInput deviceInput) {
        if (deviceInput == null) {
            throw new BadRequestsException("device input is null");
        }

        if (deviceInput.getBuildingName() == null || deviceInput.getBuildingName() == "\t"
                || deviceInput.getBuildingName() == "\n" || deviceInput.getBuildingName().trim().isEmpty()
                || deviceInput.getDeviceName() == null || deviceInput.getDeviceName() == "\t"
                || deviceInput.getDeviceName() == "\n" || deviceInput.getDeviceName().trim().isEmpty()
                || deviceInput.getDeviceType() == null || deviceInput.getDeviceType() == "\t"
                || deviceInput.getDeviceType() == "\n" || deviceInput.getDeviceType().trim().isEmpty()
                || deviceInput.getPartNumber() == null || deviceInput.getPartNumber() == "\t"
                || deviceInput.getPartNumber() == "\n" || deviceInput.getPartNumber().trim().isEmpty()) {
            throw new BadRequestsException("fields are invalid");
        }

        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestsException("id is invalid");
        }

        DeviceOutput updatedDevice = deviceService.updateDevice(
                id,
                deviceInput.getDeviceType(),
                deviceInput.getBuildingName(),
                deviceInput.getPartNumber(),
                deviceInput.getDeviceName())
                .orElseThrow(() -> new ResourceNotFoundException("device not found with id " + id));

        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        System.out.println(id);

        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestsException("id is invalid");
        }

        boolean deleted = deviceService.deleteDevice(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("device not found with id " + id);
        }
    }

    @GetMapping("/getNumberOfDevices")
    public ResponseEntity<Long> getNumberOfDevices() {
        Optional<Long> num = deviceService.getNumberOfDevices();
        return num.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}