# Windows平台使用Python切换用户运行程序

在Windows平台，如果需要以某个指定用户来运行程序，此时可以通过使用 Windows CreateProcessAsUser API来实现。但是要实现这个功能首先需要有要切换用户的用户名和密码。

``` python
import psutil
import win32process
import win32security
import win32con
import win32api
import win32service
import win32file
import win32profile
import pywintypes

try:
    username = 'admin'
    password = 'admin'
    domain = ''
    token = win32security.LogonUser (
        username,
        domain,
        password,
        win32con.LOGON32_LOGON_SERVICE,
        win32con.LOGON32_PROVIDER_DEFAULT
    )
    win32security.ImpersonateLoggedOnUser(token)

    cmd = '<a command line programe>'
    cwd = '<a work dir>'
    env = os.environ
    priority = win32con.NORMAL_PRIORITY_CLASS
    startup = win32process.STARTUPINFO()

    (hProcess, hThread, dwProcessId, dwThreadId) = \
        win32process.CreateProcessAsUser(token, None, cmd, None, None, True, priority, env, cwd, startup)

    # win32process.GetExitCodeProcess(hProcess) == win32con.STILL_ACTIVE
    process = psutil.Process(dwProcessId)
    return_code = process.wait()
    print 'return code: %s' % str(return_code)
except win32security.error as e:
    print e
```

- 要使上面的程序生效，首先需要使用一个windows service来执行这个程序（目前我是这么测试的，有人说也可以在命令行以administrator来运行，但是我没有测试成功）。关于Python创建Windows Service的方法，可以参考我前面文章。
- 要切换用户执行脚本，需要有被切换用户的用户名和密码。
- 在用 CreateProcessAsUser API执行命令后，可以通过 GetExitCodeProcess api 来检查进程状态，如果返回结果是 win32con.STILL_ACTIVE，我们可以继续等待。
- 显然，通过 GetExitCodeProcess api来检查进程是否退出不是很好用，所以这里我使用了psutil库来等待进程退出。

> 参考
> - http://timgolden.me.uk/pywin32-docs/contents.html
