
# AUTH 
## 发送验证码
### 入参
```json
{
    "phone": "1387027644"
}
```
### 响应参数
```json
{
    "success": true,
    "message": null,
    "errorCode": null,
    "data": null
}
```


### 流程图
```mermaid
flowchart TD
	A(开始) --> B[请求短信验证接口]
	B --> C[构建短信 redisKey]
	C --> D@{shape: hex, label: 判断 Redis 中是否存在 redisKey}
	D --> |是| E[返回错误信息，提醒用户请求频繁请求]
	E --> END(结束)
	D --> |否| H[生成6 位验证码]
	H --> F[调用第三方接口发送短信验证码]
	F --> G[通过 redisKey 将验证码存入redis]
	G --> END	
```
## 登录与注册
### 入参
```json
{
    "phone": "13870276442", 
    "code": "119903", 
    "password": "xx", 
    "type": 1 
}
```

### 响应参数
```json
{
	"success": true,
	"message": null,
	"errorCode": null,
	"data": "xxxxx" 
}

```

```mermaid
flowchart TD
  a(开始) --> b[接收用户请求]
  b --> d@{shape: hex, label: 是否为密码登录}
  d --> |是| e[从数据库查询用户]
  e --> f@{shape: hex, label: 用户是否存在}
  f --> |否| END(结束)
  f --> |是| g[校验密码]
  g --> h@{shape: hex, label: 密码正确}
  h --> |是| i[返回 token]
  i --> END
  h --> |否| END
  d --> |否| j@{shape: hex, label: 校验验证码是否正确}
  j --> |是| k@{shape: hex, label: 查询当前手机号是否注册}
  k --> |是| i
  j --> |否| END
  k --> |否| l[注册新用户]
  l --> i
```

## 同步角色-权限信息
```mermaid
flowchart TD
  a(服务启动) --> b[ApplicationRunner run 方法的调用]
  b --> c[从数据库中查询所有角色-权限关系]
  c --> d[从数据库中查询所有权限信息]
  d --> e[构建角色-权限关联信息集合]
  e --> f[推送到 redis]
  f --> g(结束)
```
