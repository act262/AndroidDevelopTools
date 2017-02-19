Android开发助手README
===

本应用设置方式参考
`系统设置模块`[Settings](https://android.googlesource.com/platform/packages/apps/Settings/)模块
`Framework`[cmds](https://android.googlesource.com/platform/frameworks/base/)模块

---

大部分功能不能直接使用，可用反射方式设置系统属性，部分功能需要root权限才能正常使用。

---

### SystemProperties
系统属性设置,可以在shell中调用,方便测试使用

```shell
# 获取所有的键值对
getprop
# 获取指定键的值
getprop key
# 设置指定键值对
setprop key value
```

### Settings.System、Settings.Global、Settings.Secure
全局设置,需要系统权限，所以在执行时以root用户执行的命令
对应的shell命令`settings`命令
```shell
settings list system
settings get system show_touches
settings put system show_touches 0
```


---

*感谢万能的[IconFont](http://www.iconfont.cn/plus/home/index)，本应用所有图标资源均来自[IconFont](http://www.iconfont.cn/plus/home/index)。*