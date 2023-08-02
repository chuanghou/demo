
ps -ef | grep java | grep MilkyDemoApplication | awk '{print $2}' | xargs kill -15

script_path=$(cd $(dirname $0);pwd)

nohup java -cp "./bin/*:./lib/*" -Dstatic.resource="${script_path}/static/" com.stellariver.milky.demo.MilkyDemoApplication > console.log 2>&1 &
