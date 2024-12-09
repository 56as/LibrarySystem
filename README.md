## git命令   <br>

在命令行中进入本地文件夹  <br>
`cd ./Desktop/github/LibrarySystem`<br>
添加文件到git<br>
`git add .`<br>
提交更改，添加提交说明<br>
`git commit -m "Initial commit"`<br>
最后，将本地更改推送到github的main分支<br>
`git push origin main`<br>

拉取项目代码
`git clone https://github.com/56as/LibrarySystem.git`<br>
拉取分支代码
`git pull origin browser`<br>

回退到上一版本
`git reset HEAD^`<br>
回退到指定版本
`git reset version_num`<br>
查看版本号
`git log --oneline`<br>

### 分支<br>
Browser,Server,Mysql三个分支<br>
