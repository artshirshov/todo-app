# Todo Application

## Описание
Это простая реализация Todo приложения для практики в Kotlin и TypeScript

<img src="assets/main.png" width="300" alt="Main Page"><img src="assets/edit.png" width="300" alt="Edit Task Page">

## Запуск
### Backend
```shell
cd todo-api
./gradlew generateJooqClasses
./gradlew bootJar
java -jar build/libs/todo-api-0.0.1-SNAPSHOT.jar
```

### Frontend
```shell
cd todo-ui
npm install
npm run generate
npm run dev
```

## Технологии
### Backend
- Kotlin
- Spring Book
- JOOQ
- Flyway
- PostgreSQL
- MapStruct

### Frontend
- TypeScript
- React
- Orval
- Vite