This is a small program that demonstrate how to get pid of the current process or parent process using JNA (general JNI)
See net.java.dev.jna:jna:last_version

Another option is to use of ProcessHandler from java 9+


In my case parent process is the java IntellijIdea app (using for running).
```
linux get process info:
ps -q $PID -eo euser,ruser,suser,fuser,f,comm,label

linux get process group by parent:
htop -p `pstree -p $PID | perl -ne 'push @t, /\((\d+)\)/g; END { print join ",", @t }'`
```