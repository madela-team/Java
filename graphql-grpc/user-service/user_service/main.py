import os
import time
from concurrent import futures

import grpc
import requests
from alembic import command
from alembic.config import Config
from grpc_health.v1 import health, health_pb2, health_pb2_grpc

from service import UserService
from user_service import user_pb2_grpc

# === Конфигурация сервиса из переменных окружения ===
CONSUL_HOST = os.getenv("CONSUL_HOST", "localhost")
CONSUL_PORT = int(os.getenv("CONSUL_PORT", 8500))
SERVICE_NAME = os.getenv("SERVICE_NAME", "user-service")
SERVICE_ID = os.getenv("SERVICE_ID", "user-service-1")
SERVICE_PORT = int(os.getenv("SERVICE_PORT", 9093))
SERVICE_ADDRESS = os.getenv("SERVICE_ADDRESS", "host.docker.internal")  # может быть IP или hostname внутри Docker-сети

# === Функция запуска Alembic миграций ===
def run_migrations():
    print("Running Alembic migrations...")
    # Абсолютный путь к alembic.ini (он находится на уровень выше текущего скрипта)
    alembic_cfg = Config(os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "alembic.ini")))
    command.upgrade(alembic_cfg, "head")  # применить все миграции до последней
    print("Migrations complete")

# === Регистрация gRPC-сервиса в Consul через HTTP API ===
def register_with_consul():
    payload = {
        "ID": SERVICE_ID,
        "Name": SERVICE_NAME,
        "Address": SERVICE_ADDRESS,
        "Port": SERVICE_PORT,
        "Check": {
            "GRPC": f"{SERVICE_ADDRESS}:{SERVICE_PORT}",  # Consul будет выполнять gRPC-запросы
            "GRPCUseTLS": False,
            "Interval": "10s"  # Частота health-чека
        }
    }

    url = f"http://{CONSUL_HOST}:{CONSUL_PORT}/v1/agent/service/register"
    try:
        res = requests.put(url, json=payload)
        res.raise_for_status()
        print("Registered with Consul.")
    except requests.RequestException as e:
        print(f"Failed to register with Consul: {e}")

# === Основная функция запуска gRPC сервера ===
def serve():
    # 1. Создание gRPC-сервера с пулом потоков
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    # 2. Добавление реализованного сервиса
    user_pb2_grpc.add_UserServiceServicer_to_server(UserService(), server)

    # 3. Добавление gRPC health сервиса (для Consul и мониторинга)
    health_servicer = health.HealthServicer()
    health_pb2_grpc.add_HealthServicer_to_server(health_servicer, server)
    health_servicer.set("user-service", health_pb2.HealthCheckResponse.SERVING)  # gRPC Health для имени сервиса
    health_servicer.set("", health_pb2.HealthCheckResponse.SERVING)              # и по умолчанию

    # 4. Привязка к порту и запуск
    server.add_insecure_port(f"[::]:{SERVICE_PORT}")
    server.start()

    # 5. Небольшая задержка перед регистрацией в Consul (чтобы сервер точно успел подняться)
    time.sleep(1)
    register_with_consul()

    # 6. Ожидание завершения (блокирует поток)
    server.wait_for_termination()

# === Точка входа в приложение ===
if __name__ == '__main__':
    # Вывод строки подключения к БД (для отладки)
    from sqlalchemy.engine.url import URL
    print("DB URL:",
          f"{os.getenv('POSTGRES_USER')}:{os.getenv('POSTGRES_PASSWORD')}@{os.getenv('POSTGRES_HOST')}:{os.getenv('POSTGRES_PORT')}/{os.getenv('POSTGRES_DB')}")

    # Конструируем и печатаем URL через SQLAlchemy (удобнее читается)
    DATABASE_NAME = os.getenv("POSTGRES_DB", "users")
    DATABASE_USER = os.getenv("POSTGRES_USER", "user")
    DATABASE_PASSWORD = os.getenv("POSTGRES_PASSWORD", "password")
    DATABASE_HOST = os.getenv("POSTGRES_HOST", "host.docker.internal")
    DATABASE_PORT = os.getenv("POSTGRES_PORT", "5432")
    print(
        str(URL.create(
            drivername="postgresql+psycopg",
            username=DATABASE_USER,
            password=DATABASE_PASSWORD,
            host=DATABASE_HOST,
            port=DATABASE_PORT,
            database=DATABASE_NAME
        ))
    )

    run_migrations()  # применить миграции перед запуском сервиса
    serve()           # стартовать gRPC-сервер
