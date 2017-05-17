# Windows上使用Python增加或删除权限

在使用Python在 Windows 平台上开发的时候, 有时候我们需要动态增加或删除用户的某些权限, 此时我们可以通过 AdjustTokenPrivileges API 来实现。

比如，我们要给用户分配 SE_TCB_NAME 权限

``` python
flags = win32security.TOKEN_ADJUST_PRIVILEGES | win32security.TOKEN_QUERY
token = win32security.OpenProcessToken(win32api.GetCurrentProcess(), flags)
id = win32security.LookupPrivilegeValue(None, win32security.SE_TCB_NAME)
privilege = [(id, win32security.SE_PRIVILEGE_ENABLED)]
print win32security.AdjustTokenPrivileges(token, False, privilege)
```

比如，我们要给用户去除 SE_TCB_NAME 权限

``` python
flags = win32security.TOKEN_ADJUST_PRIVILEGES | win32security.TOKEN_QUERY
token = win32security.OpenProcessToken(win32api.GetCurrentProcess(), flags)
id = win32security.LookupPrivilegeValue(None, win32security.SE_TCB_NAME)
privilege = [(id, 0)]
print win32security.AdjustTokenPrivileges(token, False, privilege)
```
