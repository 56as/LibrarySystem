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

回退到上一版本
`git reset HEAD^`<br>
回退到指定版本
`git reset version_num`<br>
查看版本号
`git log --oneline`<br>


## github多人协作开发<br>
### 分支管理<br>
* main 分支：<br>
主分支，最终的、稳定的、经过测试没有 bug 的、可部署于生产环境的分支<br>
只能由 release 和 hotfix 分支合并，任何情况下都不能直接修改代码<br>
* dev 分支：<br>
主要开发分支，贯穿于整个项目的生命周期<br>
始终保持最新版本，功能模块开发任务交给 feature 分支，测试任务交给 <br>
* hotfix 分支：<br>
热修复分支，当 main 分支部署到生产环境后发生紧急状况，需要及时处理时，该分支负责热修复，即在保证应用不下线的条件下，对 bug 进行紧急修复<br>
该分支以 main 分支为基线，修复 bug 后，合并到 main 分支部署上线，同时也合并到 dev 分支保持最新进度<br>
命名规则： hotfix/NAME 或 hotfix-NAME<br>
* feature 分支：<br>
功能模块开发分支，对应于一个特定的功能模块<br>
该分支以 dev 分支为基线，完成开发工作后再合并到 dev 分支<br>
命名规则：feature/NAME 或 feature-NAME<br>
* release 分支：<br>
预发布分支，在发布正式版本前进行全面测试和修复<br>
该分支以 dev 分支为基线进行全面测试，若发生 bug 则可直接在该分支修复并提交<br>
经过测试没有问题之后，合并到 main 分支部署上线，同时也合并到 dev 分支保持最新进度<br>
命名规则：release/NAME 或 release-NAME<br>

### 提交规范<br>
参考 <https://www.ruanyifeng.com/blog/2016/01/commit_message_change_log.html><br>

### 模块分支<br>
Browser,Server,Mysql三个模块<br>
