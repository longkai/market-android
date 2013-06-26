## 2013学年度软件工程项目
这是广西大学计算机与电子信息学院计科102班软件工程课程[神舟10号]小组课程设计。

这里包含的实 Android客户端部门的所有文件，代码等。

服务端的所有文件请见[这里][]。

## 最低要求
Android 2.2+ (推荐4.2.2)

## 用到的类库
[ActionbarSherlock][] 需要自行添加。
[SlidingMenu][] 需要自行添加。

## how to build
首先，必须搭建好Android的开发环境，推荐下载[ADT BUNDLE][]，开箱即用(装好jdk的情况下)。

其次，懂得如何将第三方的类库编译加载到本项目中。

举个例子，假设我们使用ADT

由于我们使用了ActionbarSherlock，那么我们首先需要下载它的源码

有两种方式，如果使用git

`git clone git@github.com:JakeWharton/ActionBarSherlock.git`

或者浏览器输入 *https://github.com/JakeWharton/ActionBarSherlock/archive/master.zip*

然后依次：

`File -> Import... -> Existing Android Code Into Workspace -> 进入刚才下载的库的根目录 ->
勾选除sample以外的(暂时不需要哈) -> done`

接下来adt会对刚才导入的库编译，然后我们必须右击刚才导入的项目

`Properties -> Android -> 勾选 Is Library 并且勾选上方最新的Build Target -> Apply -> Ok`

接下来，对我们的项目引入该库

导入本项目(和刚才导入ActionBar)一样，完成后右击项目：

`Properties -> Android -> Add... -> 将刚才编译的actionbar导入即可`

注意，最好把原来的给删除再添加。

其他的依赖依次类推。

*SlidingMenu* 需要修改一处源代码，(但是可能你下载的已经修复了，所以如果有错误的话)
`com.jeremyfeinstein.slidingmenu.lig.app.SlidingFragmentActivity.java`
可能需要将`extends FragmentActivity` 修改成 `extends SherlockFragmentActivity`

所有的库引入之后，选择一个模拟器或者真机即可运行。

## 非常重要
由于没有实际的服务器，如果你想访问服务端的话，必须搭建好服务端，组成一个局域网。

之后，获取服务端的ip地址，然后修改本项目的源代码。

在uitl包下的C.java *70*行左右，将 *DOMAIN* 换成你的服务器环境中的ip地址再运行!


## Contact us
If you have any questions, please feel free to [drop us a line][]!

## license
Copyright 2013 Department of Computer Science and Technology, Guangxi University

This project is released under the [MIT License][].

[这里]: https://github.com/longkai/gxu_software_engineering_2013
[drop us a line]: mailto:im.longkai@gmail.com
[ActionbarSherlock]: http://actionbarsherlock.com
[MIT License]: http://opensource.org/licenses/mit-license.php
[SlidingMenu]: https://github.com/jfeinstein10/SlidingMenu
