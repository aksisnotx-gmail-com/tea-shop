#!/bin/bash

#  检查并接受一个key
# 检查是否传递了key，如果没有传递，给出提示并退出
if [ -z "$1" ]; then
    echo "Error: No key provided. eg： deploy.sh <key>"
    exit 1  # 如果没有传递key，退出脚本并提示错误
fi

# 获取传递的key参数
KEY="$1"

# 设置变量
CONTAINER_NAME="tea-shop-web"
PORT=8080  # 根据实际需要调整端口
JAR_DIR="/home/servers/tea-shop"

# 停止并删除旧的容器（如果存在）
if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]; then
    echo "Stopping and removing container $CONTAINER_NAME..."
    docker rm -f $CONTAINER_NAME  # 停止并删除容器
fi

# 运行新的容器
echo "Running the container..."
docker run -d -p $PORT:8080 -u 0 -v $JAR_DIR:$JAR_DIR  --name $CONTAINER_NAME openjdk:17 java -jar -Duser.timezone=GMT+08 $JAR_DIR/application-1.0.0.jar $KEY
docker run -d -p 8080:8080 -u 0 -v /home/servers/tea-shop:/home/servers/tea-shop  --name tea-shop-web openjdk:17 java -jar -Duser.timezone=GMT+08 /home/servers/tea-shop/application-1.0.0.jar

echo "Deployment complete! The app is running on port $PORT."
