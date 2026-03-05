package com.example.appspringdata.Controllers;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import com.example.appspringdata.Exceptions.BadRequestsException;
import com.example.appspringdata.Exceptions.OperationFailedException;
import com.example.appspringdata.Exceptions.ResourceNotFoundException;
import com.example.appspringdata.Services.DeviceService;
import com.example.appspringdata.Utils.DeviceInput;
import com.example.appspringdata.Utils.DeviceOutput;
 
@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {
 
    @Mock
    private DeviceService deviceService;
 
    @InjectMocks
    private DeviceController deviceController;
 
    @Test
    void fetchAllDevicesReturnsOkWithBody() {
        DeviceOutput output = new DeviceOutput("dev-1", "sensor", "building-a", ZonedDateTime.now(), "pn-10", "temp",
                ZonedDateTime.now());
        when(deviceService.getAllDevices(0L)).thenReturn(Optional.of(List.of(output)));
 
        ResponseEntity<List<DeviceOutput>> response = deviceController.fetchAllDevices(0L);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(deviceService).getAllDevices(0L);
    }
 
    @Test
    void fetchAllDevicesThrowsBadRequestWhenPageNegative() {
        assertThrows(BadRequestsException.class, () -> deviceController.fetchAllDevices(-1L));
        verifyNoInteractions(deviceService);
    }
 
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "  ", "\t" })
    void getDeviceByIdThrowsBadRequestWhenIdBlank(String deviceId) {
        assertThrows(BadRequestsException.class, () -> deviceController.getDeviceById(deviceId));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void getDeviceByIdThrowsNotFoundWhenServiceReturnsEmpty() {
        when(deviceService.getDeviceById("missing-id")).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> deviceController.getDeviceById("missing-id"));
        verify(deviceService).getDeviceById("missing-id");
    }
 
    @Test
    void saveDeviceReturnsOkWhenValidPayload() {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "temp");
        DeviceOutput output = new DeviceOutput("dev-1", "sensor", "building-a", ZonedDateTime.now(), "pn-10", "temp",
                ZonedDateTime.now());
        when(deviceService.createDevice("sensor", "building-a", "pn-10", "temp")).thenReturn(Optional.of(output));
 
        ResponseEntity<DeviceOutput> response = deviceController.saveDevice(input);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(output, response.getBody());
        verify(deviceService).createDevice("sensor", "building-a", "pn-10", "temp");
    }
 
    @Test
    void saveDeviceThrowsBadRequestWhenRequiredFieldsMissing() {
        DeviceInput input = new DeviceInput(null, "building-a", "pn-10", "temp");
 
        assertThrows(BadRequestsException.class, () -> deviceController.saveDevice(input));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void saveDeviceThrowsBadRequestWhenPayloadIsNull() {
        assertThrows(BadRequestsException.class, () -> deviceController.saveDevice(null));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void saveDeviceThrowsBadRequestWhenPartNumberIsBlank() {
        DeviceInput input = new DeviceInput("sensor", "building-a", " ", "temp");
 
        assertThrows(BadRequestsException.class, () -> deviceController.saveDevice(input));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void saveDeviceThrowsBadRequestWhenDeviceNameIsBlank() {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "\t");
 
        assertThrows(BadRequestsException.class, () -> deviceController.saveDevice(input));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void saveDeviceThrowsOperationFailedWhenServiceReturnsEmpty() {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "temp");
        when(deviceService.createDevice(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
 
        assertThrows(OperationFailedException.class, () -> deviceController.saveDevice(input));
        verify(deviceService).createDevice("sensor", "building-a", "pn-10", "temp");
    }
 
    @Test
    void deleteDeviceReturnsNoContentWhenDeleted() {
        when(deviceService.deleteDevice("dev-1")).thenReturn(true);
 
        ResponseEntity<Void> response = deviceController.deleteDevice("dev-1");
 
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deviceService).deleteDevice("dev-1");
    }
 
    @Test
    void deleteDeviceThrowsNotFoundWhenDeleteFails() {
        when(deviceService.deleteDevice("dev-1")).thenReturn(false);
 
        assertThrows(ResourceNotFoundException.class, () -> deviceController.deleteDevice("dev-1"));
        verify(deviceService).deleteDevice("dev-1");
    }
 
    @Test
    void updateDeviceReturnsOkWhenValidPayload() {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "temp");
        DeviceOutput output = new DeviceOutput("dev-1", "sensor", "building-a", ZonedDateTime.now(), "pn-10", "temp",
                ZonedDateTime.now());
        when(deviceService.updateDevice("dev-1", "sensor", "building-a", "pn-10", "temp"))
                .thenReturn(Optional.of(output));
 
        ResponseEntity<DeviceOutput> response = deviceController.updateDevice("dev-1", input);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(output, response.getBody());
        verify(deviceService).updateDevice("dev-1", "sensor", "building-a", "pn-10", "temp");
    }
 
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\n" })
    void updateDeviceThrowsBadRequestWhenIdBlank(String id) {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "temp");
 
        assertThrows(BadRequestsException.class, () -> deviceController.updateDevice(id, input));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void updateDeviceThrowsBadRequestWhenPayloadNull() {
        assertThrows(BadRequestsException.class, () -> deviceController.updateDevice("dev-1", null));
        verifyNoInteractions(deviceService);
    }
 
    @Test
    void updateDeviceThrowsNotFoundWhenServiceReturnsEmpty() {
        DeviceInput input = new DeviceInput("sensor", "building-a", "pn-10", "temp");
        when(deviceService.updateDevice("missing-id", "sensor", "building-a", "pn-10", "temp"))
                .thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> deviceController.updateDevice("missing-id", input));
        verify(deviceService).updateDevice("missing-id", "sensor", "building-a", "pn-10", "temp");
    }
 
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t" })
    void deleteDeviceThrowsBadRequestWhenIdBlank(String id) {
        assertThrows(BadRequestsException.class, () -> deviceController.deleteDevice(id));
        verifyNoInteractions(deviceService);
    }
}