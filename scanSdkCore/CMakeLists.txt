cmake_minimum_required(VERSION 3.22.1)

project(document_detector)

# Configura el módulo de ruta para encontrar el archivo de configuración de OpenCV
# Asume que 'sdk' está dentro de 'scanSdkCore'
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/sdk/native/jni")

# Configura OpenCV_DIR para apuntar al directorio que contiene OpenCVConfig.cmake
# Asume que 'sdk' está dentro de 'scanSdkCore'
set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/sdk/native/jni")

# Incluye los directorios de las cabeceras
# Asume que 'sdk' está dentro de 'scanSdkCore'
include_directories(${CMAKE_SOURCE_DIR}/sdk/native/jni/include)

# Busca OpenCV
find_package(OpenCV REQUIRED)

add_library(
        document_detector
        SHARED
        src/main/cpp/document_detector.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        document_detector
        ${log-lib}
        ${OpenCV_LIBS}
)
