# Zoo
### Для запуска проекта
  - JDK 1.8
  - Idea-> Plugins -> Lombok -> Install
  - Idea -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Proccesor -> Enable annotation Proccesing
### Git Branching 
  - Новые ветки с изменениями должны называться feature/task-name! (пример: feature/telegram-bot)
  - Изменения в master только через pull request, в тайтле указать суть изменений, либо название задачи из trello
  - Прежде чем начать работу над задачей :
      - git checkout master
      - git pull origin master
      - git checkout -b feature/task-name
  - По завершении создать пул реквест и прикрепить в trello в комментарий к задаче
  
### Полезные ссылки
**Trello**  https://trello.com/b/TACgpFnq/zoo  
**Тестовая H2 DB**  http://localhost:8080/h2-console

### Как готовить Docker
  1. Для билда нужен собранный jar файл, для его получения <code>mvn clean install</code>
  2. Билд команда для докер: <code>docker build -t <imageName> . </code>
  3. Для этого проекта нужны переменные среды, для докера можно использовать обычный txt файл, запись вида Var=Val, можно найти в Trello
  4. Команда запуска: <code> docker run -p 127.0.0.1:8080:8080 —env-file <envfile.txt> <imageName> env </code>
  5. Теперь для запуска необходим второй образ с букинг сервисом
  6. Запуск с помощью <code> docker-compose -f stack.yml up </code>
### Layering  
![alt text](https://i.imgur.com/h6IBLvF.png)


### Repsy guide
  1. В Intellij Idea Settings->Maven->Override User settings file на settings.xml в корне проекта
  2. Добавить в переменные среды операционной системы логин и пароль для Repsy (REPSY_LOGIN & REPSY_PASSWORD)
  3. Найти новые переменные можно в трело
  4. При обновлении dto модуля следует поднять revision и запушить новую версию в repsy (mvn deploy)
![alt text](https://i.imgur.com/8eXIeqj.png)

### SMSC 
Для того, чтобы была возможность смс рассылки необходимо внести в разрешённые IP-адрес сервера, с которого запускается проект,
либо зарегестрироваться самостоятельно на https://smsc.ru/(при этом не забыв изменить переменные окружения)

