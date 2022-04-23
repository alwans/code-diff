# code-diff
**用于java代码的code-diff && 覆盖数据合并收集**

* 支持git仓库获取代码code-diff
* 支持多个实例服务合并收集
* 支持同分支不同commit代码覆盖率合并收集
* 支持页面配置管理实例服务：配置jacoco端口；配置includes/excludes表达式
* 支持实时生成查看报告：报告生成支持使用includes && excldes表达式过滤
* 支持提供api配合jenkins pipeline集成使用
* 支持收集函数调用链

## linux部署
> 需要部署server && web 2个服务  
> FBI warning!!!  
> 这2个服务都需要和jenkins部署在同一台linux上  
### server部署
- 先执行sql文件:[ code-diff/sql/coverage.sql](https://github.com/alwans/code-diff/blob/main/sql/coverage.sql)
- 修改数据库配置:[diff-services/src/main/resources/application.properties](https://github.com/alwans/code-diff/blob/main/diff-services/src/main/resources/application.properties)
- 构建jar：mvn clean install -Dmaven.test.skip=true
- 启动jar服务：java -jar diff-services-1.0.0.jar
### web部署
- nginx部署web服务，web文件:[code-diff/dist.zip](https://github.com/alwans/code-diff/tree/main/dist)
- 访问wen站点

## docker部署  


<!-- > *1.通过nginx部署 /dist/dist.zip 站点*

> *2.执行 /sql/coverage.sql*

> *3.结合jenkins使用/shell/copyProject.sh*

> *4.启动 diff-services服务*  *java -jar diff-service-1.0.0.jar*

> *5.访问web服务* -->
待更新...


## 示图  

### 1. 集成到jenkins的pipeline脚本中  
![image](https://user-images.githubusercontent.com/13557160/148341521-fed3ef16-2131-4522-8081-98f3d14cbe39.png)

### 2. 页面配置应用的jacoco信息  
![image](https://user-images.githubusercontent.com/13557160/148341886-ed653598-b6dc-410e-91c8-08af1cbc6070.png)

### 3. 收集状态控制  
#### 3.1 支持多台应用同时合并收集
#### 3.2 支持不同commitId 合并收集
![image](https://user-images.githubusercontent.com/13557160/148342008-71fa3f30-8c84-48f1-8b74-9b631e12bbe0.png)

### 4. 实时查看报告  
#### 4.1 支持增量 | 全量报告  
#### 4.2 支持分支增量 | commitId增量
![image](https://user-images.githubusercontent.com/13557160/148342243-1f83629c-7447-45af-a842-25691e7df1bc.png)
![image](https://user-images.githubusercontent.com/13557160/148342472-f9dff777-3ebe-490b-ba16-5095dcabd8c8.png)
![image](https://user-images.githubusercontent.com/13557160/148342492-de5f0fc0-e0c2-42ed-8934-711cbdb8f87d.png)


