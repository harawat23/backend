package com.example.appspringdata.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appspringdata.Repositories.DeviceRepository;
import com.example.appspringdata.Utils.DeviceOutput;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Optional<DeviceOutput> getDeviceById(String id) {
        return deviceRepository.getDeviceById(id);
    }

    public Optional<List<DeviceOutput>> getDeviceByDevicename(String name) {
        return deviceRepository.getDevicebyDeviceName(name);
    }

    public Optional<List<DeviceOutput>> getDeviceByPartNumber(String partNumber){
        return deviceRepository.getDeviceByPartNumber(partNumber);
    }

    public Optional<List<DeviceOutput>> getDeviceByBuildingName(String buildingName){
        return deviceRepository.getDeviceByBuildingName(buildingName);
    }

    public Optional<List<DeviceOutput>> getDeviceByDeviceType(String deviceType){
        return deviceRepository.getDeviceByDeviceType(deviceType);
    }

    public Optional<DeviceOutput> createDevice(String deviceType, String buildingName,
            String partNumber, String deviceName) {
        return deviceRepository.saveDevice(deviceType, buildingName, partNumber, deviceName);
    }

    public Optional<DeviceOutput> updateDevice(String deviceId, String deviceType, String buildingName,
            String partNumber, String deviceName) {
        return deviceRepository.updateDevice(deviceId, deviceType, buildingName, partNumber,
                deviceName);
    }

    public boolean deleteDevice(String deviceId) {
        return deviceRepository.deleteDevice(deviceId);
    }

    public Optional<List<DeviceOutput>> getAllDevices(Long pageNum){
        return deviceRepository.fetchAllDevices(pageNum);
    }

    public Optional<Long> getNumberOfDevices(){
        return deviceRepository.getNumberOfDevices();
    }
}