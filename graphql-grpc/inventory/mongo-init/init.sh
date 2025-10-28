#!/bin/bash
set -e

# Этот флаг означает: если какая-либо команда завершится с ошибкой — весь скрипт прервётся.
# Полезно, чтобы не продолжать выполнение при ошибке импорта.
set -e

echo "Importing data into inventorydb..."

# Импорт JSON-файла pharmacy_stock.json в MongoDB.
# Параметры:
# --db inventorydb         — имя базы данных
# --collection pharmacy_stock — целевая коллекция
# --file ...               — путь к JSON-файлу
# --jsonArray              — указывает, что файл содержит массив JSON-объектов
mongoimport --db inventorydb \
            --collection pharmacy_stock \
            --file /docker-entrypoint-initdb.d/pharmacy_stock.json \
            --jsonArray

echo "Inventory data import completed."
