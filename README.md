# sprint boot项目 问题归纳

***
版本：2.1.4
***

### 1.Mybatis启动问题：

    not found [ com.mao ] package
###### 这只是一个警告，说明你加了Mybatis maven，但Mybatis没有扫描到Mapper文件：
###### 使用Mybatis方法：
###### 配置以扫描到Mapper文件：
    1.Mapper接口添加 @Repository 让 Spring 可以扫描到；
    2.Mapper接口添加 @Mapper 或者在启动类添加 @MapperScan("com.mao.mapper") 指向 Mapper 文件包，让MyBatis扫描到。
    3.使用注解SQL的话上述即可使用。
    4.使用xml SQL的话须在配置文件配置：
    此mapper文件需建在resource下。如果mapper文件下有二级文件夹，须向我这样配置。
    mapper-locations: classpath:mapper/**/*Mapper.xml
    配置实体类包
    type-aliases-package: com.mao.entity

***

### 2.没报错却启动不了问题：

###### 原因：自带`tomcat`无法正常启动。
###### 去除`spring-boot-starter-tomcat`,spring boot自带tomcat，如果添加此maven，需配置本地tomcat
###### 配置`jetty`容器也是一个很好的方式：
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>
###### 成功启动后会发现：
    Starting Servlet Engine: Apache Tomcat/9.0.13` 
###### 这是`spring boot`自带的`tomcat`

***

### 3. 热部署：

###### 修改代码后自动编译。可以实时查看修改后的情况（延迟5s左右）
###### spring boot项目：引入依赖：
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
     </dependency>
###### `File` ==> `setting` ==> `Build` ==> `Compiler` ==> 勾选：`Build Project automatically`
###### `Ctrl` + `Shift` + `Alt` + `/`  ==> 选择：`registry` ==> 勾选：`compiler.automake.allow.when.app.running`
###### 完成热部署

***

### 4.lombok的使用：
###### spring boot项目：引入依赖：
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
###### `File` ==》 `setting` ==》 `Plugins` ==》 搜索 `lombok` ==》 `install`
###### 重启，完成`lombok`插件安装

***

### 5.pom.xml: project处爆红线：
###### 原因一：
###### `maven plugin` 没有下载完全。
###### 解决：
###### 关闭`idea`，重启试一试，可能`maven plugin`会重新进行下载。不会影响项目。
###### 原因二：
###### 在`module project`中，引用父`parent project`时，由于系统给的`pom.xml`的`parent`为`spring boot`。
###### 解决：
###### 将`spring boot` 的`parent`删除即可。
###### 此时`groupId`会标黄，可能一些`dependency`也会标黄，因为这些与`parent`重复。删除即可，它会引用`parent`中的。

***

### 6.初始启动项目mysql相关报错：
###### 原因：
###### `spring boot` `1.5`版本不会报错，`2.0`版本以上`spring boot`的`driver`已更改。
###### 解决一：
###### `driver-class-name`:改为：`com.mysql.cj.jdbc.Driver`
###### 解决二：
###### `dependency`的`mysql`改为`5.1.34`。（个人觉得解决一好点，人家这样配肯定有道理）

***

### 7.使用thymeleaf 访问不到静态资源：
###### 存放方式为：
###### `static`文件夹下存放静态资源（`css`、`js`、`img`），`templates`文件夹下存放页面（`html`）
###### 原因一：
###### 配置不对：
###### 正确配置：
###### 1.`pom.xml`中存在：
    spring-boot-starter-thymeleaf
###### 2.设置页面路径：
    spring.thymeleaf.prefix: classpath:/templates/
###### 3.以`/static`开头访问资源：
    spring.mvc.static-path-pattern: /static/**
###### 4.访问路径配置：
    <script th:src="@{/static/js/jquery.min.js}"></script>
###### 原因二：
###### 如果为以上配置仍报`404`：有可能文件夹名称的原因：（很容易出现，但原因不明）
###### 一般jquery的文件名通常为：
    jquery-1.11.3.min.js
###### 此时
    <script th:src="@{/static/js/jquery-1.11.3.min.js}"></script>
###### 却访问不到
###### 任何条件不变，改为
    <script th:src="@{/static/js/jquery-1-11-3.min.js}"></script>
###### 可以访问。
###### 猜想可能将`1.11.3`读成包了，但是`.min.js`不会读成包，它可以这样写。估计只对`.min`做了判断。
###### 同理的还有
    <link rel="stylesheet" th:href="@{/static/font-awesome-4-7-0/css/font-awesome.min.css}">

*** 

### 8.springboot 多模组项目打包问题：
###### 此项目结构为：
    pom    manager          父类 继承 spring boot parent
            |
    jar     |--- common     子类 继承 manager
            |
    war     |--- admin      子类 继承 manager, common
###### 打包顺序：
    manager -- common -- admin
###### 打包步骤
##### 1. manager：父类`pom`文件下会有子模块涵盖，打包前先将其注释。
###### 如果不注释打包父类时会连同子类一块运行
###### `admin`模块继承的`common`模块没有打包因此会报错。
    <modules>
        <module>common</module>
        <module>admin</module>
    </modules>
###### 打包完会在`Idea`自带的`.m2`文件中查到`xxx.pom`文件（我没有使用自配的`maven`）
##### 2. common：springboot项目创建的maven打包程序为：
    build>
        <finalName>common</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
###### `springboot`工程打包编译时，会生成两种`jar`包，一种是普通的`jar`包，一种是可执行`jar`包
###### 这2种`jar`包名称相同，`install`时，默认先生成普通`jar`包，再生成可执行`jar`包
###### 因此可执行`jar`包会覆盖掉普通`jar`包
###### 由于`common`需要打成`jar`包，被其他模块引用。需使用普通`jar`包。
###### `build`调整为：
    <build>
        <finalName>common</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
        </plugins>
    </build>
###### 添加完后，可执行`jar`包不会覆盖普通`jar`包
###### 查询`.m2`中生成情况为：
    common-0.0.1-SNAPSHOT.jar           --普通jar包
    common-0.0.1-SNAPSHOT-exec.jar      --可执行jar包
##### 3. admin： 引入common依赖：
    <dependency>
        <groupId>com.mao</groupId>
        <artifactId>common</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
###### 直接`package`即可
##### 4. 错误解析：
###### 调整`pom.xml`后还出现同样问题，清空一下，再打包：
    clear -- compile -- install / package
###### 其他子模块找不到：`maven`中没有该子模块的`jar`包：
    父模块，公共模块先打成jar包
###### 打包`admin`时，`common`中的所有类：`ClassNotFoundException`
    引用的是可执行jar包，按2步骤更改
    
***

### 9. windows下tomcat启动log信息乱码：
###### 这是由于`windows`的编码格式与`tomcat`中`log`的编码格式不同
###### windows的编码为`GBK`，查看`tomcat`中`conf/logging.properties`:
    java.util.logging.ConsoleHandler.encoding = UTF-8
###### 改为`GBK`即可

***

###10. Shiro与AspectJ冲突
###### 出现错误
    error at ::0 formal unbound in pointcut 
    未解决