 1. Удаление лекарства (DELETE + каскадные изменения)

GraphQL Mutation:

```
    graphql
        mutation {
            deleteMedication(id: "14e6f5c9-91e6-4ac6-98ad-bf701f3591c6") {
                id
                name
        }
    }
```

Что происходит:

1. Удаляет лекарство из medication-service.
2. Через gRPC вызывает inventory-service для удаления остатков в аптеках.
3. Возвращает список затронутых записей.

 2. Фильтрация лекарств с пагинацией (GET)

GraphQL Query:
```
graphql
    query {
        medications(
            filter: {
                isPrescriptionOnly: true,
                categories: ["Антибиотики", "Обезболивающие"]
            },
        pagination: { page: 1, size: 10 },
        sort: { field: "price", direction: DESC }
        ) {
            items { id name price }
            totalCount
        }
    }
```
 3. Поиск лекарств по составу (GET + nested фильтры)

```
graphql
    query {
      medications(
        filter: {
          activeSubstances: {
            name: "парацетамол"
            concentration: { gt: 500 }
          }
        }
      ) {
        items {
          id
          name
          substances {
            name
            concentration
          }
        }
        totalCount
      }
    }
```
 4. Агрегация статистики по аптеке (GET + агрегация)
```
graphql
    query {
        pharmacyStatistics(id: "P001") {
            totalMedications
            averagePrice
            stockByCategory { category count }
        }
    }
```
 5. Создание заказа с валидацией (POST)
```
graphql
    mutation {
        createOrder(input: {
        userId: "123",
        items: [
            { medicationId: "456", quantity: 2 },
            { medicationId: "789", quantity: 1 }
        ],
        deliveryAddress: "ул. Пушкина, 10"
        }) {
            id
            total
            status
            medications { name price }
        }
    }
```
Валидация:

* Проверка наличия лекарств (inventory-service).
* Проверка лимита заказов пользователя (user-service).

 6. Обновление цены лекарства (PUT + история изменений)
```
graphql
    mutation {
        updateMedicationPrice(id: "456", newPrice: 150.99) {
            id
            name
            oldPrice
            newPrice
            updatedAt
        }
    }
```
 7. Удаление просроченных лекарств (DELETE + batch-операция)
```
graphql
    mutation {
        deleteExpiredMedications(expiryDate: "2024-01-01") {
            count
            deletedIds
        }
    }
```
 8. Получение лекарств с группировкой по аптекам (GET + группировка)
```
graphql
    query {
        medicationsByPharmacy(
            filter: { category: "Витамины" },
                groupBy: "pharmacyId"
            ) {
                pharmacyId
                items { id name }
            }
    }
```
 9. Поиск пользователей с их заказами (GET + глубокие связи)
```
graphql
    query {
        users(
            filter: { role: "CUSTOMER", registrationDate: { after: "2023-01-01" } },
            pagination: { page: 1, size: 5 }
        ) {
            items {
                id
                name
                orders(status: "COMPLETED") {
                    id
                    medications { name }
                }
            }
        }
    }
```
 10. Удаление пользователя с отменой заказов (DELETE + транзакция)
```
graphql
    mutation {
        deleteUser(id: "123") {
            id
            name
            cancelledOrders { id }
        }
    }
```
Логика:

* Отменяет заказы через order-service.
* Удаляет пользователя в user-service.

 Архитектура:

* Gateway: GraphQL (Spring Boot + DGS Framework)
* Микросервисы: gRPC (Java 17 + Spring Boot 3.2)
* Базы: PostgreSQL, MongoDB

Стек технологий (Java 17 + Spring)

|Компонент | Технологии|
|----------|:-----------:|
|Gateway	|Spring Boot 3.*, GraphQL Java + DGS (Netflix), gRPC Stubs|
|Микросервисы | Spring Boot 3.*, gRPC (Netty), JPA/Hibernate, Flyway, Lombok|
|Базы данных	| PostgreSQL (для medication, order), MongoDB (для inventory)|
|Service Discovery	| HashiCorp Consul|
|Билд	| Gradle (Kotlin DSL), Docker (Docker Compose локально)|

Старт проекта

