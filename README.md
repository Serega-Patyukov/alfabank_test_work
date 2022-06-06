***
## Тестовое задание
```
        Создать сервис, который обращается к сервису курсов валют, и отображает gif:
        
    • если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную отсюда 
https://giphy.com/search/rich
    • если ниже - отсюда https://giphy.com/search/broke
        Ссылки
    • REST API курсов валют - https://docs.openexchangerates.org/
    • REST API гифок - https://developers.giphy.com/docs/api#quick-start-guide
        Must Have
    • Сервис на Spring Boot 2 + Java / Kotlin
    • Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), 
туда передается код валюты по отношению с которой сравнивается USD
    • Для взаимодействия с внешними сервисами используется Feign
    • Все параметры (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) 
вынесены в настройки
    • На сервис написаны тесты (для мока внешних сервисов можно использовать @mockbean или WireMock)
    • Для сборки должен использоваться Gradle
    • Результатом выполнения должен быть репо на GitHub с инструкцией по запуску
        Nice to Have
    • Сборка и запуск Docker контейнера с этим сервисом
```
***
## Запуск
- Склонировать репозиторий, выполнив команду:   
  `git clone https://github.com/Serega-Patyukov/alfabank_test_work.git`
- Перейти в корневую папку проекта и собрать проект:    
  `./gradlew build`
- Запустить:

- `./gradlew bootRun`
***
- Собрать докер-образ:    
  `./gradlew bootBuildImage --imageName=springio/alfabank_test_work
  `
- Запустить контейнер с созданным образом:   
  `docker run -p 8080:8080 -d -t springio/alfabank_test_work`
***
## Endpoints
- `/images/gif`  
  **Parameters**   
  str: RUB   
  **_Пример_**   
  `http://localhost:8080/images/gif?str=RUB`
------
## Oбраз с DockerHub
- `docker pull seregapatyukov/alfabank_test_work`
***
## Примечание
```
При запуске через докер адрес может быть похож на этот http://192.168.99.100:8080/images/gif?str=RUB
```
```
Описание полей есть в application.properties.
```

```
При сборке командой ./gradlew build будет ошибка.

Это нормально. Просто тесты не прошли проверку, а это потому что в тестах 
отключены моки.
Для тестирования нужно будет переключить внешние сервиси, но об этом ниже.

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':test'.
> There were failing tests. See the report at: file:///E:/alfa/alfabank_test_work/build/reports/tests/test/index.html

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at https://help.gradle.org
```

```
Для успешного выполнения тестов, в application.properties нужно переключить внешиние сервисы на моки.
Для успешного выполнения тестов должно быть так:

            #openexchangerates.url.general=https://openexchangerates.org/api
            openexchangerates.url.general=http://localhost:8089/openexchangerates.org/api

            #giphy.url.general=https://api.giphy.com/v1/gifs
            giphy.url.general=http://localhost:8089/api.giphy.com/v1/gifs
```
```

```