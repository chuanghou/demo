
ps -ef | grep java | grep MilkyDemoApplication | awk '{print $2}' | xargs kill -15

script_path=$(cd $(dirname $0);pwd)

nohup java -cp "./bin/*:./lib/*" com.stellariver.milky.demo.MilkyDemoApplication -Dstatic.resource="${script_path}/static" >/dev/null 2>&1 &