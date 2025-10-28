# Используем официальный образ Python 3.11 — современный, совместим с grpcio, SQLAlchemy и другими зависимостями.
FROM python:3.11
# Рабочая директория внутри контейнера. В неё копируются файлы и из неё будет запущено приложение.
WORKDIR /app

# Копируем скрипт генерации protobuf-файлов отдельно (чтобы использовать кэш Docker и ускорить сборку)
COPY generate_proto.sh ./generate_proto.sh
# Делаем скрипт исполняемым (в Linux это обязательно)
RUN chmod +x ./generate_proto.sh
# Копируем Alembic-конфигурацию и миграции
COPY alembic.ini ./alembic.ini
# Эти файлы нужны для управления схемой базы данных (аналог Flyway или Liquibase)
COPY migrations ./migrations

# Копируем весь остальной код (включая main.py, модели, grpc реализацию и др.)
COPY . .

# Установка всех зависимостей из requirements.txt
# --no-cache-dir уменьшает размер итогового образа (не сохраняет колёсики в кэш)
RUN pip install --no-cache-dir -r requirements.txt

# Генерация Python gRPC-кода
# Скрипт должен запускать grpcio-tools для генерации *.pb2.py и *_grpc.py файлов
RUN sh ./generate_proto.sh
# Добавляем /app в PYTHONPATH, чтобы можно было использовать абсолютные импорты внутри кода (например, import user_service.models)
ENV PYTHONPATH=/app

# Запуск приложения
CMD ["python", "user_service/main.py"]