Требования:

* Docker + Docker Compose

* Python 3.11 (user-service)

* Java 17

Первый запуск (build + run)

```bash
cd infra

docker-compose build # для быстрой сборки без команды clean
docker-compose build --build-arg CLEAN_BUILD=true        # Собираем всё (gateway, medication, order, inventory, user)
docker-compose build --build-arg CLEAN_BUILD=true --no-cache # Если нужно всё перебилдить (CLEAN_BUILD искать в Dockerfile)

# Запуск всех сервисов
# Создастся Consul, базы данных, gRPC/веб-сервисы

docker-compose up
```
Когда нужно rebuild:
```bash
# Если поменяли .proto/код Java/Python
# пересобрать:
docker-compose build
```
Просмотр всех логов
```bash
# Все логи
docker-compose logs -f

# Логи одного сервиса
docker-compose logs -f gateway
```

Остановка:
```bash
# Остановить все сервисы
docker-compose down

# Остановить и удалить volume'ы (БД будут удалены)
docker-compose down -v
```
Если докер съел много памяти:
```bash
docker system prune --volumes -f
```
Работа с .proto

Файлы .proto хранятся в корне каждого сервиса.

Java-сервисы сами генерируют java-код в ходе ./gradlew build

Python (user**-service):
```bash
# Генерирует Python-код из proto:
./generate_proto.sh
```
Утилиты

* http://localhost:8500 — UI для Consul

* http://localhost:8080/graphql — GraphQL Gateway (DGS)

Типовые ошибки:

* если сборка медленная — смотри volume *gradle-cache
* если повторяющиеся protoc ошибки в user-service — стоит очистить __pycache__, **.pyc

Потестировать через Postman можно скачав [json-коллекцию запросов](./postman_collection.json)


Что делать, если докер съел все ресурсы на Windows и команда docker system prune --volumes -f не помогла
(ВНИМАНИЕ: после всех манипуляций демон докера будет отключен. Обычно перезагрузка компьютера помогает) :

1. Найти файл ext4.vhdx - это виртуальный диск WSL2, на котором Docker Desktop хранит все свои данные: контейнеры, образы, volume’ы и прочее.
2. Остановить WSL. Надо открыть PowerShell от имени администратора и выполнить:
```
wsl --shutdown
```
3. Если есть критичные данные, то лучше сделать бэкап и сохранить ext4.vhdx в надёжное место на случай, если что-то пойдёт не так.
4. Открываем Compact (для этого нужен установленный Hyper-V (это встроено в Windows Pro, но не в Home)) :
```
Optimize-VHD -Path "C:\Users\MaDeLa\AppData\Local\Docker\wsl\data\ext4.vhdx" -Mode Full
```
Возможные ошибки на Windows 10 PRO:
1. 
   ```
   + Optimize-VHD -Mode Full
   + ~~~~~~~~~~~~
   + CategoryInfo          : ObjectNotFound: (Optimize-VHD:String) [], CommandNotFoundException
   + FullyQualifiedErrorId : CommandNotFoundException
   ```
Команда не найдена. Далее в PowerShell:

- ```Get-WindowsOptionalFeature -Online -FeatureName Microsoft-Hyper-V-All```
- Если пишет Enabled, значит, Hyper-V активен. Если Disabled, включаем:
    ```Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Hyper-V -All```
После этого перезагружаемся.
- Импортируем Hyper-V-модуль:
``` Import-Module Hyper-V ```
- Снова пробуем: 
``` Optimize-VHD -Path "C:\Users\MaDeLa\AppData\Local\Docker\wsl\data\ext4.vhdx" -Mode Full```
Если будет ошибка:
``` 
Не удалось сжать виртуальный диск.
Системе не удалось сжать "C:\Users\MaDeLa\AppData\Local\Docker\wsl\data\ext4.vhdx": Процесс не может получить доступ к файлу, так как этот файл занят другим процессом. (0x80070020).
строка:1 знак:1
+ Optimize-VHD -Path "C:\Users\MaDeLa\AppData\Local\Docker\wsl\data\ext ..
``` 
то значит забыли сделать``` wsl --shutdown ```