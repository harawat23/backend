Project Overview


Key Components of Backend


1. Service Layer

The Service Layer is the backbone of the application, containing the business logic for interacting with repositories and handling complex operations. It ensures that the application adheres to the principles of separation of concerns and provides reusable methods for controllers to interact with the database.


DeviceService


File: DeviceService.java

Responsibilities:

Handles CRUD operations for devices.

Fetches devices based on various attributes (e.g., deviceName, partNumber, buildingName, deviceType).

Supports pagination for retrieving large datasets.

Creates and updates devices with validation logic.

Deletes devices and their associated relationships.

Returns total number of devices in database.


ShelfPositionService


File: ShelfPositionService.java

Responsibilities:

Handles attachment and detachment of shelves to/from shelf positions.

Retrieves shelf positions with nested relationships.

Supports pagination for large datasets.

Creates and Deletes shelf positions of a device.


ShelfService


File: ShelfService.java

Responsibilities:

Handles CRUD operations for shelves.

Fetches shelves based on attributes (e.g., shelfName, partNumber).

Provides pagination support for retrieving shelves.


2. Controller Layer

The Controller Layer exposes REST APIs for external interaction, serving as the entry point for client requests. It validates input, interacts with services, and returns appropriate responses.


DeviceController


File: DeviceController.java

Responsibilities:

Provides endpoints for CRUD operations on devices.

Fetches devices by attributes such as deviceName, partNumber, buildingName, and deviceType.

Supports pagination for fetching device lists.



Key Endpoints:

GET /device/byId/{id} – Retrieves a device by ID.

GET /device/byDeviceName/{name} – Fetches devices by name.

GET /device/partNumber/{partnumber} - Fetches devices by part number.

GET /device/byBuildingName/{buildingName} - Fetches devices by building name.

GET /device/byDeviceType/{deviceType} - Fetches devices by device type.

POST /device/save – Creates a new device.

PUT /device/{id} – Updates an existing device.

DELETE /device/{id} – Soft Deletes a device.

GET /device/fetchAll/{pageNum} – Fetches all devices with pagination.




ShelfPositionController


File: ShelfPositionController.java

Responsibilities:

Provides endpoints for CRUD operations on shelf positions.

Manages attachment and detachment of shelves to/from shelf positions.

Supports pagination for fetching shelf position lists.



Key Endpoints:

GET /shelfposition/{id} – Retrieves a shelf position by ID.

POST /shelfposition/save – Creates a new shelf position.

PUT /shelfposition/attachshelf/{shelfpositionid}/{shelfid} – Attaches a shelf to a shelf position.

PUT /shelfposition/detachshelf/{shelfpositionid}/{shelfid} – Detaches a shelf from a shelf position.

DELETE /shelfposition/delete/{id} - Soft Deletes shelf and it relation

GET /shelfposition/fetchAll/{pageNum} – Fetches all shelf positions with pagination.




ShelfController


File: ShelfController.java

Responsibilities:

Provides endpoints for CRUD operations on shelves.

Fetches shelves by attributes such as shelfName and partNumber.

Supports pagination for fetching shelf lists.



Key Endpoints:

GET /shelf/byId/{id} – Retrieves a shelf by ID.

POST /shelf/save – Creates a new shelf.

PUT /shelf/update/{id} – Updates an existing shelf.

DELETE /shelf/delete/{id} – Deletes a shelf.

GET /shelf/fetchAll/{pageNum} – Fetches all shelves with pagination.





3. Data Transfer Objects (DTOs)

DTOs serve as the bridge between the controllers and services, encapsulating input and output data.


Input DTOs


DeviceInput:

Attributes: deviceType, buildingName, partNumber, deviceName.

Used for creating or updating devices.



ShelfInput:

Attributes: partNumber, shelfName.

Used for creating or updating shelves.



ShelfPositionInput:

Attributes: deviceId.

Used for creating shelf positions.




Output DTOs


DeviceOutput:

Attributes: deviceId, deviceName, partNumber, buildingName, deviceType, numberOfShelfPositions, updatedAt, deletedAt.

