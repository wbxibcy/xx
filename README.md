# xx
# 2023-11-20

## ssh登录阿里云(密钥文件)

```shell
chmod 400 key.pem
ssh -i key.pem 用户名@公网ip
```

## 密码安全性

### OAuth2

OAuth2是一个规范，它定义了几种处理身份认证和授权的方法。

### OpenID Connect

OpenID Connect 是另一个基于 **OAuth2** 的规范。

它只是扩展了 OAuth2，并明确了一些在 OAuth2 中相对模糊的内容，以尝试使其更具互操作性。

例如，Google 登录使用 OpenID Connect（底层使用OAuth2）。

但是 Facebook 登录不支持 OpenID Connect。它具有自己的 OAuth2 风格。

## 如何使用

### 后端

使用`fastapi`来搭建

### 前端

使用`Java+xml`来搭建



### 
