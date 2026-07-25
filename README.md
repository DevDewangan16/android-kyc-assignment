# Android KYC Banking App

An Android application built as a solution for the Android Developer Assignment. The app simulates a digital banking platform where relationship managers can browse customer accounts, verify customer KYC using an in-app selfie capture, and fetch bank details through IFSC lookup.

---

## Features

### Accounts Screen
- View customers in **Verified** and **Pending** KYC tabs
- Search customers by name or account number
- Filter customers using category chips:
  - All
  - Savings
  - Current
  - NRI
- Displays customer avatar, masked account number, balance and KYC status

### Account Details Screen
- View complete customer profile
- Displays:
  - Name
  - Photo
  - Date of Birth
  - Nationality
  - Address
  - Contact Information
  - Account Number
  - Balance
  - Bank Details
- Bank and branch information is fetched dynamically using the customer's IFSC code.

### KYC Verification
- Pending customers can complete KYC directly from the details screen.
- Selfie is captured using an **in-app CameraX implementation**.
- Captured image is stored locally.
- Customer status changes from **Pending** to **Verified** after successful verification.
- Verified selfie is displayed as the customer's profile picture.

### Offline Support
- Customer data is cached locally.
- Verified KYC status persists after app restart.
- Captured selfies remain available even after reopening the app.

---

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM Architecture
- Retrofit
- Gson
- CameraX
- SQLite
- SharedPreferences
- Kotlin Coroutines

---

## Architecture

The project follows the **MVVM (Model-View-ViewModel)** architecture to keep the code modular, scalable and easy to maintain.

```
UI
│
├── ViewModel
│
├── Repository
│
├── Network Layer
│
└── Local Storage
```

---

## APIs Used

### DummyJSON API

Used to fetch customer information including:

- Customer Name
- Avatar
- Contact Details
- Address
- Bank Information

Endpoint:

```
https://dummyjson.com/users
```

---

### Razorpay IFSC API

Used to resolve bank information from IFSC code.

Information fetched:

- Bank Name
- Branch
- City
- State

Endpoint:

```
https://ifsc.razorpay.com/{IFSC_CODE}
```

---

## Project Structure

```
app
│
├── data
│   ├── model
│   ├── network
│   ├── repository
│   └── database
│
├── domain
│
├── ui
│   ├── screens
│   ├── components
│   ├── navigation
│   └── theme
│
├── utils
│
└── MainActivity.kt
```

---

## Screenshots

### Accounts Screen

<img width="200" alt="Account Screen" src="https://github.com/user-attachments/assets/3157ef78-02a3-4996-a8dd-f3314de14a5a" />

---

### Account Details

<img width="200" alt="Detailed Screen" src="https://github.com/user-attachments/assets/feca9f3c-d3b5-44e4-8be5-6d0087f33166" />

---

### Camera Screen

<img width="200" alt="Camera Screen " src="https://github.com/user-attachments/assets/572a9e1c-966c-4493-9f09-cfc245bf1c54" />
<img width="200"" alt="Preview Camera Screen" src="https://github.com/user-attachments/assets/af386dc5-d7de-4348-8a54-2cf46cf59988" />

---

### Verified KYC

<img width="200" alt="Verified Screen" src="https://github.com/user-attachments/assets/185a9e6e-c203-4700-833f-9d6f35d8728a" />
<img width="200" alt="Detailed Verified Screen" src="https://github.com/user-attachments/assets/bafbbab8-f062-496d-854a-5605ea2ba760" />

---

## Assignment Requirements Covered

| Requirement | Status |
|-------------|--------|
| Kotlin Application | ✅ |
| Accounts Screen | ✅ |
| Details Screen | ✅ |
| Verified & Pending Tabs | ✅ |
| Search Functionality | ✅ |
| Category Chips | ✅ |
| CameraX Integration | ✅ |
| Runtime Camera Permission | ✅ |
| Selfie Capture | ✅ |
| Persist Selfie Locally | ✅ |
| Persist KYC Status | ✅ |
| IFSC API Integration | ✅ |
| API Caching | ✅ |
| MVVM Architecture | ✅ |
| Clean Project Structure | ✅ |
| Dark / Light Theme | ✅ |

---

## Challenges Faced

Some interesting challenges while building this project:

- Integrating CameraX with Jetpack Compose.
- Managing runtime camera permissions gracefully.
- Persisting captured selfies across app restarts.
- Resolving bank information dynamically from IFSC codes.
- Implementing caching to reduce unnecessary API calls.
- Keeping UI responsive during network operations.

---

## Design Decisions

- Followed MVVM architecture for better separation of concerns.
- Used Repository pattern to keep networking independent from UI.
- Cached customer data for smoother experience on slow networks.
- Used SQLite and SharedPreferences for local persistence.
- Built reusable Compose components to reduce code duplication.

---

## Future Improvements

If this project is extended further, I would like to add:

- Pagination for customer list
- Unit Tests
- UI Tests
- Biometric authentication
- Better image compression for captured selfies
- Pull-to-refresh support

---

## Final Note

This project was developed as part of the Android Developer Assignment. Along with implementing all the required features, I focused on writing clean, maintainable code and creating a simple, user-friendly interface. It was a great opportunity to work with CameraX, Jetpack Compose, local persistence, and REST APIs in a practical use case.
