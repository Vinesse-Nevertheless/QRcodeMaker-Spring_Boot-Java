# 📱 QR Code Generation REST API

A lightweight, customizable QR Code Generation RESTful web service built with **Java 17**, **Spring Boot**, and **Google's ZXing** library.

This service allows client applications to dynamically generate QR code images in multiple image formats (`PNG`, `JPEG`, `GIF`), customize dimensions, and select from four standard QR error correction levels (`L`, `M`, `Q`, `H`). It includes full parameter defaulting and a strict input validation hierarchy.

---

## ✨ Features

* **Custom QR Code Generation:** Encodes arbitrary text or URLs into readable QR codes via Google ZXing.
* **Configurable Image Formats:** Supports `PNG`, `JPEG`, and `GIF` image stream responses using custom HTTP message converters.
* **Dynamic Sizing:** Generates square QR code images within configurable dimensions (150px to 350px).
* **Configurable Error Correction:** Implements ZXing's `ErrorCorrectionLevel` (`L`: ~7%, `M`: ~15%, `Q`: ~25%, `H`: ~30% damage tolerance).
* **Smart Parameter Defaulting:** All request parameters except `contents` fall back to sane defaults (`size=250`, `correction=L`, `type=png`).
* **Strict Validation Hierarchy:** Orderly HTTP 400 Bad Request responses following a deterministic priority chain:  
  $$\text{invalid contents} \longrightarrow \text{invalid size} \longrightarrow \text{invalid correction} \longrightarrow \text{invalid type}$$

---

## 🛠️ Tech Stack

* **Language:** Java 23
* **Framework:** Spring Boot 3
* **Build Tool:** Gradle
* **Barcode Engine:** Google ZXing (`com.google.zxing:core`, `com.google.zxing:javase`)

---

## 🚀 Getting Started

### Prerequisites

* Java 23 JDK or higher installed
* Git

### Installation & Execution

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/qr-code-api.git](https://github.com/your-username/qr-code-api.git)
   cd qr-code-api
   ```

1. Build and run the application:
  ```bash
./gradlew bootRun
   ```

2. The API will be available at http://localhost:8181.

## 📖 API Documentation
### 1. Health Check
- Endpoint: GET /api/health
- Response: 200 OK

### 2. Generate QR Code
- Endpoint: GET /api/qrcode
- Produces: image/png, image/jpeg, or image/gif

### Request Parameters
| Parameter | Type | Required? | Default | Valid Values / Constraints |
|---|---|---|---|---|
| `contents` | `String` | **Yes** | *None* | Non-empty, non-blank string. |
| `size` | `Integer` | No | `250` | Integer between `150` and `350` (inclusive). |
| `correction` | `String` | No | `L` | `L` (Low), `M` (Medium), `Q` (Quartile), `H` (High). |
| `type` | `String` | No | `png` | `png`, `jpeg`, `gif`. |

## Example Requests
### Success Example (Default Values)

`GET /api/qrcode?contents=https://github.com`

| Request Details | Visual Output |
|---|---|
| **URL:** `http://localhost:8181/api/qrcode?contents=https://github.com`<br>**Status:** `200 OK`<br>**Content-Type:** `image/png`<br>**Dimensions:** 250x250px | <img width="150" alt="200px QR Code" src="https://github.com/user-attachments/assets/079929c9-18ff-480a-81cb-c0a526721d6c" />


#### 2. Customized Parameters
`GET /api/qrcode?contents=Hello+World&size=300&correction=H&type=jpeg`

| Request Details | Visual Output |
|---|---|
| **URL:** `http://localhost:8181/api/qrcode?contents=Hello+World&size=300&correction=H&type=jpeg`<br>**Status:** `200 OK`<br>**Content-Type:** `image/jpeg`<br>**Dimensions:** 300x300px | <img width="250" alt="300px QR Code" src="https://github.com/user-attachments/assets/43c726a8-2ef3-42c8-b446-255e33446af9" />


## Error Responses
If any parameter fails validation, the service returns 400 Bad Request with a JSON payload explaining the error.

### Example: Invalid Correction Level
``` Bash
'GET /api/qrcode?contents=Hello&correction=INVALID'
```

``` JSON
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Permitted error correction levels are L, M, Q, H"
}
```

### Example: Invalid Size
``` Bash
'GET /api/qrcode?contents=Hello&size=500'
```

``` JSON
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Image size must be between 150 and 350 pixels"
}
```

## 📐 Architecture Highlights
- Separation of Concerns: Controller routing logic is separated from business input assertion (QRInputValidator) and ZXing image creation (QRWriter).
- Custom Message Converters: Configured a BufferedImageHttpMessageConverter bean in Spring's configuration context to allow controllers to stream raw BufferedImage instances seamlessly.
- Spring-Bound Parameter Defaulting: Parameter defaulting is handled explicitly at the @RequestParam framework boundary, maintaining clear constructor execution lifecycles.

  
