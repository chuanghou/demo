### 安装JDK
```bash
yum install -y java-1.8.0-openjdk-devel.x86_64
```

### 安装docker
```bash
yum install -y docker
```

### 启动docker
```bash
systemctl start docker
```

### 查看docker状态
```bash
systemctl status docker
```

### 修改docker镜像（可选）修改(注册了阿里云账户可以用)
[阿里云镜像服务](https://help.aliyun.com/zh/acr/user-guide/accelerate-the-pulls-of-docker-official-images?spm=5176.ecs-console-storage_imageList.top-nav.5.50f34df5P8LrtI&scm=20140722.S_help%40%40%E6%96%87%E6%A1%A3%40%4060750.S_BB2%40bl%2BRQW%40ag0%2BBB1%40ag0%2Bhot%2Bos0.ID_60750-RL_docker%E9%95%9C%E5%83%8F%E5%8A%A0%E9%80%9F%E5%99%A8-LOC_consoleUNDhelp-OR_ser-V_2-P0_0)
```bash
/etc/docker/daemon.json
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
"registry-mirrors": ["https://3vk9j1li.mirror.aliyuncs.com"]
}
sudo systemctl daemon-reload
sudo systemctl restart docker
```bash

```bash
docker run -p 3306:3306 -name mysql -v ~/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --privileged=true docker.io/mysql
```





