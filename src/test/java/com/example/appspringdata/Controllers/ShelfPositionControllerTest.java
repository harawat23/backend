package com.example.appspringdata.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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
import com.example.appspringdata.Services.ShelfPositionService;
import com.example.appspringdata.Utils.DeviceInput;
import com.example.appspringdata.Utils.DeviceOutput;
import com.example.appspringdata.Utils.ShelfInput;
import com.example.appspringdata.Utils.ShelfOutput;
import com.example.appspringdata.Utils.ShelfPositionInput;
import com.example.appspringdata.Utils.ShelfPositionOutput;

@ExtendWith(MockitoExtension.class)
public class ShelfPositionControllerTest {
    @Mock
    private ShelfPositionService shelfPositionService;

    @InjectMocks
    private ShelfPositionController shelfPositionController;

    @Test
    void fetchAllShelvesThrowsBadRequestWhenPageNegative() {
        assertThrows(BadRequestsException.class, () -> shelfPositionController.getAllShelfPositions(-1L));
        verifyNoInteractions(shelfPositionService);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "  ", "\t" })
    void getDeviceByIdThrowsBadRequestWhenIdBlank(String shelfId) {
        assertThrows(BadRequestsException.class, () -> shelfPositionController.getShelfPositionById(shelfId));
        verifyNoInteractions(shelfPositionService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenRequiredFieldsMissing() {
        ShelfPositionInput input = new ShelfPositionInput(null);

        assertThrows(BadRequestsException.class, () -> shelfPositionController.saveShelfPosition(input));
        verifyNoInteractions(shelfPositionService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenPayloadIsNull() {
        assertThrows(BadRequestsException.class, () -> shelfPositionController.saveShelfPosition(null));
        verifyNoInteractions(shelfPositionService);
    }

    @Test
    void saveShelfThrowsBadRequestWhenPartNumberIsBlank() {
        ShelfPositionInput input = new ShelfPositionInput("");

        assertThrows(BadRequestsException.class, () -> shelfPositionController.saveShelfPosition(input));
        verifyNoInteractions(shelfPositionService);
    }

    @Test
    void deleteDeviceReturnsNoContentWhenDeleted() {
        when(shelfPositionService.deleteShelfPosition("SP-1001-1")).thenReturn(true);

        ResponseEntity<Boolean> response = shelfPositionController.deleteShelfPosition("SP-1001-1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(shelfPositionService).deleteShelfPosition("SP-1001-1");
    }

    @Test
    void deleteDeviceThrowsNotFoundWhenDeleteFails() {
        when(shelfPositionService.deleteShelfPosition("SP-1001-1")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> shelfPositionController.deleteShelfPosition("SP-1001-1"));
        verify(shelfPositionService).deleteShelfPosition("SP-1001-1");
    }
}
