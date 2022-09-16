### 步骤

   - 围栏数据上传到服务器 /tmp/iscsi/test/
   - 点击围栏查询,获取 Cookie 
   - tar -zcvf test.tar.gz test/*
   - nohup jdk1.8.0_241/bin/java -jar demo-1.0.0.jar --spring.profiles.active=test &

Quick setup — if you’ve done this kind of thing before
or
https://github.com/15751064254/java-utils.git
Get started by creating a new file or uploading an existing file. We recommend every repository include a README, LICENSE, and .gitignore.

…or create a new repository on the command line
echo "# java-utils" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/15751064254/java-utils.git
git push -u origin main
…or push an existing repository from the command line
git remote add origin https://github.com/15751064254/java-utils.git
git branch -M main
git push -u origin main
…or import code from another repository
You can initialize this repository with code from a Subversion, Mercurial, or TFS project.

ProTip! Use the URL for this page when adding GitHub as a remote.