# Windows上使用Python给用户增加或删除安全策略

在使用Python在 Windows 平台上开发的时候, 有时候我们需要动态增加或删除用户的某些访问策略, 此时我们可以通过LsaAddAccountRights 和LsaRemoveAccountRights API来实现。

比如，我们要给用户分配SE_SERVICE_LOGON_NAME安全策略

``` python
try:
    user = 'admin'
    policy_handle = win32security.LsaOpenPolicy(None, win32security.POLICY_ALL_ACCESS)
    sid, domain, account_type = win32security.LookupAccountName(None, user)
    win32security.LsaAddAccountRights(policy_handle, sid, (win32security.SE_SERVICE_LOGON_NAME,))
    win32security.LsaClose(policy_handle)
except pywintypes.error as e:
    print e
```

如果需要删除用户的的某个安全策略

``` python
try:
    user = 'admin'
    policy_handle = win32security.LsaOpenPolicy(None, win32security.POLICY_ALL_ACCESS)
    sid, domain, account_type = win32security.LookupAccountName(None, user)
    win32security.LsaRemoveAccountRights(policy_handle, sid, False, (win32security.SE_SERVICE_LOGON_NAME,))
    win32security.LsaClose(policy_handle)
except pywintypes.error as e:
    print e
```

最后看看查询用户安全策略

``` python
try:
    user = 'admin'
    policy_handle = win32security.LsaOpenPolicy(None, win32security.POLICY_ALL_ACCESS)
    sid, domain, account_type = win32security.LookupAccountName(None, user)
    print win32security.LsaEnumerateAccountRights(policy_handle, sid)
    win32security.LsaClose(policy_handle)
except pywintypes.error as e:
    print e
```

* 对于查询API，LsaEnumerateAccountRights API返回的是这个用户拥有的Policy tuple
，但是这里有一点需要注意，当用户没有任何Policy的时候，LsaEnumerateAccountRights API返回的并不是一个空的tuple，而是会抛出一个pywintypes.error错误，如下：

``` shell
(2, 'LsaEnumerateAccountRights', 'The system cannot find the file specified.')
```
