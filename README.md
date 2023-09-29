![Texto alternativo](https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExeGgxbTFoemNiY3BvNmNueHBvMW54aGZ4OTg5MWhqcjlzd3FmZWRieiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Zf6xbo2DMc0LP9txpj/giphy.gif)


## Project Structure and Modules

This project is organized into multiple modules to streamline development and ease the implementation for clients. Below is an outline detailing the key modules and their functions:

**Getting Started:**

### 1. scanSdk: The Client Application

This is the client-side application of the project. When you clone and open the project, make sure to **run the project under this module**. This serves as your entry point and initializes the other modules.


### 2. scanSdkLibrary: The Bridging Library Module

`scanSdkLibrary` acts as a bridge between public and private functionalities provided by the project. It is essentially the library consumed by the `scanSdk` client application. It links the user-facing utilities with the internal logic and data structures.

### 3. scanSdkCore: The Core Module

This is arguably the most robust module in the project. It encompasses everything related to OpenCV and the internal processing logic of the application. Note that this module is **private** and only intended for internal usage.

### 4. scanSdkPublic: The Public Utilities Module

This module is designed for public access and is what users will interact with when they import the library into their own projects. Unlike `scanSdkCore`, this is **public-facing** and includes all the utilities and methods a user would need for regular operations.

---

**Example Usage for End-Users:**

```java
// Import the scanSdkPublic library
import com.yourproject.scanSdkPublic;

// Utilize public methods
scanSdkPublic.someMethod();
```

---

By organizing the project in this manner, it becomes easier to isolate functionalities, improve code maintainability, and offer a seamless user experience. Please follow the respective setup procedures and configurations mentioned for each module.
 
