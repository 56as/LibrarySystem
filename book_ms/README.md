## 环境要求
JDK 17
Mysql 5.7.43
mysql账号root，密码为123456
运行Application.java启动程序
访问http://localhost:8082/
## 模块概述

### 后端模块结构
```
src/main/java/com
├── config                    // 配置类
│   └── CorsConfig.java      // 跨域配置
├── controller               // 控制器层：处理HTTP请求
│   ├── AdminController.java     // 管理员控制器
│   ├── AdminBookController.java // 图书管理控制器
│   ├── AdminAnnouncementController.java // 公告管理控制器
│   └── BorrowController.java   // 借阅管理控制器
├── model                    // 实体类层
│   ├── Book.java           // 图书实体
│   ├── User.java           // 用户基础实体
│   ├── Admin.java          // 管理员实体
│   ├── Reader.java         // 读者实体
│   ├── Borrowing.java      // 借阅记录实体
│   ├── Announcement.java   // 公告实体
│   ├── Notification.java   // 通知实体
│   └── SystemSetting.java  // 系统设置实体
├── repository              // 数据访问层
│   ├── BookRepository.java      // 图书数据访问
│   ├── UserRepository.java      // 用户数据访问
│   ├── AdminRepository.java     // 管理员数据访问
│   ├── ReaderRepository.java    // 读者数据访问
│   ├── BorrowingRepository.java // 借阅数据访问
│   └── NotificationRepository.java // 通知数据访问
├── service                 // 业务逻辑层
│   ├── impl               // 接口实现类
│   │   ├── BookServiceImpl.java      // 图书服务实现
│   │   ├── UserServiceImpl.java      // 用户服务实现
│   │   ├── ReaderServiceImpl.java    // 读者服务实现
│   │   └── AnnouncementServiceImpl.java // 公告服务实现
│   ├── BookService.java          // 图书服务接口
│   ├── UserService.java          // 用户服务接口
│   ├── ReaderService.java        // 读者服务接口
│   └── AnnouncementService.java  // 公告服务接口
└── Application.java        // 应用程序启动类
```

### 后端模块说明

#### 1. 实体层（Model）
- **Book**: 图书信息实体
  - ISBN、标题、作者、出版社、出版日期
  - 分类、馆藏位置、总复本数、可用复本数
  - 创建时间、更新时间的自动维护

- **User**: 用户基础信息
  - 用户名、密码、邮箱
  - 用户角色（ADMIN/STAFF/READER）
  - 创建时间、更新时间的自动维护

- **Reader**: 读者信息
  - 基本信息（姓名、电话、地址）
  - 会员状态（ACTIVE/EXPIRED/SUSPENDED）
  - 关联用户账号

- **Borrowing**: 借阅记录
  - 借阅信息（读者、图书、借阅日期、应还日期）
  - 借阅状态（BORROWED/RETURNED/OVERDUE）
  - 续借次数统计

#### 2. 控制器层（Controller）
- **AdminController**: 管理员相关操作
  - 管理员账户管理
  - 系统设置管理

- **AdminBookController**: 图书管理操作
  - 图书信息的增删改查
  - 库存管理
  - 分类管理

- **AdminAnnouncementController**: 公告管理
  - 发布系统公告
  - 管理公告信息

- **BorrowController**: 借阅管理
  - 处理借书请求
  - 处理还书请求
  - 续借操作
  - 借阅历史查询

#### 3. 服务层（Service）
- **BookService**: 图书管理业务逻辑
  - 图书信息维护
  - 库存管理
  - 图书检索

- **UserService**: 用户管理业务逻辑
  - 用户认证
  - 用户信息管理
  - 权限控制

- **ReaderService**: 读者管理业务逻辑
  - 读者信息管理
  - 借阅资格验证
  - 读者统计

- **AnnouncementService**: 公告管理业务逻辑
  - 公告发布
  - 公告推送
  - 历史公告管理

#### 4. 数据访问层（Repository）
- **BookRepository**: 图书数据操作
  - 基础的CRUD操作
  - 自定义查询方法

- **BorrowingRepository**: 借阅数据操作
  - 借阅记录管理
  - 统计查询
  - 自定义报表查询

- **NotificationRepository**: 通知数据操作
  - 通知记录管理
  - 未读通知查询
  - 按类型查询通知

### 前端模块结构
```
src/main/resources
└── templates                    // 模板目录
    ├── layout                  // 布局模板
    │   ├── header.html        // 页头模板
    │   └── sidebar.html       // 侧边栏模板
    ├── admin                  // 管理员页面
    │   └── dashboard.html     // 管理控制台
    ├── books_management       // 图书管理页面
    │   ├── list.html         // 图书列表
    │   ├── add.html          // 添加图书
    │   └── edit.html         // 编辑图书
    ├── announcement_management // 公告管理
    │   ├── list.html         // 公告列表
    │   └── edit.html         // 编辑公告
    ├── borrowing             // 借阅管理
    │   ├── list.html         // 借阅列表
    │   └── history.html      // 借阅历史
    ├── reader                // 读者管理
    │   ├── list.html         // 读者列表
    │   └── edit.html         // 读者信息编辑
    ├── profile               // 个人信息
    │   └── edit.html         // 个人信息编辑
    ├── notification          // 通知管理
    │   └── list.html         // 通知列表
    ├── report                // 统计报表
    │   └── statistics.html   // 统计数据
    ├── login.html            // 登录页面
    └── register.html         // 注册页面
```

### 前端模块说明

#### 1. 页面模块
- **图书管理模块**
  - 图书列表：展示和搜索图书
  - 添加图书：新增图书信息
  - 编辑图书：修改图书信息和库存

- **借阅管理模块**
  - 借阅列表：当前借阅记录
  - 借阅历史：历史借阅查询
  - 借还操作：处理借书和还书

- **公告管理模块**
  - 公告列表：系统公告展示
  - 公告编辑：发布和修改公告
  - 公告详情：查看公告内容

- **读者管理模块**
  - 读者列表：读者信息管理
  - 读者编辑：修改读者信息
  - 借阅权限：管理借阅资格

#### 2. 布局模块
- **Header**：页面顶部布局
  - 系统标题
  - 用户信息
  - 导航菜单

- **Sidebar**：侧边栏布局
  - 功能菜单
  - 快捷操作
  - 权限控制

#### 3. 功能组件
- **表单组件**
  - 数据验证
  - 提交处理
  - 错误提示

- **列表组件**
  - 分页显示
  - 搜索过滤
  - 排序功能

#### 4. 工具功能
- **用户认证**
  - 登录注册
  - 权限验证
  - 会话管理

- **数据处理**
  - 日期格式化
  - 数据验证
  - 错误处理


在Maven项目中，`target` 文件夹是构建过程中自动生成的目录，它包含了编译后的类文件、打包的JAR/WAR文件以及其他构建产物。这个文件夹通常不需要被版本控制（比如不需要提交到Git中），因为：

1. 它包含的都是由源代码编译生成的文件，可以随时通过Maven重新构建得到
2. 这些文件会占用大量空间
3. 不同开发者在不同环境下构建可能会产生略微不同的输出

建议：
1. 将 `target` 文件夹添加到 `.gitignore` 文件中
2. 如果想清理项目，可以安全地删除 `target` 文件夹
3. 需要重新构建项目时，使用 `mvn clean install` 命令，Maven 会自动重新生成所需的文件

总的来说，`target` 文件夹对于项目的版本控制和分享来说是不需要的，可以随时删除它。这个文件夹只在本地开发和构建过程中临时使用。
