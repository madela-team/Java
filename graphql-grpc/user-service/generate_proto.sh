#!/bin/bash

# Скрипт генерации Python-классов из .proto-файлов для gRPC-сервиса
# Автор: MaDeLa

# Проверяем, установлен ли модуль grpc_tools. Если нет — устанавливаем необходимые зависимости.
if ! python -c "import grpc_tools" &> /dev/null; then
  echo "grpcio-tools not found. Installing..."
  pip install grpcio grpcio-tools
fi

# Запускаем генерацию gRPC и protobuf-классов из файла proto/user.proto
# -I./proto: указывает директорию, где искать import-ы внутри proto-файлов
# --python_out: путь для генерации обычных Python-классов (user_pb2.py)
# --grpc_python_out: путь для генерации gRPC-классов (user_pb2_grpc.py)
# Результат будет помещён в директорию user_service

python -m grpc_tools.protoc \
  -I./proto \
  --python_out=./user_service \
  --grpc_python_out=./user_service \
  proto/user.proto
