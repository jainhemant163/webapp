
# CSYE 6225 - Spring 2020 Cloud Computing Course
## Team Information

| Name        | NEU ID    | Email Address           |

| Hemant Jain  | 001305974 | jain.he@husky.neu.edu  |
# UNIVERSITY STUDENT BILL MANAGEGMENT SYSTEM

[Web Application Code Repository](https://github.com/jainhemant163/webapp)

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

# Deploying a web application on AWS-EC2

**Application Use Cases:**

* Application manages Billing invoices for the customer
* Users and Bills records are saved in MySQL RDS Instance
* Bill related files are uploaded to Amazon S3 bucket with lifecycle policy of 30 days
* Bill file metadata is stored in RDS Instance itself for retrieval purpose
* User receives his due bills in email via AWS Simple email service

# Architecture Design


![](images/Architecture-Diagram.png)



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

## Deploy Instructions
   MySQL port is default 3306  <br/>
   Server: server side as RESTful architectural style. As a default, it is listening at http://localhost:8080/ <br/>
   Code Deploy for AMI     --> Code Deploy for Lambda Function       --> Code Deploy for WebApp

## DNS Setup

### Create hosted zone for domain in ROOT AWS account
1. Register a domain name with a domain registrar such as Namecheap
2. Create a public hosted zone in Amazon Route 53.
3. Configure Namecheap to use custom nameservers provided by Amazon Route 53 to use the Route53 nameservers.
4. Create a type TXT record for your domain with TTL of 1 minute. Type TXT record should contain the text value "csye6225-spring2020"

### Create subdomain and hosted zone for DEV AWS account
1. Create a public hosted zone in Amazon Route 53 for the subdomain dev.yourdomainname.tld.
2. Configure nameservers for the subdomain in the root account. See docs.
3. Create a type TXT record for the subdomain with TTL of 1 minute. Type TXT record should contain the text value "csye6225-spring2020-dev".

### Create subdomain and hosted zone for PROD AWS account
1. Create a public hosted zone in Amazon Route 53 for the subdomain prod.yourdomainname.tld.
2. Configure nameservers for the subdomain in the root account. See docs.
3. Create a type TXT record for the subdomain with TTL of 1 minute. Type TXT record should contain the text value "csye6225-spring2020-prod".

### DNS updates to host application on cloud using load balancer
1. Route53 resource record for your domain name should now be an alias for your load balancer application.
2. Your CloudFormation template should configure Route53 so that your domain points to your load balancer and your web application is accessible thru http://your-domain-name.tld/.
3. Your application must be accessible using root context i.e. https://your-domain-name.tld/ and not https://your-domain-name.tld/app-0.1/

## JMeter Load Testing Script
1. Using Apache JMeter create tests that can be run against your application APIs.
2. JMeter tests need to make 500 concurrent API calls to your application to create bills.
3. All bills can be created under a single user account and the user account itself can be created via API call made outside of JMeter.


## CI/CD Pipeline - AMI - Hashicorp Packer

[HashiCorp Packer Code Repository](https://github.com/jainhemant163/ami)

* Automated AMI creation using Hashicorp packer
* Created AMI template to share the image between multiple AWS accounts
* Created golden images by adding provisioners to boostrap instances with - NPM, Code deploy and Cloud watch agaent

## CI/CD Pipeline - AWS Code Deployment

* Integrated Github repository with Circle-CI for continuous Integration
* Bootstrapped circle CI container with docker image to run the test cases and generate new code artifact
* Artifact is copied to S3 bucket and code deployement is triggered on running instances of autoscaling group
* In-Place deployment configuration hooks are placed for routing the traffic during deployment



![](images/Circle-CI-Integration.png)




## Logging & Alerting - Cloud Watch Services

* Embedded statD to collect various metrics such as counter for APIs hits and API response time etc
* logged the info, errors and warnings using log4js and further mounted them in AWS cloud-watch for analysis
* Implemented CPU Utilization based alarms for changing number of instances in auto scaling group

## Serverless Computing - Lambda 



[Serverless Lambda Code Repository](https://github.com/jainhemant163/serverless)




* Implemented pub/sub mechanism with SNS and Lambda function
* user requesting for his due bills, puts a message onto the AWS SQS service
* SQS-Consumer in the application checks already existing entry for user in Dynamodb
* If no email has sent already, SQS consumer process the request and puts the response in SNS 
* Once message is published to SNS Topic, subscribed lambda function is trigged 
* Lambda delivers due bills email to requesting user and saves the entry in Dynamo DB with TTL of 60 minutes

![](images/Serverless-Computing.png)
