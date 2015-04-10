# DataHandler #

The StockData handler classes are the DataHandler-s. There are two types of handlers:
  * data senders: producers of data - **[DataSource](DataHandler#Data_Source.md)**
  * data receivers: the consumer of data - **[DataReceiver](DataHandler#Data_Receiver.md)**
There is a third type of handlers, the mixture of previous two: the **[DataProcessor](DataHandler#Data_Processor.md)**. It's a receiver which transforms the input data, and generates new one.

## Structure ##
  * **Name** short name of handler
  * **Description** description about handler

## Data Source ##
The source of StockDatas. It generates the input information to the application from the outer world (for example a CSV file, a web page, etc.). For test purpose there is a RandomDataSource.
### Structure ###
The source must define its output parameters: the name of instruments, and the type of data.

## Data Receiver ##
TODO
### Structure ###

## Data Processor ##
TODO
### Structure ###