# code-diff
**用于java代码的code-dif && 覆盖数据收集**


## 部署流程  

> *1.通过nginx部署 /dist/dist.zip 站点*

> *2.执行 /sql/coverage.sql*

> *3.结合jenkins使用/shell/copyProject.sh*

> *4.启动 diff-services服务*  *java -jar diff-service-1.0.0.jar*

> *5.访问web服务*
