#!/bin/bash
cd /home/ubuntu/webapp/
sudo chown -R ubuntu:ubuntu /home/ubuntu/webapp/
sudo chmod +x WebApplication-0.0.1-SNAPSHOT.jar
source /etc/profile.d/envvariable.sh
sudo systemctl stop tomcat8
export awsRDS=$awsRDS
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/webapp/cloudwatchconfig.json -s
kill -9 $(ps -ef|grep WebApplication | grep -v grep | awk '{print$2}')
nohup java $JAVA_OPTS -jar WebApplication-0.0.1-SNAPSHOT.jar > /home/ubuntu/output.log 2> /home/ubuntu/output.log < /home/ubuntu/output.log &
