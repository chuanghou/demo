cd ~
# shellcheck disable=SC2006
pid = `ps -ef|grep cluster-app.jar |grep -v grep|awk '{print $2}'`
kill -15 pid
rm -rf *


