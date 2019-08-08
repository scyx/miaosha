# 高并发秒杀系统设计与优化

-------------------

[TOC]

## 项目简介

本项目是针对在大并发的秒杀场景下的设计与优化。

**技术架构图如下所示**
![](doc-material\md\assets\项目技术结构.png)

##  快速启动

1. 第一步 克隆仓库到本地
```
git clone 
```

2.  初始化数据库，sql文件为resoruces/miaosha.sql
3.  构建工程
```
mvn clean package
```
4.  启动工程
```
java -jar miaosha2.jar
```
**项目入口为 http://localhost:8090/login/to_login**
用户名 13100000000 密码 123456
## 数据库设计
事实上，实际的数据库应该比这个复杂的多，这里我们的目的不是为了让数据库多复杂，而是好而多的对这个项目进行优化，提高QPS。
![](doc-material\md\assets\项目技术结构.png)
## 主要解决的问题
**秒杀场景下主要面对的问题有**
1.最基本的模块：登录注册，商品列表，商品详情，秒杀功能，成功以后查看订单
2.分布式session
3.优化手段
4.消息队列，主要是异步下单
5.提高QPS
6.接口安全，限流防刷(暂未实现)

## 登录功能
**明文密码两次MD5加密**
一次前端加密，加密后的值传给后端再一次加密，前端采用固定salt值，后端应该采用随机的salt值加密，这样更加安全。如果不加密，http传输的数据被截取或者数据库被攻击破解之后，用户的信息将被泄漏。两次加密，如果前端的MD5算法被破解，后端还有一次加密，保证安全。
**分布式Session**
用户登录成功以后将用户信息存在redis中，生成一个token返回给浏览器，客户端以后再访问服务器的时候只需要根据token去redis中去取用户信息就可以了，由于redis的访问速度远大于mysql数据库，减少了数据库的访问压力。
## 优化手段：页面优化
**本项目采用页面缓存+页面静态化+对象缓存**
- 什么是**页面缓存**？
**页面缓存**这个技术在这个项目是针对商品列表这种变化不大的页面来使用的，Springboot的模板引擎Thymeleaf，每次访问Thymeleaf页面商品列表，都要去查数据库，把商品列表查出来，通过Model添加对象来填充页面返回给客户端，在大量的请求过来的情况下，如果每次都做这么一件事，即使单次的开销不是很大，但是在短时间内的成千上万的请求还是会对服务器数据库造成一定的压力。但是在秒杀这个场景中，不仅请求多，而且时间短，商品列表的页面在短时间内可能也不会怎么变化，如果秒杀商品数量小的话，当我们看到页面变化的时候，商品可能已经被秒杀完了，所以可以把这个整个页面缓存到redis中，每次都先到redis中取，没有了再去查数据库渲染，然后再存到redis中，可以减轻数据库的压力。

- 在这里我们主要是针对商品列表页使用页面缓存，因为商品列表页面变化不大，把它存到redis中中，只要redis中有这个页面，就不再通过Thymeleaf渲染返回了，直接从redis中返回，至于页面缓存用到的就是ThymeleafResolver来实现的，这个ThymeleafResolver的作用我理解的就是可以把渲染后的.html解析成Thymeleaf模板视图，解析之后返回的是一个String类型的字符串，而这段字符串就可以存到redis中，返回给客户端时客户端会自动渲染，具体实现在com.miaoshaproject.controller.GoodsController的toList方法中。这里的理解就是在实际的秒杀场景中，商品列表应该是分页显示的，结合实际情况看，我们在看到秒杀商品时，我们可能只是会去点个前一两页翻翻看，所以我觉得在商品分页显示并且比较多的情况下，缓存前几页效果会非常好。因为大部分用户可能只看前几页。
	
- 什么是**页面静态化**？
百度百科关于页面静态化的解释：**静态页面是网页（html、htm）的代码都在页面中，不需要执行asp,php,jsp,.net等动态语言而生成客户端网页代码的网页**。页面静态化也就是纯html写的，说白了就是ajax异步向服务器请求数据来渲染页面，不通过模板引擎渲染页面，这里把商品详情页面和订单详情页面静态化，每次只要根据商品id去取数据就可以，静态化的好处是可以让浏览器缓存这些页面，在秒杀开始前，许多用户会不停的刷新页面来看到底开没开始，而商品详情页在秒杀开始之前一般不会变，所以静态化的好处是每次不用从服务器拉页面了，用户看到的是本地缓存的页面，这点在运行时用火狐浏览器打开商品详情页刷新，可以看到返回的状态码是304,304的意思就是服务端的资源并没有发生变化，客户端可以继续使用浏览器缓存，减少了网络开销和请求次数。

