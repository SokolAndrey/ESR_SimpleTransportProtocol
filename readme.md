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

## 4. Description of solution
The ``` Socket.class``` implements simple transport protocol.
There are 4 types of messages:
* ``` HelloMessage``` - using to establish connection;
 This type of message has next fields:
  * sender - the address of sender;
  * receiver - the address of receiver;
  * batchCount - the number of data chunks, which will be sent;
  * checkSum - the check sum;
  
* ``` DataMessage``` - using to send valuable data;
  * id - the id of the message;
  * batchNumber - the number of current batch;
  * checkSum - the check sum;
  * dataChunk - the valuable data.
* ``` ByeMessage``` - using to send last message, which means that everything is fine;
  * id - the id of the message;
  * checkSum - the check sum;
* ``` ResponseMessage``` - using to send response for successfully delivered messages.
  * id - the id of the message;
  * messageNumber - the number of successfully received message.
  * checkSum - the check sum;
  
The ```Socket``` has 2 public methods to send and receive data.
The protocol guarantee that if the connection is established all messages will be delivered to the destination address.