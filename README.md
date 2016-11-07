#代码    

实体类没在一个包下    

自定义的PopupWindow类分包不对    

工具类和帮助类放到同一个包下,StringHelper分包不对    

添加好友/注册的页面怎么不用MVP去写    

AddFriends类命名不对    

LayoutInflater不要使用Application的Context去初始化,需要使用Activity对象    

ui包下的类需要重新分包    

整体的进度需要再快点

--------

Application new一个出来的原因是什么

PreferenceManager分包不对,不需要写成单例

MainActivity对象里面有个静态的MainActivity?这不内存泄露么?

MVP使用的不好,

RegisterPresenter,线程处理的部分最好放到M层

工具类放的位置不正确,分包有问题.