- 什么是**对象缓存**？
字面意思，就是把对象给缓存到redis中，这里缓存的主要是用户，粒度更细，在登录时检查用户的用户名是否存在，先去redis缓存中拿，有了就返回，没有就再去数据库中拿，如果存在，就存到redis中再返回，不存在就返回错误。在前面的redis实现的session共享中说过，token是存在redis中保存用户信息的，所以登录后的user对象会被缓存到redis中，登录检查用户名也可以用这个方法实现，先去缓存中拿对应用户名的user，有就返回，没有就去数据库拿，如果有就存缓存返回，没有就返回用户名不存在的错误。
##秒杀功能实现及优化
除了最基本的功能实现之外，秒杀面对的问题有两个，一个是卖超问题，一个是大并发。卖超包括大并发下的商品库存小于0还可以被秒杀的情况，另一种情况是同一个用户刷接口能买到两个商品的问题。我们先看秒杀的基本流程，一个用户点击了秒杀，先判断他的token有没有过期，如果没过期就开始进行秒杀流程：1.判断商品库存是不是大于0  2.判断他是不是已经秒杀到了这个商品（这里所有的商品数量都限制为1）如果没有秒杀到，执行秒杀流程：减库存，生成订单，这两个操作当然是原子操作，所以这里是一个事务。其中这里面有很多问题，也有很多可以优化的点。
### 优化过程
在减库存生成订单的操作之前会有一步就是看库存是不是大于0；现在如果库存有1个，同时来了两个请求，都看到了库存是1，那么两个请求都可以执行下面的语句
```
update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} ;
```
获取行锁执行秒杀操作，库存-2变成-1了就为负数了，这显然不合理。而在超高并发下同时过来的可能几百几千个请求，那么就超卖了几千个，其实这条语句是有问题的，在减库存这条语句中还应该判断一下当前库存是否大于0（或者大于当前购买数），正确的应该在后面加上库存数量判断，
```
update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0;
```
后来我也通过压测验证了，确实不会发生超卖现象。至少在5000个线程循环十次的条件下测试十次没有超卖。
### Redis加载库存，预减库存，查找订单，内存标记
在项目启动时把秒杀商品库存数量加载到redis中，在秒杀操作执行时，先对redis预减库存，redis库存不是大于0那就可以直接返回了，不用查数据库了，减少了对数据库的访问压力，订单也不仅生成在数据库中，也生成在redis中
什么是**内存标记**？
内存标记就是在项目启动时，用一个Hashmap，Key可以存为秒杀商品id，Value初始化为false，一个键值对的意思就是这个商品是否被秒杀完了，在查redis的时候，如果商品数量不是大于0了，就可以把该商品id对应的Value直接设置成true，表示该商品秒杀完了，在每次秒杀操作的时候都先看对应商品在这个Hashmap的Value是不是true，如果是true直接返回秒杀完了，连redis都不用访问了，虽然访问redis非常快，但是还是会有一定的网络开销，这个步骤可以减少redis访问，减少网络开销。

### RabbitMQ异步下单
消息队列的常见使用场景和效果就是异步，解耦，削峰但是他也有缺点
缺点：
系统可用性降低
系统引入的外部依赖越多，越容易挂掉。本来你就是 A 系统调用 BCD 三个系统的接口就好了，人 ABCD 四个系统好好的，没啥问题，你偏加个 MQ 进来，万一 MQ 挂了咋整，MQ 一挂，整套系统崩溃，不就完了？
系统复杂度提高
硬生生加个 MQ 进来，你怎么保证消息没有重复消费？怎么处理消息丢失的情况？怎么保证消息传递的顺序性？头大头大，问题一大堆，痛苦不已。
本项目只是简单的使用了RabbitMQ的direct模式，在秒杀操作执行的时候，如果库存大于0并且没有秒杀过，就入队一个含用户id和商品id的消息，返回等待中，客户端每零点几秒轮询一次，看是否成功秒杀到，生成订单