Contains nested relationships with shelf positions and shelves.



ShelfOutput:

Attributes: shelfId, partNumber, shelfName, updatedAt, createdAt.



ShelfPositionOutput:

Attributes: shelfPosId, deviceId, updatedAt, createdAt.

Contains nested relationships with shelves.





4. Repository Layer

The Repository Layer interacts directly with Neo4j using Cypher queries. It ensures efficient data retrieval and manipulation while abstracting database operations for the service layer.


DeviceRepository


File: DeviceRepository.java

Responsibilities:

Executes Cypher queries for CRUD operations on devices.

Fetches devices with nested relationships (e.g., shelf positions and shelves).

Supports pagination for large datasets.


ShelfPositionRepository


File: ShelfPositionRepository.java

Responsibilities:

Executes Cypher queries for CRUD operations on shelf positions.

Handles attachment and detachment of shelves to/from shelf positions.

Supports pagination for large datasets.


ShelfRepository


File: ShelfRepository.java

Responsibilities:

Executes Cypher queries for CRUD operations on shelves.

Fetches shelves based on attributes.

Supports pagination for large datasets.


Exception handling:

1. Global Exception Handler


File: GlobalExceptionHandler.java



Purpose:
Acts as a centralized mechanism for handling exceptions across the application.

Maps exceptions to meaningful HTTP responses with detailed error messages and timestamps.



Implementation:
Annotated with @ControllerAdvice to intercept exceptions thrown by any controller in the application.

Contains specific methods to handle custom exceptions and generic exceptions.

Builds structured error responses with the following attributes:

timestamp: When the error occurred.

status: HTTP status code.

error: Type of error (e.g., "Bad Request", "Conflict").

message: Detailed error message explaining the issue.





Key Methods:
handleResourceNotFound(ResourceNotFoundException ex):

Handles cases where requested resources are not found in the database.

Returns 404 Not Found.



handleBadRequest(BadRequestsException ex):

Handles invalid input data provided by the client.

Returns 400 Bad Request.



handleConflict(ConflictException ex):

Handles scenarios of conflicting operations (e.g., attempting to attach a shelf already attached).

Returns 409 Conflict.



handleOperationFailed(OperationFailedException ex):

Handles unexpected failures during database operations.

Returns 500 Internal Server Error.



handleGenericException(Exception ex):

Captures all other unhandled exceptions.

Returns 500 Internal Server Error with a generic message.







2. Custom Exception Classes

The application defines custom exceptions to handle specific error scenarios. These exceptions improve the clarity of error messages and help the application’s consumers understand the cause of failures.

ResourceNotFoundException


File: ResourceNotFoundException.java

Purpose:

Thrown when the requested resource (e.g., device, shelf, shelf position) does not exist in the database.

Example: Fetching a device by ID that does not exist.



Usage in Controllers:

Used in DeviceController, ShelfController, and ShelfPositionController to handle missing resources.



HTTP Status Code: 404 Not Found.


BadRequestsException


File: BadRequestsException.java

Purpose:

Thrown when the client provides invalid input data (e.g., null or empty fields).

Example: Providing an invalid device name or ID in API requests.



Usage in Controllers:

Used to validate input DTOs before processing.



HTTP Status Code: 400 Bad Request.


ConflictException


File: ConflictException.java

Purpose:

Thrown when an operation conflicts with the current state of the database or entities.
Example: Attempting to attach a shelf that is already attached to a shelf position.


Usage in Repositories and Services:

Used to ensure the integrity of relationships and prevent duplicate or conflicting operations.

HTTP Status Code: 409 Conflict.

OperationFailedException
File: OperationFailedException.java

Purpose:

Thrown when a database operation fails due to unexpected issues (e.g., connectivity problems, query errors).

Example: Failure to save a device or shelf due to database constraints.

Usage in Repositories:

Used to wrap database-related errors and provide meaningful feedback to service layers.
HTTP Status Code: 500 Internal Server Error.