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
import com.example.appspringdata.Services.ShelfService;
import com.example.appspringdata.Utils.DeviceInput;
import com.example.appspringdata.Utils.DeviceOutput;
import com.example.appspringdata.Utils.ShelfInput;
import com.example.appspringdata.Utils.ShelfOutput;

@ExtendWith(MockitoExtension.class)
public class ShelfControllerTest {
    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfController shelfController;

    @Test
    void fetchAllShelvesThrowsBadRequestWhenPageNegative() {
        assertThrows(BadRequestsException.class, () -> shelfController.getAllShelfPositions(-1L));
        verifyNoInteractions(shelfService);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "  ", "\t" })
    void getDeviceByIdThrowsBadRequestWhenIdBlank(String shelfId) {
        assertThrows(BadRequestsException.class, () -> shelfController.getShelfById(shelfId));
        verifyNoInteractions(shelfService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenRequiredFieldsMissing() {
        ShelfInput input = null;

        assertThrows(BadRequestsException.class, () -> shelfController.saveShelf(input));
        verifyNoInteractions(shelfService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenPayloadIsNull() {
        assertThrows(BadRequestsException.class, () -> shelfController.saveShelf(null));
        verifyNoInteractions(shelfService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenPartNumberIsBlank() {
        ShelfInput input = new ShelfInput("", "building-a");

        assertThrows(BadRequestsException.class, () -> shelfController.saveShelf(input));
        verifyNoInteractions(shelfService);
    }

    @Test
    void deleteDeviceReturnsNoContentWhenDeleted() {
        when(shelfService.deleteShelf("SH-5001")).thenReturn(true);

        ResponseEntity<Boolean> response = shelfController.deleteShelf("SH-5001");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(shelfService).deleteShelf("SH-5001");
    }

    @Test
    void deleteDeviceThrowsNotFoundWhenDeleteFails() {
        when(shelfService.deleteShelf("SH-5001")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> shelfController.deleteShelf("SH-5001"));
        verify(shelfService).deleteShelf("SH-5001");
    }

    @Test
    void updateDeviceReturnsOkWhenValidPayload() {
        ShelfInput input = new ShelfInput("SPN-220", "building-a");
        ShelfOutput output = new ShelfOutput("SPN-220", "building-a", "SH-5001", ZonedDateTime.now(),
                ZonedDateTime.now());
        when(shelfService.updateShelf("SH-5001", "building-a", "SPN-220"))
                .thenReturn(Optional.of(output));

        ResponseEntity<ShelfOutput> response = shelfController.updateShelf(input, "SH-5001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(output, response.getBody());
        verify(shelfService).updateShelf("SH-5001", "building-a", "SPN-220");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\n" })
    void updateDeviceThrowsBadRequestWhenIdBlank(String id) {
        ShelfInput input = new ShelfInput("", "building-a");

        assertThrows(BadRequestsException.class, () -> shelfController.updateShelf(input, id));
        verifyNoInteractions(shelfService);
    }

    @Test
    void updateDeviceThrowsBadRequestWhenPayloadNull() {
        ShelfInput input = null;
        String id = "SH-5001";
        assertThrows(BadRequestsException.class, () -> shelfController.updateShelf(input, id));
        verifyNoInteractions(shelfService);
    }

    @Test
    void updateDeviceThrowsNotFoundWhenServiceReturnsEmpty() {
        ShelfInput input = new ShelfInput("PN-200", "building-a");
        when(shelfService.updateShelf("SH-5001",input.getShelfName(),input.getPartNumber()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> shelfController.updateShelf(input,"SH-5001"));
        verify(shelfService).updateShelf("SH-5001", "building-a", "PN-200");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t" })
    void deleteDeviceThrowsBadRequestWhenIdBlank(String id) {
        assertThrows(BadRequestsException.class, () -> shelfController.deleteShelf(id));
        verifyNoInteractions(shelfService);
    }
}
