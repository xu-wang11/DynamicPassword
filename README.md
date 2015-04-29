DynamicPassword
## NodeJs 后台部署

后台使用NodeJs + MongoDB + Express 完成。<br>
安装NodeJs <br>
```
sudo brew install NodeJs
```

安装MongoDB
```
sudo brew install mongodb
```
安装完成NodeJs和MongoDB后，进入到后台代码文件夹，可以看到package.json文件，该文件内包含了所有的依赖包。执行如下命令安装依赖包：
```
npm install
```
安装依赖包后，启动Mongodb，启动mongodb需要指定数据存放的文件夹，这里使用当前目录下地Data文件夹。
```
mongod --dbpath data/
```
完成上述步骤后，就可以启动网站了
```
npm start
```
默认情况网站启动使用的3000端口，在浏览器里面打开http://127.0.0.1:3000就可以看到是否正常启动了。
## 安卓前端部署
安卓前端使用的时Android Studio进行开发。需要注意的是在NetworkHelper类中指定了服务器的IP地址，部署后台之后，使用`ifconfig`查看当前服务器ip地址。将对应位置修改。
```
ip = "http://101.5.219.233:3000";
```
