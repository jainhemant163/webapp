
# CSYE 6225 - Spring 2020 Cloud Computing Course
## Team Information

| Name        | NEU ID    | Email Address           |

| Hemant Jain  | 001305974 | jain.he@husky.neu.edu  |
# UNIVERSITY STUDENT BILL MANAGEGMENT SYSTEM

## Technology Stack

Programming Language: Java 1.8

Web Framework: Springboot 2.1.2.RELEASE

Database: MySQL

IDE: Eclipse IDE

Version Control: Git

Project Management: Maven

Test Tool: Postman

Development Environment: Ubuntu

## Frameworks and AWS Services Used
Springboot<br/> 
Maven<br/> 
MySQL<br/> 
GitHub Account<br/> 
Apache Tomcat Server<br/> 
Hashing Techniques<br/> 
Salting Using Bcrypt Algorithm<br/> 

**AWS Cloud Services namely**
VPC, Subnets,Internet Gateway, Route53,CloudFormation, S3 Bucket, Auto Scaling,Load Balancing,Configuration Management, CloudWatch, Log Management, SQS, SES, SNS, Lambda, ACM Certificate Management, Security Group Configuration and Management, RDS, DynamoDB.

## Build Instructions
WebApp --> Import the project using the existing maven project, and find the class having the main method to run the SpringBoot Application.<br/>

Before running your SpringBoot application make sure the connection properties mentioned in the application.properties file for the MySQL server is up and running properly.<br/>

Once,your application shows that application running successfully on the port 8080 of the Tomcat Server,we can move ahead and do all the API's application testing using the Postman tools for the various REST API Calls.<br/>

Added Webapplication folder inside webapp repository. Designed and implemented the following folders:<br/>

1)Dao<br/> 
2)Sql<br/> 
3) Authentication<br/> 
4) controller<br/> 
5) entity<br/> 
6)query<br/>
7)service<br/> 
8)util<br/>

Following API are implemented in this complete web application using Spring Boot framework and AWS SDKs.<br/>

1.GET /v1/user/self<br/>
  Get User Information<br/>
2.PUT /v1/user/self <br/> 
  Update user information<br/>
3.POST /v1/bill/  <br/>
  Create a new bill<br/>
4.GET /v1/bills  <br/>
  Get all bills.<br/>
5.DELETE /v1/bill/{id}  <br/>
  Delete a bill<br/>
6.GET /v1/bill/{id}     <br/>
  Get a bill<br/>
7.PUT /v1/bill/{id} <br/>
  Update a bill <br/>
8.POST /v1/user   <br/>
  Create a user<br/>
9.POST /v1/bill/{id}/file  <br/>
  Attach a file to the bill <br/>
10.GET /v1/bill/{billId}/file/{fileId}  <br/>
   Get a bill's attachment<br/>
11.DELETE /v1/bill/{billId}/file/{fileId} <br/>
   Delete file <br/>

## DATABASE SCHEMA CREATED IN MYSQL and RDS Instance:
   Following are the schemas created in the Database to support all the REST APIs implemented in the Spring Boot application:

### 1.User
{
id	string($uuid)
example: d290f1ee-6c54-4b01-90e6-d701748f0851
readOnly: true
first_name*	string
example: Jane
last_name*	string
example: Doe
password*	string($password)
example: skdjfhskdfjhg
writeOnly: true
email_address*	string($email)
example: jane.doe@example.com
account_created	string($date-time)
example: 2016-08-29T09:12:33.001Z
readOnly: true
account_updated	string($date-time)
example: 2016-08-29T09:12:33.001Z
readOnly: true
}


### 2.Bill
{
id*	string($uuid)
example: d290f1ee-6c54-4b01-90e6-d701748f0851
readOnly: true
created_ts	string($date-time)
example: 2016-08-29T09:12:33.001Z
readOnly: true
updated_ts	string($date-time)
example: 2016-08-29T09:12:33.001Z
readOnly: true
owner_id*	string($uuid)
example: a460a1ef-6d54-4b01-90e6-d7017sad851
readOnly: true
vendor*	string
example: Northeastern University
bill_date*	string($date)
example: 2020-01-06
due_date*	string($date)
example: 2020-01-12
amount_due*	number($double)
example: 7000.51
minimum: 0.01
categories*	[
              uniqueItems: true
              example: List [ "college", "tuition", "spring2020" ]
string]
paymentStatus*	string
Enum:
     [ paid, due, past_due, no_payment_required ]
attachment	File{
              file_name*	string
              readOnly: true
              example: mybill.pdf
              id*	string($uuid)
              readOnly: true
              example: d290f1ee-6c54-4b01-90e6-d701748f0851
              url*	string
              readOnly: true
              example: /tmp/file.jpg
              upload_date*	string($date)
              readOnly: true
              example: 2020-01-12
}
}

### 3.File
{
file_name*	string
readOnly: true
example: mybill.pdf
id*	string($uuid)
readOnly: true
example: d290f1ee-6c54-4b01-90e6-d701748f0851
url*	string
readOnly: true
example: /tmp/file.jpg
upload_date*	string($date)
readOnly: true
example: 2020-01-12
}
