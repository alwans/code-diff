#!/usr/bin/env bash

if [ $# -eq 2 ]; then
	echo "开始获取commit id"
else
	echo "参数个数错误"
	exit 2
fi


# define branch name
typeset BRANCH
BRANCH=$1 || exit 2

# define project id
typeset PROJECT_ID
PROJECT_ID=$2 || exit 2

# define current path
typeset PATH
PATH=${PWD}

# define curl path
typeset CURL
#CURL=`which curl`


# define coverage server host
typeset HOST
HOST="127.0.0.1"

# define coverage serer port
typeset PORT
PORT="1990"

echo "branch name: ${BRANCH}"
echo "project id: ${PROJECT_ID}"
echo "current path: ${PATH}"
#echo "curl path: ${CURL}"

result=$(/usr/bin/curl -N -s "http://${HOST}:${PORT}/api/file/copy/project?path=${PATH}&branch=${BRANCH}&id=${PROJECT_ID}")

echo "响应内容： ${result}"

code=$(echo ${result} | /usr/bin/python -c "import json; import sys; obj=json.load(sys.stdin); print obj['code']")

data=$(echo ${result} | /usr/bin/python -c "import json; import sys; obj=json.load(sys.stdin); print obj['data']")

if [ ${code} -eq 10000 ]; then
	echo "请求成功，commitId: ${data}"
	/usr/bin/cat /dev/null > commit.txt
	echo "${data}" >> commit.txt
	exit 0
else
	echo "请求失败"
	exit 1
fi
