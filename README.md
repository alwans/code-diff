# code-diff
**用于java代码的code-dif && 覆盖数据收集**


## 部署流程  

> *1.通过nginx部署 /dist/dist.zip 站点*

> *2.执行 /sql/coverage.sql*

> *3.结合jenkins使用/shell/copyProject.sh*

> *4.启动 diff-services服务*  *java -jar diff-service-1.0.0.jar*

> *5.访问web服务*

## 示图  

### 1. 集成到jenkins的pipeline脚本中  
![image](https://user-images.githubusercontent.com/13557160/148341521-fed3ef16-2131-4522-8081-98f3d14cbe39.png)

### 2. 页面配置应用的jacoco信息  
![image](https://user-images.githubusercontent.com/13557160/148341886-ed653598-b6dc-410e-91c8-08af1cbc6070.png)

### 3. 收集状态控制  
#### 3.1 支持多台应用同时合并收集
#### 3.2 支持不同commitId 合并收集
![image](https://user-images.githubusercontent.com/13557160/148342008-71fa3f30-8c84-48f1-8b74-9b631e12bbe0.png)

### 4. 随时查看报告  
#### 4.1 支持增量 | 全量报告  
#### 4.2 支持分支增量 | commitId增量
![image](https://user-images.githubusercontent.com/13557160/148342243-1f83629c-7447-45af-a842-25691e7df1bc.png)
![image](https://user-images.githubusercontent.com/13557160/148342472-f9dff777-3ebe-490b-ba16-5095dcabd8c8.png)
![image](https://user-images.githubusercontent.com/13557160/148342492-de5f0fc0-e0c2-42ed-8934-711cbdb8f87d.png)


