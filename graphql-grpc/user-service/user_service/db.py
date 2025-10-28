import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# ==== Чтение переменных окружения ====
# Значения по умолчанию позволяют запускать код локально без дополнительных настроек

POSTGRES_HOST = os.getenv("POSTGRES_HOST", "host.docker.internal")  # Хост PostgreSQL. В Docker по умолчанию "host.docker.internal"
POSTGRES_PORT = os.getenv("POSTGRES_PORT", "5432")                  # Порт PostgreSQL
POSTGRES_DB = os.getenv("POSTGRES_DB", "users")                    # Имя БД
POSTGRES_USER = os.getenv("POSTGRES_USER", "user")                 # Имя пользователя
POSTGRES_PASSWORD = os.getenv("POSTGRES_PASSWORD", "password")    # Пароль

# ==== Формирование строки подключения ====
# Используется драйвер psycopg3 (через [binary]), который указан в requirements.txt

DATABASE_URL = (
    f"postgresql+psycopg://{POSTGRES_USER}:{POSTGRES_PASSWORD}"
    f"@{POSTGRES_HOST}:{POSTGRES_PORT}/{POSTGRES_DB}"
)

# ==== Создание SQLAlchemy Engine и Session ====
# Engine управляет соединением с базой, а Session используется для выполнения операций (CRUD)

engine = create_engine(DATABASE_URL)          # Создаёт объект подключения к БД
SessionLocal = sessionmaker(bind=engine)      # Фабрика сессий для работы с БД в отдельных потоках/запросах
