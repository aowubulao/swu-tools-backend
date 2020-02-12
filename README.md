# 西南大学助手

西南大学助手后端程序。

项目详情请于前端仓库查看。

项目前端地址：[https://github.com/aowubulao/swu-tools-web/](https://github.com/aowubulao/swu-tools-web/)



## 构建项目

### 配置 application.yaml

在resources目录下创建application.yaml文件并进行配置

```yaml
server:
  #端口
  port:
spring:
  application:
  	#名称
    name: 
  main:
    allow-bean-definition-overriding: true
#日志文件配置
logging:
  file:
    path: 
    name: 
```

### 打包项目

```
mvn package
```

将项目打包为jar包

### 运行项目

```
java -jar xxx.jar
```