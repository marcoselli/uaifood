# UaiFood

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)


> Fast food self-service project created to support the expansion of the UaiFood restaurant.

## 📝 Documentation

[UaiFood Miro](https://miro.com/app/board/uXjVLKN9J9Y=/)

## 🚀 Running uaifood

Be sure that you have installed:
- Docker
- Docker Compose (v2.1 or higher)

After that, run the commands at the root path of the project:

1. Create a local image

```
docker build --no-cache -t uaifood:2.0.0 .

```

2. Start the project

```
docker-compose up

```

## ☕ Using uaifood

After running the commands ahead you can access the endpoints at: http://localhost:8080/swagger-ui/index.html
