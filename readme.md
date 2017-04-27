# Simple Transport Protocol
## 1. Task
The task is to write a simple transport protocol on top of a lower level communication stack.

* The communication stack provides a _link layer_ that is capable of transmitting data in chunks of **20 bytes**.
* The link layer doesn't give any transmission order guaranties.
* Data loss or corruption error in the link layer is unlikely but can occur.
* The link layer provides API consisting of the **asynchronous** callback on data arrival
 and **synchronous** call to send data.

## 2. What is important?
* Code readability and clarity;
* Correctness;
* Creativity.

## 3. Assigment
Write transport protocol on top of the provided link-layer 
that is capable of reliable transmission of the data having arbitrary size.

## 4. How to build?