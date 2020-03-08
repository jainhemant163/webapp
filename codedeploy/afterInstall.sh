  
#!/bin/bash
cd /home/ubuntu/WebApplication
sudo chown -R ubuntu:ubuntu /home/ubuntu/WebApplication
sudo chmod +x WebApplication-0.0.1-SNAPSHOT.jar
source /etc/profile.d/envvariable.sh
kill -9 $(ps -ef|grep WebApplication | grep -v grep | awk '{print$2}')
nohup java -jar WebApplication-0.0.1-SNAPSHOT.jar > /home/ubuntu/output.txt 2> /home/ubuntu/output.txt < /home/ubuntu/output.txt &
