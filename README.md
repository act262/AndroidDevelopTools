Android开发助手
===


采用反射方式设置系统属性


### SystemProperties
系统属性设置,可以在shell中调用,方便测试使用

```
# 获取所有的键值对
getprop
# 获取指定键的值
getprop key
# 设置指定键值对
setprop key value
```

### Settings.Global
全局设置,有些设置需要安全权限和系统相同UID,会导致有些设备不能安装.