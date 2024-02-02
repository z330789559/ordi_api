#!/bin/bash

set -e
echo "删除备份"
ssh root@47.236.143.182 "rm -rf /opt/backend/yudao-server.jar.bak"
# 1. back remote file
echo "备份远程文件"
ssh root@47.236.143.182 "mv /opt/backend/yudao-server.jar /opt/backend/yudao-server.jar.bak"
echo "上传文件"
# 2. upload local file
scp -l 1000000 -o ConnectTimeout=6000 ./yudao-server/target/yudao-server.jar root@47.236.143.182:/opt/backend/
echo "启动远程服务"
# 3. 关闭远程java进程
ssh root@47.236.143.182 "ps -ef | grep java | grep -v grep | awk '{print \$2}' | xargs kill -9"
# 4. 启动远程java进程
ssh root@47.236.143.182 "nohup /opt/backend/start.sh> /opt/backend/yudao-server.log 2>&1 &"
echo "deploy success"
set +e